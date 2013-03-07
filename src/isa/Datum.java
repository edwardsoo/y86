package isa;

import util.BitString;
import util.BitStream;


/**
 * A single concrete instance of data in memory.
 */

public class Datum extends MemoryCell {
  String lastRequestedLabel = null;
  
  Datum (Memory aMemory, int anAddress, BitString aValue, String aLabel, String aComment) {
    super (aMemory, anAddress, aValue, aLabel, aComment);
  }
  
  public static Datum valueOfMemory (Memory memory, int address, int length, String label, String comment) {
    return new Datum (memory, address, new BitStream (memory, address).getValue (length*8), label, comment);
  }
  
  public static Datum valueOf (Memory memory, int address, int value, int byteSize, String label, String comment) {
    return new Datum (memory, address, new BitString (byteSize*8, memory.normalizeEndianness (value, byteSize)), label, comment);
  }
  
  public static Datum valueOf (Memory memory, int address, int value, String label, String comment) {
    return valueOf (memory, address, value, 4, label, comment);
  }
  
  @Override
  public void copyFrom (MemoryCell aCell) {
    super.copyFrom (aCell);
    Datum dat = (Datum) aCell;
    lastRequestedLabel = dat.lastRequestedLabel;
  }
  
  String asmStringOf (BitString aValue) {
    String asmString = null;
    if (aValue.length () == 8)
      asmString = String .format (".byte 0x%x", memory.normalizeEndianness ((int) aValue.getValue (), 1));
    else if (aValue.length () == 16)
      asmString = String .format (".word 0x%x", memory.normalizeEndianness ((int) aValue.getValue (), 2));
    else if (aValue.length () == 32)
      asmString = String .format (".long 0x%x", memory.normalizeEndianness ((int) aValue.getValue (), 4));
    else 
      assert (false);
    return asmString;
  }
  
  public String toAsm () {
    return asmStringOf (value);
  }
  
  String toSavableAsm () {
    return asmStringOf (getSavableValue ());
  }
  
  public String toMac () {
    return String.format (String.format ("%%0%dx", value.length () / 4), value.getValue ());
  }
  
  String valueAsLabel () {
    lastRequestedLabel = memory.getLabelMap ().getLabel (memory.normalizeEndianness ((int) value.getValue (), length ()));
    return lastRequestedLabel;
  }
  
  boolean memoryResyncedFromAsm () {
    String lvLast = lastRequestedLabel;
    String lv     = valueAsLabel ();
    return ! (lvLast!=null? lvLast : "").trim ().equals ((lv!=null? lv : "").trim ());
  }
  
  boolean asmResyncedFromMemory () {
    return memoryResyncedFromAsm ();
  }
}

