package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BlockMushroom extends BlockBush implements IGrowable {
  protected static final AxisAlignedBB MUSHROOM_AABB = new AxisAlignedBB(0.30000001192092896D, 0.0D, 0.30000001192092896D, 0.699999988079071D, 0.4000000059604645D, 0.699999988079071D);
  
  protected BlockMushroom() {
    setTickRandomly(true);
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return MUSHROOM_AABB;
  }
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (rand.nextInt(25) == 0) {
      int i = 5;
      int j = 4;
      for (BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-4, -1, -4), pos.add(4, 1, 4))) {
        if (worldIn.getBlockState(blockpos).getBlock() == this) {
          i--;
          if (i <= 0)
            return; 
        } 
      } 
      BlockPos blockpos1 = pos.add(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);
      for (int k = 0; k < 4; k++) {
        if (worldIn.isAirBlock(blockpos1) && canBlockStay(worldIn, blockpos1, getDefaultState()))
          pos = blockpos1; 
        blockpos1 = pos.add(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);
      } 
      if (worldIn.isAirBlock(blockpos1) && canBlockStay(worldIn, blockpos1, getDefaultState()))
        worldIn.setBlockState(blockpos1, getDefaultState(), 2); 
    } 
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    return (super.canPlaceBlockAt(worldIn, pos) && canBlockStay(worldIn, pos, getDefaultState()));
  }
  
  protected boolean canSustainBush(IBlockState state) {
    return state.isFullBlock();
  }
  
  public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
    if (pos.getY() >= 0 && pos.getY() < 256) {
      IBlockState iblockstate = worldIn.getBlockState(pos.down());
      if (iblockstate.getBlock() == Blocks.MYCELIUM)
        return true; 
      if (iblockstate.getBlock() == Blocks.DIRT && iblockstate.getValue((IProperty)BlockDirt.VARIANT) == BlockDirt.DirtType.PODZOL)
        return true; 
      return (worldIn.getLight(pos) < 13 && canSustainBush(iblockstate));
    } 
    return false;
  }
  
  public boolean generateBigMushroom(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    WorldGenBigMushroom worldGenBigMushroom;
    worldIn.setBlockToAir(pos);
    WorldGenerator worldgenerator = null;
    if (this == Blocks.BROWN_MUSHROOM) {
      worldGenBigMushroom = new WorldGenBigMushroom(Blocks.BROWN_MUSHROOM_BLOCK);
    } else if (this == Blocks.RED_MUSHROOM) {
      worldGenBigMushroom = new WorldGenBigMushroom(Blocks.RED_MUSHROOM_BLOCK);
    } 
    if (worldGenBigMushroom != null && worldGenBigMushroom.generate(worldIn, rand, pos))
      return true; 
    worldIn.setBlockState(pos, state, 3);
    return false;
  }
  
  public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
    return true;
  }
  
  public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
    return (rand.nextFloat() < 0.4D);
  }
  
  public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
    generateBigMushroom(worldIn, pos, state, rand);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockMushroom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */