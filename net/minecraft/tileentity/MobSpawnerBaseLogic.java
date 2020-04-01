package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;

public abstract class MobSpawnerBaseLogic {
  private int spawnDelay = 20;
  
  private final List<WeightedSpawnerEntity> potentialSpawns = Lists.newArrayList();
  
  private WeightedSpawnerEntity randomEntity = new WeightedSpawnerEntity();
  
  private double mobRotation;
  
  private double prevMobRotation;
  
  private int minSpawnDelay = 200;
  
  private int maxSpawnDelay = 800;
  
  private int spawnCount = 4;
  
  private Entity cachedEntity;
  
  private int maxNearbyEntities = 6;
  
  private int activatingRangeFromPlayer = 16;
  
  private int spawnRange = 4;
  
  @Nullable
  private ResourceLocation func_190895_g() {
    String s = this.randomEntity.getNbt().getString("id");
    return StringUtils.isNullOrEmpty(s) ? null : new ResourceLocation(s);
  }
  
  public void func_190894_a(@Nullable ResourceLocation p_190894_1_) {
    if (p_190894_1_ != null)
      this.randomEntity.getNbt().setString("id", p_190894_1_.toString()); 
  }
  
  private boolean isActivated() {
    BlockPos blockpos = getSpawnerPosition();
    return getSpawnerWorld().isAnyPlayerWithinRangeAt(blockpos.getX() + 0.5D, blockpos.getY() + 0.5D, blockpos.getZ() + 0.5D, this.activatingRangeFromPlayer);
  }
  
  public void updateSpawner() {
    if (!isActivated()) {
      this.prevMobRotation = this.mobRotation;
    } else {
      BlockPos blockpos = getSpawnerPosition();
      if ((getSpawnerWorld()).isRemote) {
        double d3 = (blockpos.getX() + (getSpawnerWorld()).rand.nextFloat());
        double d4 = (blockpos.getY() + (getSpawnerWorld()).rand.nextFloat());
        double d5 = (blockpos.getZ() + (getSpawnerWorld()).rand.nextFloat());
        getSpawnerWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d3, d4, d5, 0.0D, 0.0D, 0.0D, new int[0]);
        getSpawnerWorld().spawnParticle(EnumParticleTypes.FLAME, d3, d4, d5, 0.0D, 0.0D, 0.0D, new int[0]);
        if (this.spawnDelay > 0)
          this.spawnDelay--; 
        this.prevMobRotation = this.mobRotation;
        this.mobRotation = (this.mobRotation + (1000.0F / (this.spawnDelay + 200.0F))) % 360.0D;
      } else {
        if (this.spawnDelay == -1)
          resetTimer(); 
        if (this.spawnDelay > 0) {
          this.spawnDelay--;
          return;
        } 
        boolean flag = false;
        for (int i = 0; i < this.spawnCount; i++) {
          NBTTagCompound nbttagcompound = this.randomEntity.getNbt();
          NBTTagList nbttaglist = nbttagcompound.getTagList("Pos", 6);
          World world = getSpawnerWorld();
          int j = nbttaglist.tagCount();
          double d0 = (j >= 1) ? nbttaglist.getDoubleAt(0) : (blockpos.getX() + (world.rand.nextDouble() - world.rand.nextDouble()) * this.spawnRange + 0.5D);
          double d1 = (j >= 2) ? nbttaglist.getDoubleAt(1) : (blockpos.getY() + world.rand.nextInt(3) - 1);
          double d2 = (j >= 3) ? nbttaglist.getDoubleAt(2) : (blockpos.getZ() + (world.rand.nextDouble() - world.rand.nextDouble()) * this.spawnRange + 0.5D);
          Entity entity = AnvilChunkLoader.readWorldEntityPos(nbttagcompound, world, d0, d1, d2, false);
          if (entity == null)
            return; 
          int k = world.getEntitiesWithinAABB(entity.getClass(), (new AxisAlignedBB(blockpos.getX(), blockpos.getY(), blockpos.getZ(), (blockpos.getX() + 1), (blockpos.getY() + 1), (blockpos.getZ() + 1))).expandXyz(this.spawnRange)).size();
          if (k >= this.maxNearbyEntities) {
            resetTimer();
            return;
          } 
          EntityLiving entityliving = (entity instanceof EntityLiving) ? (EntityLiving)entity : null;
          entity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, world.rand.nextFloat() * 360.0F, 0.0F);
          if (entityliving == null || (entityliving.getCanSpawnHere() && entityliving.isNotColliding())) {
            if (this.randomEntity.getNbt().getSize() == 1 && this.randomEntity.getNbt().hasKey("id", 8) && entity instanceof EntityLiving)
              ((EntityLiving)entity).onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity)), null); 
            AnvilChunkLoader.spawnEntity(entity, world);
            world.playEvent(2004, blockpos, 0);
            if (entityliving != null)
              entityliving.spawnExplosionParticle(); 
            flag = true;
          } 
        } 
        if (flag)
          resetTimer(); 
      } 
    } 
  }
  
  private void resetTimer() {
    if (this.maxSpawnDelay <= this.minSpawnDelay) {
      this.spawnDelay = this.minSpawnDelay;
    } else {
      int i = this.maxSpawnDelay - this.minSpawnDelay;
      this.spawnDelay = this.minSpawnDelay + (getSpawnerWorld()).rand.nextInt(i);
    } 
    if (!this.potentialSpawns.isEmpty())
      setNextSpawnData((WeightedSpawnerEntity)WeightedRandom.getRandomItem((getSpawnerWorld()).rand, this.potentialSpawns)); 
    broadcastEvent(1);
  }
  
  public void readFromNBT(NBTTagCompound nbt) {
    this.spawnDelay = nbt.getShort("Delay");
    this.potentialSpawns.clear();
    if (nbt.hasKey("SpawnPotentials", 9)) {
      NBTTagList nbttaglist = nbt.getTagList("SpawnPotentials", 10);
      for (int i = 0; i < nbttaglist.tagCount(); i++)
        this.potentialSpawns.add(new WeightedSpawnerEntity(nbttaglist.getCompoundTagAt(i))); 
    } 
    if (nbt.hasKey("SpawnData", 10)) {
      setNextSpawnData(new WeightedSpawnerEntity(1, nbt.getCompoundTag("SpawnData")));
    } else if (!this.potentialSpawns.isEmpty()) {
      setNextSpawnData((WeightedSpawnerEntity)WeightedRandom.getRandomItem((getSpawnerWorld()).rand, this.potentialSpawns));
    } 
    if (nbt.hasKey("MinSpawnDelay", 99)) {
      this.minSpawnDelay = nbt.getShort("MinSpawnDelay");
      this.maxSpawnDelay = nbt.getShort("MaxSpawnDelay");
      this.spawnCount = nbt.getShort("SpawnCount");
    } 
    if (nbt.hasKey("MaxNearbyEntities", 99)) {
      this.maxNearbyEntities = nbt.getShort("MaxNearbyEntities");
      this.activatingRangeFromPlayer = nbt.getShort("RequiredPlayerRange");
    } 
    if (nbt.hasKey("SpawnRange", 99))
      this.spawnRange = nbt.getShort("SpawnRange"); 
    if (getSpawnerWorld() != null)
      this.cachedEntity = null; 
  }
  
  public NBTTagCompound writeToNBT(NBTTagCompound p_189530_1_) {
    ResourceLocation resourcelocation = func_190895_g();
    if (resourcelocation == null)
      return p_189530_1_; 
    p_189530_1_.setShort("Delay", (short)this.spawnDelay);
    p_189530_1_.setShort("MinSpawnDelay", (short)this.minSpawnDelay);
    p_189530_1_.setShort("MaxSpawnDelay", (short)this.maxSpawnDelay);
    p_189530_1_.setShort("SpawnCount", (short)this.spawnCount);
    p_189530_1_.setShort("MaxNearbyEntities", (short)this.maxNearbyEntities);
    p_189530_1_.setShort("RequiredPlayerRange", (short)this.activatingRangeFromPlayer);
    p_189530_1_.setShort("SpawnRange", (short)this.spawnRange);
    p_189530_1_.setTag("SpawnData", (NBTBase)this.randomEntity.getNbt().copy());
    NBTTagList nbttaglist = new NBTTagList();
    if (this.potentialSpawns.isEmpty()) {
      nbttaglist.appendTag((NBTBase)this.randomEntity.toCompoundTag());
    } else {
      for (WeightedSpawnerEntity weightedspawnerentity : this.potentialSpawns)
        nbttaglist.appendTag((NBTBase)weightedspawnerentity.toCompoundTag()); 
    } 
    p_189530_1_.setTag("SpawnPotentials", (NBTBase)nbttaglist);
    return p_189530_1_;
  }
  
  public Entity getCachedEntity() {
    if (this.cachedEntity == null) {
      this.cachedEntity = AnvilChunkLoader.readWorldEntity(this.randomEntity.getNbt(), getSpawnerWorld(), false);
      if (this.randomEntity.getNbt().getSize() == 1 && this.randomEntity.getNbt().hasKey("id", 8) && this.cachedEntity instanceof EntityLiving)
        ((EntityLiving)this.cachedEntity).onInitialSpawn(getSpawnerWorld().getDifficultyForLocation(new BlockPos(this.cachedEntity)), null); 
    } 
    return this.cachedEntity;
  }
  
  public boolean setDelayToMin(int delay) {
    if (delay == 1 && (getSpawnerWorld()).isRemote) {
      this.spawnDelay = this.minSpawnDelay;
      return true;
    } 
    return false;
  }
  
  public void setNextSpawnData(WeightedSpawnerEntity p_184993_1_) {
    this.randomEntity = p_184993_1_;
  }
  
  public abstract void broadcastEvent(int paramInt);
  
  public abstract World getSpawnerWorld();
  
  public abstract BlockPos getSpawnerPosition();
  
  public double getMobRotation() {
    return this.mobRotation;
  }
  
  public double getPrevMobRotation() {
    return this.prevMobRotation;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\tileentity\MobSpawnerBaseLogic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */