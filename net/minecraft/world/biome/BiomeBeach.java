package net.minecraft.world.biome;

import net.minecraft.init.Blocks;

public class BiomeBeach extends Biome {
  public BiomeBeach(Biome.BiomeProperties properties) {
    super(properties);
    this.spawnableCreatureList.clear();
    this.topBlock = Blocks.SAND.getDefaultState();
    this.fillerBlock = Blocks.SAND.getDefaultState();
    this.theBiomeDecorator.treesPerChunk = -999;
    this.theBiomeDecorator.deadBushPerChunk = 0;
    this.theBiomeDecorator.reedsPerChunk = 0;
    this.theBiomeDecorator.cactiPerChunk = 0;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\biome\BiomeBeach.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */