package net.minecraft.entity.ai;

import java.util.List;
import java.util.Random;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityAIMate extends EntityAIBase {
  private final EntityAnimal theAnimal;
  
  private final Class<? extends EntityAnimal> field_190857_e;
  
  World theWorld;
  
  private EntityAnimal targetMate;
  
  int spawnBabyDelay;
  
  double moveSpeed;
  
  public EntityAIMate(EntityAnimal animal, double speedIn) {
    this(animal, speedIn, (Class)animal.getClass());
  }
  
  public EntityAIMate(EntityAnimal p_i47306_1_, double p_i47306_2_, Class<? extends EntityAnimal> p_i47306_4_) {
    this.theAnimal = p_i47306_1_;
    this.theWorld = p_i47306_1_.world;
    this.field_190857_e = p_i47306_4_;
    this.moveSpeed = p_i47306_2_;
    setMutexBits(3);
  }
  
  public boolean shouldExecute() {
    if (!this.theAnimal.isInLove())
      return false; 
    this.targetMate = getNearbyMate();
    return (this.targetMate != null);
  }
  
  public boolean continueExecuting() {
    return (this.targetMate.isEntityAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 60);
  }
  
  public void resetTask() {
    this.targetMate = null;
    this.spawnBabyDelay = 0;
  }
  
  public void updateTask() {
    this.theAnimal.getLookHelper().setLookPositionWithEntity((Entity)this.targetMate, 10.0F, this.theAnimal.getVerticalFaceSpeed());
    this.theAnimal.getNavigator().tryMoveToEntityLiving((Entity)this.targetMate, this.moveSpeed);
    this.spawnBabyDelay++;
    if (this.spawnBabyDelay >= 60 && this.theAnimal.getDistanceSqToEntity((Entity)this.targetMate) < 9.0D)
      spawnBaby(); 
  }
  
  private EntityAnimal getNearbyMate() {
    List<EntityAnimal> list = this.theWorld.getEntitiesWithinAABB(this.field_190857_e, this.theAnimal.getEntityBoundingBox().expandXyz(8.0D));
    double d0 = Double.MAX_VALUE;
    EntityAnimal entityanimal = null;
    for (EntityAnimal entityanimal1 : list) {
      if (this.theAnimal.canMateWith(entityanimal1) && this.theAnimal.getDistanceSqToEntity((Entity)entityanimal1) < d0) {
        entityanimal = entityanimal1;
        d0 = this.theAnimal.getDistanceSqToEntity((Entity)entityanimal1);
      } 
    } 
    return entityanimal;
  }
  
  private void spawnBaby() {
    EntityAgeable entityageable = this.theAnimal.createChild((EntityAgeable)this.targetMate);
    if (entityageable != null) {
      EntityPlayerMP entityplayermp = this.theAnimal.func_191993_do();
      if (entityplayermp == null && this.targetMate.func_191993_do() != null)
        entityplayermp = this.targetMate.func_191993_do(); 
      if (entityplayermp != null) {
        entityplayermp.addStat(StatList.ANIMALS_BRED);
        CriteriaTriggers.field_192134_n.func_192168_a(entityplayermp, this.theAnimal, this.targetMate, entityageable);
      } 
      this.theAnimal.setGrowingAge(6000);
      this.targetMate.setGrowingAge(6000);
      this.theAnimal.resetInLove();
      this.targetMate.resetInLove();
      entityageable.setGrowingAge(-24000);
      entityageable.setLocationAndAngles(this.theAnimal.posX, this.theAnimal.posY, this.theAnimal.posZ, 0.0F, 0.0F);
      this.theWorld.spawnEntityInWorld((Entity)entityageable);
      Random random = this.theAnimal.getRNG();
      for (int i = 0; i < 7; i++) {
        double d0 = random.nextGaussian() * 0.02D;
        double d1 = random.nextGaussian() * 0.02D;
        double d2 = random.nextGaussian() * 0.02D;
        double d3 = random.nextDouble() * this.theAnimal.width * 2.0D - this.theAnimal.width;
        double d4 = 0.5D + random.nextDouble() * this.theAnimal.height;
        double d5 = random.nextDouble() * this.theAnimal.width * 2.0D - this.theAnimal.width;
        this.theWorld.spawnParticle(EnumParticleTypes.HEART, this.theAnimal.posX + d3, this.theAnimal.posY + d4, this.theAnimal.posZ + d5, d0, d1, d2, new int[0]);
      } 
      if (this.theWorld.getGameRules().getBoolean("doMobLoot"))
        this.theWorld.spawnEntityInWorld((Entity)new EntityXPOrb(this.theWorld, this.theAnimal.posX, this.theAnimal.posY, this.theAnimal.posZ, random.nextInt(7) + 1)); 
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\ai\EntityAIMate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */