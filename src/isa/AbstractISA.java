package isa;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;
import util.DataModel;
import util.BitStream;
import util.BitString;
import util.IntStream;

public abstract class AbstractISA {
  
  private String                      name;
  private Endianness                  endianness;
  private AbstractAssembler           assembler;
  private Map<Integer,InstructionDef> isaDef;
  private Map<Integer,String>         addressToLabelMap;
  private InstructionDef              placeholderInstructionDef = null;
  private BitString                   placeholderInstructionValue = null;
  
  public enum Endianness { BIG, LITTLE };
  
  public AbstractISA (String aName, Endianness anEndianness, AbstractAssembler anAssembler) {
    name       = aName;
    assembler  = anAssembler;
    endianness = anEndianness;
    isaDef = new HashMap<Integer,InstructionDef> ();
    addressToLabelMap = null;
   }
  
  void setAddressToLabelMap (Map<Integer,String> aMap) {
    addressToLabelMap = aMap;
  }
  
  /**
   * Name of this ISA.
   */
  public String getName () {
    return name;
  }
  
  /**
   * Called by subclass to define instructions that are part of its ISA.
   *
   * @param opCode opCode number for this instruction.
   * @param layout layout description describing how bits of instruction are to be interpreted.
   */
  protected void define (int opCode, InsLayout layout) {
    isaDef.put (new Integer(opCode), new InstructionDef (opCode, layout));
  }  
  
  /**
   * Called by subclass to specify an instruction to use as a placeholder (nop).
   */
  protected void setPlaceholderInstruction (int opCode) {
    placeholderInstructionDef   = isaDef.get (opCode);
    assert placeholderInstructionDef != null;
    placeholderInstructionValue = placeholderInstructionDef.getValue (new IntStream (0), 0);
  }
  
  public InstructionDef getPlaceholderInstructionDef () {
    assert placeholderInstructionDef != null;
    return placeholderInstructionDef;
  }
  
  public BitString getPlaceholderInstructionValue () {
    assert placeholderInstructionValue != null;
    return placeholderInstructionValue;
  }
  
  //
  // Layout Definition
  //
  
  /**
   * Describes the in-memory layout of instructions.  One for each instruction in ISA.
   */
  protected interface InsLayout {
    String    toMac                 (BitString value, int offset, int pc);    
    String    toAsm                 (BitString value, int offset, int pc);
    String    toDsc	            (BitString value, int offset, int pc);
    BitString getValue              (IntStream fieldValues, int pc);
    BitString getValue              (BitStream bitStream); 
    int       length                ();
    int       byteLength            ();
    int       getOffsetTo           (InsLayout field);
    InsLayout getFirstFieldForClass (Class aClass);
  }
  
  int signExtend (int value, int length) {
    return (value << (32-length)) >> (32-length);
  }  
  
  /**
   * Shift instruction.
   */
  protected class ShiftInsField extends CompoundField {
    public ShiftInsField (InsLayout[] aFields, int[] anAsmOrder, String[] anAsmFormats, int[] aDscOrder, String[] aDscFormats, int opCodeField, int shiftField) {
      super (aFields, anAsmOrder, anAsmFormats, aDscOrder, aDscFormats);
      ((ShiftOpCodeField) fields[opCodeField]).setShiftField (getOffsetTo (fields[shiftField]), fields[shiftField].length ());
    }
  }
  
  /*
   * Shift opcode name is partially determined by value of shift parameter (neg=> shr, pos=> shl).
   */
  protected class ShiftOpCodeField extends OpCodeField {
    String nonNegAsm, negAsm, nonNegDsc, negDsc;
    int    shiftOffset, shiftLength;
    public ShiftOpCodeField (int aLength, String aMacFormat, String anAsmFormat, String aNonNegAsm, String aNegAsm, String aDscFormat, String aNonNegDsc, String aNegDsc) {
      super (aLength, aMacFormat, anAsmFormat, aDscFormat);
      nonNegAsm = aNonNegAsm;
      negAsm    = aNegAsm;
      nonNegDsc = aNonNegDsc;
      negDsc    = aNegDsc;
    }
    
    void setShiftField (int aShiftOffset, int aShiftLength) {
      shiftOffset = aShiftOffset;
      shiftLength = aShiftLength;
    }
    
    public String toAsm (BitString ins, int offset, int pc) {
      return String.format (asmFormat, signExtend (ins.getValueAt (shiftOffset, shiftLength), shiftLength)>=0? nonNegAsm : negAsm);
    }
    
    public String toDsc (BitString ins, int offset, int pc) {
      return String.format (dscFormat, signExtend (ins.getValueAt (shiftOffset, shiftLength), shiftLength)>=0? nonNegDsc : negDsc);
    }
  }
  
  /**
   * Shift value.
   */
  protected class ShiftField extends SimpleField {
    public ShiftField (int aLength, String aMacFormat, String anAsmFormat, String aDscFormat) {
      super (aLength, aMacFormat, anAsmFormat, aDscFormat);
    }
    int unscaleValue (int value, int pc) {
      int seValue = signExtend (value, length);
      return seValue < 0? -seValue : seValue;
    }
  }  
  
  /**
   * Compound field combines one or more other fields.
   */
  protected class CompoundField implements InsLayout {
    InsLayout[] fields;
    int[]       asmOrder;
    String[]    asmFormats;
    int[]       dscOrder;
    String[]    dscFormats;
    
    public CompoundField (InsLayout[] aFields, int[] anAsmOrder, String[] anAsmFormats, int[] aDscOrder, String[] aDscFormats) {
      fields     = aFields;
      asmOrder   = anAsmOrder;
      asmFormats = anAsmFormats;
      dscOrder   = aDscOrder;
      dscFormats = aDscFormats;
    }
    
    public String toMac (BitString ins, int offset, int pc) {
      String mac = "";
      for (int i=0; i<fields.length; i++) {
	mac = mac.concat (fields[i].toMac (ins, offset, pc));
	offset += fields[i].length ();
      }
      return mac;
    }
    
    public String toAsm (BitString ins, int offset, int pc) {
      String[] fldAsm = new String[fields.length];
      String   asm = "";
      for (int i=0; i<fields.length; i++) {
	fldAsm[i] = fields[i].toAsm (ins, offset, pc);
	offset += fields[i].length ();
      }
      for (int i=0; i<asmOrder.length; i++) 
	asm = asm.concat (String.format (asmFormats[i], fldAsm[asmOrder[i]]));
      return asm;
    }
    
    public String toDsc (BitString ins, int offset, int pc) {
      String[] fldDsc = new String[fields.length];
      String   dsc = "";
      for (int i=0; i<fields.length; i++) {
	fldDsc[i] = fields[i].toDsc (ins, offset, pc);
	offset += fields[i].length ();
      }
      for (int i=0; i<dscOrder.length; i++) 
	dsc = dsc.concat (String.format (dscFormats[i], fldDsc[dscOrder[i]]));
      return dsc;
    }
    
    public BitString getValue (IntStream fieldValues, int pc) {
      BitString value = new BitString ();
      for (int i=0; i<fields.length; i++) 
	value = value.concat (fields[i].getValue (fieldValues, pc));	
      return value;
    }
    
    public BitString getValue (BitStream bitStream) {
      BitString value = new BitString ();
      for (int i=0; i<fields.length; i++) 
	value = value.concat (fields[i].getValue (bitStream));
      return value;
    }
    
    public int length () {
      int length = 0;
      for (int i=0; i<fields.length; i++)
	length += fields[i].length ();
      return length;
    }
    
    public int byteLength () {
      return length () / 8;
    }
    
    public int getOffsetTo (InsLayout aField) {
      int offset = 0;;
      for (int i=0; i<fields.length; i++)
	if (fields[i] == aField)
	  return offset;
	else
	  offset += fields[i].length ();
      throw new RuntimeException ();
    }
    
    public InsLayout getFirstFieldForClass (Class aClass) {
      for (int i=0; i<fields.length; i++) {
	InsLayout aField = fields[i].getFirstFieldForClass (aClass);
	if (aField != null)
	  return aField;
      }
      throw new RuntimeException ();
    }
  }
  
  /**
   * PC-relative address field.
   */
  protected class PCRelativeField extends LabelableField {
    public PCRelativeField (int aLength, String aMacFormat, String aNoLabelAsmFormat, String aLabelAsmFormat, String aDscFormat, String aLabelDscFormat) {
      super (aLength, aMacFormat, aNoLabelAsmFormat, aLabelAsmFormat, aDscFormat, aLabelDscFormat);
    }
    int scaleValue (int value, int pc) {
      return (value - (pc+2))/2;
    }
    int unscaleValue (int value, int pc) {
      return signExtend (value, length)*2 + (pc+2);
    }
  }
  
  /**
   * For register names (etc.), field name comes from dictonary lookup.
   */
  protected class DictonaryField extends SimpleField {
    Map <Integer, String> names;
    public DictonaryField (int aLength, String aMacFormat, String anAsmFormat, String aDscFormat, Map <Integer, String> aNames) {
      super (aLength, aMacFormat, anAsmFormat, aDscFormat);
      names = aNames;
    }
    public String toAsm (BitString insValue, int offset, int pc) {
      return String.format (asmFormat, names.get (insValue.getValueAt (offset, length)));     
    }
    public String toDsc (BitString insValue, int offset, int pc) {
      return String.format (dscFormat, names.get (insValue.getValueAt (offset, length)));     
    }
  }
  
  /**
   * A field that is always assigned a constant value.
   */
  protected class ConstantField extends SimpleField {
    int value;
    public ConstantField (int aLength, String aMacFormat, String anAsmFormat, String aDscFormat, int aValue) {
      super (aLength, aMacFormat, anAsmFormat, aDscFormat);
      value = aValue;
    }
    int scaleValue (int aValue, int pc) {
      return value;
    }
    int unscaleValue (int aValue, int pc) {
      return value;
    }
  }
  
  /**
   * For index addressing mode, this field is scaled by index scaling factor in instruction.
   */
  protected class ScaledField extends SimpleField {
    int scale;
    public ScaledField (int aLength, String aMacFormat, String anAsmFormat, String aDscFormat, int aScale) {
      super (aLength, aMacFormat, anAsmFormat, aDscFormat);
      scale = aScale;
    }
    int scaleValue (int value, int pc) {
      return value / scale;
    }
    int unscaleValue (int value, int pc) {
      return value * scale;
    }
  }
  
  /**
   * Instruction op code.
   */
  protected class OpCodeField extends SimpleField {
    public OpCodeField (int aLength, String aMacFormat, String anAsmFormat, String aDscFormat) {
      super (aLength, aMacFormat, anAsmFormat, aDscFormat);
    }
  }
  
  /**
   * Simple field whose value can be represted by a label.  Labels are taken from the addressToLabelMap maintained by ISA.Memory.
   */
  protected class LabelableField extends SimpleField {
    String labelAsmFormat;
    String labelDscFormat;
    
    public LabelableField (int aLength, String aMacFormat, String aNoLabelAsmFormat, String aLabelAsmFormat, String aDscFormat, String aLabelDscFormat) {
      super (aLength, aMacFormat, aNoLabelAsmFormat, aDscFormat);
      labelAsmFormat = aLabelAsmFormat;
      labelDscFormat = aLabelDscFormat;
    }
    
    @Override
    int scaleValue (int value, int pc) {
      return normalizeEndianness (value, length);
    }
    
    @Override
    int unscaleValue (int value, int pc) {
      return normalizeEndianness (value, length);
    }
    
    public String toAsm (BitString insValue, int offset, int pc) {
      int value = unscaleValue (insValue.getValueAt (offset, length), pc);
      String label = addressToLabelMap!=null? addressToLabelMap.get (value) : null;
      if (label!=null)
	return String.format (labelAsmFormat, label);
      else
	return String.format (asmFormat, value);
    }
    
    public String toDsc (BitString insValue, int offset, int pc) {
      int value = unscaleValue (insValue.getValueAt (offset, length), pc);
      String label = addressToLabelMap!=null? addressToLabelMap.get (value) : null;
      if (label!=null)
	return String.format (labelDscFormat, label);
      else
	return String.format (dscFormat, value);
    }
  }
  
  /**
   * Simple instruction field.  Extended by more other single-value fields and may be combined with
   * other fields in a CompoundField.
   */
  protected class SimpleField implements InsLayout {
    int    length;
    String macFormat;
    String asmFormat;
    String dscFormat;
    
    public SimpleField (int aLength, String aMacFormat, String anAsmFormat, String aDscFormat) {
      length    = aLength;
      macFormat = aMacFormat;
      asmFormat = anAsmFormat;
      dscFormat = aDscFormat;
    }
    
    int scaleValue (int value, int pc) {
      return value;
    }
    
    int unscaleValue (int value, int pc) {
      return value;
    }
    
    public String toMac (BitString insValue, int offset, int pc) {
      return String.format (macFormat, insValue.getValueAt (offset, length), pc);
    }
    
    public String toAsm (BitString insValue, int offset, int pc) {
      return String.format (asmFormat, unscaleValue (insValue.getValueAt (offset, length), pc));
    }
    
    public String toDsc (BitString insValue, int offset, int pc) {
      return String.format (dscFormat, unscaleValue (insValue.getValueAt (offset, length), pc));
    }
    
    public BitString getValue (IntStream fields, int pc) {
      return new BitString (length, scaleValue (fields.getValue (), pc));
    }
    
    public BitString getValue (BitStream bitStream) {
      return bitStream.getValue (length);
    }
    
    public int length () {
      return length;
    }
    
    public int byteLength () {
      return length () / 8;
    }
    
    public int getOffsetTo (InsLayout aField) {
      assert aField==this;
      return 0;
    }
    
    public InsLayout getFirstFieldForClass (Class aClass) {
      return aClass.isInstance (this)? this : null;
    }
  }
  
  /**
   * Instruction layout definition
   */
  class InstructionDef {
    int         opCode;
    InsLayout   layout;
    OpCodeField opCodeLayout;
    int         opCodeOffset;
    
    InstructionDef (int anOpCode, InsLayout aLayout) {
      opCode       = anOpCode;
      layout       = aLayout;
      opCodeLayout = (OpCodeField) layout.getFirstFieldForClass (OpCodeField.class);
      opCodeOffset = layout.getOffsetTo (opCodeLayout);
    }
    
    BitString getValue (BitStream bitStream) {
      bitStream.mark ();
      bitStream.skip (opCodeOffset);
      boolean match = opCodeLayout.getValue (bitStream).getValue () == opCode;
      bitStream.rewind ();
      if (match)
	return layout.getValue (bitStream);
      else 
	return null;
    }
    
    BitString getValue (IntStream operandValues, int pc) {
      return layout.getValue ((new IntStream (opCode)).concat (operandValues), pc);
    }
    
    InsLayout getLayout () {
      return layout;
    }
  }
  
  InstructionDef getDefForOpCode (int opCode) {
    return isaDef.get (opCode);
  }
  
  InstructionDef getDefForValue (BitStream mem) {
    for (InstructionDef def : isaDef.values()) {
      mem.rewind ();
      if (def.getValue (mem) != null) {
	return def;
      }
    }
    return null;    
  }
  
  /**
   * Switch between machine and user number representation, based on ISA Endiannnes.  
   */
  public int normalizeEndianness (int num, int byteSize) {
    int normalizedNum = 0;
    switch (endianness) {
      case BIG:
        normalizedNum = num;
        break;
      case LITTLE:
        for (int b=0; b<byteSize; b++)
          normalizedNum = normalizedNum << 8 | (num >> (b*8) & 0xff);
        break;
    }
    return normalizedNum;
  }
  
  void assembleFile (String filename, Memory memory) throws AbstractAssembler.AssemblyException, java.io.FileNotFoundException, java.io.IOException {
    assembler.assembleFile (filename, memory);
  }
  
  void assembleLine (int address, String label, String statement, String comment, Memory memory) throws AbstractAssembler.AssemblyException {
    assembler.assembleLine (address, label, statement, comment, memory);
  }
  
  void checkAssemblyLineSyntax (int address, String label, String statement, String comment, Memory memory) throws AbstractAssembler.AssemblyException {
    assembler.checkLineSyntax (address, label, statement, comment, memory);
  }
  
  void checkAssemblyLabelSyntax (String label, Memory memory) throws AbstractAssembler.AssemblyException {
    assembler.checkLabelSyntax (label, memory);
  }
}