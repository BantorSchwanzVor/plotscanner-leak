package net.minecraft.util;

public enum BlockRenderLayer {
  SOLID("Solid"),
  CUTOUT_MIPPED("Mipped Cutout"),
  CUTOUT("Cutout"),
  TRANSLUCENT("Translucent");
  
  private final String layerName;
  
  BlockRenderLayer(String layerNameIn) {
    this.layerName = layerNameIn;
  }
  
  public String toString() {
    return this.layerName;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\BlockRenderLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */