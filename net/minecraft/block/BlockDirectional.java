package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;

public abstract class BlockDirectional extends Block {
  public static final PropertyDirection FACING = PropertyDirection.create("facing");
  
  protected BlockDirectional(Material materialIn) {
    super(materialIn);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockDirectional.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */