package isa;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;
import util.DataModelEvent;
import util.TableCellIndex;
import util.BitString;

/**
 * Region of instructions.
 *
 * DataModel columns: address, mac, asm, comment.
 */

public class InstructionRegion extends Region {
  
  private Map<InstructionMemoryValue,Instruction> trash;
  
  public InstructionRegion (Memory aMemory) {
    super (aMemory, Type.INSTRUCTIONS);
    trash = new HashMap<InstructionMemoryValue,Instruction> ();
  }
  
  @Override 
  protected void replace (Vector<MemoryCell> oldCells, Vector<MemoryCell> newCells) {
    super.replace (oldCells, newCells);
    if (oldCells != null)
      for (MemoryCell cell : oldCells) 
	trash.put (new InstructionMemoryValue (cell), (Instruction) cell);
  }
  
  /**
   * Create a new placeholder instruction.
   */
  @Override
  MemoryCell newPlaceholderCell (int address) {
    return Instruction.valueOfPlaceholder (memory, address, "", "");
  }
  
  /**
   * Called by Region update().
   */
  @Override
  void memorySyncChangedCellLength (MemoryCell cell, int oldLength) {
    rebuild (cell.getAddress () + cell.length (), cell.getAddress () + oldLength);
  }
  
  /**
   * Changing an instruction cell can change the length of the instruciton.  If so,
   * subsequent instructions need to be rebuilt.  This method performs this operation, 
   * rebuilding as necessary until the address of rebuilt instructions aligns with an 
   * existing instruction.
   */
  void rebuild (int newAdr, int oldAdr) {
    Vector<MemoryCell> graftIn  = new Vector<MemoryCell> ();
    Vector<MemoryCell> graftOut = new Vector<MemoryCell> ();
    String             label    = "";
    String             comment  = "";
    int                endAdr   = getAddress () + byteLength () - 1;
    while (newAdr != oldAdr) {
      while (newAdr > oldAdr && oldAdr <= endAdr) {
	Instruction oldIns = (Instruction) map.get (oldAdr);
	if (oldIns == null)
	  break;
	if (label.equals ("")) 
	  label = oldIns.getLabel ();
	if (comment.equals (""))
	  comment = oldIns.getComment ();
	graftOut.add (oldIns);
	oldAdr += oldIns.length ();
      }
      if (newAdr > endAdr)
	break;
      while (newAdr < oldAdr) {
	Instruction newIns = Instruction.valueOfMemory (memory, newAdr, label, comment);
	if (newIns == null)
	  newIns = Instruction.valueOfPlaceholder (memory, newAdr, label, comment);
	Instruction delIns = trash.get (new InstructionMemoryValue (newIns));
	if (delIns != null) {
	  if (!delIns.getLabel ().equals (""))
	    newIns.setLabel   (delIns.getLabel ());
	  if (!delIns.getComment ().equals (""))
	    newIns.setComment (delIns.getComment ());
	}
	newAdr += newIns.length ();
	graftIn.add (newIns);
      }
    }
    replace (graftOut, graftIn);
    if (graftIn.size () > graftOut.size ()) 
      tellObservers (new DataModelEvent (DataModelEvent.Type.ROWS_INSERTED, 
					 rows.size () - (graftIn.size () - graftOut.size ()), 
					 rows.size () - 1));
    else if (graftIn.size () < graftOut.size ())
      tellObservers (new DataModelEvent (DataModelEvent.Type.ROWS_DELETED, 
					 rows.size (),
					 rows.size () + (graftOut.size () - graftIn.size () - 1)));
    fireByteLengthChanged ();
  }
  
  /**
   * Just the memory-value part of an Instruction to index into trash.
   */
  class InstructionMemoryValue {
    private int       address;
    private BitString value;
    public InstructionMemoryValue (MemoryCell cell) {
      address = cell.getAddress ();
      value   = cell.getValue   ();
    }
    public boolean equals (Object o) {
      if (o instanceof InstructionMemoryValue) {
	InstructionMemoryValue imv = (InstructionMemoryValue) o;
	return address == imv.address && value == imv.value;
      } else
	return false;
    }
    public int hashCode () {
      return address + value.hashCode ();
    }
  }
  
  @Override boolean isMemoryValueColumn (int columnIndex) {
    return columnIndex == 0 || columnIndex == 1 || columnIndex == 3;
  }
  
  @Override public int getAsmColumn () {
    return 3;
  }
      
  public Class getColumnClass (int columnIndex) {
    if (columnIndex==0)
      return Integer.class;
    else if (columnIndex==1)
      return String.class;
    else if (columnIndex==2)
      return LabelString.class;
    else if (columnIndex==3)
      return AssemblyString.class;
    else if (columnIndex==4)
      return String.class;
    else 
      throw new AssertionError ();
  }
  
  public int getColumnCount () {
    return 5;
  }
  
  public String getColumnName (int columnIndex) {
    if (columnIndex==0)
      return "Addr";
    else if (columnIndex==1)
      return "Mac";
    else if (columnIndex==2)
      return "Label";
    else if (columnIndex==3)
      return "Asm";
    else if (columnIndex==4)
      return "Comment";
    else
      throw new AssertionError ();
  }
  
  public Object getValueAt (int rowIndex, int columnIndex) {
    MemoryCell cell = rows.get (rowIndex);
     if (cell==null)
      return null;
    else if (columnIndex==0)
      return cell.getAddress ();
    else if (columnIndex==1)
      return cell.toMac ();
    else if (columnIndex==2) 
      return new LabelString (cell.getAddress (), cell.getLabel ());
    else if (columnIndex==3)
      return new AssemblyString (cell.getAddress (), cell.getLabel (), cell.toAsm (), cell.getComment ());
    else if (columnIndex==4)
      return cell.getComment ();
    else
      throw new AssertionError ();
  }
  
  public boolean isCellEditable (int rowIndex, int columnIndex) {
    if (columnIndex==0)
      return false;
    else if (columnIndex==1)
      return false;
    else if (columnIndex==2)
      return true;
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
      if (columnIndex==2) {
	String newValue = ((LabelString) aValue).toString ().trim ();
	if (! newValue.equals (cell.getLabel ())) {
	  memory.addUndo (new UndoChange (String.format ("Label Change at 0x%x\n", cell.getAddress ()), rowIndex, 2, UndoChangeType.LABEL));
	  memory.setChanged (true);
	  cell.setLabel (newValue);
	  tellObservers (new DataModelEvent (DataModelEvent.Type.WRITE_BY_USER, rowIndex, columnIndex));
	}
      } else if (columnIndex==3) {
	AssemblyString asm = (AssemblyString) aValue;
	if (asm.hasChanged ()) {
	  memory.addUndo (new UndoChange (String.format ("Instruction Change at 0x%x\n", cell.getAddress ()), rowIndex, 3, UndoChangeType.VALUE));
	  memory.loadAssemblyLine (cell.getAddress (), cell.getLabel (), asm.toString (), cell.getComment ());
	  memory.setChanged (true);
	}
      } else if (columnIndex==4) {
	String newValue = ((String) aValue).trim ();
	if (! newValue.equals (cell.getComment ())) {
	  memory.addUndo (new UndoChange (String.format ("Comment Change at 0x%x\n", cell.getAddress ()), rowIndex, 4, UndoChangeType.COMMENT));
	  cell.setComment (newValue);
	  memory.setChanged (true);
	  tellObservers (new DataModelEvent (DataModelEvent.Type.WRITE_BY_USER, rowIndex, columnIndex));
	}
      }
      else 
	throw new AssertionError ();
    }
  }
}
