package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockCompressedPowered extends Block {
  public BlockCompressedPowered(Material materialIn, MapColor color) {
    super(materialIn, color);
  }
  
  public boolean canProvidePower(IBlockState state) {
    return true;
  }
  
  public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return 15;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockCompressedPowered.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */