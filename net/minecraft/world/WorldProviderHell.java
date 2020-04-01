package net.minecraft.world;

import net.minecraft.init.Biomes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.gen.ChunkGeneratorHell;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldProviderHell extends WorldProvider {
  public void createBiomeProvider() {
    this.biomeProvider = (BiomeProvider)new BiomeProviderSingle(Biomes.HELL);
    this.isHellWorld = true;
    this.hasNoSky = true;
  }
  
  public Vec3d getFogColor(float p_76562_1_, float p_76562_2_) {
    return new Vec3d(0.20000000298023224D, 0.029999999329447746D, 0.029999999329447746D);
  }
  
  protected void generateLightBrightnessTable() {
    float f = 0.1F;
    for (int i = 0; i <= 15; i++) {
      float f1 = 1.0F - i / 15.0F;
      this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * 0.9F + 0.1F;
    } 
  }
  
  public IChunkGenerator createChunkGenerator() {
    return (IChunkGenerator)new ChunkGeneratorHell(this.worldObj, this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.worldObj.getSeed());
  }
  
  public boolean isSurfaceWorld() {
    return false;
  }
  
  public boolean canCoordinateBeSpawn(int x, int z) {
    return false;
  }
  
  public float calculateCelestialAngle(long worldTime, float partialTicks) {
    return 0.5F;
  }
  
  public boolean canRespawnHere() {
    return false;
  }
  
  public boolean doesXZShowFog(int x, int z) {
    return true;
  }
  
  public WorldBorder createWorldBorder() {
    return new WorldBorder() {
        public double getCenterX() {
          return super.getCenterX() / 8.0D;
        }
        
        public double getCenterZ() {
          return super.getCenterZ() / 8.0D;
        }
      };
  }
  
  public DimensionType getDimensionType() {
    return DimensionType.NETHER;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\WorldProviderHell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */