package net.minecraft.world.biome;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.Blocks;

public class BiomeEnd extends Biome {
  public BiomeEnd(Biome.BiomeProperties properties) {
    super(properties);
    this.spawnableMonsterList.clear();
    this.spawnableCreatureList.clear();
    this.spawnableWaterCreatureList.clear();
    this.spawnableCaveCreatureList.clear();
    this.spawnableMonsterList.add(new Biome.SpawnListEntry((Class)EntityEnderman.class, 10, 4, 4));
    this.topBlock = Blocks.DIRT.getDefaultState();
    this.fillerBlock = Blocks.DIRT.getDefaultState();
    this.theBiomeDecorator = new BiomeEndDecorator();
  }
  
  public int getSkyColorByTemp(float currentTemperature) {
    return 0;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\biome\BiomeEnd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */