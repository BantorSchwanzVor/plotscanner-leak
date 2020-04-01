package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.util.math.MathHelper;

public class NBTTagFloat extends NBTPrimitive {
  private float data;
  
  NBTTagFloat() {}
  
  public NBTTagFloat(float data) {
    this.data = data;
  }
  
  void write(DataOutput output) throws IOException {
    output.writeFloat(this.data);
  }
  
  void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
    sizeTracker.read(96L);
    this.data = input.readFloat();
  }
  
  public byte getId() {
    return 5;
  }
  
  public String toString() {
    return String.valueOf(this.data) + "f";
  }
  
  public NBTTagFloat copy() {
    return new NBTTagFloat(this.data);
  }
  
  public boolean equals(Object p_equals_1_) {
    return (super.equals(p_equals_1_) && this.data == ((NBTTagFloat)p_equals_1_).data);
  }
  
  public int hashCode() {
    return super.hashCode() ^ Float.floatToIntBits(this.data);
  }
  
  public long getLong() {
    return (long)this.data;
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
    return this.data;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\nbt\NBTTagFloat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */