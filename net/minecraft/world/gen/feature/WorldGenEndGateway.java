package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class WorldGenEndGateway extends WorldGenerator {
  public boolean generate(World worldIn, Random rand, BlockPos position) {
    for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(position.add(-1, -2, -1), position.add(1, 2, 1))) {
      boolean flag = (blockpos$mutableblockpos.getX() == position.getX());
      boolean flag1 = (blockpos$mutableblockpos.getY() == position.getY());
      boolean flag2 = (blockpos$mutableblockpos.getZ() == position.getZ());
      boolean flag3 = (Math.abs(blockpos$mutableblockpos.getY() - position.getY()) == 2);
      if (flag && flag1 && flag2) {
        setBlockAndNotifyAdequately(worldIn, new BlockPos((Vec3i)blockpos$mutableblockpos), Blocks.END_GATEWAY.getDefaultState());
        continue;
      } 
      if (flag1) {
        setBlockAndNotifyAdequately(worldIn, (BlockPos)blockpos$mutableblockpos, Blocks.AIR.getDefaultState());
        continue;
      } 
      if (flag3 && flag && flag2) {
        setBlockAndNotifyAdequately(worldIn, (BlockPos)blockpos$mutableblockpos, Blocks.BEDROCK.getDefaultState());
        continue;
      } 
      if ((flag || flag2) && !flag3) {
        setBlockAndNotifyAdequately(worldIn, (BlockPos)blockpos$mutableblockpos, Blocks.BEDROCK.getDefaultState());
        continue;
      } 
      setBlockAndNotifyAdequately(worldIn, (BlockPos)blockpos$mutableblockpos, Blocks.AIR.getDefaultState());
    } 
    return true;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\gen\feature\WorldGenEndGateway.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */