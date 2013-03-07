package isa;

import java.util.Observable;
import java.util.Observer;
import java.util.List;
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javax.swing.event.UndoableEditListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.UndoableEdit;
import javax.swing.undo.AbstractUndoableEdit;
import util.AbstractDataModel;
import util.DataModel;
import util.MapModel;
import util.DataModelEvent;
import util.TableCellIndex;
import util.BitStream;
import util.BitString;

/**
 * From the ISA perspective, memory consists of a set of code or data regions.  
 * Regions are data models available for gui.  Memory observes the PC register 
 * and fires a cursor-change event on regions as PC moves though code.  As data models, 
 * regions can be updated by the gui and they fire change events when they change.  
 * Regions can be extended after creation and if a region extends into another 
 * the two are coallesced.  
 *
 * Regions themselves are views of memory and register as observers on memory so
 * they can reflect memory changes to region observers.  The translation between
 * InstructionRegions and Memory is tricky, because (a) it requires assembly/disassembly
 * and (b) InstructionRegions contain line comments and labels that are not stored in memory,
 * but that are nevertheless retained.  
 */

public class Memory extends AbstractDataModel implements Observer {
  private AbstractISA    isa;
  private DataModel      mainMemory;
  private DataModel      pc;
  private Integer        lastPC;
  private LabelMap       labelMap;
  private Vector<Region> regions;
  private String         loadedFile;
  private boolean        unsavedChanges;
  private List<StateChangedListener>  stateChangedListeners  = new Vector<StateChangedListener> ();
  private List<LengthChangedListener> lengthChangedListeners = new Vector<LengthChangedListener> ();
  Vector<UndoableEditListener>        undoListeners          = new Vector<UndoableEditListener> ();
  
  public Memory (AbstractISA anISA, DataModel aMainMemory, DataModel aPC) {
    isa                  = anISA;
    mainMemory           = aMainMemory;
    pc                   = aPC;
    lastPC               = null;
    labelMap             = new LabelMap ();
    regions              = new Vector<Region> ();
    loadedFile           = null;
    unsavedChanges       = false;
    mainMemory.addObserver   (this);
    pc.addObserver           (this);
    isa.setAddressToLabelMap (labelMap.addressToLabelMap);
    labelMap.addObserver     (this);
  }
  
  public Memory (Memory fromMemory, DataModel aMainMemory, DataModel aPC) {
    this (fromMemory.isa, aMainMemory, aPC);
  }
  
  public boolean valueEquals (Memory aMemory) {
    boolean equalSoFar = regions.size()==aMemory.regions.size();
    for (int i=0; equalSoFar && i<regions.size(); i++)
      equalSoFar &= regions.get(i).valueEquals (aMemory.regions.get(i));
    return equalSoFar;
  }
  
  public static enum LabelMapEventType { 
    ADD_OR_REMOVE, ADDRESS_CHANGE;
  }  
  
  public class LabelMap extends Observable implements MapModel {    
    private Map<Integer,String> addressToLabelMap;
    private Map<String,Integer> labelToAddressMap;
    public LabelMap () {
      addressToLabelMap = new HashMap<Integer,String> ();
      labelToAddressMap = new HashMap<String,Integer> ();      
    }
    public Integer getAddress (String label) {
      return labelToAddressMap.get (label);
    }
    
    public String getLabel (Integer address) {
      return addressToLabelMap.get (address);
    }
    
    public void changeAddresses (Vector <MemoryCell> cells) {
      if (cells.size () > 0) {
	for (MemoryCell c : cells) {
	  String label = c.getLabel ();
	  if (label != null && ! label.trim ().equals ("")) {
	    addressToLabelMap.remove (labelToAddressMap.get (label));
	    labelToAddressMap.remove (label);
	  }
	}
	for (MemoryCell c : cells) {
	  String label    = c.getLabel ();
	  Integer address = c.getAddress ();
	  if (label != null && ! label.trim ().equals ("")) {
	    addressToLabelMap.put (address, label);
	    labelToAddressMap.put (label, address);
	  }
	}
	setChanged ();
	notifyObservers (new DataModelEvent (DataModelEvent.Type.WRITE_BY_USER, cells.get (0).getAddress (), LabelMapEventType.ADDRESS_CHANGE.ordinal ()));
      }
    }
    
    public void add (MemoryCell cell) {
      LabelMapEventType changeType;
      String label   = cell.getLabel ();
      int    address = cell.getAddress ();
      String curLabelForAddress = addressToLabelMap.get (address);
      if (curLabelForAddress == null || ! curLabelForAddress.equals (label)) {
	if (curLabelForAddress != null) {
	  addressToLabelMap.remove (address);
	  labelToAddressMap.remove (curLabelForAddress);
	}
	Integer oldAddressForLabel = labelToAddressMap.get (label);
	if (oldAddressForLabel != null) {
	  addressToLabelMap.remove (oldAddressForLabel);
	  labelToAddressMap.remove (label);
	  changeType = LabelMapEventType.ADDRESS_CHANGE;
	} else
	  changeType = LabelMapEventType.ADD_OR_REMOVE;
	boolean hasLabel = label != null & !label.trim ().equals ("");
	if (hasLabel) {
	  addressToLabelMap.put (address, label);
	  labelToAddressMap.put (label, address);
	}
	if (hasLabel || curLabelForAddress != null || oldAddressForLabel != null) {
	  setChanged ();
	  notifyObservers (new DataModelEvent (DataModelEvent.Type.WRITE_BY_USER, address,changeType.ordinal ()));
	} 
      }
    }
    public void remove (MemoryCell cell) {
      String label = cell.getLabel ();
      if (label != null & ! label.trim ().equals ("")) {
	Integer address = labelToAddressMap.get (label);
        if (address!=null) {
          addressToLabelMap.remove (address);
          labelToAddressMap.remove (label);	
          setChanged ();
          notifyObservers (new DataModelEvent (DataModelEvent.Type.WRITE_BY_USER, address,LabelMapEventType.ADD_OR_REMOVE.ordinal ()));
        }
      }      
    }
    public void clear () {
      addressToLabelMap.clear ();
      labelToAddressMap.clear ();
    }
    public Object get (Object key) {
      return addressToLabelMap.get ((Integer) key);
    }
    public Object reverseGet (Object key) {
      return labelToAddressMap.get ((String) key);
    }
  }
  
  public LabelMap getLabelMap () {
    return labelMap;
  }
  
  public boolean hasLoadedFile () {
    return loadedFile != null;
  }
  
  public String getLoadedFilename () {
    String[] path = loadedFile.split ("[/\\\\:]");
    return path[path.length-1];
  }
  
  public String getLoadedPathname () {
    return loadedFile;
  }
  
  public boolean hasUnsavedChanges () {
    return unsavedChanges;
  }
  
  public interface StateChangedListener {
    public void memoryStateChanged ();
  }

  public void setChanged (boolean isChanged) {
    final boolean oldValue = unsavedChanges;
    unsavedChanges = isChanged;
    for (StateChangedListener l : stateChangedListeners)
      l.memoryStateChanged ();
    if (isChanged != oldValue) 
      addUndo (new Undo ("",false) {
	boolean value = oldValue;
	private void setValue () {
	 boolean v = unsavedChanges;
	 unsavedChanges = value;
	 value = v;
	 for (StateChangedListener l : stateChangedListeners)
	   l.memoryStateChanged ();
	}
	@Override
	public void undo () {
	 super.undo ();
	 setValue ();
	}
	@Override
	public void redo () {
	 super.redo ();
	 setValue ();
	}
      }); 
  }
  
  public void addStateChangedListener (StateChangedListener l) {
    stateChangedListeners.add (l);
  }
  
  public void removeStateChangedListener (StateChangedListener l) {
    stateChangedListeners.remove (l);
  }
  
  /**
   * Interface for classes that listen for length change events.
   */
  public interface LengthChangedListener {
    public static enum Type { INSERTED, DELETED };
    void changed (int address, int length, int lastAddress, Type type);
  }
  public void addLengthChangedListener (LengthChangedListener l) {
    lengthChangedListeners.add (l);
  }
  public void fireInserted (int address, int length, int lastAddress) {
    for (LengthChangedListener l : lengthChangedListeners)
      l.changed (address, length, lastAddress, LengthChangedListener.Type.INSERTED);
  }
  public void fireDeleted (int address, int length, int lastAddress) {
    for (LengthChangedListener l : lengthChangedListeners)
      l.changed (address, length, lastAddress, LengthChangedListener.Type.DELETED);
  }
  
  /**
   * Get a list of all memory regions.
   */
  public List<Region> getRegions () {
    return new Vector<Region> (regions);
  }
  
  /**
   * Get region for address
   */
  public Region regionForAddress (int address) {
    for (Region r : regions)
      if (address >= r.getAddress () && address <= (r.getAddress () + r.byteLength () - 1)) 
	return r;
    return null;
  }
  
  /**
   * Get vector of all regions that overlapp with specified range.
   */
  Vector<Region> regionsForAddressRange (int address, int length) {
    Vector<Region> selectedRegions = new Vector<Region> ();
    for (Region r : regions) {
      boolean endsAtOrAfterRegionStart   = address + length - 1 >= r.getAddress ();
      boolean startsAtOrBeforeRegionEnd  = address              <= r.getAddress () + r.byteLength () - 1;
      if (endsAtOrAfterRegionStart && startsAtOrBeforeRegionEnd)
	selectedRegions.add (r);
    }
    return selectedRegions;
  }
  
  /**
   * Add cell's label to symbol table without adding cell to memory.  Adding cell
   * to memory does automatically add its label to the symbol table.
   */
  public void addLabelOnly (MemoryCell cell) {
    labelMap.add (cell);
  }
  
  /**
   * Add cell to appropriate region; creating or extending the region, possibly coallescing two regions.
   */
  public void add (MemoryCell cell) {
    Region.Type type;
    if (cell instanceof Instruction)
      type = Region.Type.INSTRUCTIONS;
    else if (cell instanceof Datum)
      type = Region.Type.DATA;
    else
      throw new AssertionError ();
    Vector<Region> neighbours = regionsForAddressRange (cell.getAddress () - 1, cell.length () + 2);
    assert neighbours.size () <=2;
    if (neighbours.size () == 2 && type == neighbours.get (0).getType () && type == neighbours.get (1).getType ()) {
      neighbours.get (0).add (cell);
      neighbours.get (0).replace (null, neighbours.get (1).rows);
      regions.remove (neighbours.get (1));
    } else if (neighbours.size () == 2 && type == neighbours.get (1).getType ()) {
      neighbours.get (1).add (cell);
    } else if (neighbours.size () >= 1 && type == neighbours.get (0).getType ()) {
      neighbours.get (0).add (cell);
    } else {
      Region region;
      switch (type) {
	case INSTRUCTIONS:
	  region = new InstructionRegion (this);
	  break;
	case DATA:
	  region = new DataRegion (this);
	  break;
	default:
	  throw new AssertionError ();
      }
      regions.add (region);
      region.add  (cell);
    }
  }
  
  /**
   * Clear all regions 
   */
  public void clear () {
    regions.clear ();
    labelMap.clear ();
  }
  
  /**
   * Called when the PC or memory that backs a region changes.
   */
  public void update (Observable o, Object arg) {
    DataModelEvent event = (DataModelEvent) arg;
    Region         region;
    DataModelEvent ce;
    if (o == pc) {
      int newPC = (Integer) pc.getValueAt (event.getCells ().get(0).rowIndex, event.getCells ().get(0).columnIndex);
      if (lastPC==null || lastPC!=newPC) {
        if (lastPC != null) {
          region = regionForAddress (lastPC);
          if (region != null) {
            ce = region.update (new DataModelEvent (DataModelEvent.Type.CURSOR_CLEAR, lastPC, 0));
            tellObservers      (ce);
          }
        }
        lastPC = newPC;
        region = regionForAddress (lastPC);
        if (region != null) {
          ce = region.update (new DataModelEvent (DataModelEvent.Type.CURSOR_SET, lastPC, 0));
          tellObservers      (ce);
        }
      }
    } else if (o == mainMemory) {
      int          firstAddress    = event.getCells ().get(0).rowIndex;
      int          lastAddress     = event.getCells ().get(event.getCells ().size () - 1).rowIndex;
      List<Region> affectedRegions = regionsForAddressRange (firstAddress, lastAddress-firstAddress+1);
      for (Region r : affectedRegions) 
	r.update (event);
    } else if (o == labelMap) {
      for (Region r : regions) {
	switch (LabelMapEventType.values () [event.getCells ().get (0).columnIndex]) {
	  case ADDRESS_CHANGE:
	    r.syncMemoryFromAsm ();
	    break;
	  case ADD_OR_REMOVE:
	    r.syncAsmFromMemory ();
	    break;
	  default:
	    throw new AssertionError ();
	}
      }
    } else 
      throw new ClassCastException ();
  }
  
  /**
   * Get address alinged to next valid instruction.
   */
  public int getAlignedInstructionAddress (int addr) {
    Region region   = regionForAddress (addr);
    MemoryCell cell = region.getCellContainingAddress (addr);
    return cell.getAddress () == addr? addr: cell.getAddress () + cell.length ();
  }
  
  /**
   * Get the instruction definition for specified opCode from the ISA.
   */
  AbstractISA.InstructionDef getInstructionDefForOpCode (int opCode) {
    return isa.getDefForOpCode (opCode);
  }
  
  /**
   * Get the instruction definition for specified value from the ISA.
   */
  AbstractISA.InstructionDef getInstructionDefForValue (int address) {
    return isa.getDefForValue (new BitStream (this, address));
  }  
  
  /**
   * Get Placeholder instruction defintion from ISA.
   */
  AbstractISA.InstructionDef getPlaceholderInstructionDef () {
    return isa.getPlaceholderInstructionDef ();
  }
  
  /**
   * Get Value of placeholder instruction from ISA.
   */
  BitString getPlaceholderInstructionValue () {
    return isa.getPlaceholderInstructionValue ();
  }
  
  /**
   * Get ISA Name
   */
  
  public String getIsaName () {  // called by UI ... some strange entanglments here XXX
    return isa.getName ();
  }
  
  /**
   * Switch between machine and user byte ordering based on ISA endianness.
   */
  public int normalizeEndianness (int num, int byteSize) {
    return isa.normalizeEndianness (num, byteSize);
  }
  
  /**
   * Exception for syntax errors in asm and mac input files.
   */
  public class InputFileSyntaxException extends Exception {
    String  message;
    boolean knowLineNumber;
    int     lineNumber;
    InputFileSyntaxException (String aMessage, boolean aKnowLineNumber, int aLineNumber) {
      lineNumber     = aLineNumber;
      knowLineNumber = aKnowLineNumber;
      message        = aMessage;
    }
    InputFileSyntaxException (String aMessage, int aLineNumber) {
      this (aMessage, true, aLineNumber);
    }
    InputFileSyntaxException (int aLineNumber) {
      this (null, true, aLineNumber);
    }
    InputFileSyntaxException (String aMessage) {
      this (aMessage, false, 0);
    }
    InputFileSyntaxException () {
      this (null, false, 0);
    }
    public String toString () {
      if (knowLineNumber)
	return String.format ("%s online %d.", message!=null? message : "", lineNumber);
      else
	return String.format ("%s.", message!=null? message : "");
    }
  }
  
  /**
   * Load a single line of assembly code into memory.  Format is determined by an ISA (extension of AbstractISA).
   */
  void loadAssemblyLine (int address, String label, String statement, String comment) throws AbstractAssembler.AssemblyException {
    isa.assembleLine (address, label, statement, comment, this);
  }
  void checkAssemblyLineSyntax (int address, String label, String statement, String comment) throws AbstractAssembler.AssemblyException {
    isa.checkAssemblyLineSyntax (address, label, statement, comment, this);
  }
  void checkAssemblyLabelSyntax (String label) throws AbstractAssembler.AssemblyException {
    isa.checkAssemblyLabelSyntax (label, this);
  }
  
  /**
   * Checkpoint Data
   */
  public void checkpointData (boolean changesMemory) {
    for (Region r : regions)
      if (r.getType () == Region.Type.DATA)
	((DataRegion) r).checkpoint ();
    if (changesMemory)
      setChanged (true);
  }
  
  /**
   * Restore from checkpoint
   */
  public void restoreDataFromCheckpoint () {
    for (Region r : regions)
      if (r.getType () == Region.Type.DATA)
	((DataRegion) r).restoreFromCheckpoint ();
  }
  
  /**
   * Save to file
   */
  public void saveToFile (String filename) throws FileNotFoundException {
    if (filename == null)
      filename = loadedFile;
    if (filename != null) {
      PrintStream ps = new PrintStream (filename);
      for (Region r : regions) {
	ps.print (String.format (".pos 0x%x\n", r.getAddress ()));
	for (MemoryCell c : r.getSavableRows ()) {
	  String label   = c.getLabel ();
	  String comment = c.getComment ();
	  ps.print (String.format ("%-16s %-24s %s\n", 
				   label != null   && ! label.equals   ("")? label.concat (":")    : "",
				   c.toAsm (),
				   comment != null && ! comment.equals ("")? "# ".concat (comment) : ""));
	}
      }
      ps.close ();
      setChanged (false);
    }
  }
  
  /**
   * Exception for errors in put file type.
   */
  public static class FileTypeException extends Exception {}
  
  /**
   * Load a file into memory where type of file is determined by it's extension (i.e., *.s for assembly langauge and *.gold for machine code).
   *
   * @throws  FileTypeException   indicates unknown file type
   */
  public void loadFile (String filename) throws InputFileSyntaxException, FileNotFoundException, IOException, FileTypeException {
    clear ();
    if (filename.length ()>=3 && filename.substring (filename.length () - 2).equals (".s")) {
      loadAssemblyFile (filename);
      loadedFile = filename;
      setChanged (false);
    } else if (filename.length ()>=9 && filename.substring (filename.length () -8).equals (".machine")) {
      loadMachineFile (filename);
      loadedFile = filename.substring (0, filename.length () - 7).concat ("s");
      setChanged (true);
    } else
      throw new FileTypeException ();
    for (StateChangedListener l : stateChangedListeners)
      l.memoryStateChanged ();
    checkpointData (false);
  }
  
  /**
   * Load assembly code file into memory. Format is determined by an ISA (extension of AbstractISA).
   *
   * @param   filename			pathname of file.
   * @throws  InputFileSyntaxException  indicates assembly-code syntax error.
   * @see     AbstractISA
   */
  private void loadAssemblyFile (String filename) throws InputFileSyntaxException, FileNotFoundException, IOException {
    try {
       isa.assembleFile (filename, this);
    } catch (AbstractAssembler.AssemblyException e) {
      throw new InputFileSyntaxException (e.toString ());
    }
  }
  
  /**
   * Load machine code file. 
   *
   * @param   aFilename  pathname of file
   */
  private void loadMachineFile (String aFilename) throws InputFileSyntaxException, java.io.FileNotFoundException, java.io.IOException {
    if (aFilename==null)
      throw new java.io.FileNotFoundException ();
    File aFile = new File (aFilename);
    int curLine = 0;
    
    Pattern linePattern = Pattern.compile ("^\\s*(([0-9a-fA-F]{1,8}):)?\\s*((([0-9a-fA-F]{4})\\s*([0-9a-fA-F]{8})?)|(([0-9a-fA-F]{8}))|())\\s*(#.*)?$");
    BufferedReader exec = new BufferedReader ( new InputStreamReader ( new FileInputStream (aFile)));
    int startAddress = 0;
    int curAddress = 0;
    String aGroup;
    String comment;
    
    try {
      while (exec.ready()) {
	int lineLength = 0;
	String aLine = (exec.readLine());
	Matcher lineMatcher = linePattern.matcher (aLine);
	curLine += 1;
	comment = "";
	if (lineMatcher.matches()) {
	  if (lineMatcher.group (1) != null) {
	    // address label
	    startAddress = Integer.parseInt (lineMatcher.group (2), 16);
	    curAddress = startAddress;
	  }
	  if (lineMatcher.group (5) != null) {
	    // first two-bytes of an instruction
	    int val = Integer.parseInt (lineMatcher.group (5), 16);
	    byte[] valBytes = { (byte)(val>>8), (byte)(val) };
	    setValueAt (new Byte (valBytes[0]), curAddress+lineLength, 1);
	    setValueAt (new Byte (valBytes[1]), curAddress+lineLength+1, 1);
	    lineLength = 2;
	  }
	  if (lineMatcher.group (6) != null) {
	    // second four-bytes of an instruction
	    int val = (int) Long.parseLong (lineMatcher.group (6), 16);
	    byte[] valBytesHi = { (byte)(val>>24), (byte)(val>>16) };
	    byte[] valBytesLo = { (byte)(val>>8), (byte)(val) };
	    setValueAt (new Byte (valBytesHi[0]), curAddress+lineLength, 1);
	    setValueAt (new Byte (valBytesHi[1]), curAddress+lineLength+1, 1);
	    setValueAt (new Byte (valBytesLo[0]), curAddress+lineLength+2, 1);
	    setValueAt (new Byte (valBytesLo[1]), curAddress+lineLength+3, 1);
	    lineLength = 6;
	  }
	  if (lineMatcher.group (8) != null) {
	    // data
	    int val = (int) Long.parseLong (lineMatcher.group (8), 16);
	    byte[] valBytesHi = { (byte)(val>>24), (byte)(val>>16) };
	    byte[] valBytesLo = { (byte)(val>>8), (byte)(val) };
	    setValueAt (new Byte (valBytesHi[0]), curAddress+lineLength, 1);
	    setValueAt (new Byte (valBytesHi[1]), curAddress+lineLength+1, 1);
	    setValueAt (new Byte (valBytesLo[0]), curAddress+lineLength+2, 1);
	    setValueAt (new Byte (valBytesLo[1]), curAddress+lineLength+3, 1);
	    lineLength = 4;
	  }
	  if (lineMatcher.group (10) != null) {
	    comment = lineMatcher.group (10).substring (1).trim ();
	  }
	  if (lineLength==2 || lineLength==6) {
	    Instruction ins = Instruction.valueOfMemory (this, curAddress, "", comment);
	    if (ins != null)
	      add (ins);
	    else
	      throw new InputFileSyntaxException ("Invalid instruction in input file", curLine);
	  } else if (lineLength==4)
	    add (Datum.valueOfMemory (this, curAddress, lineLength, "", comment));
	  curAddress += lineLength;
	} else {
	  throw new InputFileSyntaxException ("Invalid format in input file", curLine);
	}
      }
    } catch (IndexOutOfBoundsException e) {
      throw new InputFileSyntaxException ("Illegal address in input file", curLine);
    } finally {
      if (exec!=null)
	exec.close ();
    }
  }
  
  ////////////////
  // Undo Support
  
  public void addUndoableEditListener (UndoableEditListener l) {
    undoListeners.add (l);
  }
  
  protected void addUndo (UndoableEdit e) {
    for (UndoableEditListener l : undoListeners)
      l.undoableEditHappened (new UndoableEditEvent (this, e));
  }
  
  static class Undo extends AbstractUndoableEdit {
    String  presentationName;
    boolean isSignificant;
    public Undo (String name, boolean sig) {
      presentationName = name;
      isSignificant    = sig;
    }
    @Override
    public String getPresentationName () {
      return presentationName;
    }
    @Override
    public boolean isSignificant () {
      return isSignificant;
    }
  }

  ///////////////
  // Data Model Support
  
  public void addObserver (Observer anObserver) {
    super.addObserver (anObserver);
    mainMemory.addObserver (anObserver);
  }
  
  public Class getColumnClass (int columnIndex) {
    return mainMemory.getColumnClass (columnIndex);
  }
  
  public int getColumnCount () {
    return mainMemory.getColumnCount ();
  }
  
  public String getColumnName (int columnIndex) {
    return mainMemory.getColumnName (columnIndex);
  }
  
  public int getRowCount () {
    return mainMemory.getRowCount ();
  }
  
  public Object getValueAt (int rowIndex, int columnIndex) {
    return mainMemory.getValueAt (rowIndex, columnIndex);
  }
  
  public boolean isCellEditable (int rowIndex, int columnIndex) {
    return mainMemory.isCellEditable (rowIndex, columnIndex);
  }
  
  @Override
  public void setValueAt (Object[] aValue, int rowIndex, int columnIndex) {
    mainMemory.setValueAt (aValue, rowIndex, columnIndex);
  }
  
  @Override
  public void setValueAt (Object aValue, int rowIndex, int columnIndex) {
    mainMemory.setValueAt (aValue, rowIndex, columnIndex);
  }
  
  @Override
  public void setValueAtByUser (Object aValue, int rowIndex, int columnIndex) {
    mainMemory.setValueAtByUser (aValue, rowIndex, columnIndex);
  }
  
  @Override
  public void setValueAtByUser (Object[] aValue, int rowIndex, int columnIndex) {
    mainMemory.setValueAtByUser (aValue, rowIndex, columnIndex);
  }
}