package isa;

import util.DataModelEvent;

/**
 * Region of data.
 *
 * DataModel columns: address, comment.
 */

public class DataRegion extends Region {
  
  public DataRegion (Memory aMemory) {
    super (aMemory, Type.DATA);
  }
  
  /**
   * Create a new placeholder instruction.
   */
  @Override
  MemoryCell newPlaceholderCell (int address) {
    return Datum.valueOf (memory, address, 0, "", "");
  }
  
  public void checkpoint () {
    for (MemoryCell c : rows)
      c.checkpointValue ();
  }
  
  public void restoreFromCheckpoint () {
    for (MemoryCell c : rows)
      c.restoreValueFromCheckpoint ();
  }
  
  public class Value {
    Integer value;
    Value (Integer aValue) {
      value = aValue;
    }
    public String toString () {
      return value.toString ();
    }
    public Integer toNumber () {
      return value;
    }
    public void setValue (String text) {
      setValue (text, 10);
    }
    public void setValue (String text, int radix) {
      if (text.matches ("\\s*-?[0-9]+\\s*"))
	value = Integer.valueOf (text, radix);
      else if (text.matches ("\\s*-?0x[0-9,a-f,A-F]+\\s*"))
	value = Integer.valueOf (text.split ("0x",2)[1], 16);
      else {
	Integer v = (Integer) memory.getLabelMap ().reverseGet (text);
	if (v != null)
	  value = v;
	else
	  throw new NumberFormatException ();
      }
    }
  }
  
  void memorySyncChangedCellLength (MemoryCell cell, int oldLength) {
    assert false;
  }
  
  @Override public int getAsmColumn () {
    return 2;
  }
  public Class getColumnClass (int columnIndex) {
    if (columnIndex==0)
      return Integer.class;
    else if (columnIndex==1)
      return Value.class;
    else if (columnIndex==2)
      return String.class;
    else if (columnIndex==3)
      return LabelString.class;
    else if (columnIndex==4)
      return String.class;
    else 
      throw new AssertionError ();
  }
  
  @Override
  public boolean isMemoryValueColumn (int columnIndex) {
    return columnIndex == 0 || columnIndex == 1 || columnIndex == 2;
  }
  
  public int getColumnCount () {
    return 5;
  }
  
  public String getColumnName (int columnIndex) {
    if (columnIndex==0)
      return "Addr";
    else if (columnIndex==1)
      return "As Int";
    else if (columnIndex==2)
      return "As Ref";
    else if (columnIndex==3)
      return "Label";
    else if (columnIndex==4)
      return "Comment";
    else
      throw new AssertionError ();
  }
  
  public Object getValueAt (int rowIndex, int columnIndex) {
    MemoryCell cell  = rows.get (rowIndex);
    if (columnIndex==0)
      return new Integer (cell.getAddress ());
    else if (columnIndex==1)
      return new Value (memory.normalizeEndianness ((int) cell.getValue ().getValue (), cell.length ()));
    else if (columnIndex==2) {
      String label = ((Datum)cell).valueAsLabel ();
      return label!=null? label : "";
    } else if (columnIndex==3)
      return new LabelString (cell.getAddress (), cell.getLabel ());
    else if (columnIndex==4)
      return cell.getComment ();
    else
      throw new AssertionError ();
  }
  
  public boolean isCellEditable (int rowIndex, int columnIndex) {
    if (columnIndex==0)
      return false;
    else if (columnIndex==1)
      return true;
    else if (columnIndex==2)
      return false;
    else if (columnIndex==3)
      return true;
    else if (columnIndex==4)
      return true;
    else
      throw new AssertionError ();
  }
  
  public void setValueAt (Object aValue, int rowIndex, int columnIndex) {
    if (rowIndex < getRowCount ()) {
      MemoryCell cell = rows.get (rowIndex);
      assert cell != null;
      if (columnIndex==1) {
	int  v   = memory.normalizeEndianness (((Value) aValue).toNumber (), cell.length ());
	Byte b[] = new Byte [cell.length ()];
        for (int i=b.length-1; i>=0; i--) {
          b[i] = (byte) v;
          v >>= 8;
        }
	boolean changed = false;
	for (int i=0; i<b.length; i++)
	  if ((Byte) memory.getValueAt (cell.getAddress () + i, 1) != b[i]) {
	    changed = true;
	    break;
	  }
	if (changed) {
	  memory.addUndo (new UndoChange (String.format ("Value Change at 0x%x\n", cell.getAddress ()), rowIndex, 0, UndoChangeType.VALUE));
	  memory.setValueAtByUser (b, cell.getAddress (), 1);	  
	}
      } else if (columnIndex==3) {
	String newValue = ((LabelString) aValue).toString ().trim ();
	if (! newValue.equals (cell.getLabel ())) {
	  memory.addUndo (new UndoChange (String.format ("Label Change at 0x%x\n", cell.getAddress ()), rowIndex, 2, UndoChangeType.LABEL));
	  cell.setLabel (newValue);
	  memory.setChanged (true);
	}
      } else if (columnIndex==4) {
	String newValue = ((String) aValue).trim ();
	if (! newValue.equals (cell.getComment())) {
	  memory.addUndo (new UndoChange (String.format ("Comment Change at 0x%x\n", cell.getAddress ()), rowIndex, 3, UndoChangeType.COMMENT));
	  cell.setComment (newValue);
	  memory.setChanged (true);
	}
      } else 
	throw new AssertionError ();
    }
  }
}


