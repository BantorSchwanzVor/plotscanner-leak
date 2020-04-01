package net.minecraft.world.biome;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;

public class BiomeHell extends Biome {
  public BiomeHell(Biome.BiomeProperties properties) {
    super(properties);
    this.spawnableMonsterList.clear();
    this.spawnableCreatureList.clear();
    this.spawnableWaterCreatureList.clear();
    this.spawnableCaveCreatureList.clear();
    this.spawnableMonsterList.add(new Biome.SpawnListEntry((Class)EntityGhast.class, 50, 4, 4));
    this.spawnableMonsterList.add(new Biome.SpawnListEntry((Class)EntityPigZombie.class, 100, 4, 4));
    this.spawnableMonsterList.add(new Biome.SpawnListEntry((Class)EntityMagmaCube.class, 2, 4, 4));
    this.spawnableMonsterList.add(new Biome.SpawnListEntry((Class)EntityEnderman.class, 1, 4, 4));
    this.theBiomeDecorator = new BiomeHellDecorator();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\biome\BiomeHell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */