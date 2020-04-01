package net.minecraft.world.biome;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BiomeColorHelper {
  private static final ColorResolver GRASS_COLOR = new ColorResolver() {
      public int getColorAtPos(Biome biome, BlockPos blockPosition) {
        return biome.getGrassColorAtPos(blockPosition);
      }
    };
  
  private static final ColorResolver FOLIAGE_COLOR = new ColorResolver() {
      public int getColorAtPos(Biome biome, BlockPos blockPosition) {
        return biome.getFoliageColorAtPos(blockPosition);
      }
    };
  
  private static final ColorResolver WATER_COLOR = new ColorResolver() {
      public int getColorAtPos(Biome biome, BlockPos blockPosition) {
        return biome.getWaterColor();
      }
    };
  
  private static int getColorAtPos(IBlockAccess blockAccess, BlockPos pos, ColorResolver colorResolver) {
    int i = 0;
    int j = 0;
    int k = 0;
    for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(pos.add(-1, 0, -1), pos.add(1, 0, 1))) {
      int l = colorResolver.getColorAtPos(blockAccess.getBiome((BlockPos)blockpos$mutableblockpos), (BlockPos)blockpos$mutableblockpos);
      i += (l & 0xFF0000) >> 16;
      j += (l & 0xFF00) >> 8;
      k += l & 0xFF;
    } 
    return (i / 9 & 0xFF) << 16 | (j / 9 & 0xFF) << 8 | k / 9 & 0xFF;
  }
  
  public static int getGrassColorAtPos(IBlockAccess blockAccess, BlockPos pos) {
    return getColorAtPos(blockAccess, pos, GRASS_COLOR);
  }
  
  public static int getFoliageColorAtPos(IBlockAccess blockAccess, BlockPos pos) {
    return getColorAtPos(blockAccess, pos, FOLIAGE_COLOR);
  }
  
  public static int getWaterColorAtPos(IBlockAccess blockAccess, BlockPos pos) {
    return getColorAtPos(blockAccess, pos, WATER_COLOR);
  }
  
  static interface ColorResolver {
    int getColorAtPos(Biome param1Biome, BlockPos param1BlockPos);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\biome\BiomeColorHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */