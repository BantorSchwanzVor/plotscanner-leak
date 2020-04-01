package net.minecraft.world.gen.layer;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;

public class GenLayerAddMushroomIsland extends GenLayer {
  public GenLayerAddMushroomIsland(long p_i2120_1_, GenLayer p_i2120_3_) {
    super(p_i2120_1_);
    this.parent = p_i2120_3_;
  }
  
  public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
    int i = areaX - 1;
    int j = areaY - 1;
    int k = areaWidth + 2;
    int l = areaHeight + 2;
    int[] aint = this.parent.getInts(i, j, k, l);
    int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);
    for (int i1 = 0; i1 < areaHeight; i1++) {
      for (int j1 = 0; j1 < areaWidth; j1++) {
        int k1 = aint[j1 + 0 + (i1 + 0) * k];
        int l1 = aint[j1 + 2 + (i1 + 0) * k];
        int i2 = aint[j1 + 0 + (i1 + 2) * k];
        int j2 = aint[j1 + 2 + (i1 + 2) * k];
        int k2 = aint[j1 + 1 + (i1 + 1) * k];
        initChunkSeed((j1 + areaX), (i1 + areaY));
        if (k2 == 0 && k1 == 0 && l1 == 0 && i2 == 0 && j2 == 0 && nextInt(100) == 0) {
          aint1[j1 + i1 * areaWidth] = Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND);
        } else {
          aint1[j1 + i1 * areaWidth] = k2;
        } 
      } 
    } 
    return aint1;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\gen\layer\GenLayerAddMushroomIsland.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */