package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagByte extends NBTPrimitive {
  private byte data;
  
  NBTTagByte() {}
  
  public NBTTagByte(byte data) {
    this.data = data;
  }
  
  void write(DataOutput output) throws IOException {
    output.writeByte(this.data);
  }
  
  void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
    sizeTracker.read(72L);
    this.data = input.readByte();
  }
  
  public byte getId() {
    return 1;
  }
  
  public String toString() {
    return String.valueOf(this.data) + "b";
  }
  
  public NBTTagByte copy() {
    return new NBTTagByte(this.data);
  }
  
  public boolean equals(Object p_equals_1_) {
    return (super.equals(p_equals_1_) && this.data == ((NBTTagByte)p_equals_1_).data);
  }
  
  public int hashCode() {
    return super.hashCode() ^ this.data;
  }
  
  public long getLong() {
    return this.data;
  }
  
  public int getInt() {
    return this.data;
  }
  
  public short getShort() {
    return (short)this.data;
  }
  
  public byte getByte() {
    return this.data;
  }
  
  public double getDouble() {
    return this.data;
  }
  
  public float getFloat() {
    return this.data;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\nbt\NBTTagByte.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */