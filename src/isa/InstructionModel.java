package isa;

import util.AbstractDataModel;
import util.DataModel;

public class InstructionModel extends AbstractDataModel {
  Memory memory;
  public InstructionModel (Memory aMemory) {
    memory = aMemory;
  }
  public Class getColumnClass (int col) {
    return String.class;
  }
  public int getColumnCount () {
    return 3;
  }
  public String getColumnName (int col) {
    if (col==0)
      return "Address";
    else if (col==1)
      return "Asm";
    else 
      return "Dsc";
  }
  public int getRowCount () {
    return 1;
  }
  public Object getValueAt (int row, int col) {
    if (col==0)
      return row;
    else if (col==1) {
      if (row >= 0)
	try {
	  return Instruction.valueOfMemory (memory, row, "", "").toAsm ();
	} catch (Exception e) {
	  return "";
	}
      else 
	return Instruction.valueOfPlaceholder (memory, 0, "", "").toAsm ();
    } else {
      if (row >= 0)
	try {
	  return Instruction.valueOfMemory (memory, row, "", "").toDsc ();
	} catch (Exception e) {
	  return "";
	}
      else 
	return Instruction.valueOfPlaceholder (memory, 0, "", "").toAsm ();
    }    
  }
  public boolean isCellEditable (int row, int col) {
    return false;
  }
  public void setValueAt (Object value, int row, int col) {
    ;
  }
}