package net.minecraft.block.material;

public class MaterialTransparent extends Material {
  public MaterialTransparent(MapColor color) {
    super(color);
    setReplaceable();
  }
  
  public boolean isSolid() {
    return false;
  }
  
  public boolean blocksLight() {
    return false;
  }
  
  public boolean blocksMovement() {
    return false;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\material\MaterialTransparent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */