package net.minecraft.world.biome;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenDesertWells;
import net.minecraft.world.gen.feature.WorldGenFossils;

public class BiomeDesert extends Biome {
  public BiomeDesert(Biome.BiomeProperties properties) {
    super(properties);
    this.spawnableCreatureList.clear();
    this.topBlock = Blocks.SAND.getDefaultState();
    this.fillerBlock = Blocks.SAND.getDefaultState();
    this.theBiomeDecorator.treesPerChunk = -999;
    this.theBiomeDecorator.deadBushPerChunk = 2;
    this.theBiomeDecorator.reedsPerChunk = 50;
    this.theBiomeDecorator.cactiPerChunk = 10;
    this.spawnableCreatureList.clear();
    this.spawnableCreatureList.add(new Biome.SpawnListEntry((Class)EntityRabbit.class, 4, 2, 3));
    Iterator<Biome.SpawnListEntry> iterator = this.spawnableMonsterList.iterator();
    while (iterator.hasNext()) {
      Biome.SpawnListEntry biome$spawnlistentry = iterator.next();
      if (biome$spawnlistentry.entityClass == EntityZombie.class || biome$spawnlistentry.entityClass == EntityZombieVillager.class)
        iterator.remove(); 
    } 
    this.spawnableMonsterList.add(new Biome.SpawnListEntry((Class)EntityZombie.class, 19, 4, 4));
    this.spawnableMonsterList.add(new Biome.SpawnListEntry((Class)EntityZombieVillager.class, 1, 1, 1));
    this.spawnableMonsterList.add(new Biome.SpawnListEntry((Class)EntityHusk.class, 80, 4, 4));
  }
  
  public void decorate(World worldIn, Random rand, BlockPos pos) {
    super.decorate(worldIn, rand, pos);
    if (rand.nextInt(1000) == 0) {
      int i = rand.nextInt(16) + 8;
      int j = rand.nextInt(16) + 8;
      BlockPos blockpos = worldIn.getHeight(pos.add(i, 0, j)).up();
      (new WorldGenDesertWells()).generate(worldIn, rand, blockpos);
    } 
    if (rand.nextInt(64) == 0)
      (new WorldGenFossils()).generate(worldIn, rand, pos); 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\biome\BiomeDesert.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */