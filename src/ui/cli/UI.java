package ui.cli;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Observable;
import java.util.EnumSet;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.StringReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.io.FileNotFoundException;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.RecognitionException;
import machine.AbstractCPU;
import machine.AbstractMainMemory;
import machine.RegisterSet;
import machine.Register;
import isa.AbstractAssembler;
import isa.Memory;
import isa.MemoryCell;
import isa.Region;
import grammar.CliParser;
import grammar.CliLexer;
import ui.Machine;
import ui.AbstractUI;

public class UI extends AbstractUI implements Observer {
  
  final static String CLI_USAGE   = "\t[-t <test-filename>] [-ba <benchmark-arch> -bv <benchmark-variant>] \n\t[-r <reg>[:<count>]]* [-m <addr>[:<count>]]* [-c <state>|all] [-p <start-pc>] [<file>]";
  
  private static enum CliEnv {BENCHMARK_ARCHITECTURE, BENCHMARK_VARIANT};
  private static Env <CliEnv> env = new Env <CliEnv> (CliEnv.class);
  {
    usageList.add (CLI_USAGE);
    envList.add   (env);
  }
    
  boolean        isTraceProgram = false;
  CommandHandler cmd            = this.new CommandHandler ();
  
  final String         argBnchArch;
  final String         argBnchVariant;
  final Integer        argStartPC;
  final String         argShowCpuState;
  final List <String>  argTestFilenameList;
  final List <String>  argLoadFilenameList;
  final List <Integer> argShowRegisterNumberList;
  final List <Integer> argShowRegisterCountList;
  final List <Integer> argShowMemoryAddressList;
  final List <Integer> argShowMemoryCountList;
    
  public UI (ArrayList <String> args) throws ArgException {
    super (args);
    machine.addObserver (this);
    
    // Parse Command-Line Args
    argStartPC          = getArgInt  (args, "-p",  false, true, null);
    argBnchArch         = getArg     (args, "-ba", false, true, env.valueOf (CliEnv.BENCHMARK_ARCHITECTURE));
    argBnchVariant      = getArg     (args, "-bv", false, true, env.valueOf (CliEnv.BENCHMARK_VARIANT, ""));
    argTestFilenameList = getArgList (args, "-t",  false, true, true, null);
    argShowCpuState     = getArg     (args, "-c",  false, true, null);
    
    // Get Show-Register List
    ArrayList <Integer> regList = new ArrayList <Integer> ();
    ArrayList <Integer> cntList = new ArrayList <Integer> ();
    while (true) {
      String regCnt = getArg (args, "-r", false, true, null); 
      if (regCnt!=null) {
        String[] parts = regCnt.split (":");
        Register reg = machine.registerFile.getRegister (parts[0].replaceAll ("%",""));
        if (reg!=null) {
          int count;
          if (parts.length==1)
            count=1;
          else if (parts.length==2)
            try {
              count = Integer.parseInt (parts[1]);
            } catch (NumberFormatException e) {
              throw new ArgException ("Invalid register count.");
            }
          else 
            throw new ArgException ("Invalid register interval.");
          regList.add (machine.registerFile.getAll().indexOf (reg));
          cntList.add (count);
        } else
          throw new ArgException ("Invalid register name.");
      } else
        break;
    }
    argShowRegisterNumberList = regList;
    argShowRegisterCountList  = cntList;
    
    // Get Show-Memory List
    ArrayList <Integer> addrList  = new ArrayList <Integer> ();
    ArrayList <Integer> countList = new ArrayList <Integer> ();
    while (true) {
      String adrCnt=getArg (args, "-m", false, true, null);
      if (adrCnt!=null) {
        String[] parts = adrCnt.split (":");
        try {
          int radix;
          if (parts[0].substring(0,2).toLowerCase().equals ("0x")) {
            parts[0] = parts[0].substring (2, parts[0].length());
            radix = 16;
          } else
            radix = 10;
          int addr = Integer.parseInt (parts[0], radix);
          int count;
          if (parts.length==1)
            count = 1;
          else if (parts.length==2)
            count = Integer.parseInt (parts[1]);
          else
            throw new NumberFormatException ();
          addrList.add (new Integer (addr));
          countList.add (new Integer (count));
        } catch (NumberFormatException e) {
          throw new ArgException ("Invalid memory interval.");
        }
      } else
        break;
    }
    argShowMemoryAddressList = addrList;
    argShowMemoryCountList   = countList;
    
    // Finish with Args
    argLoadFilenameList = new ArrayList <String> ();
    while (true) {
      if (!args.isEmpty() && !args.get(0).substring(0,1).equals("-")) {
        argLoadFilenameList.add (args.remove(0));
      } else
        break;
    }
    if (argLoadFilenameList.size()>0 && argTestFilenameList.size()>0)
      throw new ArgException ("Invalid combination of command-line arguments.");
    else if (args.size()!=0)
      throw new ArgException ("Invalid command-line syntax.");
}
  
  /**
   * Optionally use the jline package for reading from console.  Jline provides shell-like
   * command-line editing for console input.  It's distributed under BSD License.  Put the
   * jar (e.g., jline-0.9.94.jar) in your classpath to use it.  Otherwise, we use the
   * standard Java BufferedReader.
   */
  class ConsoleReader {
    String         prompt;
    boolean        isUsingJline = false;
    Object         jlineConsoleReader;
    Method         jlineConsoleReaderReadLine;
    BufferedReader bufferedReader;

    ConsoleReader (String aPrompt) {
      prompt = aPrompt;
      try {
        Class <?> jlineConsoleReaderClass = Class.forName ("jline.ConsoleReader");
        jlineConsoleReader                = jlineConsoleReaderClass.newInstance ();
        jlineConsoleReaderReadLine        = jlineConsoleReaderClass.getMethod ("readLine", String.class);
        isUsingJline                      = true;
      } catch (ClassNotFoundException e) {        
      } catch (NoSuchMethodException e)  {        
      } catch (InstantiationException e) {        
      } catch (IllegalAccessException e) {
      }
      if (!isUsingJline) {
        InputStreamReader ins = new InputStreamReader (System.in);
        bufferedReader        = new BufferedReader    (ins);
      }
    }
    String readLine () throws IOException {
      if (isUsingJline)
        try {
          return (String) jlineConsoleReaderReadLine.invoke (jlineConsoleReader, prompt);
        } catch (IllegalAccessException e) {
          throw new AssertionError ();
        } catch (InvocationTargetException e) {
          Throwable te = e.getTargetException ();
          if (te instanceof IOException)
            throw (IOException) te;
          else
            throw new AssertionError (e);
        }
      else {
        System.out.printf (prompt);
        return bufferedReader.readLine ();
      }
    }
  }
  
  class CommandException extends RuntimeException {}
  
  @Override
  public void run () {
    System.out.printf ("%s\n", applicationFullName);
    if (argLoadFilenameList.size()>0) {
      runFile ();
    } else if (argTestFilenameList.size()>0) {
      for (String file : argTestFilenameList)
        cmd.test (file, null, null);
    } else {
      ConsoleReader in = new ConsoleReader ("(sm) ");
      while (true) {
        try {
          String line = in.readLine ();
          if (line!=null) {
            StringReader      sr = new StringReader      (line);
            ANTLRReaderStream rs = new ANTLRReaderStream (sr);
            CliLexer          lx = new CliLexer          (rs);
            CommonTokenStream tk = new CommonTokenStream (lx);
            CliParser         ps = new CliParser         (tk);
            ps.setCommandHandler (cmd);
            ps.command ();
        } else {
          System.out.print ("\n");
            return;
        }
        } catch (CliLexer.SyntaxErrorException e) {
          System.out.print ("Undefined or malformed command. Try \"help\".\n");
        } catch (CliParser.SyntaxErrorException e) {
          System.out.print ("Undefined or malformed command. Try \"help\".\n");
        } catch (CommandException e) {
        } catch (CliParser.QuitException e) {
          return;
        } catch (IOException e) {
          throw new AssertionError (e);
        } catch (RecognitionException e) {
          throw new AssertionError (e);
        }
      }
    }
  }
    
  private void runFile () {
    for (String filename : argLoadFilenameList) {
      cmd.load (filename);
      System.out.printf ("Running %s, starting at PC 0x%x.\n", filename, (Integer) machine.pc.getValueAt (0,1));
      cmd.run  ();
      for (int i=0; i<argShowRegisterNumberList.size(); i++)
        for (String dsc : regDsc (argShowRegisterNumberList.get(i), argShowRegisterCountList.get(i)))
          System.out.print (dsc);
      for (int i=0; i<argShowMemoryAddressList.size(); i++)
        for (String dsc : memDsc (argShowMemoryAddressList.get(i), 
                                  CliParser.CommandHandler.MemFormat.HEX,
                                   EnumSet.allOf (Region.Type.class),
                                  argShowMemoryCountList.get(i)))
          System.out.print (dsc);
      if (argShowCpuState!=null)
        cmd.examineProc (argShowCpuState.equalsIgnoreCase ("all")? null: argShowCpuState);
    }
  }
  
  ArrayList <String> memDsc (int addr, 
                             CliParser.CommandHandler.MemFormat format, 
                             EnumSet <Region.Type> regionSet, 
                             int count) 
  {
    ArrayList <String> dsc     = new ArrayList <String> ();
    int                curAddr = addr;
    List <Region>      regions = machine.memory.getRegions();
    while (curAddr <= addr+(count>0?count:1)-1 || count==-1) {
      Region  closestRegion = null;
      boolean foundMatch    = false;
      for (Region region : regions) {
        if (curAddr >= region.getAddress() && curAddr <= region.getAddress()+region.byteLength()-1 &&
            regionSet.contains (region.getType ())) {
          MemoryCell cell = region.getCellContainingAddress (curAddr);
          assert cell != null;
          switch (format) {
            case ASM:
              String label   = cell.getLabel ();
              String comment = cell.getComment ();
              dsc.add (String.format ("%08x:  %-16s %-24s %s\n",
                                      cell.getAddress (),
                                      label!=null && !label.equals ("")? label.concat (":"): "",
                                      cell.toAsm (),
                                      comment!=null && !comment.equals ("")? "# ".concat (comment): ""));
              break;
            case HEX:
              dsc.add (String.format("%08x: %s\n", cell.getAddress (), cell.toMac ()));
              break;
          }
          curAddr = cell.getAddress()+cell.length();
          foundMatch = true;
          break;
        } else if (curAddr<region.getAddress() && (closestRegion==null || region.getAddress()<closestRegion.getAddress())) 
          closestRegion = region;
      }
      if (!foundMatch) {
        if (closestRegion!=null)
          curAddr = closestRegion.getAddress ();
        else
          break;
      }
    }
    if (dsc.size() == 0)
      dsc.add ("Address out of bounds.\n");
    return dsc;
  }
  
  ArrayList <String> regDsc (int regNum, int count) {
    List      <Register> regs = machine.registerFile.getAll ();
    ArrayList <String> dsc    = new ArrayList <String> ();
    if (count==0)
      count = 1;
    else if (count==-1)
      count = regs.size ();
    for (int r=regNum; r<Math.min (regNum+count, regs.size()); r++) {
      Register reg = regs.get (r);
      dsc.add (String.format ("%%%s:  0x%-8x  %d\n", reg.getName(), reg.get(), reg.get()));
    }
    if (dsc.size() == 0)
      dsc.add ("No registers selected.\n");
    return dsc;
  }
  
  void showStatus (String msg) {
    if (!msg.isEmpty ())
      System.out.printf ("%s\n",msg);
  }
  
  public void update (Observable o, Object arg) {
    if (o instanceof Machine) {
      Machine.Event e = (Machine.Event) arg;
      switch (e.type) {
        case INSTRUCTION_PROLOG:
          if (isTraceProgram)
            cmd.showWhere ();
          break;
        case TRACE_POINT:
          Machine.DebugEvent de = (Machine.DebugEvent) e;
          String pointDsc;
          String pointNam="?";
          String pointVal="?";
          switch (de.point) {
            case MEMORY_READ: case MEMORY_WRITE:
              pointNam = String.format ("0x%x", de.value);
              Region region = machine.memory.regionForAddress (de.value);
              if (region!=null) {
                MemoryCell cell = region.getCellContainingAddress (de.value);
                assert cell!=null;
                pointVal = String.format ("%s=%s", cell.toAsm(), cell.toMac());
              }
              break;
            case REGISTER_READ: case REGISTER_WRITE:
              Register reg = machine.registerFile.getAll ().get (de.value);
              pointNam = "%".concat (reg.getName ());
              pointVal = String.format ("0x%x=%d", reg.get(), reg.get());
              break;
          }
          switch (de.point) {
            case INSTRUCTION:
              pointDsc = String.format ("ins 0x%x", de.value);
              break;
            case MEMORY_READ:
              pointDsc = String.format ("rd mem[%s]=%s", pointNam, pointVal);
              break;
            case MEMORY_WRITE:
              pointDsc = String.format ("wr mem[%s]=%s", pointNam, pointVal);
              break;
            case REGISTER_READ:
              pointDsc = String.format ("rd %s=%s", pointNam, pointVal);
              break;
            case REGISTER_WRITE:
              pointDsc = String.format ("wr %s=%s", pointNam, pointVal);
              break;
            default:
              throw new AssertionError ();
          }
          switch (de.debugType) {
            case BREAK:
              System.out.printf ("Break %-20s:  ", pointDsc);
              cmd.showWhere ();
              break;
            case TRACE:
              System.out.printf ("Trace %-20s:  ", pointDsc);
              cmd.showWhere ();
              break;
          }
          break;
      }
    }
  }
  
  private class CommandHandler implements CliParser.CommandHandler {
    
    @Override public void load (String filename) {
      try {
        machine.memory.loadFile (filename);
        Integer startPC = argStartPC!=null? argStartPC: machine.getFirstInstructionAddress ();
        if (startPC!=null)
          machine.gotoPC (startPC);
        else
          System.out.print ("File has no instructions\n");
      } catch (FileNotFoundException e) {
        System.out.print ("File not found.\n");
      } catch (Memory.FileTypeException fte) {
        System.out.print ("Invalid file type.\n");
      } catch (Memory.InputFileSyntaxException ifse) {
        System.out.printf ("%s\n", ifse.toString () );
      } catch (Exception ex) {
        throw new AssertionError (ex);
      }
    }
    
    @Override public void test (String filename, String bnchArch, String bnchVariant) {
      if (bnchArch==null)
        bnchArch = argBnchArch;
      if (bnchVariant==null) 
        bnchVariant = argBnchVariant!=null? argBnchVariant: "";
      final List <String> checkedState = Arrays.asList ("cc");
      boolean isSuccessful = false;
      try {
        Machine testMachine = Machine.newInstance (machine);
        testMachine.memory.loadFile (filename);
        Integer startPC = argStartPC!=null? argStartPC: testMachine.getFirstInstructionAddress ();
        if (startPC==null) {
          System.out.printf ("File has no instructions\n");
          throw new CommandException ();
        }
        testMachine.gotoPC (startPC);
        Machine bnchMachine = Config.newMachine (bnchArch, bnchVariant);
        assert bnchMachine != null;
        bnchMachine.memory.loadFile (filename);
        bnchMachine.gotoPC (startPC);
        System.out.printf ("Testing %s against %s.\n", filename, bnchMachine.getName());
        testMachine.run (false, 0);
        bnchMachine.run (false, 0);
        if (testMachine.getStatus()!=bnchMachine.getStatus()) {
          System.out.printf ("ISA Status (%s) != Machine Status (%s).",
                             bnchMachine.getStatus().toString(), testMachine.getStatus().toString ());
          return;
        }
        EnumSet <Machine.ComparisonFailure> cmps = testMachine.compareTo (bnchMachine, checkedState);
        if (cmps.isEmpty())
          isSuccessful = true;
        else {
          for (Machine.ComparisonFailure cmp : cmps)
            switch (cmp) {
              case REGISTER_FILE_MISMATCH:
                System.out.print ("ISA Register File != Machine Register File.\n");
                break;
              case PROCESSOR_STATE_MISMATCH:
                System.out.print ("ISA Processor State != Machine Processor State.\n");
                break;
              case MAIN_MEMORY_MISMATCH:
                System.out.print ("ISA Memory != Machine Memory.\n");
                break;
              default:
                throw new AssertionError ();
            }            
        }
      } catch (FileNotFoundException e) {
        System.out.print ("File not found.\n");
      } catch (Memory.FileTypeException fte) {
        System.out.print ("Invalid file type.\n");
      } catch (Memory.InputFileSyntaxException ifse) {
        System.out.printf ("%s\n", ifse.toString () );
      } catch (ArgException e) {
        System.out.printf ("Benchmark implementation not found.\n");
      } catch (Throwable ex) {
        throw new AssertionError (ex);
      } finally {
        if (isSuccessful)
          System.out.printf ("ISA Check Succeeds.\n");
        else {
          System.out.printf ("ISA Check Fails.\n");
        }
      }
    }
    
    @Override public void run () {
      showStatus (machine.run (false, 0));
    }
    
    @Override public void step () {
      showStatus (machine.run (true, 0));
      showWhere ();
    }
    
    @Override public void showWhere () {
      System.out.print (memDsc ((Integer) machine.pc.getValueAt (0,1), 
                                CliParser.CommandHandler.MemFormat.ASM, 
                                EnumSet.of (Region.Type.INSTRUCTIONS),
                                1).get (0));
    }
    
    @Override public void gotoPC (int pc) {
      machine.gotoPC (pc);
    }
    
    @Override public void examineMem (int count, CliParser.CommandHandler.MemFormat format, int addr) {
      for (String dsc : memDsc (addr, format, EnumSet.allOf (Region.Type.class), count))
        System.out.print (dsc);
    }
    
    @Override public void examineMemAll (CliParser.CommandHandler.MemFormat format, 
                                         CliParser.CommandHandler.MemRegion region) 
    {
      EnumSet <Region.Type> regionSet;
      switch (region) {
        case INS:
          regionSet = EnumSet.of (Region.Type.INSTRUCTIONS);
          break;
        case DAT:
          regionSet = EnumSet.of (Region.Type.DATA);
          break;
        case ALL:
          regionSet = EnumSet.allOf (Region.Type.class);
          break;
        default:
          throw new AssertionError ();
      }
      for (String dsc : memDsc (0, format, regionSet, -1))
        System.out.print (dsc);
    }
    
    @Override public void examineReg (int count, int reg) {
      for (String dsc : regDsc (reg, count))
        System.out.print (dsc);
    }
    
    @Override public void examineRegAll () {
      for (String dsc : regDsc (0, -1))
        System.out.print (dsc);
    }
    
    @Override public void examineProc (String state) {
      for (RegisterSet rs : machine.processorState)
        if (state==null || rs.getName().equalsIgnoreCase (state)) {
          if (state==null)
            System.out.printf ("[%s] CPU State:\n", rs.getName());
          for (Register reg : rs.getAll())
            System.out.printf ("%-4s:  0x%-8x  %d\n", reg.getName(), reg.get(), reg.get());
        }
    }
    
    @Override public void setReg (int regNum, int value) {
      Register reg = machine.registerFile.getAll().get (regNum);
      reg.set       (value);
      reg.tickClock (Register.ClockTransition.NORMAL);
    }
    
    @Override public void setMem (int addr, int value) {
      if (machine.memory.regionForAddress (addr) != null) {
        try {
          machine.mainMemory.writeInteger (addr, value);
        } catch (AbstractMainMemory.InvalidAddressException e) {
          throw new AssertionError ();
        } 
      } else {
        System.out.printf ("Address out of bounds.\n");
        throw new CommandException ();
      }
    }
    
    @Override public void setIns (int addr, CliParser.CommandHandler.InsOper oper, String value) {
      Region region = machine.memory.regionForAddress (addr);
      if (region==null) {
        System.out.printf ("Address out of bounds.\n");
        throw new CommandException ();
      } else if (region.getType()!=Region.Type.INSTRUCTIONS) {
        System.out.printf ("Address is not in code section of memory.\n");
        throw new CommandException ();
      } else {
        int row = region.getRowIndexForAddress (addr);
        int col = region.getAsmColumn ();
        try {
          switch (oper) {
            case INSERT:
              region.insertRow (row);
              break;
            case DELETE:
              region.deleteRow (row);
              break;
            case REPLACE:
          }
          switch (oper) {
            case REPLACE:
            case INSERT:
              Region.AssemblyString as = (Region.AssemblyString) region.getValueAt (row, col);
              as.setValue (value);
              region.setValueAt (as, row, col);
              break;
            case DELETE:
          }
        } catch (AbstractAssembler.AssemblyException e) {
          switch (oper) {
            case INSERT:
              region.deleteRow (row);
              break;
            case REPLACE:
            case DELETE:
          }
          System.out.printf ("%s\n", e.toString ());
          throw new CommandException ();
        }
      }
    }
      
    @Override public void debugPoint (CliParser.CommandHandler.DebugType  cmdType, 
                                      CliParser.CommandHandler.DebugPoint cmdPoint, 
                                      boolean isEnabled, int value) 
    {
      switch (cmdPoint) {
        case MEMORY_ACCESS:
          debugPoint (cmdType, CliParser.CommandHandler.DebugPoint.MEMORY_READ,    isEnabled, value);
          debugPoint (cmdType, CliParser.CommandHandler.DebugPoint.MEMORY_WRITE,   isEnabled, value);
          break;
        case REGISTER_ACCESS:
          debugPoint (cmdType, CliParser.CommandHandler.DebugPoint.REGISTER_READ,  isEnabled, value);
          debugPoint (cmdType, CliParser.CommandHandler.DebugPoint.REGISTER_WRITE, isEnabled, value);
          break;
        case INSTRUCTION: case MEMORY_READ: case MEMORY_WRITE: case REGISTER_READ: case REGISTER_WRITE:
          Machine.DebugType type;
          switch (cmdType) {
            case BREAK:
              type = Machine.DebugType.BREAK;
              break;
            case TRACE:
              type = Machine.DebugType.TRACE;
              break;
            default:
              throw new AssertionError ();
          }
          Machine.DebugPoint point = null;
          switch (cmdPoint) {
            case INSTRUCTION:
              point = Machine.DebugPoint.INSTRUCTION;
              break;
            case MEMORY_READ:
              point = Machine.DebugPoint.MEMORY_READ;
              break;
            case MEMORY_WRITE:
              point = Machine.DebugPoint.MEMORY_WRITE;
              break;
            case REGISTER_READ:
              point = Machine.DebugPoint.REGISTER_READ;
              break;
            case REGISTER_WRITE:
              point = Machine.DebugPoint.REGISTER_WRITE;
              break;
            case MEMORY_ACCESS: case REGISTER_ACCESS: default:
              throw new AssertionError ();
          }
          switch (point) {
            case INSTRUCTION: 
              boolean badAddress = false;
              Region r = machine.memory.regionForAddress (value);
              if (r==null || r.getType()!=Region.Type.INSTRUCTIONS)
                badAddress = true;
              else {
                MemoryCell c = r.getCellContainingAddress (value);
                badAddress = (c==null || c.getAddress()!=value);
              }
              if (badAddress) {
                System.out.printf ("Invalid memory address for break point.\n");
                throw new CommandException ();
              }
              break;
            case MEMORY_READ: case MEMORY_WRITE:
              if (machine.memory.regionForAddress (value)==null) {
                System.out.printf ("Address out of bounds.\n");
                throw new CommandException ();
              }
              break;
            default:
          }
          machine.setDebugPoint (type, point, value, isEnabled);
      }
    }
    
    @Override public void traceProg (boolean isEnabled) {
      isTraceProgram = isEnabled;
    }
    
    @Override public void clearDebugPoints (CliParser.CommandHandler.DebugType cmdType) {
      Machine.DebugType type;
      switch (cmdType) {
        case BREAK:
          type = Machine.DebugType.BREAK;
          break;
        case TRACE:
          type = Machine.DebugType.TRACE;
          break;
        default:
          throw new AssertionError ();
      }
      machine.clearAllDebugPoints (type);
    }
    
    @Override public void showDebugPoints (CliParser.CommandHandler.DebugType cmdType) {
      Machine.DebugType type;
      switch (cmdType) {
        case BREAK:
          type = Machine.DebugType.BREAK;
          break;
        case TRACE:
          type = Machine.DebugType.TRACE;
          break;
        default:
          throw new AssertionError ();
      }
      boolean typeShown = false;
      for (Machine.DebugPoint point : EnumSet.allOf (Machine.DebugPoint.class)) {
        boolean pointShown = false;
        for (Integer value : machine.getDebugPoints (type, point)) {
          String valueString = "";
          switch (point) {
            case INSTRUCTION: case MEMORY_READ: case MEMORY_WRITE:
              valueString = String.format ("0x%x", value);
              break;
            case REGISTER_READ: case REGISTER_WRITE:
              valueString = "%".concat (machine.registerFile.getAll().get (value).getName ());
              break;
            default:
              throw new AssertionError ();
          }
          System.out.printf ("%-5s %-14s %s\n",
                             !typeShown?  type.toString ():  "",
                             !pointShown? point.toString (): "",
                             valueString);
          typeShown  = true;
          pointShown = true;
        }
      }
    }
    
    @Override public void help () {
      System.out.print ("  l|load <file>|\"<file>\"\n");
      System.out.print ("  test <file> [<against-arch> [<against-variant>]]\n");
      System.out.print ("  r|run\n");
      System.out.print ("  s|step\n");
      System.out.print ("  w|where\n");
      System.out.print ("  g|goto <pc>\n");
      System.out.print ("  e|examine[/x] <mem-address> [: <count>]\n");
      System.out.print ("  e|examine     <register> [: <count>]\n");
      System.out.print ("  i|info[/x] mem|ins|dat\n");
      System.out.print ("  i|info reg\n");
      System.out.print ("  i|info cpu [state]\n");
      System.out.print ("  <regiser> = <new-value>\n");
      System.out.print ("  m <mem-address> = <new-numeric-value>\n");
      System.out.print ("  i <mem-address> = <replacement-instruction>\n");
      System.out.print ("  i <mem-address> + <insertion-instruction>\n");
      System.out.print ("  i <mem-address> -\n");
      System.out.print ("  [no]break|trace x <instruction-address>\n");
      System.out.print ("  [no]break|trace r|w|a <memory-address>|<register>\n");
      System.out.print ("  [no]trace prog\n");
      System.out.print ("  clear break|trace\n");
      System.out.print ("  i|info break|trace\n");
      System.out.print ("  quit\n");
    }
    
    @Override public int getRegisterNumber (String registerName) {
      Register reg = machine.registerFile.getRegister (registerName);
      if (reg!=null)
        return machine.registerFile.getAll().indexOf (reg);
      else {
        System.out.printf ("Invalid register name.\n");
        throw new CommandException ();
      }
    }
    
    @Override public int getLabelValue (String label) {
      Integer value = machine.memory.getLabelMap ().getAddress (label);
      if (value!=null)
        return value;
      else {
        System.out.printf ("Undefined label.\n");
        throw new CommandException ();
      }
    }
  }
  
}