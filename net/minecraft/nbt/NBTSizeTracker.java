package net.minecraft.nbt;

public class NBTSizeTracker {
  public static final NBTSizeTracker INFINITE = new NBTSizeTracker(0L) {
      public void read(long bits) {}
    };
  
  private final long max;
  
  private long read;
  
  public NBTSizeTracker(long max) {
    this.max = max;
  }
  
  public void read(long bits) {
    this.read += bits / 8L;
    if (this.read > this.max)
      throw new RuntimeException("Tried to read NBT tag that was too big; tried to allocate: " + this.read + "bytes where max allowed: " + this.max); 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\nbt\NBTSizeTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */