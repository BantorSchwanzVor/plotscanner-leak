package net.minecraft.entity.item;

import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityEnderPearl extends EntityThrowable {
  private EntityLivingBase thrower;
  
  public EntityEnderPearl(World worldIn) {
    super(worldIn);
  }
  
  public EntityEnderPearl(World worldIn, EntityLivingBase throwerIn) {
    super(worldIn, throwerIn);
    this.thrower = throwerIn;
  }
  
  public EntityEnderPearl(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }
  
  public static void registerFixesEnderPearl(DataFixer fixer) {
    EntityThrowable.registerFixesThrowable(fixer, "ThrownEnderpearl");
  }
  
  protected void onImpact(RayTraceResult result) {
    EntityLivingBase entitylivingbase = getThrower();
    if (result.entityHit != null) {
      if (result.entityHit == this.thrower)
        return; 
      result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage((Entity)this, (Entity)entitylivingbase), 0.0F);
    } 
    if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
      BlockPos blockpos = result.getBlockPos();
      TileEntity tileentity = this.world.getTileEntity(blockpos);
      if (tileentity instanceof TileEntityEndGateway) {
        TileEntityEndGateway tileentityendgateway = (TileEntityEndGateway)tileentity;
        if (entitylivingbase != null) {
          if (entitylivingbase instanceof EntityPlayerMP)
            CriteriaTriggers.field_192124_d.func_192193_a((EntityPlayerMP)entitylivingbase, this.world.getBlockState(blockpos)); 
          tileentityendgateway.teleportEntity((Entity)entitylivingbase);
          setDead();
          return;
        } 
        tileentityendgateway.teleportEntity((Entity)this);
        return;
      } 
    } 
    for (int i = 0; i < 32; i++)
      this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX, this.posY + this.rand.nextDouble() * 2.0D, this.posZ, this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian(), new int[0]); 
    if (!this.world.isRemote) {
      if (entitylivingbase instanceof EntityPlayerMP) {
        EntityPlayerMP entityplayermp = (EntityPlayerMP)entitylivingbase;
        if (entityplayermp.connection.getNetworkManager().isChannelOpen() && entityplayermp.world == this.world && !entityplayermp.isPlayerSleeping()) {
          if (this.rand.nextFloat() < 0.05F && this.world.getGameRules().getBoolean("doMobSpawning")) {
            EntityEndermite entityendermite = new EntityEndermite(this.world);
            entityendermite.setSpawnedByPlayer(true);
            entityendermite.setLocationAndAngles(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, entitylivingbase.rotationYaw, entitylivingbase.rotationPitch);
            this.world.spawnEntityInWorld((Entity)entityendermite);
          } 
          if (entitylivingbase.isRiding())
            entitylivingbase.dismountRidingEntity(); 
          entitylivingbase.setPositionAndUpdate(this.posX, this.posY, this.posZ);
          entitylivingbase.fallDistance = 0.0F;
          entitylivingbase.attackEntityFrom(DamageSource.fall, 5.0F);
        } 
      } else if (entitylivingbase != null) {
        entitylivingbase.setPositionAndUpdate(this.posX, this.posY, this.posZ);
        entitylivingbase.fallDistance = 0.0F;
      } 
      setDead();
    } 
  }
  
  public void onUpdate() {
    EntityLivingBase entitylivingbase = getThrower();
    if (entitylivingbase != null && entitylivingbase instanceof net.minecraft.entity.player.EntityPlayer && !entitylivingbase.isEntityAlive()) {
      setDead();
    } else {
      super.onUpdate();
    } 
  }
  
  @Nullable
  public Entity changeDimension(int dimensionIn) {
    if (this.thrower.dimension != dimensionIn)
      this.thrower = null; 
    return super.changeDimension(dimensionIn);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\item\EntityEnderPearl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */