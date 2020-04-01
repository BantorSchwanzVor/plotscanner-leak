package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMelon;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenVines;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeJungle extends Biome {
  private final boolean isEdge;
  
  private static final IBlockState JUNGLE_LOG = Blocks.LOG.getDefaultState().withProperty((IProperty)BlockOldLog.VARIANT, (Comparable)BlockPlanks.EnumType.JUNGLE);
  
  private static final IBlockState JUNGLE_LEAF = Blocks.LEAVES.getDefaultState().withProperty((IProperty)BlockOldLeaf.VARIANT, (Comparable)BlockPlanks.EnumType.JUNGLE).withProperty((IProperty)BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
  
  private static final IBlockState OAK_LEAF = Blocks.LEAVES.getDefaultState().withProperty((IProperty)BlockOldLeaf.VARIANT, (Comparable)BlockPlanks.EnumType.OAK).withProperty((IProperty)BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
  
  public BiomeJungle(boolean isEdgeIn, Biome.BiomeProperties properties) {
    super(properties);
    this.isEdge = isEdgeIn;
    if (isEdgeIn) {
      this.theBiomeDecorator.treesPerChunk = 2;
    } else {
      this.theBiomeDecorator.treesPerChunk = 50;
    } 
    this.theBiomeDecorator.grassPerChunk = 25;
    this.theBiomeDecorator.flowersPerChunk = 4;
    if (!isEdgeIn)
      this.spawnableMonsterList.add(new Biome.SpawnListEntry((Class)EntityOcelot.class, 2, 1, 1)); 
    this.spawnableCreatureList.add(new Biome.SpawnListEntry((Class)EntityParrot.class, 40, 1, 2));
    this.spawnableCreatureList.add(new Biome.SpawnListEntry((Class)EntityChicken.class, 10, 4, 4));
  }
  
  public WorldGenAbstractTree genBigTreeChance(Random rand) {
    if (rand.nextInt(10) == 0)
      return (WorldGenAbstractTree)BIG_TREE_FEATURE; 
    if (rand.nextInt(2) == 0)
      return (WorldGenAbstractTree)new WorldGenShrub(JUNGLE_LOG, OAK_LEAF); 
    return (!this.isEdge && rand.nextInt(3) == 0) ? (WorldGenAbstractTree)new WorldGenMegaJungle(false, 10, 20, JUNGLE_LOG, JUNGLE_LEAF) : (WorldGenAbstractTree)new WorldGenTrees(false, 4 + rand.nextInt(7), JUNGLE_LOG, JUNGLE_LEAF, true);
  }
  
  public WorldGenerator getRandomWorldGenForGrass(Random rand) {
    return (rand.nextInt(4) == 0) ? (WorldGenerator)new WorldGenTallGrass(BlockTallGrass.EnumType.FERN) : (WorldGenerator)new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
  }
  
  public void decorate(World worldIn, Random rand, BlockPos pos) {
    super.decorate(worldIn, rand, pos);
    int i = rand.nextInt(16) + 8;
    int j = rand.nextInt(16) + 8;
    int k = rand.nextInt(worldIn.getHeight(pos.add(i, 0, j)).getY() * 2);
    (new WorldGenMelon()).generate(worldIn, rand, pos.add(i, k, j));
    WorldGenVines worldgenvines = new WorldGenVines();
    for (int j1 = 0; j1 < 50; j1++) {
      k = rand.nextInt(16) + 8;
      int l = 128;
      int i1 = rand.nextInt(16) + 8;
      worldgenvines.generate(worldIn, rand, pos.add(k, 128, i1));
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\biome\BiomeJungle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */