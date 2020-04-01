package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockHay extends BlockRotatedPillar {
  public BlockHay() {
    super(Material.GRASS, MapColor.YELLOW);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)AXIS, (Comparable)EnumFacing.Axis.Y));
    setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
  }
  
  public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
    entityIn.fall(fallDistance, 0.2F);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockHay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */