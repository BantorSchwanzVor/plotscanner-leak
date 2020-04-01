package net.minecraft.entity.ai;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.util.math.Vec3d;

public class EntityAILlamaFollowCaravan extends EntityAIBase {
  public EntityLlama field_190859_a;
  
  private double field_190860_b;
  
  private int field_190861_c;
  
  public EntityAILlamaFollowCaravan(EntityLlama p_i47305_1_, double p_i47305_2_) {
    this.field_190859_a = p_i47305_1_;
    this.field_190860_b = p_i47305_2_;
    setMutexBits(1);
  }
  
  public boolean shouldExecute() {
    if (!this.field_190859_a.getLeashed() && !this.field_190859_a.func_190718_dR()) {
      List<EntityLlama> list = this.field_190859_a.world.getEntitiesWithinAABB(this.field_190859_a.getClass(), this.field_190859_a.getEntityBoundingBox().expand(9.0D, 4.0D, 9.0D));
      EntityLlama entityllama = null;
      double d0 = Double.MAX_VALUE;
      for (EntityLlama entityllama1 : list) {
        if (entityllama1.func_190718_dR() && !entityllama1.func_190712_dQ()) {
          double d1 = this.field_190859_a.getDistanceSqToEntity((Entity)entityllama1);
          if (d1 <= d0) {
            d0 = d1;
            entityllama = entityllama1;
          } 
        } 
      } 
      if (entityllama == null)
        for (EntityLlama entityllama2 : list) {
          if (entityllama2.getLeashed() && !entityllama2.func_190712_dQ()) {
            double d2 = this.field_190859_a.getDistanceSqToEntity((Entity)entityllama2);
            if (d2 <= d0) {
              d0 = d2;
              entityllama = entityllama2;
            } 
          } 
        }  
      if (entityllama == null)
        return false; 
      if (d0 < 4.0D)
        return false; 
      if (!entityllama.getLeashed() && !func_190858_a(entityllama, 1))
        return false; 
      this.field_190859_a.func_190715_a(entityllama);
      return true;
    } 
    return false;
  }
  
  public boolean continueExecuting() {
    if (this.field_190859_a.func_190718_dR() && this.field_190859_a.func_190716_dS().isEntityAlive() && func_190858_a(this.field_190859_a, 0)) {
      double d0 = this.field_190859_a.getDistanceSqToEntity((Entity)this.field_190859_a.func_190716_dS());
      if (d0 > 676.0D) {
        if (this.field_190860_b <= 3.0D) {
          this.field_190860_b *= 1.2D;
          this.field_190861_c = 40;
          return true;
        } 
        if (this.field_190861_c == 0)
          return false; 
      } 
      if (this.field_190861_c > 0)
        this.field_190861_c--; 
      return true;
    } 
    return false;
  }
  
  public void resetTask() {
    this.field_190859_a.func_190709_dP();
    this.field_190860_b = 2.1D;
  }
  
  public void updateTask() {
    if (this.field_190859_a.func_190718_dR()) {
      EntityLlama entityllama = this.field_190859_a.func_190716_dS();
      double d0 = this.field_190859_a.getDistanceToEntity((Entity)entityllama);
      float f = 2.0F;
      Vec3d vec3d = (new Vec3d(entityllama.posX - this.field_190859_a.posX, entityllama.posY - this.field_190859_a.posY, entityllama.posZ - this.field_190859_a.posZ)).normalize().scale(Math.max(d0 - 2.0D, 0.0D));
      this.field_190859_a.getNavigator().tryMoveToXYZ(this.field_190859_a.posX + vec3d.xCoord, this.field_190859_a.posY + vec3d.yCoord, this.field_190859_a.posZ + vec3d.zCoord, this.field_190860_b);
    } 
  }
  
  private boolean func_190858_a(EntityLlama p_190858_1_, int p_190858_2_) {
    if (p_190858_2_ > 8)
      return false; 
    if (p_190858_1_.func_190718_dR()) {
      if (p_190858_1_.func_190716_dS().getLeashed())
        return true; 
      EntityLlama entityllama = p_190858_1_.func_190716_dS();
      p_190858_2_++;
      return func_190858_a(entityllama, p_190858_2_);
    } 
    return false;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\ai\EntityAILlamaFollowCaravan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */