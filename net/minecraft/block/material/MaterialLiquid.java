package net.minecraft.block.material;

public class MaterialLiquid extends Material {
  public MaterialLiquid(MapColor color) {
    super(color);
    setReplaceable();
    setNoPushMobility();
  }
  
  public boolean isLiquid() {
    return true;
  }
  
  public boolean blocksMovement() {
    return false;
  }
  
  public boolean isSolid() {
    return false;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\material\MaterialLiquid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */