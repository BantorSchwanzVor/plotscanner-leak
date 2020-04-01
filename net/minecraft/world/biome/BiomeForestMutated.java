package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BiomeForestMutated extends BiomeForest {
  public BiomeForestMutated(Biome.BiomeProperties properties) {
    super(BiomeForest.Type.BIRCH, properties);
  }
  
  public WorldGenAbstractTree genBigTreeChance(Random rand) {
    return rand.nextBoolean() ? (WorldGenAbstractTree)BiomeForest.SUPER_BIRCH_TREE : (WorldGenAbstractTree)BiomeForest.BIRCH_TREE;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\biome\BiomeForestMutated.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */