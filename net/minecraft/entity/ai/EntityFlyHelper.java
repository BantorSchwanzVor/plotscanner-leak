package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.math.MathHelper;

public class EntityFlyHelper extends EntityMoveHelper {
  public EntityFlyHelper(EntityLiving p_i47418_1_) {
    super(p_i47418_1_);
  }
  
  public void onUpdateMoveHelper() {
    if (this.action == EntityMoveHelper.Action.MOVE_TO) {
      float f1;
      this.action = EntityMoveHelper.Action.WAIT;
      this.entity.setNoGravity(true);
      double d0 = this.posX - this.entity.posX;
      double d1 = this.posY - this.entity.posY;
      double d2 = this.posZ - this.entity.posZ;
      double d3 = d0 * d0 + d1 * d1 + d2 * d2;
      if (d3 < 2.500000277905201E-7D) {
        this.entity.setMoveForward(0.0F);
        this.entity.func_191989_p(0.0F);
        return;
      } 
      float f = (float)(MathHelper.atan2(d2, d0) * 57.29577951308232D) - 90.0F;
      this.entity.rotationYaw = limitAngle(this.entity.rotationYaw, f, 10.0F);
      if (this.entity.onGround) {
        f1 = (float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
      } else {
        f1 = (float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.field_193334_e).getAttributeValue());
      } 
      this.entity.setAIMoveSpeed(f1);
      double d4 = MathHelper.sqrt(d0 * d0 + d2 * d2);
      float f2 = (float)-(MathHelper.atan2(d1, d4) * 57.29577951308232D);
      this.entity.rotationPitch = limitAngle(this.entity.rotationPitch, f2, 10.0F);
      this.entity.setMoveForward((d1 > 0.0D) ? f1 : -f1);
    } else {
      this.entity.setNoGravity(false);
      this.entity.setMoveForward(0.0F);
      this.entity.func_191989_p(0.0F);
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\ai\EntityFlyHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */