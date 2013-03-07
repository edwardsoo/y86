package isa;

import java.util.List;
import java.util.Vector;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.HashSet;
import util.AbstractDataModel;
import util.DataModel;
import util.DataModelEvent;
import util.TableCellIndex;
import util.BitString;

/**
 * The code description of a contiguous range of memory.  Maintains mapping between memory values
 * and ISA definitions that underly them.  Proivdes DataModel interface that keeps Gui's
 * view of ISA information in synch with values in memory.
 */

public abstract class Region extends AbstractDataModel {
  private Type                  type;
  Memory                        memory;
  Vector<MemoryCell>            rows;
  SortedMap<Integer,MemoryCell> map;
  
  public static enum Type { INSTRUCTIONS, DATA };
  
  Region (Memory aMemory, Type aType) {
    memory = aMemory;
    type   = aType;
    rows   = new Vector<MemoryCell> ();
    map    = new TreeMap<Integer,MemoryCell> ();
  }
  
  public boolean valueEquals (Region aRegion) {
    boolean equalSoFar = rows.size() == aRegion.rows.size();
    for (int i=0; equalSoFar && i<rows.size(); i++)
      equalSoFar &= rows.get(0).valueEquals (aRegion.rows.get(0));
    return equalSoFar;
  }
  
  public Vector <MemoryCell> getSavableRows () {
    return rows;
  }
  
  public Type getType () {
    return type;
  }
  
  public int length () {
    return rows.size ();
  }
  
  public int getAddress () {
    return map.size()>0? map.get (map.firstKey()).getAddress () : 0;
  }
  
  public int byteLength () {
    if (map.size () > 0) {
      MemoryCell lastCell = map.get (map.lastKey ());
      return (lastCell.getAddress () + lastCell.length () - getAddress ());
    } else
      return 0;
  }
  
  public MemoryCell getCellForRowIndex (int rowIndex) {
    return rows.get (rowIndex);
  }
  
  public int getRowIndexForAddress (int address) {
    return rows.indexOf (map.get (address));
  }
  
  public MemoryCell getCellContainingAddress (int address) {
    MemoryCell cell = map.get (address);
    if (cell != null)
      return cell;
    else
      for (MemoryCell c : rows)
	if (address >= c.getAddress () && address <= c.getAddress () + c.length () - 1)
	  return c;
    return null;
  }
  
  /**
   * Change the address of region's cells starting with specified row by adding
   * addressDelta to each cell address.  Adjusts label addresses, re-computes asm
   * so that instructions that refer to those labels use updated value, and
   * adjust breakpoints to the new cell addresses.  
   */
  private void changeCellAddresses (int startingRow, int addressDelta) {
    assert startingRow >= 0 && startingRow < rows.size ();
    for (int r = startingRow; r < rows.size (); r ++) {
      MemoryCell cell  = rows.get (r);
      int        adr   = cell.getAddress ();
      map.remove (adr);
      cell.setAddress (adr + addressDelta);
    }
    for (int r = startingRow; r < rows.size (); r++) {
      MemoryCell cell = rows.get (r);
      map.put (cell.getAddress (), cell);
      cell.syncToMemory ();
      tellObservers (new DataModelEvent (DataModelEvent.Type.WRITE_BY_USER, r, 0));
    }
    Vector <MemoryCell> cells = new Vector <MemoryCell> ();
    for (int r = startingRow; r < rows.size () ; r++) 
      cells.add (rows.get (r));
    memory.getLabelMap ().changeAddresses (cells);
    if (addressDelta > 0)
      memory.fireInserted (rows.get(startingRow).getAddress()-addressDelta, addressDelta,  getAddress ()+byteLength ()-1);
    else
      memory.fireDeleted  (rows.get(startingRow).getAddress(), -addressDelta, getAddress()+byteLength()-1-addressDelta);
  }
  
  /**
   * Insert new row in region, adding a place-holder cell and adjusting the address of all
   * subsequent cells.
   * @return  true iff row is inserted.
   */
  @Override
  public boolean insertRow (int row) {
    assert row >= 0 && row <= rows.size ();
    int adr         = row < rows.size ()? rows.get (row).getAddress () : rows.get (row-1).getAddress () + rows.get (row-1).length ();
    MemoryCell cell = newPlaceholderCell (adr);
    memory.addUndo (new UndoInsert (row, cell));
    insertCell (row, cell);
    memory.setChanged (true);
    return true;
  }
  
  private void insertCell (int row, MemoryCell cell) {
    tellObservers (new DataModelEvent (DataModelEvent.Type.CHANGING, getAllCellsInRow (row)));
    rows.insertElementAt (cell, row);
    if (row+1 < rows.size ()) 
      changeCellAddresses (row+1, cell.length ());
    map.put (cell.getAddress (), cell);
    memory.getLabelMap ().add (cell);
    fireByteLengthChanged ();
    cell.syncToMemory ();
    tellObservers (new DataModelEvent (DataModelEvent.Type.ROWS_INSERTED, row, row));
  }
  
  /**
   * Report whether an insert of the specified row would succeed given the current state
   * of the system.  
   */
  @Override
  public boolean canInsertRow (int row) {
    return true;
  }
  
  /**
   * Delete row in region.
   * @return true iff row is deleted.
   */
  @Override 
  public boolean deleteRow (int row) {
    assert row >= 0 && row < rows.size ();
    if (canDeleteRow (row)) {
      MemoryCell cell = rows.get (row);
      memory.addUndo (new UndoDelete (row, cell));
      deleteCell (row);
      memory.setChanged (true);
      return true;
    } else
      return false;
  }
  
  private void deleteCell (int row) {
    assert canDeleteRow (row);
    tellObservers (new DataModelEvent (DataModelEvent.Type.CHANGING, getAllCellsInRow (row)));
    MemoryCell cell = rows.get (row);
    map.remove (cell.getAddress ());
    rows.removeElementAt (row);
    if (row < rows.size ())
      changeCellAddresses (row, -cell.length ());
    memory.getLabelMap ().remove (cell);
    fireByteLengthChanged ();
    tellObservers (new DataModelEvent (DataModelEvent.Type.ROWS_DELETED, row, row));    
  }
  
  /**
   * Report whether delete of specified row would suceed given the current state of
   * the system.
   */
  public boolean canDeleteRow (int row) {
    return rows.size () > 1;
  }
    
  /**
   * Add cell to region.  Ensure that memory reflects the value in the cell.  If the cell is 
   * already in the region, writing to memory will cause an up call to the region that will 
   * update the cell to reflect memory.
   */
  protected void add (MemoryCell cell) {
    assert (type==Type.INSTRUCTIONS && (cell instanceof Instruction)) || (type==Type.DATA && (cell instanceof Datum));
    Integer    cellAddress  = cell.getAddress ();
    MemoryCell existingCell = map.get (cellAddress);
    if (existingCell==null || ! existingCell.equals (cell)) {
      int oldRegionLength = byteLength ();
      if (existingCell != null) {
	int oldLength = existingCell.length ();
	existingCell.copyFrom (cell);
	cell = existingCell;
	int row = rows.indexOf (cell);
	if (row != rows.size () - 1 && oldLength != cell.length ())
	  changeCellAddresses (row + 1, cell.length () - oldLength);
	cell.syncToMemory ();
	tellObservers (new DataModelEvent (DataModelEvent.Type.WRITE_BY_USER, getAllCellsInRow (map.size ())));
      } else {
	map.put (cellAddress, cell);
	if (map.lastKey () == cellAddress) {
	  rows.add (cell);
	  tellObservers (new DataModelEvent (DataModelEvent.Type.WRITE_BY_USER, getAllCellsInRow (map.size ())));
	} else
	  rebuildRows ();
      }
      cell.syncToMemory ();
      memory.getLabelMap ().add (cell);
      if (byteLength () != oldRegionLength)
	fireByteLengthChanged ();
    }
  }
  
  protected void replace (Vector<MemoryCell> removeCells, Vector<MemoryCell> addCells) {
    if (removeCells != null)
      for (MemoryCell cell : removeCells) {
	map.remove (cell.getAddress ());
	memory.getLabelMap ().remove (cell);
      }
    if (addCells != null)
      for (MemoryCell cell : addCells) {
	map.put (cell.getAddress (), cell);
	memory.getLabelMap ().add (cell);
      }
     rebuildRows ();
  }
  
  /**
   * Called to indicate a change in the status of an underlying data source.
   * Read-access notifications are not fowarded.  Write-accesses are fowarded
   * only if memory cells have actually changed.
   *
   * May be called with list of cells, not all of which are for this region
   * and some of which affect the same row.  Process only our events and
   * only once per row.
   *
   * @return event generated by this update and sent to the region's observers
   *         translated into the memory's namespace.
   */
  DataModelEvent update (DataModelEvent event) {
    boolean changed = false;
    HashSet<MemoryCell> affectedCells = new HashSet<MemoryCell> ();
    List<TableCellIndex> mc           = new Vector<TableCellIndex> ();
    for (TableCellIndex tableCell : event.getCells ()) {
      int address = tableCell.rowIndex;
      if (type == Region.Type.DATA)
	address = (address >>> 2) << 2;
      if (address >= getAddress () && address <= getAddress () + byteLength () - 1) {
	MemoryCell memoryCell = getCellContainingAddress (address);
	if (memoryCell != null) {
	  affectedCells.add (memoryCell);
	  if (! isMemoryValueColumn (tableCell.columnIndex))
	    memory.setChanged (true);
	}
      }
    }
    for (MemoryCell cell : affectedCells) {
      if (event.getType () == DataModelEvent.Type.WRITE || event.getType () == DataModelEvent.Type.WRITE_BY_USER) {
	int len = cell.length ();
	if (cell.syncFromMemory ()) {
	  changed = true;
	  if (cell.length () != len)
	    memorySyncChangedCellLength (cell, len);
	  if (type == Type.INSTRUCTIONS)
	    memory.setChanged (true);
	}
      } 
      tellObservers (new DataModelEvent (event.getType (), getAllCellsInRow (rows.indexOf (cell))));
      for (int i=0; i<cell.length (); i++)
        mc.add (new TableCellIndex (cell.getAddress ()+i, 1));
    }
    return new DataModelEvent (event.getType (), mc);
  }
  
  abstract boolean isMemoryValueColumn (int columnIndex);
  abstract void memorySyncChangedCellLength (MemoryCell cell, int oldLength);
  
  void rebuildRows () {
    Vector<MemoryCell> oldRows = rows;
    rows = new Vector<MemoryCell> (map.values ());
    for (MemoryCell cell : rows) {
      int newIndex = rows.indexOf (cell);
      if (newIndex >= oldRows.size () || ! cell.equals (oldRows.get (newIndex))) 
	tellObservers (new DataModelEvent (DataModelEvent.Type.WRITE_BY_USER, getAllCellsInRow (newIndex)));
     }
  }
  
  /**
   * Ensure that memory and asm are in sync treating asm as the truth.
   * Called when a label value changes.
   *
   */
  void syncMemoryFromAsm () {
    for (MemoryCell cell : rows)
      if (cell.memoryResyncedFromAsm () && getAsmColumn () >=0) 
	tellObservers (new DataModelEvent (DataModelEvent.Type.WRITE_BY_USER, rows.indexOf (cell), getAsmColumn ()));
  }
  
  /**
   * Ensure that memory and asm are in sync treating memory as the truth.  
   * Called when a label is removed.
   */
  void syncAsmFromMemory () {
    for (MemoryCell cell : rows)
      if (cell.asmResyncedFromMemory () && getAsmColumn () >=0) 
	tellObservers (new DataModelEvent (DataModelEvent.Type.WRITE_BY_USER, rows.indexOf (cell), getAsmColumn ()));
  }
  
  List<TableCellIndex> getAllCellsInRow (int row) {
    List<TableCellIndex> cells = new Vector<TableCellIndex> ();
    for (int i=0; i<getColumnCount (); i++)
      cells.add (new TableCellIndex (row, i));
    return cells;
  }
  
  public int getRowCount () {
    return rows.size ();
  }

  public class AssemblyString {
    int     address;
    boolean changed = false;
    String label, value, comment;
    public AssemblyString (int anAddress, String aLabel, String aValue, String aComment) {
      address = anAddress;
      label   = aLabel;
      value   = aValue;
      comment = aComment;
    }
    private String selectedValue () {
      return value.trim ().equals ("nop")? "" : value.trim ();
    }
    public String toString () {
      return value;
    }
    public String toSelectedString () {
      return selectedValue ();
    }
    public void setValue (String text) throws AbstractAssembler.AssemblyException {
      if (! text.trim ().equals (value.trim ()) && ! text.trim ().equals (selectedValue ())) {
	memory.checkAssemblyLineSyntax (address, label, text, comment);
	value   = text;
	changed = true;
      }
    }
    public boolean hasChanged () {
      return changed;
    }
  }
  
  public class LabelString {
    int    address;
    String value;
    public LabelString (int anAddress, String aValue) {
      address = anAddress;
      value   = aValue;
    }
    public String toString () {
      return value;
    }
    public void setValue (String text) throws AbstractAssembler.AssemblyException {
      if (! text.trim ().equals (value)) {
	Integer curAddress = memory.getLabelMap ().getAddress (text);
	if (curAddress != null && curAddress != address)
	  throw new AbstractAssembler.AssemblyException ("Duplicate label.");
	else {
	  memory.checkAssemblyLabelSyntax (text);
	  value = text;
	}
      }
    }
  }
  
  public static interface ByteLengthChangedListener {
    public void byteLengthChanged ();
  }
  
  private Vector <ByteLengthChangedListener> byteLengthChangedListeners = new Vector <ByteLengthChangedListener> ();

  public void addByteLengthChangedListener (ByteLengthChangedListener l) {
    byteLengthChangedListeners.add (l);
  }
  
  protected void fireByteLengthChanged () {
    for (ByteLengthChangedListener l : byteLengthChangedListeners)
      l.byteLengthChanged ();
  }
  
  abstract MemoryCell newPlaceholderCell (int address);
  public abstract int getAsmColumn ();  
  
  enum UndoChangeType { LABEL, VALUE, COMMENT };

  class UndoChange extends Memory.Undo {
    int            row;
    int            column;
    UndoChangeType type;
    Object         value;
    public UndoChange (String aName, int aRow, int aColumn, UndoChangeType aType, Object aValue) {
      super (aName, true);
      type   = aType;
      row    = aRow;
      column = aColumn;
      value  = aValue;
    }
    public UndoChange (String aName, int aRow, int aColumn, UndoChangeType aType) {
      super (aName, true);
      type   = aType;
      row    = aRow;
      column = aColumn;
      value  = getCellValue ();
    }
    private Object getCellValue () {
      MemoryCell cell = rows.get (row);
      if (type == UndoChangeType.LABEL)
	return cell.getLabel ();
      else if (type == UndoChangeType.VALUE)
	return cell.getValue ();
      else if (type == UndoChangeType.COMMENT)
	return cell.getComment ();
      else
	throw new AssertionError (type);
    }
    private void setCellValue () {
      MemoryCell cell = rows.get (row);
      tellObservers (new DataModelEvent (DataModelEvent.Type.CHANGING, row, column));
      Object v = getCellValue ();
      if (type == UndoChangeType.LABEL)
	cell.setLabel ((String) value);
      else if (type == UndoChangeType.VALUE) {
	cell.setValue ((BitString) value);
	cell.syncToMemory ();
      } else if (type == UndoChangeType.COMMENT)
	cell.setComment ((String) value);
      else
	throw new AssertionError (type);
      value = v;
      tellObservers (new DataModelEvent (DataModelEvent.Type.WRITE_BY_USER, row, column));
    }
    @Override
    public void undo () {
      super.undo ();
      setCellValue ();
    }
    @Override 
    public void redo () {
      super.redo ();
      setCellValue ();
      memory.setChanged (true);
    }
  }
  
  class UndoInsert extends Memory.Undo {
    int        row;
    MemoryCell cell;
    public UndoInsert (int aRow, MemoryCell aCell) {
      super (String.format ("Insert Row %d",aRow), true);
      row  = aRow;
      cell = aCell;
    }
    @Override
    public void undo () {
      super.undo ();
      deleteCell (row);
    }
    @Override
    public void redo () {
      super.redo ();
      insertCell (row, cell);
      memory.setChanged (true);
    }
  }
  
  class UndoDelete extends Memory.Undo {
    int        row;
    MemoryCell cell;
    public UndoDelete (int aRow, MemoryCell aCell) {
      super (String.format ("Delete Row %d",aRow), true);
      row  = aRow;
      cell = aCell;
    }
    @Override
    public void undo () {
      super.undo ();
      insertCell (row, cell);
    }
    @Override
    public void redo () {
      super.redo ();
      deleteCell (row);
      memory.setChanged (true);
    }
  }
}


