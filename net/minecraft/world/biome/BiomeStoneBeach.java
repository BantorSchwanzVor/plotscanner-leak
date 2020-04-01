package net.minecraft.world.biome;

import net.minecraft.init.Blocks;

public class BiomeStoneBeach extends Biome {
  public BiomeStoneBeach(Biome.BiomeProperties properties) {
    super(properties);
    this.spawnableCreatureList.clear();
    this.topBlock = Blocks.STONE.getDefaultState();
    this.fillerBlock = Blocks.STONE.getDefaultState();
    this.theBiomeDecorator.treesPerChunk = -999;
    this.theBiomeDecorator.deadBushPerChunk = 0;
    this.theBiomeDecorator.reedsPerChunk = 0;
    this.theBiomeDecorator.cactiPerChunk = 0;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\biome\BiomeStoneBeach.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */