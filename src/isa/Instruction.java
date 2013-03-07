package isa;

import util.BitStream;
import util.BitString;
import util.IntStream;

/**
 * A single concrete instance of an instruction in memory.  
 */

public class Instruction extends MemoryCell {
  private AbstractISA.InstructionDef def;
  private String                     lastRequestedAsm;
  
  Instruction (Memory aMemory, Integer anAddress, AbstractISA.InstructionDef aDef, BitString aValue, String aLabel, String aComment) {
    super (aMemory, anAddress, aValue, aLabel, aComment);
    assert aValue != null;
    def = aDef;
    lastRequestedAsm = null;
    toAsm ();
  }
  
  public static Instruction valueOfMemory (Memory memory, int address, String label, String comment) {
    AbstractISA.InstructionDef def = memory.getInstructionDefForValue (address);
    if (def != null) {
      BitString value = new BitStream (memory, address).getValue (def.getLayout ().length ());
      return new Instruction (memory, address, def, value, label, comment);
    } else 
      return null;
  }
  
  @Override
  public void copyFrom (MemoryCell aCell) {
    super.copyFrom (aCell);
    Instruction ins = (Instruction) aCell;
    def              = ins.def;
    lastRequestedAsm = ins.lastRequestedAsm;
  }
  
  public static Instruction valueOfPlaceholder (Memory memory, int address, String label, String comment) {
    return new Instruction (memory, address, memory.getPlaceholderInstructionDef (), memory.getPlaceholderInstructionValue (), label, comment);
  }
  
  public static Instruction valueOf (Memory memory, int address, int opCode, int[] operands, String label, String comment) {
    AbstractISA.InstructionDef def = memory.getInstructionDefForOpCode (opCode);
    if (def != null) 
      return new Instruction (memory, address, def, def.getValue (new IntStream (operands), address), label, comment);
    else 
      return null;
  }
  
  @Override
  boolean syncFromMemory () {
    boolean hasChanged = super.syncFromMemory ();
    if (hasChanged) {
      def = memory.getInstructionDefForValue (address);
      if (def != null)
	value = new BitStream (memory, address).getValue (def.getLayout ().length ());
    }
    return hasChanged;
  }
  
  
  private void syncAsmToMemory (String asm) {
    try {
      memory.loadAssemblyLine (address, label, asm, comment);
      lastRequestedAsm = asm;
    } catch (AbstractAssembler.AssemblyException e) {
      throw new AssertionError (e);
    }
  }

  public String toAsm () {
    lastRequestedAsm = def != null? def.getLayout ().toAsm (value, 0, address) : "";
    return lastRequestedAsm;
  }
  
  String toSavableAsm () {
    lastRequestedAsm = def != null? def.getLayout ().toAsm (getSavableValue (), 0, address) : "";
    return lastRequestedAsm;
 }
  
  String toDsc () {
    return def != null? def.getLayout ().toDsc (value, 0, address) : "";
  }
  
  public String toMac () {
    return def != null? def.getLayout ().toMac (value, 0, address) : "?";
  }
  
  boolean memoryResyncedFromAsm () {
    String asmTruth = lastRequestedAsm;
    if (lastRequestedAsm!=null && !lastRequestedAsm.equals (toAsm())) {
      syncAsmToMemory (asmTruth);
      return true;
    } else
      return false;
  }
  
  boolean asmResyncedFromMemory () {
    if (lastRequestedAsm!=null && !lastRequestedAsm.equals (toAsm())) {
      return true;
    } else
      return false;
  }
}

