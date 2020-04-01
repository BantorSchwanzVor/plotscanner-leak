package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class WorldGenAbstractTree extends WorldGenerator {
  public WorldGenAbstractTree(boolean notify) {
    super(notify);
  }
  
  protected boolean canGrowInto(Block blockType) {
    Material material = blockType.getDefaultState().getMaterial();
    return !(material != Material.AIR && material != Material.LEAVES && blockType != Blocks.GRASS && blockType != Blocks.DIRT && blockType != Blocks.LOG && blockType != Blocks.LOG2 && blockType != Blocks.SAPLING && blockType != Blocks.VINE);
  }
  
  public void generateSaplings(World worldIn, Random random, BlockPos pos) {}
  
  protected void setDirtAt(World worldIn, BlockPos pos) {
    if (worldIn.getBlockState(pos).getBlock() != Blocks.DIRT)
      setBlockAndNotifyAdequately(worldIn, pos, Blocks.DIRT.getDefaultState()); 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\gen\feature\WorldGenAbstractTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */