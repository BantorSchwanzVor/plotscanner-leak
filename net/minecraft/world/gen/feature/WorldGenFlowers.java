package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenFlowers extends WorldGenerator {
  private BlockFlower flower;
  
  private IBlockState state;
  
  public WorldGenFlowers(BlockFlower flowerIn, BlockFlower.EnumFlowerType type) {
    setGeneratedBlock(flowerIn, type);
  }
  
  public void setGeneratedBlock(BlockFlower flowerIn, BlockFlower.EnumFlowerType typeIn) {
    this.flower = flowerIn;
    this.state = flowerIn.getDefaultState().withProperty(flowerIn.getTypeProperty(), (Comparable)typeIn);
  }
  
  public boolean generate(World worldIn, Random rand, BlockPos position) {
    for (int i = 0; i < 64; i++) {
      BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
      if (worldIn.isAirBlock(blockpos) && (!worldIn.provider.getHasNoSky() || blockpos.getY() < 255) && this.flower.canBlockStay(worldIn, blockpos, this.state))
        worldIn.setBlockState(blockpos, this.state, 2); 
    } 
    return true;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\gen\feature\WorldGenFlowers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */