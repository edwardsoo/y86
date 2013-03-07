package ui;

import isa.AbstractISA;
import isa.Memory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import machine.AbstractCPU;
import machine.AbstractMainMemory;

/**
 * Base class for Simulator UI.  Extended by UI implementations.
 */

public abstract class AbstractUI {

  protected static final String   APPLICATION_NAME       = "Simple Machine";
  protected static final String   MANIFSET_VERSION       = AbstractUI.class.getPackage().getImplementationVersion();
  protected static final String   APPLICATION_VERSION    = String.format ("Version %s", MANIFSET_VERSION!=null? MANIFSET_VERSION: "not specified");
  protected static final String[] APPLICATION_COPYRIGHT  = new String[] {"University of British Columbia", "Copyright \u00A9 2010 - 2012 Mike Feeley.", "All rights reserved."};
  protected static final String   APPLICATION_USAGE      = "SimpleMachine -i [gui|cli] -a <arch> -v [student|solution]";
  protected static final String   APPLICATION_ENV_PREFIX = "SIMPLE_MACHINE_";
  protected static final int      MAIN_MEMORY_SIZE       = 1*1024*1024;
  protected static final String   SOLUTION_VARIANT       = "solution";

  private static enum AppEnv {ARCHITECTURE, VARIANT, UI, UI_OPTIONS};
  private static Env <AppEnv> env = new Env <AppEnv> (AppEnv.class);

  protected static List <String>  usageList = new ArrayList <String> ();
  protected static List <Env>     envList   = new ArrayList <Env>    ();
  {
    usageList.add (APPLICATION_USAGE);
    envList.add   (env);
  }

  static {
    System.setProperty ("com.apple.mrj.application.apple.menu.about.name", APPLICATION_NAME);
  }

  protected Machine               machine;
  protected String                applicationFullName;

  // Machine Configuration

  /**
   * Configuration Definitions
   */
  static private HashMap <String, Config> configs = new HashMap <String, Config> ();
  static {
    new Config ("SM213-VM",
	"arch.sm213.isa.ISA",
	"arch.sm213.machine.solution.MainMemory",
	"arch.sm213.machine.solution.VirtualMemoryCPU",
	"arch.sm213.machine.<variant>.MainMemory",
	"arch.sm213.machine.<variant>.VirtualMemoryCPU",
	"[showMac][animation]");
    new Config ("SM213",
	"arch.sm213.isa.ISA",
	"arch.sm213.machine.solution.MainMemory",
	"arch.sm213.machine.solution.CPU",
	"arch.sm213.machine.<variant>.MainMemory",
	"arch.sm213.machine.<variant>.CPU",
	"[showMac][animation]");
    new Config ("Y86-Seq",
	"arch.y86.isa.ISA",
	"arch.y86.machine.MainMemory",
	"arch.y86.machine.seq.solution.CPU",
	"arch.y86.machine.MainMemory",
	"arch.y86.machine.seq.<variant>.CPU",
	"[twoProcStateCols][showDataAddr][animation]");
    new Config ("Y86-Pipe-Minus",
	"arch.y86.isa.ISA",
	"arch.y86.machine.MainMemory",
	"arch.y86.machine.pipeminus.solution.CPU",
	"arch.y86.machine.MainMemory",
	"arch.y86.machine.pipeminus.<variant>.CPU",
	"[twoProcStateCols][smallCurInsDpy][showDataAddr]");
    new Config ("Y86-Pipe",
	"arch.y86.isa.ISA",
	"arch.y86.machine.MainMemory",
	"arch.y86.machine.pipe.solution.CPU",
	"arch.y86.machine.MainMemory",
	"arch.y86.machine.pipe.<variant>.CPU",
	"[twoProcStateCols][smallCurInsDpy][showDataAddr]");
    new Config ("Y86-Pipe-Super",
	"arch.y86.isa.ISA",
	"arch.y86.machine.MainMemory",
	"arch.y86.machine.pipesuper.solution.CPU",
	"arch.y86.machine.MainMemory",
	"arch.y86.machine.pipe.<variant>.CPU",
	"[twoProcStateCols][smallCurInsDpy][showDataAddr]");
    new Config ("Y86-Benchmark",
	"arch.y86.isa.ISA",
	"arch.y86.machine.MainMemory",
	"arch.y86.machine.benchmark.solution.CPU",
	"arch.y86.machine.MainMemory",
	"arch.y86.machine.benchmark.solution.CPU",
	"[twoProcStateCols][showDataAddr]");
  }

  /**
   * Machine Configuration Definition
   */
  protected static class Config {
    String name;
    String isa;
    String mem;
    String cpu;
    String memVar;
    String cpuVar;
    String uiOptions;

    private Config (String aName, String anIsa, String aMem, String aCpu, String aMemVar, String aCpuVar, String aUiOptions) {
      name=aName; isa=anIsa; mem=aMem; cpu=aCpu; memVar=aMemVar; cpuVar=aCpuVar; uiOptions=aUiOptions;
      configs.put (name.toLowerCase(), this);
    }

    private class ConfigException extends ArgException {
      ConfigException (String variant, String message) {
	super (String.format ("Missing %s for %s-%s", message, name, variant));
      }
    }

    private AbstractISA newISA (String variant) throws ConfigException {
      String errMsg = "ISA definition";
      try {
	return (AbstractISA) Class.forName(isa).getConstructor ().newInstance ();
      } catch (ClassNotFoundException e) {
	throw this.new ConfigException (variant, errMsg);
      } catch (NoSuchMethodException e) {
	throw this.new ConfigException (variant, errMsg);
      } catch (InstantiationException e) {
	throw this.new ConfigException (variant, errMsg);
      } catch (IllegalAccessException e) {
	throw this.new ConfigException (variant, errMsg);
      } catch (InvocationTargetException e) {
	throw this.new ConfigException (variant, errMsg);
      }
    }

    private AbstractMainMemory newMainMemory (String variant, int byteCapacity) throws ConfigException {
      String errMsg = "main-memory implementation";
      try {
	String c = variant.equals (SOLUTION_VARIANT)? mem: memVar.replaceAll ("<variant>", variant);
	return (AbstractMainMemory) Class.forName(c).getConstructor (int.class).newInstance (byteCapacity);
      } catch (ClassNotFoundException e) {
	throw this.new ConfigException (variant, errMsg);
      } catch (NoSuchMethodException e) {
	throw this.new ConfigException (variant, errMsg);
      } catch (InstantiationException e) {
	throw this.new ConfigException (variant, errMsg);
      } catch (IllegalAccessException e) {
	throw this.new ConfigException (variant, errMsg);
      } catch (InvocationTargetException e) {
	throw this.new ConfigException (variant, errMsg);
      }
    }

    private AbstractCPU newCPU (String variant, AbstractMainMemory mainMemory) throws ConfigException {
      String errMsg = "cpu implementation";
      try {
	String fullname = name;
	if (!variant.isEmpty()) {
	  String vc = variant.substring(0,1).toUpperCase().concat(variant.substring(1,variant.length()));
	  fullname = fullname.concat ("-".concat (vc));
	}
	String c = variant.equals (SOLUTION_VARIANT)? cpu: cpuVar.replaceAll ("<variant>", variant);
	return (AbstractCPU) Class.forName(c).getConstructor (String.class, AbstractMainMemory.class).newInstance (fullname, mainMemory);
      } catch (ClassNotFoundException e) {
	throw this.new ConfigException (variant, errMsg);
      } catch (NoSuchMethodException e) {
	throw this.new ConfigException (variant, errMsg);
      } catch (InstantiationException e) {
	throw this.new ConfigException (variant, errMsg);
      } catch (IllegalAccessException e) {
	throw this.new ConfigException (variant, errMsg);
      } catch (InvocationTargetException e) {
	throw (RuntimeException) e.getTargetException ();
      }
    }

    public static Machine newMachine (String archName, String variantName) throws ArgException {
      Config config = configs.get (archName);
      if (config==null)
	throw new ArgException (String.format ("Unknown architecture %s\n", archName));

      AbstractISA isa               = config.newISA        (variantName);
      AbstractMainMemory mainMemory = config.newMainMemory (variantName, MAIN_MEMORY_SIZE);
      AbstractCPU cpu               = config.newCPU        (variantName, mainMemory);
      Memory      memory            = new Memory           (isa, mainMemory, cpu.getPC ());

      return new Machine (cpu, memory, config.uiOptions);
    }
  }

  // Environment Variables

  /**
   * Environment Variable Handling
   */
  public static class Env <E extends Enum <E>> {
    private final Class <E> enumClass;
    private static String prefix = APPLICATION_ENV_PREFIX;
    private static String nameOf (String var) {
      return prefix.concat (var);
    }
    private static String valueOf (String var) {
      return System.getenv (nameOf (var));
    }
    public Env (Class <E> anEnumClass) {
      enumClass = anEnumClass;
    }
    public final String nameOf (E var) {
      return nameOf (var.toString());
    }
    public final String valueOf (E var) {
      return valueOf (var.toString());
    }
    public final String valueOf (E var, String ifNullValue) {
      String value = valueOf (var);
      return value!=null? value.toLowerCase(): (ifNullValue!=null? ifNullValue.toLowerCase(): null);
    }
    public final List <String> getNames () {
      ArrayList <String> names = new ArrayList <String> ();
      for (E var : EnumSet.allOf (enumClass))
	names.add (nameOf (var));
      return names;
    }
  }

  static void showUsage () {
    System.out.print ("Usage: ");
    for (String s : usageList)
      System.out.printf ("%s\n",s);
  }

  static void showEnv () {
    System.out.print ("Environment Variables:\n");
    for (Env <?> e : envList) {
      for (String s : e.getNames ())
	System.out.printf ("\t%s\n",s);
    }
  }

  // Argument Parsing

  /**
   * Thrown to indicate syntax error in command line arguments.
   */
  protected static class ArgException extends Exception {
    public ArgException (String msg) {
      super (msg);
    }
  }

  /**
   * Parse command-line arguments for specified switch, returning its value.
   * @param  args                     list of command-line arguments.
   * @param  argSwitch                string (starting with "-" that starts argument.
   * @param  isRequired               switch is required.
   * @param  isSwitchValueRequired    true iff a value is required to follow switch if it is present.
   * @param  isSwitchValueListAllowed true iff a list of values is allowed to follow the switch.
   * @param  valueIfSwitchMissing     value to return if switch is missing.
   * @return                          list of values of argSwitch or "" if it is found, but with no value.
   * @throws ArgException             if switch is not found, isRequired is true, and valueIfSwitchMissing is null
   *                                  or if it is found without a value and isSwitchValueRequired is true.
   */
  protected static List <String> getArgList (List <String> args,
      String        argSwitch,
      boolean       isRequired,
      boolean       isSwitchValueRequired,
      boolean       isSwitchValueListAllowed,
      String        valueIfSwitchMissing)
	  throws ArgException {
    for (int i=0; i<args.size(); i++)
      if (args.get(i).equals (argSwitch)) {
	args.remove (i);
	ArrayList <String> al = new ArrayList <String> ();
	while (args.size()>i && !args.get(i).startsWith ("-")) {
	  al.add (args.remove(i).toLowerCase());
	  if (!isSwitchValueListAllowed)
	    break;
	}
	if (al.size()==0) {
	  if (isSwitchValueRequired)
	    throw new ArgException (String.format ("Missing argument value for %s", argSwitch));
	  else if (valueIfSwitchMissing!=null)
	    al.add (valueIfSwitchMissing.toLowerCase());
	  else
	    al.add ("");
	}
	return al;
      }
    if (!isRequired || valueIfSwitchMissing!=null) {
      if (valueIfSwitchMissing!=null)
	return Arrays.asList (valueIfSwitchMissing.toLowerCase());
      else
	return new ArrayList <String> ();
    } else
      throw new ArgException (String.format ("Missing argument %s", argSwitch));
  }

  protected static String getArg (ArrayList <String> args, String argSwitch, boolean isRequired, boolean isSwitchValueRequired, String valueIfSwitchMissing) throws ArgException {
    List <String> argList = getArgList (args, argSwitch, isRequired, isSwitchValueRequired, false, valueIfSwitchMissing);
    assert argList.size()<=1;
    if (argList.size()>0)
      return argList.get (0);
    else
      return null;
  }

  protected static Integer getArgInt (ArrayList <String> args, String argSwitch, boolean isRequired, boolean isSwitchValueRequired, String valueIfSwitchMissing) throws ArgException {
    String arg = getArg (args, argSwitch, isRequired, isSwitchValueRequired, valueIfSwitchMissing);
    if (arg!=null)
      try {
	int radix;
	if (arg.substring(0,2).toLowerCase().equals ("0x")) {
	  arg = arg.substring (2, arg.length());
	  radix = 16;
	} else
	  radix = 10;
	return new Integer (Integer.parseInt (arg, radix));
      } catch (NumberFormatException e) {
	throw new ArgException ("Command argument must be a number.");
      } else
	return null;
  }


  /**
   * Main entry point to Simple Machine (usualled called by SimpleMachine.main() to give the execution
   * instance the name "SimpleMachine" instead of "AbstractUI", but starting with this class works fine too).
   */

  public static void main (String[] argsArray) {
    ArrayList <String> args  = new ArrayList <String> (Arrays.asList (argsArray));
    String        errMessage = "";
    AbstractUI    ui         = null;

    // Find constructor for UI class specified in args
    try {
      String uiName = getArg (args, "-i", true, true, env.valueOf (AppEnv.UI, "gui"));
      try {
	errMessage         = String.format ("UI %s not supported.", uiName);
	Class <?> uiClass  = Class.forName ("ui.".concat (uiName).concat (".UI"));
	Constructor uiCtor = uiClass.getConstructor (ArrayList.class);
	ui = (AbstractUI) uiCtor.newInstance (args);
	ui.run ();
      } catch (ClassNotFoundException cnfe) {
	throw new ArgException (errMessage);
      } catch (NoSuchMethodException nsme) {
	throw new ArgException (errMessage);
      } catch (InstantiationException e) {
	throw new ArgException (errMessage);
      } catch (IllegalAccessException e) {
	throw new ArgException (errMessage);
      } catch (InvocationTargetException e) {
	throw e.getTargetException ();
      }
    } catch (Throwable e) {
      if (e instanceof ArgException) {
	System.out.printf ("%s\n", e.getMessage ());
	showUsage ();
	showEnv   ();
      } else
	throw new AssertionError (e);
    }
  }

  public AbstractUI (ArrayList <String> args) throws ArgException {
    // Initialize the AbstractUI Part 1

    // Parse args and configure machine
    String archName    = getArg (args, "-a", true, true, env.valueOf (AppEnv.ARCHITECTURE));
    String variantName = getArg (args, "-v", true, true, env.valueOf (AppEnv.VARIANT,    "student"));
    String uiOptions   = getArg (args, "-o", true, true, env.valueOf (AppEnv.UI_OPTIONS, ""));
    machine            = Config.newMachine (archName, variantName);

    // Initialize the AbstractUI Part 2
    applicationFullName = String.format ("%s (%s)", APPLICATION_NAME, machine.getName());
  }

  /**
   * Run the UI
   */
  public abstract void run ();
}