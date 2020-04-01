package optifine;

public class RangeInt {
  private int min;
  
  private int max;
  
  public RangeInt(int p_i80_1_, int p_i80_2_) {
    this.min = Math.min(p_i80_1_, p_i80_2_);
    this.max = Math.max(p_i80_1_, p_i80_2_);
  }
  
  public boolean isInRange(int p_isInRange_1_) {
    if (p_isInRange_1_ < this.min)
      return false; 
    return (p_isInRange_1_ <= this.max);
  }
  
  public int getMin() {
    return this.min;
  }
  
  public int getMax() {
    return this.max;
  }
  
  public String toString() {
    return "min: " + this.min + ", max: " + this.max;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\optifine\RangeInt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */