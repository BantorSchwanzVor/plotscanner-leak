package net.minecraft.world.biome;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;

public class BiomeProviderSingle extends BiomeProvider {
  private final Biome biome;
  
  public BiomeProviderSingle(Biome biomeIn) {
    this.biome = biomeIn;
  }
  
  public Biome getBiome(BlockPos pos) {
    return this.biome;
  }
  
  public Biome[] getBiomesForGeneration(Biome[] biomes, int x, int z, int width, int height) {
    if (biomes == null || biomes.length < width * height)
      biomes = new Biome[width * height]; 
    Arrays.fill((Object[])biomes, 0, width * height, this.biome);
    return biomes;
  }
  
  public Biome[] getBiomes(@Nullable Biome[] oldBiomeList, int x, int z, int width, int depth) {
    if (oldBiomeList == null || oldBiomeList.length < width * depth)
      oldBiomeList = new Biome[width * depth]; 
    Arrays.fill((Object[])oldBiomeList, 0, width * depth, this.biome);
    return oldBiomeList;
  }
  
  public Biome[] getBiomes(@Nullable Biome[] listToReuse, int x, int z, int width, int length, boolean cacheFlag) {
    return getBiomes(listToReuse, x, z, width, length);
  }
  
  @Nullable
  public BlockPos findBiomePosition(int x, int z, int range, List<Biome> biomes, Random random) {
    return biomes.contains(this.biome) ? new BlockPos(x - range + random.nextInt(range * 2 + 1), 0, z - range + random.nextInt(range * 2 + 1)) : null;
  }
  
  public boolean areBiomesViable(int x, int z, int radius, List<Biome> allowed) {
    return allowed.contains(this.biome);
  }
  
  public boolean func_190944_c() {
    return true;
  }
  
  public Biome func_190943_d() {
    return this.biome;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\biome\BiomeProviderSingle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */