package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockNetherBrick extends Block {
  public BlockNetherBrick() {
    super(Material.ROCK);
    setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
  }
  
  public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_) {
    return MapColor.NETHERRACK;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockNetherBrick.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */