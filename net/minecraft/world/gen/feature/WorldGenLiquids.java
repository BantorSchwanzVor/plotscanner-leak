package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenLiquids extends WorldGenerator {
  private final Block block;
  
  public WorldGenLiquids(Block blockIn) {
    this.block = blockIn;
  }
  
  public boolean generate(World worldIn, Random rand, BlockPos position) {
    if (worldIn.getBlockState(position.up()).getBlock() != Blocks.STONE)
      return false; 
    if (worldIn.getBlockState(position.down()).getBlock() != Blocks.STONE)
      return false; 
    IBlockState iblockstate = worldIn.getBlockState(position);
    if (iblockstate.getMaterial() != Material.AIR && iblockstate.getBlock() != Blocks.STONE)
      return false; 
    int i = 0;
    if (worldIn.getBlockState(position.west()).getBlock() == Blocks.STONE)
      i++; 
    if (worldIn.getBlockState(position.east()).getBlock() == Blocks.STONE)
      i++; 
    if (worldIn.getBlockState(position.north()).getBlock() == Blocks.STONE)
      i++; 
    if (worldIn.getBlockState(position.south()).getBlock() == Blocks.STONE)
      i++; 
    int j = 0;
    if (worldIn.isAirBlock(position.west()))
      j++; 
    if (worldIn.isAirBlock(position.east()))
      j++; 
    if (worldIn.isAirBlock(position.north()))
      j++; 
    if (worldIn.isAirBlock(position.south()))
      j++; 
    if (i == 3 && j == 1) {
      IBlockState iblockstate1 = this.block.getDefaultState();
      worldIn.setBlockState(position, iblockstate1, 2);
      worldIn.immediateBlockTick(position, iblockstate1, rand);
    } 
    return true;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\gen\feature\WorldGenLiquids.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */