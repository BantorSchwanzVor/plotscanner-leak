package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagShort extends NBTPrimitive {
  private short data;
  
  public NBTTagShort() {}
  
  public NBTTagShort(short data) {
    this.data = data;
  }
  
  void write(DataOutput output) throws IOException {
    output.writeShort(this.data);
  }
  
  void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
    sizeTracker.read(80L);
    this.data = input.readShort();
  }
  
  public byte getId() {
    return 2;
  }
  
  public String toString() {
    return String.valueOf(this.data) + "s";
  }
  
  public NBTTagShort copy() {
    return new NBTTagShort(this.data);
  }
  
  public boolean equals(Object p_equals_1_) {
    return (super.equals(p_equals_1_) && this.data == ((NBTTagShort)p_equals_1_).data);
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
    return this.data;
  }
  
  public byte getByte() {
    return (byte)(this.data & 0xFF);
  }
  
  public double getDouble() {
    return this.data;
  }
  
  public float getFloat() {
    return this.data;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\nbt\NBTTagShort.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */