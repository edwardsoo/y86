package isa;

import util.BitString;
import util.BitStream;

/**
 * A cell of ISA-abstracted memory: an instruction or a data element. 
 *
 * A MemoryCell serves two roles.  
 *
 * First it is an immutable snapshot of  an instruction or data element in 
 * real memory.  A cell is created from values in memory or from an opcode 
 * and set of operands.  The memory region to which the cell belongs is 
 * responsible for tracking changes to the real memory that underlies the
 * cell.  When memory changes, the memory region creates new cell objects
 * for those new memory values.  When an instruction is created from an
 * opcode and operand list, the region writes the cell's value into memory.
 *
 * Second is is a repository for auxiliary information about the cell that
 * is not stored in MainMemory such as labels and comments.  This information
 * is not immutable.  It can be changed without requiring a new MemoryCell.
 */

public abstract class MemoryCell {
  protected Memory    memory;
  protected int       address;
  protected BitString value;
  protected String    label;
  protected String    comment;
  protected BitString checkpointValue;
  
  public MemoryCell (Memory aMemory, int anAddress, BitString aValue, String aLabel, String aComment) {
    memory          = aMemory;
    address         = anAddress;
    value           = aValue;
    label           = aLabel;
    comment         = aComment;
    checkpointValue = null;
  }
  
  public boolean valueEquals (MemoryCell aMemoryCell) {
    return value.equals (aMemoryCell.value);
  }
  
  public void copyFrom (MemoryCell aCell) {
    memory          = aCell.memory;
    address         = aCell.address;
    value           = aCell.value;
    label           = aCell.label;
    comment         = aCell.comment;
    checkpointValue = aCell.checkpointValue;
  }
  
  public void checkpointValue () {
    checkpointValue = value;
  }
  
  public void restoreValueFromCheckpoint () {
    if (checkpointValue != null && ! checkpointValue.equals (value)) {
      checkpointValue.writeToByUser (memory, address);
    }
  }
  
  public int getAddress () {
    return address;
  }
  
  BitString getValue () {
    return value;
  }
  
  BitString getSavableValue () {
    return checkpointValue != null? checkpointValue : value;
  }
  
  public int length () {
    return value.byteLength ();
  }
  
  public String getLabel () {
    return label;
  }
  
  public String getComment () {
    return comment;
  }
  
  void setAddress (int anAddress) {
    address = anAddress;
  }
  
  void setLabel (String aLabel) {
    label = aLabel;
    memory.getLabelMap ().add (this);
  }
  
  void setValue (BitString aValue) {
    value = aValue;
  }
  
  void setComment (String aComment) {
    comment = aComment;
  }
  
  void syncToMemory () {
    BitString memValue = new BitStream (memory, address).getValue (value.length ());
    if (! value.equals (memValue)) 
      value.writeToByUser (memory, address);
  }
  
  /**
   * @return  true iff cell's in-memory value has changed.
   */
  boolean syncFromMemory () {
    BitString memValue = new BitStream (memory, address).getValue (value.length ());
    if (! memValue.equals (value)) {
      value = memValue;
      return true;
    } else
      return false;
  }

  public boolean equals (Object o)  {
    if (o instanceof MemoryCell) {
      MemoryCell cell = (MemoryCell) o;
      return getAddress () == cell.getAddress () && getValue ().equals (cell.getValue ()) && getLabel ().equals (cell.getLabel ()) && getComment ().equals (cell.getComment ());
    } else
      return false;
  }  
  
  public int hashCode () {
    return getAddress () + getValue ().hashCode () + getLabel ().hashCode () + getComment ().hashCode ();
  }
  
  public abstract String  toAsm ();
  abstract        String  toSavableAsm ();
  public abstract String  toMac ();
  abstract        boolean memoryResyncedFromAsm ();
  abstract        boolean asmResyncedFromMemory ();
}
