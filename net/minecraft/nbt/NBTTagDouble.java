package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.util.math.MathHelper;

public class NBTTagDouble extends NBTPrimitive {
  private double data;
  
  NBTTagDouble() {}
  
  public NBTTagDouble(double data) {
    this.data = data;
  }
  
  void write(DataOutput output) throws IOException {
    output.writeDouble(this.data);
  }
  
  void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
    sizeTracker.read(128L);
    this.data = input.readDouble();
  }
  
  public byte getId() {
    return 6;
  }
  
  public String toString() {
    return String.valueOf(this.data) + "d";
  }
  
  public NBTTagDouble copy() {
    return new NBTTagDouble(this.data);
  }
  
  public boolean equals(Object p_equals_1_) {
    return (super.equals(p_equals_1_) && this.data == ((NBTTagDouble)p_equals_1_).data);
  }
  
  public int hashCode() {
    long i = Double.doubleToLongBits(this.data);
    return super.hashCode() ^ (int)(i ^ i >>> 32L);
  }
  
  public long getLong() {
    return (long)Math.floor(this.data);
  }
  
  public int getInt() {
    return MathHelper.floor(this.data);
  }
  
  public short getShort() {
    return (short)(MathHelper.floor(this.data) & 0xFFFF);
  }
  
  public byte getByte() {
    return (byte)(MathHelper.floor(this.data) & 0xFF);
  }
  
  public double getDouble() {
    return this.data;
  }
  
  public float getFloat() {
    return (float)this.data;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\nbt\NBTTagDouble.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */