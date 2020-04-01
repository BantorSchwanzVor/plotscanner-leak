package net.minecraft.world;

import javax.annotation.Nullable;
import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.end.DragonFightManager;
import net.minecraft.world.gen.ChunkGeneratorEnd;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldProviderEnd extends WorldProvider {
  private DragonFightManager dragonFightManager;
  
  public void createBiomeProvider() {
    this.biomeProvider = (BiomeProvider)new BiomeProviderSingle(Biomes.SKY);
    NBTTagCompound nbttagcompound = this.worldObj.getWorldInfo().getDimensionData(DimensionType.THE_END);
    this.dragonFightManager = (this.worldObj instanceof WorldServer) ? new DragonFightManager((WorldServer)this.worldObj, nbttagcompound.getCompoundTag("DragonFight")) : null;
  }
  
  public IChunkGenerator createChunkGenerator() {
    return (IChunkGenerator)new ChunkGeneratorEnd(this.worldObj, this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.worldObj.getSeed(), getSpawnCoordinate());
  }
  
  public float calculateCelestialAngle(long worldTime, float partialTicks) {
    return 0.0F;
  }
  
  @Nullable
  public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks) {
    return null;
  }
  
  public Vec3d getFogColor(float p_76562_1_, float p_76562_2_) {
    int i = 10518688;
    float f = MathHelper.cos(p_76562_1_ * 6.2831855F) * 2.0F + 0.5F;
    f = MathHelper.clamp(f, 0.0F, 1.0F);
    float f1 = 0.627451F;
    float f2 = 0.5019608F;
    float f3 = 0.627451F;
    f1 *= f * 0.0F + 0.15F;
    f2 *= f * 0.0F + 0.15F;
    f3 *= f * 0.0F + 0.15F;
    return new Vec3d(f1, f2, f3);
  }
  
  public boolean isSkyColored() {
    return false;
  }
  
  public boolean canRespawnHere() {
    return false;
  }
  
  public boolean isSurfaceWorld() {
    return false;
  }
  
  public float getCloudHeight() {
    return 8.0F;
  }
  
  public boolean canCoordinateBeSpawn(int x, int z) {
    return this.worldObj.getGroundAboveSeaLevel(new BlockPos(x, 0, z)).getMaterial().blocksMovement();
  }
  
  public BlockPos getSpawnCoordinate() {
    return new BlockPos(100, 50, 0);
  }
  
  public int getAverageGroundLevel() {
    return 50;
  }
  
  public boolean doesXZShowFog(int x, int z) {
    return false;
  }
  
  public DimensionType getDimensionType() {
    return DimensionType.THE_END;
  }
  
  public void onWorldSave() {
    NBTTagCompound nbttagcompound = new NBTTagCompound();
    if (this.dragonFightManager != null)
      nbttagcompound.setTag("DragonFight", (NBTBase)this.dragonFightManager.getCompound()); 
    this.worldObj.getWorldInfo().setDimensionData(DimensionType.THE_END, nbttagcompound);
  }
  
  public void onWorldUpdateEntities() {
    if (this.dragonFightManager != null)
      this.dragonFightManager.tick(); 
  }
  
  @Nullable
  public DragonFightManager getDragonFightManager() {
    return this.dragonFightManager;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\WorldProviderEnd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */