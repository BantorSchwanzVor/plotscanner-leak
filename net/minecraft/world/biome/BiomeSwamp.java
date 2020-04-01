package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenFossils;

public class BiomeSwamp extends Biome {
  protected static final IBlockState WATER_LILY = Blocks.WATERLILY.getDefaultState();
  
  protected BiomeSwamp(Biome.BiomeProperties properties) {
    super(properties);
    this.theBiomeDecorator.treesPerChunk = 2;
    this.theBiomeDecorator.flowersPerChunk = 1;
    this.theBiomeDecorator.deadBushPerChunk = 1;
    this.theBiomeDecorator.mushroomsPerChunk = 8;
    this.theBiomeDecorator.reedsPerChunk = 10;
    this.theBiomeDecorator.clayPerChunk = 1;
    this.theBiomeDecorator.waterlilyPerChunk = 4;
    this.theBiomeDecorator.sandPerChunk2 = 0;
    this.theBiomeDecorator.sandPerChunk = 0;
    this.theBiomeDecorator.grassPerChunk = 5;
    this.spawnableMonsterList.add(new Biome.SpawnListEntry((Class)EntitySlime.class, 1, 1, 1));
  }
  
  public WorldGenAbstractTree genBigTreeChance(Random rand) {
    return (WorldGenAbstractTree)SWAMP_FEATURE;
  }
  
  public int getGrassColorAtPos(BlockPos pos) {
    double d0 = GRASS_COLOR_NOISE.getValue(pos.getX() * 0.0225D, pos.getZ() * 0.0225D);
    return (d0 < -0.1D) ? 5011004 : 6975545;
  }
  
  public int getFoliageColorAtPos(BlockPos pos) {
    return 6975545;
  }
  
  public BlockFlower.EnumFlowerType pickRandomFlower(Random rand, BlockPos pos) {
    return BlockFlower.EnumFlowerType.BLUE_ORCHID;
  }
  
  public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
    double d0 = GRASS_COLOR_NOISE.getValue(x * 0.25D, z * 0.25D);
    if (d0 > 0.0D) {
      int i = x & 0xF;
      int j = z & 0xF;
      for (int k = 255; k >= 0; k--) {
        if (chunkPrimerIn.getBlockState(j, k, i).getMaterial() != Material.AIR) {
          if (k == 62 && chunkPrimerIn.getBlockState(j, k, i).getBlock() != Blocks.WATER) {
            chunkPrimerIn.setBlockState(j, k, i, WATER);
            if (d0 < 0.12D)
              chunkPrimerIn.setBlockState(j, k + 1, i, WATER_LILY); 
          } 
          break;
        } 
      } 
    } 
    generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
  }
  
  public void decorate(World worldIn, Random rand, BlockPos pos) {
    super.decorate(worldIn, rand, pos);
    if (rand.nextInt(64) == 0)
      (new WorldGenFossils()).generate(worldIn, rand, pos); 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\biome\BiomeSwamp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */