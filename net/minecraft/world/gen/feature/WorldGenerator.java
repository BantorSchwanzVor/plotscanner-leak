package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class WorldGenerator {
  private final boolean doBlockNotify;
  
  public WorldGenerator() {
    this(false);
  }
  
  public WorldGenerator(boolean notify) {
    this.doBlockNotify = notify;
  }
  
  public abstract boolean generate(World paramWorld, Random paramRandom, BlockPos paramBlockPos);
  
  public void setDecorationDefaults() {}
  
  protected void setBlockAndNotifyAdequately(World worldIn, BlockPos pos, IBlockState state) {
    if (this.doBlockNotify) {
      worldIn.setBlockState(pos, state, 3);
    } else {
      worldIn.setBlockState(pos, state, 2);
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\gen\feature\WorldGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */