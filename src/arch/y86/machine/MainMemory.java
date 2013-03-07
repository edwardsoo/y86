package arch.y86.machine;

import machine.AbstractMainMemory;
import util.UnsignedByte;

public class MainMemory extends AbstractMainMemory {
  private byte [] mem;
  
  public MainMemory (int byteCapacity) {
    mem = new byte [byteCapacity];
  }
  
  @Override
  protected boolean isAccessAligned (int address, int length) {
    return address % length == 0;
  }
  
  @Override
  public int bytesToInteger (UnsignedByte byteAtAddrPlus0, UnsignedByte byteAtAddrPlus1, UnsignedByte byteAtAddrPlus2, UnsignedByte byteAtAddrPlus3) {
    return (int) (byteAtAddrPlus3.value () << 24 | byteAtAddrPlus2.value () << 16 | byteAtAddrPlus1.value () << 8 | byteAtAddrPlus0.value ());
  }
  
  @Override
  public UnsignedByte[] integerToBytes (int i) {
    UnsignedByte[] b = new UnsignedByte [4];
    b [3] = new UnsignedByte (i >>> 24);
    b [2] = new UnsignedByte (i >>> 16);
    b [1] = new UnsignedByte (i >>> 8);
    b [0] = new UnsignedByte (i);
    return b;
  }
  
  @Override
  protected UnsignedByte[] get (int address, int length) throws InvalidAddressException {
    if (address < 0 || address+length-1 >= mem.length)
      throw new InvalidAddressException ();
    UnsignedByte[] value = new UnsignedByte[length];
    for (int i=0; i<length; i++)
      value[i] =  new UnsignedByte (mem[address+i]);
    return value;
  }
  
  @Override
  protected void set (int address, UnsignedByte[] value) throws InvalidAddressException {
    if (address < 0 || address+value.length-1 >= mem.length)
      throw new InvalidAddressException ();
    for (int i=0; i<value.length; i++)
      mem[address+i] = (byte) value[i].value ();
  }
  
  @Override
  public int length () {
    return mem.length;
  }
}
