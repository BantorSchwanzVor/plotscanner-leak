package net.minecraft.world.biome;

import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.init.Blocks;

public class BiomeMushroomIsland extends Biome {
  public BiomeMushroomIsland(Biome.BiomeProperties properties) {
    super(properties);
    this.theBiomeDecorator.treesPerChunk = -100;
    this.theBiomeDecorator.flowersPerChunk = -100;
    this.theBiomeDecorator.grassPerChunk = -100;
    this.theBiomeDecorator.mushroomsPerChunk = 1;
    this.theBiomeDecorator.bigMushroomsPerChunk = 1;
    this.topBlock = Blocks.MYCELIUM.getDefaultState();
    this.spawnableMonsterList.clear();
    this.spawnableCreatureList.clear();
    this.spawnableWaterCreatureList.clear();
    this.spawnableCreatureList.add(new Biome.SpawnListEntry((Class)EntityMooshroom.class, 8, 4, 8));
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\biome\BiomeMushroomIsland.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */