package arch.y86.machine;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import machine.AbstractCPU;
import machine.AbstractMainMemory;
import machine.Register;
import machine.RegisterSet;
import util.HalfByteNumber;

public abstract class AbstractY86CPU extends AbstractCPU {

  //  Here is a list of all non-standard instructions added to the grammar for
  //  the Y86 CPU. Those listed with a * are given to the students. All others
  //  are only available as part of the solutions (and can be used when we ask
  //  students to implement new instructions.
  //
  //  Instruction           iCd    iFn
  //  --------------------------------
  //  rmmovl rA, D(rB,1)     4      1
  //  rmmovl rA, D(rB,2)     4      2
  //  rmmovl rA, D(rB,4)     4      4
  //
  //  mrmovl D(rB,1), rA     5      1
  //  mrmovl D(rB,2), rA     5      2
  //  mrmovl D(rB,4), rA     5      4
  //
  //  mull                   6      4   *
  //  divl                   6      5   *
  //  modl                   6      6   *
  //
  //  call   (rA)            8      8
  //  call  D(rB),   rA      8      1
  //  call  D(rB,1), rA      8      1
  //  call  D(rB,2), rA      8      2
  //  call  D(rB,4), rA      8      4
  //  call *D(rB),   rA      8      9
  //  call *D(rB,1), rA      8      9
  //  call *D(rB,2), rA      8      A
  //  call *D(rB,4), rA      8      C
  //
  //  iaddl                  C      0
  //  isubl                  C      1
  //  iandl                  C      2
  //  ixorl                  C      3
  //  imull                  C      4
  //  idivl                  C      5
  //  imodl                  C      6
  //
  //  leave                  D      0
  //
  //  jmp   D(rB)            E      1
  //  jmp   D(rB,1)          E      1
  //  jmp   D(rB,2)          E      2
  //  jmp   D(rB,4)          E      4
  //  jmp  *D(rB)            E      9
  //  jmp  *D(rB,1)          E      9
  //  jmp  *D(rB,2)          E      A
  //  jmp  *D(rB,4)          E      C

  // Specialized Registers
  protected final static int R_ESP           = 0x4;
  protected final static int R_EBP           = 0x5;

  // One more: this one needs to be public because the parser needs it.
  public final static int R_NONE          = 0xF;

  // Opcodes
  public final static int I_HALT          = 0x0;  // 00
  public final static int I_NOP           = 0x1;  // 10
  public final static int I_RRMVXX        = 0x2;  // 2c rr
  public final static int I_IRMOVL        = 0x3;  // 30 Fr vvvvvvvv
  public final static int I_RMMOVL        = 0x4;  // 4s rr vvvvvvvv
  public final static int I_MRMOVL        = 0x5;  // 5s rr vvvvvvvv
  public final static int I_OPL           = 0x6;  // 6a rr
  public final static int I_JXX           = 0x7;  // 7c vvvvvvvv
  public final static int I_CALL          = 0x8;  // 80 vvvvvvvv
  public final static int I_RET           = 0x9;  // 90
  public final static int I_PUSHL         = 0xa;  // A0 rF
  public final static int I_POPL          = 0xb;  // B0 rF
  public final static int I_IOPL          = 0xc;  // Ca Fr vvvvvvvv
  public final static int I_LEAVE         = 0xd;  // d0
  public final static int I_JMPI          = 0xe;  // ex Fr vvvvvvvv

  // Function codes for I_OPL and I_IOPL
  public final static int A_ADDL          = 0x0;
  public final static int A_SUBL          = 0x1;
  public final static int A_ANDL          = 0x2;
  public final static int A_XORL          = 0x3;

  // Enhanced function codes for I_OPL and I_IOPL
  public final static int A_MULL          = 0x4;
  public final static int A_DIVL          = 0x5;
  public final static int A_MODL          = 0x6;

  // Additional ALU function nodes not permitted in I_OPL and I_IOPL instructions
  public final static int A_ADDL_LSHIFT_1 = 0x7;
  public final static int A_ADDL_LSHIFT_2 = 0x8;

  // Mask for the bits encoding the register scale.
  public final static int X_SCALE_MASK        = 0x7;

  // Function codes for I_JMPI and I_CALL
  public final static int X_INDIRECT_MASK     = 0x8;
  public final static int X_INDIRECT_FLAG     = 0x0;
  public final static int X_DBL_INDIRECT_FLAG = 0x8;

  public final static int X_DIRECT_CALL       = 0x0;
  public final static int X_INDIRECT_CALL     = 0x8;

  // Function codes for I_RRMVXX and I_JXX
  public final static int C_NC            = 0x0;
  public final static int C_LE            = 0x1;
  public final static int C_L             = 0x2;
  public final static int C_E             = 0x3;
  public final static int C_NE            = 0x4;
  public final static int C_GE            = 0x5;
  public final static int C_G             = 0x6;

  // Instruction status values
  protected final static int S_AOK           = 0x1;  // Normal operation
  protected final static int S_ADR           = 0x2;  // Address exception
  protected final static int S_INS           = 0x3;  // Illegal instruction exception
  protected final static int S_HLT           = 0x4;  // Machine halt

  public abstract static class Sequential extends Base {
    public Sequential (String name, AbstractMainMemory memory) {
      super (name, memory, false);
    }
  }

  public abstract static class Pipelined extends Base {
    public Pipelined (String name, AbstractMainMemory memory) {
      super (name, memory, true);
    }
  }

  private AbstractY86CPU (String name, AbstractMainMemory memory, boolean anIsPipelined) {
    super (name, memory);
  }

  public abstract static class Base extends AbstractY86CPU {

    private final   ProcessorState                  proc;
    protected ProcessorState.InputPorts       p;
    protected ProcessorState.OutputPorts      P;
    private final   WriteBackStageState             wrBk;
    protected FetchStageState.InputPorts      w;
    protected WriteBackStageState.OutputPorts W;
    private final   MemoryStageState                mmry;
    protected WriteBackStageState.InputPorts  m;
    protected MemoryStageState.OutputPorts    M;
    private final   ExecuteStageState               exec;
    protected MemoryStageState.InputPorts     e;
    protected ExecuteStageState.OutputPorts   E;
    private final   DecodeStageState                decd;
    protected ExecuteStageState.InputPorts    d;
    protected DecodeStageState.OutputPorts    D;
    private final   FetchStageState                 ftch;
    protected DecodeStageState.InputPorts     f;
    protected FetchStageState.OutputPorts     F;

    private final   boolean isPipelined;

    public Base (String name, AbstractMainMemory memory, boolean anIsPipelined) {
      super (name, memory, anIsPipelined);
      isPipelined = anIsPipelined;
      is.regFile.addSigned ("eax", true);
      is.regFile.addSigned ("ecx", true);
      is.regFile.addSigned ("edx", true);
      is.regFile.addSigned ("ebx", true);
      is.regFile.addSigned ("esp", true);
      is.regFile.addSigned ("ebp", true);
      is.regFile.addSigned ("esi", true);
      is.regFile.addSigned ("edi", true);
      proc = newProcessorState      ();
      ftch = newFetchStageState     ();
      decd = newDecodeStageState    ();
      exec = newExecuteStageState   ();
      mmry = newMemoryStageState    ();
      wrBk = newWriteBackStageState ();
      p    = proc.getInputPorts     ();
      P    = proc.getOutputPorts    ();
      f    = decd.getInputPorts     ();
      F    = ftch.getOutputPorts    ();
      d    = exec.getInputPorts     ();
      D    = decd.getOutputPorts    ();
      e    = mmry.getInputPorts     ();
      E    = exec.getOutputPorts    ();
      m    = wrBk.getInputPorts     ();
      M    = mmry.getOutputPorts    ();
      w    = ftch.getInputPorts     ();
      W    = wrBk.getOutputPorts    ();
      is.processorState.add (proc);
      is.processorState.add (wrBk);
      is.processorState.add (mmry);
      is.processorState.add (exec);
      is.processorState.add (decd);
      is.processorState.add (ftch);
      if (isPipelined) {
	ftch.add (AbstractCPU.InternalState.CURRENT_INSTRUCTION_ADDRESS, Integer.class, true, false, false, -1);
	decd.add (AbstractCPU.InternalState.CURRENT_INSTRUCTION_ADDRESS, Integer.class, true, false, false, -1);
	exec.add (AbstractCPU.InternalState.CURRENT_INSTRUCTION_ADDRESS, Integer.class, true, false, false, -1);
	mmry.add (AbstractCPU.InternalState.CURRENT_INSTRUCTION_ADDRESS, Integer.class, true, false, false, -1);
	wrBk.add (AbstractCPU.InternalState.CURRENT_INSTRUCTION_ADDRESS, Integer.class, true, false, false, -1);
      } else
	proc.add (AbstractCPU.InternalState.CURRENT_INSTRUCTION_ADDRESS, Integer.class, true, false, false, -1);
    }

    /**
     * Called by AbstractY86CPU constructor.  Override in subclass to extend this register set.
     */
    private ProcessorState newProcessorState () {
      return new ProcessorState ();
    }

    /**
     * Called by AbstractY86CPU constructor.  Override in subclass to extend this register set.
     */
    private FetchStageState newFetchStageState () {
      return new FetchStageState ();
    }

    /**
     * Called by AbstractY86CPU constructor.  Override in subclass to extend this register set.
     */
    private DecodeStageState newDecodeStageState () {
      return new DecodeStageState ();
    }

    /**
     * Called by AbstractY86CPU constructor.  Override in subclass to extend this register set.
     */
    private ExecuteStageState newExecuteStageState () {
      return new ExecuteStageState ();
    }

    /**
     * Called by AbstractY86CPU constructor.  Override in subclass to extend this register set.
     */
    private MemoryStageState newMemoryStageState () {
      return new MemoryStageState ();
    }

    /**
     * Called by AbstractY86CPU constructor.  Override in subclass to extend this register set.
     */
    private WriteBackStageState newWriteBackStageState () {
      return new WriteBackStageState ();
    }

    private class MultiPortRegisterSet <InputPorts, OutputPorts> extends RegisterSet {
      final static int INPUT_PORTS_TYPE_ARGUMENT_NUMBER  = 0;
      final static int OUTPUT_PORTS_TYPE_ARGUMENT_NUMBER = 1;
      Field[] flds;
      int[] inputMap;
      int[] outputMap;

      {
	flds = getClass().getDeclaredFields();
      }

      @SuppressWarnings ("unchecked")
      Class <InputPorts> getInputPortsClass () {
	java.lang.reflect.Type type = getClass ().getGenericSuperclass ();
	java.lang.reflect.ParameterizedType paramType = (java.lang.reflect.ParameterizedType) type;
	return (Class<InputPorts>) paramType.getActualTypeArguments () [INPUT_PORTS_TYPE_ARGUMENT_NUMBER];
      }

      @SuppressWarnings ("unchecked")
      Class <OutputPorts> getOutputPortsClass () {
	java.lang.reflect.Type type = getClass ().getGenericSuperclass ();
	java.lang.reflect.ParameterizedType paramType = (java.lang.reflect.ParameterizedType) type;
	return (Class<OutputPorts>) paramType.getActualTypeArguments () [OUTPUT_PORTS_TYPE_ARGUMENT_NUMBER];
      }

      protected InputPorts getInputPorts () {
	try {
	  Class <InputPorts>       ipc  = getInputPortsClass();
	  Constructor <InputPorts> con  = ipc.getConstructor (getClass());
	  InputPorts               ip   = con.newInstance (this);
	  Field[]                  prts = ipc.getDeclaredFields ();
	  for (int i=0; i < Math.min (prts.length, inputMap.length); i++) {
	    Register reg = (Register) flds [inputMap [i]].get (this);
	    prts[i].set (ip, reg.getInputPort());
	  }
	  return ip;
	} catch (NoSuchMethodException e) {
	  throw new AssertionError (e);
	} catch (InstantiationException e) {
	  throw new AssertionError (e);
	} catch (IllegalAccessException e) {
	  throw new AssertionError (e);
	} catch (InvocationTargetException e) {
	  Throwable t = e.getCause();
	  if (t instanceof RuntimeException)
	    throw (RuntimeException) t;
	  throw new AssertionError (t);
	}
      }

      protected OutputPorts getOutputPorts () {
	try {
	  Class <OutputPorts>       opc  = getOutputPortsClass();
	  Constructor <OutputPorts> con  = opc.getConstructor (getClass());
	  OutputPorts               op   = con.newInstance (this);
	  Field[]                   prts = opc.getDeclaredFields ();
	  for (int i=0; i < Math.min (prts.length, outputMap.length); i++) {
	    Register reg = (Register) flds [outputMap [i]].get (this);
	    prts[i].set (op, reg.getOutputPort());
	  }
	  return op;
	} catch (NoSuchMethodException e) {
	  throw new AssertionError (e);
	} catch (InstantiationException e) {
	  throw new AssertionError (e);
	} catch (IllegalAccessException e) {
	  throw new AssertionError (e);
	} catch (InvocationTargetException e) {
	  Throwable t = e.getCause();
	  if (t instanceof RuntimeException)
	    throw (RuntimeException) t;
	  throw new AssertionError (t);
	}
      }

      protected void mapInputs (int... fieldMap) {
	inputMap = fieldMap;
      }

      protected void mapOutputs (int... fieldMap) {
	outputMap = fieldMap;
      }

      protected MultiPortRegisterSet (String aName) {
	super (aName);
      }
    }

    protected class ProcessorState extends MultiPortRegisterSet <ProcessorState.InputPorts, ProcessorState.OutputPorts> {
      public Register pc   = add         (AbstractCPU.InternalState.PC, Integer.class, true, false, false, -1);
      public Register cCnt = addUnsigned ("cCnt", Long.class, true);
      public Register iCnt = addUnsigned ("iCnt", Long.class, true);
      public Register cc   = addUnsigned ("cc",   Short.class);
      public ProcessorState () {
	super ("P");
      }
      public class InputPorts {
	public Register.InputPort pc, cCnt, iCnt, cc;
	{mapInputs (0,1,2,3);}
      }
      public class OutputPorts {
	public Register.OutputPort pc, cCnt, iCnt, cc;
	{mapOutputs (0,1,2,3);}
      }
    }

    private class StageState <InputPorts,OutputPorts> extends MultiPortRegisterSet <InputPorts, OutputPorts> {
      public StageState (String name) {
	super (name);
      }
    }

    protected class FetchStageState extends StageState <FetchStageState.InputPorts, FetchStageState.OutputPorts> {
      public Register prPC = add ("prPC", Integer.class, true, false, isPipelined, -1);
      public Register pc   = add ("pc",   Integer.class, true, false, true, -1);
      public Register stat = add ("stat", Integer.class, true, false, false, S_AOK);
      public FetchStageState () {
	super ("F");
      }
      public class InputPorts {
	public Register.InputPort prPC, pc, stat;
	{mapInputs (0,1,2);}
      }
      public class OutputPorts {
	public Register.OutputPort prPC, pc;
	{mapOutputs (0,1);}
	public boolean stall, bubble;
      }
    }

    protected class DecodeStageState extends StageState <DecodeStageState.InputPorts, DecodeStageState.OutputPorts> {
      public Register pc   = add         ("pc",   Integer.class,        true, false, false, -1);
      public Register prPC = add         ("prPC", Integer.class,        true, false, false, -1);
      public Register stat = add         ("stat", HalfByteNumber.class, true, false, true, S_AOK);
      public Register iCd  = add         ("iCd",  HalfByteNumber.class, true, false, true, I_NOP);
      public Register iFn  = addUnsigned ("iFn",  HalfByteNumber.class);
      public Register rA   = addUnsigned ("rA",   HalfByteNumber.class);
      public Register rB   = addUnsigned ("rB",   HalfByteNumber.class);
      public Register valC = addSigned   ("valC", Integer.class);
      public Register valP = addUnsigned ("valP", Integer.class);
      public DecodeStageState () {
	super ("D");
      }
      public class InputPorts {
	public Register.InputPort pc, prPC, stat, iCd, iFn, rA, rB, valC, valP;
	{mapInputs (0,1,2,3,4,5,6,7,8);}
      }
      public class OutputPorts {
	public Register.OutputPort stat, iCd, iFn, rA, rB, valC, valP;
	{mapOutputs (2,3,4,5,6,7,8);}
	public boolean stall, bubble;
      }
    }

    protected class ExecuteStageState extends StageState <ExecuteStageState.InputPorts, ExecuteStageState.OutputPorts> {
      public Register stat = add         ("stat", HalfByteNumber.class, true, false, true, S_AOK);
      public Register iCd  = add         ("iCd",  HalfByteNumber.class, true, false, true, I_NOP);
      public Register iFn  = addUnsigned ("iFn",  HalfByteNumber.class);
      public Register valC = addSigned   ("valC", Integer.class);
      public Register valA = addSigned   ("valA", Integer.class);
      public Register valB = addSigned   ("valB", Integer.class);
      public Register dstE = addUnsigned ("dstE", HalfByteNumber.class, R_NONE);
      public Register dstM = addUnsigned ("dstM", HalfByteNumber.class, R_NONE);
      public Register srcA = addUnsigned ("srcA", HalfByteNumber.class, R_NONE);
      public Register srcB = addUnsigned ("srcB", HalfByteNumber.class, R_NONE);
      public Register valP = addUnsigned ("valP", Integer.class);
      public ExecuteStageState () {
	super ("E");
      }
      public class InputPorts {
	public Register.InputPort stat, iCd, iFn, valC, valA, valB, dstE, dstM, srcA, srcB, valP;
	{mapInputs (0,1,2,3,4,5,6,7,8,9,10);}
      }
      public class OutputPorts {
	public Register.OutputPort stat, iCd, iFn, valC, valA, valB, dstE, dstM, srcA, srcB, valP;
	{mapOutputs (0,1,2,3,4,5,6,7,8,9,10);}
	public boolean stall, bubble;
      }
    }

    protected class MemoryStageState extends StageState <MemoryStageState.InputPorts, MemoryStageState.OutputPorts> {
      public Register stat = add         ("stat", HalfByteNumber.class, true, false, true, S_AOK);
      public Register iCd  = add         ("iCd",  HalfByteNumber.class, true, false, true, I_NOP);
      public Register iFn  = addUnsigned ("iFn",  HalfByteNumber.class);
      public Register cnd  = addUnsigned ("cnd",  HalfByteNumber.class);
      public Register valE = addSigned   ("valE", Integer.class);
      public Register valC = addSigned   ("valC", Integer.class);
      public Register valA = addSigned   ("valA", Integer.class);
      public Register dstE = addUnsigned ("dstE", HalfByteNumber.class, R_NONE);
      public Register dstM = addUnsigned ("dstM", HalfByteNumber.class, R_NONE);
      public Register valP = addUnsigned ("valP", Integer.class);
      public MemoryStageState () {
	super ("M");
      }
      public class InputPorts {
	public Register.InputPort stat, iCd, iFn, cnd, valE, valC, valA, dstE, dstM, valP;
	{mapInputs (0,1,2,3,4,5,6,7,8,9);}
      }
      public class OutputPorts {
	public Register.OutputPort stat, iCd, iFn, cnd, valE, valC, valA, dstE, dstM, valP;
	{mapOutputs (0,1,2,3,4,5,6,7,8,9);}
	public boolean stall, bubble;
      }
    }

    protected class WriteBackStageState extends StageState <WriteBackStageState.InputPorts, WriteBackStageState.OutputPorts> {
      public Register stat = add         ("stat", HalfByteNumber.class, true, false, true, S_AOK);
      public Register iCd  = add         ("iCd",  HalfByteNumber.class, true, false, true, I_NOP);
      public Register iFn  = addUnsigned ("iFn",  HalfByteNumber.class);
      public Register cnd  = addUnsigned ("cnd",  HalfByteNumber.class);
      public Register valE = addSigned   ("valE", Integer.class);
      public Register valM = addSigned   ("valM", Integer.class);
      public Register dstE = addUnsigned ("dstE", HalfByteNumber.class, R_NONE);
      public Register dstM = addUnsigned ("dstM", HalfByteNumber.class, R_NONE);
      public Register valP = addUnsigned ("valP", Integer.class);
      public WriteBackStageState () {
	super ("W");
      }
      public class InputPorts {
	public Register.InputPort stat, iCd, iFn, cnd, valE, valM, dstE, dstM, valP;
	{mapInputs (0,1,2,3,4,5,6,7,8);}
      }
      public class OutputPorts {
	public Register.OutputPort stat, iCd, iFn, cnd, valE, valM, dstE, dstM, valP;
	{mapOutputs (0,1,2,3,4,5,6,7,8);}
	public boolean stall, bubble;
      }
    }

    @Override public void resetMachineToPC (int aPC) {
      proc.tickClock (Register.ClockTransition.BUBBLE);
      if (isPipelined)
	ftch.setValue (AbstractCPU.InternalState.CURRENT_INSTRUCTION_ADDRESS, aPC);
      ftch.setValue  ("prPC", aPC);
      ftch.setValue  ("pc", aPC);
      ftch.tickClock (Register.ClockTransition.NORMAL);
      decd.tickClock (Register.ClockTransition.BUBBLE);
      exec.tickClock (Register.ClockTransition.BUBBLE);
      mmry.tickClock (Register.ClockTransition.BUBBLE);
      wrBk.tickClock (Register.ClockTransition.BUBBLE);
      super.resetMachineToPC (aPC);
    }

    protected void cycleSeq () throws InvalidInstructionException, AbstractMainMemory.InvalidAddressException, MachineHaltException, Register.TimingException, ImplementationException {
      proc.cCnt.set (proc.cCnt.get () + 1);
      proc.iCnt.set (proc.iCnt.get () + 1);
      proc.setValue (AbstractCPU.InternalState.CURRENT_INSTRUCTION_ADDRESS, ftch.pc.get ());
      try {
	fetch             ();
	decd.tickClock    (Register.ClockTransition.NORMAL);
	ftch.tickClock    (Register.ClockTransition.NORMAL);
	decode            ();
	exec.tickClock    (Register.ClockTransition.NORMAL);
	execute           ();
	mmry.tickClock    (Register.ClockTransition.NORMAL);
	memory            ();
	wrBk.tickClock    (Register.ClockTransition.NORMAL);
	writeBack         ();
	ftch.tickClock    (Register.ClockTransition.NORMAL);
	proc.setValue     (AbstractCPU.InternalState.PC, F.pc.get ());
	proc.tickClock    (Register.ClockTransition.NORMAL);
	is.regFile.tickClock (Register.ClockTransition.NORMAL);
      } catch (Exception e) {
	int pc = F.pc.get ();
	if (e instanceof RegisterSet.InvalidRegisterNumberException)
	  throw new InvalidInstructionException (e, pc);
	else if (e instanceof InvalidInstructionException)
	  throw new InvalidInstructionException (pc);
	else if (e instanceof AbstractMainMemory.InvalidAddressException)
	  throw new AbstractMainMemory.InvalidAddressException (pc);
	else if (e instanceof MachineHaltException)
	  throw new MachineHaltException (pc);
	else if (e instanceof Register.TimingException) {
	  Register.TimingException rte = (Register.TimingException) e;
	  rte.setPC (pc);
	  throw rte;
	} else if (e instanceof RuntimeException)
	  throw new ImplementationException ((RuntimeException) e, pc);
	else
	  throw new AssertionError (e);
      }
    }

    private class StageThread extends Thread {
      int       exceptionPC = -1;
      Exception exception   = null;
    }

    protected void cyclePipe () throws InvalidInstructionException, AbstractMainMemory.InvalidAddressException, MachineHaltException, Register.TimingException, ImplementationException {
      F.stall = false; F.bubble = false;
      D.stall = false; D.bubble = false;
      E.stall = false; E.bubble = false;
      M.stall = false; M.bubble = false;
      W.stall = false; W.bubble = false;
      try {
	StageThread fetchThread;
	Vector <StageThread> threads = new Vector <StageThread> ();
	fetchThread = new StageThread () {
	  @Override
	  public void run () {
	    try {
	      fetch ();
	    } catch (Exception ex) {
	      exceptionPC = ftch.getValue (AbstractCPU.InternalState.CURRENT_INSTRUCTION_ADDRESS);
	      exception   = ex;
	    }
	  }};
	  threads.add (fetchThread);
	  threads.add (new StageThread () {
	    @Override
	    public void run () {
	      try {
		decode ();
	      } catch (Exception ex) {
		exceptionPC = decd.getValue (AbstractCPU.InternalState.CURRENT_INSTRUCTION_ADDRESS);
		exception   = ex;
	      }
	    }});
	  threads.add (new StageThread () {
	    @Override
	    public void run () {
	      try {
		execute ();
	      } catch (Exception ex) {
		exceptionPC = exec.getValue (AbstractCPU.InternalState.CURRENT_INSTRUCTION_ADDRESS);
		exception   = ex;
	      }
	    }});
	  threads.add (new StageThread () {
	    @Override
	    public void run () {
	      try {
		memory ();
	      } catch (Exception ex) {
		exceptionPC = mmry.getValue (AbstractCPU.InternalState.CURRENT_INSTRUCTION_ADDRESS);
		exception   = ex;
	      }
	    }});
	  threads.add (new StageThread () {
	    @Override
	    public void run () {
	      try {
		writeBack ();
	      } catch (Exception ex) {
		exceptionPC = wrBk.getValue (AbstractCPU.InternalState.CURRENT_INSTRUCTION_ADDRESS);
		exception   = ex;
	      }
	    }});
	  threads.add (new StageThread () {
	    @Override
	    public void run () {
	      try {
		pipelineHazardControl ();
		if (F.stall && F.bubble)
		  throw new RuntimeException ("F stage set to both stall and bubble");
		if (D.stall && D.bubble)
		  throw new RuntimeException ("D stage set to both stall and bubble");
		if (E.stall && E.bubble)
		  throw new RuntimeException ("E stage set to both stall and bubble");
		if (M.stall && M.bubble)
		  throw new RuntimeException ("M stage set to both stall and bubble");
		if (W.stall && W.bubble)
		  throw new RuntimeException ("W stage set to both stall and bubble");
	      } catch (Exception ex) {
		exceptionPC = -1;
		exception   = ex;
	      }
	    }
	  });
	  for (StageThread t : threads)
	    t.start ();
	  for (StageThread t : threads)
	    try {
	      t.join ();
	    } catch (InterruptedException ie) {
	      ;
	    }
	  if (wrBk.iCd.get()!=I_NOP && !W.stall && !W.bubble)
	    proc.iCnt.set (proc.iCnt.get () + 1);
	  proc.cCnt.set (P.cCnt.get () + 1);
	  for (StageThread t : threads)
	    if (t.exception != null) {
	      if (t.exception instanceof RegisterSet.InvalidRegisterNumberException)
		throw new InvalidInstructionException (t.exception, t.exceptionPC);
	      else if (t.exception instanceof InvalidInstructionException)
		throw new InvalidInstructionException (t.exceptionPC);
	      else if (t.exception instanceof AbstractMainMemory.InvalidAddressException)
		throw new AbstractMainMemory.InvalidAddressException (t.exceptionPC);
	      else if (t.exception instanceof MachineHaltException)
		throw new MachineHaltException (t.exceptionPC);
	      else if (t.exception instanceof Register.TimingException) {
		Register.TimingException rte = (Register.TimingException) t.exception;
		rte.setPC (t.exceptionPC);
		throw rte;
	      } else if (t.exception instanceof RuntimeException)
		throw new ImplementationException ((RuntimeException) t.exception, t.exceptionPC);
	      else
		throw new AssertionError (t.exception);
	    }
      } finally {
	decd.setValue (AbstractCPU.InternalState.CURRENT_INSTRUCTION_ADDRESS, decd.pc.getInput ());
	exec.setValue (AbstractCPU.InternalState.CURRENT_INSTRUCTION_ADDRESS, decd.getValue (AbstractCPU.InternalState.CURRENT_INSTRUCTION_ADDRESS));
	mmry.setValue (AbstractCPU.InternalState.CURRENT_INSTRUCTION_ADDRESS, exec.getValue (AbstractCPU.InternalState.CURRENT_INSTRUCTION_ADDRESS));
	wrBk.setValue (AbstractCPU.InternalState.CURRENT_INSTRUCTION_ADDRESS, mmry.getValue (AbstractCPU.InternalState.CURRENT_INSTRUCTION_ADDRESS));
	is.regFile.tickClock (Register.ClockTransition.NORMAL);
	decd.tickClock    (D.stall? Register.ClockTransition.STALL: D.bubble? Register.ClockTransition.BUBBLE: Register.ClockTransition.NORMAL);
	ftch.prPC.set   (decd.prPC.get ());
	ftch.tickClock    (F.stall? Register.ClockTransition.STALL: F.bubble? Register.ClockTransition.BUBBLE: Register.ClockTransition.NORMAL);
	exec.tickClock    (E.stall? Register.ClockTransition.STALL: E.bubble? Register.ClockTransition.BUBBLE: Register.ClockTransition.NORMAL);
	mmry.tickClock    (M.stall? Register.ClockTransition.STALL: M.bubble? Register.ClockTransition.BUBBLE: Register.ClockTransition.NORMAL);
	wrBk.tickClock    (W.stall? Register.ClockTransition.STALL: W.bubble? Register.ClockTransition.BUBBLE: Register.ClockTransition.NORMAL);
      }
      // Need to do a bit of the next cycle, to get PC set in F
      fetch_SelectPC  ();
      ftch.pc.set   (decd.pc.getInput ());
      ftch.setValue   (AbstractCPU.InternalState.CURRENT_INSTRUCTION_ADDRESS, ftch.pc.getInput ());
      proc.setValue   (AbstractCPU.InternalState.PC, ftch.pc.getInput ());
      proc.tickClock  (Register.ClockTransition.NORMAL);
    }
  }

  protected abstract void fetch     () throws Register.TimingException;
  protected abstract void decode    () throws Register.TimingException;
  protected abstract void execute   () throws Register.TimingException;
  protected abstract void memory    () throws Register.TimingException;
  protected abstract void writeBack () throws MachineHaltException, InvalidInstructionException, AbstractMainMemory.InvalidAddressException, Register.TimingException;
  protected          void fetch_SelectPC () throws Register.TimingException {
    ;
  }
  protected          void pipelineHazardControl () throws Register.TimingException {
    ;
  }

}