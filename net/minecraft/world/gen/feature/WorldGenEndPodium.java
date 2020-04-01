package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.properties.IProperty;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class WorldGenEndPodium extends WorldGenerator {
  public static final BlockPos END_PODIUM_LOCATION = BlockPos.ORIGIN;
  
  public static final BlockPos END_PODIUM_CHUNK_POS = new BlockPos(END_PODIUM_LOCATION.getX() - 4 & 0xFFFFFFF0, 0, END_PODIUM_LOCATION.getZ() - 4 & 0xFFFFFFF0);
  
  private final boolean activePortal;
  
  public WorldGenEndPodium(boolean activePortalIn) {
    this.activePortal = activePortalIn;
  }
  
  public boolean generate(World worldIn, Random rand, BlockPos position) {
    for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(new BlockPos(position.getX() - 4, position.getY() - 1, position.getZ() - 4), new BlockPos(position.getX() + 4, position.getY() + 32, position.getZ() + 4))) {
      double d0 = blockpos$mutableblockpos.getDistance(position.getX(), blockpos$mutableblockpos.getY(), position.getZ());
      if (d0 <= 3.5D) {
        if (blockpos$mutableblockpos.getY() < position.getY()) {
          if (d0 <= 2.5D) {
            setBlockAndNotifyAdequately(worldIn, (BlockPos)blockpos$mutableblockpos, Blocks.BEDROCK.getDefaultState());
            continue;
          } 
          if (blockpos$mutableblockpos.getY() < position.getY())
            setBlockAndNotifyAdequately(worldIn, (BlockPos)blockpos$mutableblockpos, Blocks.END_STONE.getDefaultState()); 
          continue;
        } 
        if (blockpos$mutableblockpos.getY() > position.getY()) {
          setBlockAndNotifyAdequately(worldIn, (BlockPos)blockpos$mutableblockpos, Blocks.AIR.getDefaultState());
          continue;
        } 
        if (d0 > 2.5D) {
          setBlockAndNotifyAdequately(worldIn, (BlockPos)blockpos$mutableblockpos, Blocks.BEDROCK.getDefaultState());
          continue;
        } 
        if (this.activePortal) {
          setBlockAndNotifyAdequately(worldIn, new BlockPos((Vec3i)blockpos$mutableblockpos), Blocks.END_PORTAL.getDefaultState());
          continue;
        } 
        setBlockAndNotifyAdequately(worldIn, new BlockPos((Vec3i)blockpos$mutableblockpos), Blocks.AIR.getDefaultState());
      } 
    } 
    for (int i = 0; i < 4; i++)
      setBlockAndNotifyAdequately(worldIn, position.up(i), Blocks.BEDROCK.getDefaultState()); 
    BlockPos blockpos = position.up(2);
    for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
      setBlockAndNotifyAdequately(worldIn, blockpos.offset(enumfacing), Blocks.TORCH.getDefaultState().withProperty((IProperty)BlockTorch.FACING, (Comparable)enumfacing)); 
    return true;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\gen\feature\WorldGenEndPodium.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */