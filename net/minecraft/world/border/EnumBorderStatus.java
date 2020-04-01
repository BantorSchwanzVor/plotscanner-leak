package net.minecraft.world.border;

public enum EnumBorderStatus {
  GROWING(4259712),
  SHRINKING(16724016),
  STATIONARY(2138367);
  
  private final int id;
  
  EnumBorderStatus(int id) {
    this.id = id;
  }
  
  public int getID() {
    return this.id;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\border\EnumBorderStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */