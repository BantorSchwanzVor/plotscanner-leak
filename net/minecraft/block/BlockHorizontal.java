package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.util.EnumFacing;

public abstract class BlockHorizontal extends Block {
  public static final PropertyDirection FACING = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
  
  protected BlockHorizontal(Material materialIn) {
    super(materialIn);
  }
  
  protected BlockHorizontal(Material materialIn, MapColor colorIn) {
    super(materialIn, colorIn);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockHorizontal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */