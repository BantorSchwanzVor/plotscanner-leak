package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockGravel extends BlockFalling {
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    if (fortune > 3)
      fortune = 3; 
    return (rand.nextInt(10 - fortune * 3) == 0) ? Items.FLINT : super.getItemDropped(state, rand, fortune);
  }
  
  public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_) {
    return MapColor.STONE;
  }
  
  public int getDustColor(IBlockState p_189876_1_) {
    return -8356741;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockGravel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */