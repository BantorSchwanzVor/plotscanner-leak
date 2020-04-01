package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WorldGenEndIsland extends WorldGenerator {
  public boolean generate(World worldIn, Random rand, BlockPos position) {
    float f = (rand.nextInt(3) + 4);
    for (int i = 0; f > 0.5F; i--) {
      for (int j = MathHelper.floor(-f); j <= MathHelper.ceil(f); j++) {
        for (int k = MathHelper.floor(-f); k <= MathHelper.ceil(f); k++) {
          if ((j * j + k * k) <= (f + 1.0F) * (f + 1.0F))
            setBlockAndNotifyAdequately(worldIn, position.add(j, i, k), Blocks.END_STONE.getDefaultState()); 
        } 
      } 
      f = (float)(f - rand.nextInt(2) + 0.5D);
    } 
    return true;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\gen\feature\WorldGenEndIsland.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */