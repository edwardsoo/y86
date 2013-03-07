package ui;

import java.util.Observer;
import java.util.Observable;
import java.util.List;
import java.util.EnumMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import util.DataModel;
import util.DataModelEvent;
import util.TableCellIndex;
import util.UserVisibleException;
import isa.Memory;
import isa.Region;
import machine.AbstractCPU;
import machine.AbstractMainMemory;
import machine.RegisterSet;
import machine.Register;

/**
 * UI Encapsulation of a Machine.  Includes debugging infrastructure, abstracted from any UI specifics.
 */

public class Machine extends Observable implements Observer {
  private final AbstractCPU        cpu;
  public  final RegisterSet        registerFile;
  public  final AbstractMainMemory mainMemory;
  public  final DataModel          pc;
  public  final List <RegisterSet> processorState;
  public  final String             curInsAddrRegName;
  public  final Memory             memory;
  public  final String             options;
  private boolean                  isRunning = false;
  private int                      pauseMilliseconds = 0;
  private boolean                  isFirstInstruction;
  private boolean                  isSingleStepEnabled;
  private boolean                  isAtBreakPoint;
  private boolean                  isAtEndOfSingleStep;
  
  /**
   * Observers receive EventType events
   */
  public enum EventType {
    /**
     * Occurs immediately before the underlying machine executes an instrucion.  
     */
    INSTRUCTION_PROLOG, 
    /**
     * Occurs when underlying machine has arrived at a registered trace point.
     */
    TRACE_POINT
  }

  public class Event {
    public final EventType type;
    public Event (EventType aType) {
      type = aType;
    }
  }
  
  public Machine (AbstractCPU aCPU, Memory aMemory, String anOptions) {
    cpu             = aCPU;
    registerFile    = cpu.getRegisterFile ();
    mainMemory      = cpu.getMainMemory     ();
    pc              = cpu.getPC             ();
    processorState  = cpu.getProcessorState ();
    curInsAddrRegName = AbstractCPU.InternalState.CURRENT_INSTRUCTION_ADDRESS;
    memory          = aMemory;
    options = anOptions;
    cpu.addObserver (this);
    cpu.getRegisterFile ().addObserver (this);
    cpu.getMainMemory ().addObserver (this);
    debugPointMonitor = new DebugPointMonitor ();
  }
  
  public static Machine newInstance (Machine aMachine) {
    AbstractCPU newCPU    = AbstractCPU.newInstance (aMachine.cpu);
    Memory      newMemory = new Memory (aMachine.memory, newCPU.getMainMemory(), newCPU.getPC());
    return new Machine (newCPU, newMemory, aMachine.options);
  }
  
  public enum Status {INTERRUPT, SINGLE_STEP, BREAK_POINT, HALT, INVALID_ADDRESS, INVALID_INSTRUCTION, REGISTER_TIMING_ERROR, IMPLEMENTATION_ERROR}

  private Status status;
  
  public Status getStatus () {
    return status;
  }
  
  public String run (boolean isSingleStep, int aMilliseconds) {
    try {
      isRunning           = true;
      isSingleStepEnabled = isSingleStep;
      isFirstInstruction  = true;
      isAtBreakPoint      = false;
      isAtEndOfSingleStep = false;
      pauseMilliseconds   = aMilliseconds;

      cpu.start ();

      
      if (isAtEndOfSingleStep) {
        status = Status.SINGLE_STEP;
        return "";
      } else if (isAtBreakPoint) {
        status = Status.BREAK_POINT;
        return "";
      } else {
        status = Status.INTERRUPT;
        return String.format ("Stopped at pc 0x%x\n", (Integer) pc.getValueAt (0,1));
      }
      
    } catch (UserVisibleException e) {
      if (e instanceof AbstractCPU.MachineHaltException) 
        status = Status.HALT;
      else if (e instanceof AbstractCPU.InvalidInstructionException) 
        status = Status.INVALID_INSTRUCTION;
      else if (e instanceof AbstractMainMemory.InvalidAddressException) 
        status = Status.INVALID_ADDRESS;
      else if (e instanceof Register.TimingException) 
        status = Status.REGISTER_TIMING_ERROR;
      else
        status = Status.IMPLEMENTATION_ERROR;
      return e.getMessage ();
    } finally {
      isRunning = false;
    }
  }
  
  public int getPauseMilliseconds () {
    return pauseMilliseconds;
  }
  
  public void setPauseMilliseconds (int aMilliseconds) {
    synchronized (this) {
      pauseMilliseconds = aMilliseconds;
      notifyAll ();
    }
  }
  
  public String getName () {
    return cpu.getName ();
  }
  
  public void stop () {
    synchronized (this) {
      cpu.triggerInterrupt ();
      if (pausedThread!=null)
        pausedThread.interrupt();
      this.notifyAll ();
    }
  }
  
  public void gotoPC (int anAddress) {
    cpu.resetMachineToPC (anAddress);
  }
  
  public Integer getFirstInstructionAddress () {
    Integer startPC = null;
    for (Region r : memory.getRegions ())
      if (r.getType () == Region.Type.INSTRUCTIONS) {
        startPC = r.getAddress ();
        break;
      }
    return startPC;
  }
  
  public enum ComparisonFailure {REGISTER_FILE_MISMATCH, PROCESSOR_STATE_MISMATCH, MAIN_MEMORY_MISMATCH};
  
  private Register findRegInList (List <RegisterSet> rsl, String regName) {
    for (RegisterSet rs : rsl) {
      Register r = rs.getRegister (regName);
      if (r!=null)
        return r;
    }
    return null;
  }
  
  public EnumSet <ComparisonFailure> compareTo (Machine anotherMachine, List <String> checkedState) {
    EnumSet <ComparisonFailure> cmp = EnumSet.noneOf (ComparisonFailure.class);
    // Compare register files
    if (!registerFile.valueEquals (anotherMachine.registerFile))
      cmp.add (ComparisonFailure.REGISTER_FILE_MISMATCH);
    // Compare main memories
    if (!memory.valueEquals (anotherMachine.memory))
      cmp.add (ComparisonFailure.MAIN_MEMORY_MISMATCH);
    // Compare selected processor state (e.g., condition codes)
    for (String state : checkedState) {
      Register aReg = findRegInList (processorState, state);
      Register bReg = findRegInList (anotherMachine.processorState, state);
      if ((aReg!=null || bReg!=null) && (aReg==null || bReg==null || !aReg.valueEquals (bReg))) {
        cmp.add (ComparisonFailure.PROCESSOR_STATE_MISMATCH);
        break;
      }
    }
    return cmp;
  }
  
  /**
   * Debugging Declarations
   */
  public enum DebugType  {BREAK, TRACE}
  public enum DebugPoint {INSTRUCTION, MEMORY_READ, MEMORY_WRITE, REGISTER_READ, REGISTER_WRITE}  
  
  /**
   * Determine whether a debug point is enabled at location.
   */
  public boolean isDebugPointEnabled (DebugType debugType, DebugPoint debugPoint, int value) {
    SortedSet<Integer> dpSet = debugPointSet.get (debugType) .get (debugPoint);
    return dpSet.contains (value);
  }
  
  /**
   * Create or modify a debug point.
   */
  public void setDebugPoint (DebugType debugType, DebugPoint debugPoint, int value, boolean isEnabled) {
    SortedSet<Integer> dpSet = debugPointSet.get (debugType) .get (debugPoint);
    if (isEnabled)
      dpSet.add (value);
    else
      dpSet.remove (value);
    debugPointSetObservable.tellObservers (new DataModelEvent (DataModelEvent.Type.WRITE_BY_USER, value,0));
  }
  
  /**
   * Clear all debug points of given type.
   */
  public void clearAllDebugPoints (DebugType debugType) {
    ArrayList <TableCellIndex> bpList = new ArrayList <TableCellIndex> ();
    for (SortedSet <Integer> dpSet : debugPointSet.get (debugType).values ()) {
      for (Integer adr : dpSet)
	bpList.add (new TableCellIndex (adr, 0));
      dpSet.clear ();
    }
    debugPointSetObservable.tellObservers (new DataModelEvent (DataModelEvent.Type.WRITE_BY_USER, bpList));
  }  
  
  /**
   * Get a list of all debug values for given type and point.
   */
  public Collection <Integer> getDebugPoints (DebugType type, DebugPoint point) {
    return debugPointSet.get (type).get (point);
  }
  
  /**
   * Add new observer to debug point set.
   */
  public void addDebugPointObserver (Observer o) {
    debugPointSetObservable.addObserver (o);
  }
  
  // End of public interface
  //////////////////////////
  
  /**
   * Debugging event sent to observers
   */
  
  public class DebugEvent extends Event {
    public final DebugType  debugType;
    public final DebugPoint point;
    public final int        value;
    public DebugEvent (DebugType aDebugType, DebugPoint aPoint, int aValue) {
      super (EventType.TRACE_POINT);
      debugType  = aDebugType;
      point      = aPoint;
      value      = aValue;
    }
  }
  
  /**
   * Debugging Infrastructure
   */
  private DebugPointSetObservable debugPointSetObservable = new DebugPointSetObservable ();
  private DebugPointMonitor       debugPointMonitor;
  private EnumMap <DebugType, EnumMap <DebugPoint, SortedSet <Integer>>> debugPointSet = new EnumMap <DebugType, EnumMap <DebugPoint, SortedSet <Integer>>> (DebugType.class);
  { 
    for (DebugType type : DebugType.values ()) {
      debugPointSet.put (type, new EnumMap <DebugPoint, SortedSet <Integer>> (DebugPoint.class));
      for (DebugPoint point : DebugPoint.values ()) 
        debugPointSet.get(type).put (point, new TreeSet <Integer> ());
        }
  }

  /**
   * An observable debug point.
   */
  private class DebugPointSetObservable extends Observable {
    void tellObservers (DataModelEvent event) {
      setChanged ();
      notifyObservers (event);
    }
  }
  
  /**
   * Monitor memory to adjust breakpoints addresses when memory data inserted or deleted.
   */
  private class DebugPointMonitor implements Memory.LengthChangedListener {
    DebugPointMonitor () {
      memory.addLengthChangedListener (this);
    }
    public void changed (int address, int length, int lastAddress, Type type) {
      for (EnumMap <DebugPoint,SortedSet<Integer>> dType : debugPointSet.values ())
	for (SortedSet<Integer> dPoint : dType.values ()) {
	  ArrayList <Integer> chg = new ArrayList <Integer> ();
	  for (Integer dAdr : dPoint)
	    if (dAdr >= address && dAdr <= lastAddress)
	      chg.add (dAdr);
	  for (Integer dAdr : chg) 
	    dPoint.remove (dAdr);
	  for (Integer dAdr : chg)
	    if (type == Memory.LengthChangedListener.Type.DELETED) {
	      if (dAdr > address)
		dPoint.add (dAdr - length);
	    } else if (type == Memory.LengthChangedListener.Type.INSERTED) {
	      dPoint.add (dAdr + length);
	    } else
	      throw new AssertionError (type);
	}
    }
  }
  
  /**
   * Observer Implementation
   */
  
  private int prevInsAddr;
  
  Thread pausedThread=null;
  
  /**
   * For Observer interface.  Upcalled by cpu, register file or main memory.
   */
  
  public void update (Observable o, Object arg) {
    if (isRunning) {
      try {
        isRunning = false;
        if (o==cpu) {
          
          // CPU is about to execute an instruction (unless interrupt occurs)
          if (!isFirstInstruction && isSingleStepEnabled) {
            isAtEndOfSingleStep = true;
            cpu.triggerInterrupt ();
          } else if (!isFirstInstruction && debugPointSet.get(DebugType.BREAK).get(DebugPoint.INSTRUCTION).contains (pc.getValueAt (0,1))) {
            setChanged ();
            notifyObservers (new DebugEvent (DebugType.BREAK, DebugPoint.INSTRUCTION, (Integer) pc.getValueAt (0,1)));
            isAtBreakPoint = true;
            cpu.triggerInterrupt ();
          } else if (!isFirstInstruction && !isSingleStepEnabled) {
            pausedThread = Thread.currentThread();
            try {pausedThread.sleep (pauseMilliseconds);} catch (InterruptedException e) {}
            pausedThread = null;
          }
          if (!cpu.isInterrupt ()) {  // XXX Possible race condition, never seen
            setChanged ();
            notifyObservers (new Event (EventType.INSTRUCTION_PROLOG));
            if (!isFirstInstruction && debugPointSet.get (DebugType.TRACE).get (DebugPoint.INSTRUCTION).contains (prevInsAddr)) {
              setChanged ();
              notifyObservers (new DebugEvent (DebugType.TRACE, DebugPoint.INSTRUCTION, (Integer) pc.getValueAt (0,1)));
            }
          }
          
          prevInsAddr        = (Integer) pc.getValueAt (0,1);
          isFirstInstruction = false;
          
        } else if (o==cpu.getRegisterFile ()) {
          
          // Register has been accessed
          DataModelEvent event  = (DataModelEvent) arg;
          int            regNum = event.getRowIndex ();
          DebugPoint     access = event.getType ()==DataModelEvent.Type.READ? DebugPoint.REGISTER_READ: DebugPoint.REGISTER_WRITE;
          if (debugPointSet.get (DebugType.TRACE).get (access).contains (regNum)) {
            setChanged ();
            notifyObservers (new DebugEvent (DebugType.TRACE, access, regNum));
          }
          if (debugPointSet.get (DebugType.BREAK).get (access).contains (regNum)) {
            setChanged ();
            notifyObservers (new DebugEvent (DebugType.BREAK, access, regNum));
            isAtBreakPoint = true;
            cpu.triggerInterrupt ();
          }
          
        } else if (o==cpu.getMainMemory ()) {
          
          // Main Memory has been accesssed
          DataModelEvent event    = (DataModelEvent) arg;
          int            memAddr = event.getCells ().get (0).rowIndex;
          DebugPoint     access  = event.getType ()==DataModelEvent.Type.READ? DebugPoint.MEMORY_READ: DebugPoint.MEMORY_WRITE;
          if (debugPointSet.get (DebugType.TRACE).get (access).contains (memAddr)) {
            setChanged ();
            notifyObservers (new DebugEvent (DebugType.TRACE, access, memAddr));
          }
          if (debugPointSet.get (DebugType.BREAK).get (access).contains (memAddr)) {
            setChanged ();
            notifyObservers (new DebugEvent (DebugType.BREAK, access, memAddr));
            isAtBreakPoint = true;
            cpu.triggerInterrupt ();
          }
        }
      } finally {
        isRunning = true;
      }
    }
  }
}