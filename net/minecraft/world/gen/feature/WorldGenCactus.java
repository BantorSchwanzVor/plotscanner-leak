package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenCactus extends WorldGenerator {
  public boolean generate(World worldIn, Random rand, BlockPos position) {
    for (int i = 0; i < 10; i++) {
      BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
      if (worldIn.isAirBlock(blockpos)) {
        int j = 1 + rand.nextInt(rand.nextInt(3) + 1);
        for (int k = 0; k < j; k++) {
          if (Blocks.CACTUS.canBlockStay(worldIn, blockpos))
            worldIn.setBlockState(blockpos.up(k), Blocks.CACTUS.getDefaultState(), 2); 
        } 
      } 
    } 
    return true;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\gen\feature\WorldGenCactus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */