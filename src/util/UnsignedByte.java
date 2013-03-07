package util;

/**
 * An {@code UnsignedByte} is like a {@code Byte}, but its values range from 0 to 255 instead of -128 to 127.  
 * <p>
 * Most languages have
 * a native unsigned-byte type (e.g., C, C++, C#), but Java doesn't.  
 * <p>
 * When manipulating bytes
 * as bit sequences, as we do in the CPU implementation, it is helpful to treat them as unsigned. 
 * The key difference between <i>unsigned</i> and <i>signed</i> in our context is what happens when you assign an 
 * (unsigned) byte to an integer.  If a signed byte is assigned, then the resulting integer is sign extended 
 * (i.e., 0xff becomes 0xffffffff).  If an unsigned byte is assigned, then the resulting integer is
 * zero extended (i.e., 0xff becomes 0x000000ff).  The former behaviour is what you want if the program
 * thinks of the byte as a signed integer (i.e., -1 stays -1).  The later is what you want if the program
 * (in our case the CPU class) thinks of the byte as a sequence of bits.
 */

public class UnsignedByte {
  private byte value;
  /**
   * Create an unsigned byte from a byte.
   */
  public UnsignedByte (Byte aByte) {
    value = aByte;
  }
  /**
   * Create an unsigned byte from an integer.
   */
  public UnsignedByte (int anInt) {
    value = (byte) anInt;
  }
  /**
   * Get the value of the unsigned byte.
   * @return zero-extended long version of the unsigned byte's value.
   */
  public long value () {
    return ((long) value) & 0xff;
  }
  /**
   * Like Java Numbers, two UnsignedByte's are equal if their values are equal.
   */
  @Override public boolean equals (Object o) {
    UnsignedByte anotherUnsignedByte = (UnsignedByte) o;
    return value == anotherUnsignedByte.value;
  }
}