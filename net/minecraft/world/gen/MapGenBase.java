package net.minecraft.world.gen;

import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class MapGenBase {
  protected int range = 8;
  
  protected Random rand = new Random();
  
  protected World worldObj;
  
  public void generate(World worldIn, int x, int z, ChunkPrimer primer) {
    int i = this.range;
    this.worldObj = worldIn;
    this.rand.setSeed(worldIn.getSeed());
    long j = this.rand.nextLong();
    long k = this.rand.nextLong();
    for (int l = x - i; l <= x + i; l++) {
      for (int i1 = z - i; i1 <= z + i; i1++) {
        long j1 = l * j;
        long k1 = i1 * k;
        this.rand.setSeed(j1 ^ k1 ^ worldIn.getSeed());
        recursiveGenerate(worldIn, l, i1, x, z, primer);
      } 
    } 
  }
  
  public static void func_191068_a(long p_191068_0_, Random p_191068_2_, int p_191068_3_, int p_191068_4_) {
    p_191068_2_.setSeed(p_191068_0_);
    long i = p_191068_2_.nextLong();
    long j = p_191068_2_.nextLong();
    long k = p_191068_3_ * i;
    long l = p_191068_4_ * j;
    p_191068_2_.setSeed(k ^ l ^ p_191068_0_);
  }
  
  protected void recursiveGenerate(World worldIn, int chunkX, int chunkZ, int p_180701_4_, int p_180701_5_, ChunkPrimer chunkPrimerIn) {}
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\gen\MapGenBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */