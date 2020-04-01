package net.minecraft.entity.boss.dragon.phase;

import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.DamageSource;

public abstract class PhaseSittingBase extends PhaseBase {
  public PhaseSittingBase(EntityDragon p_i46794_1_) {
    super(p_i46794_1_);
  }
  
  public boolean getIsStationary() {
    return true;
  }
  
  public float getAdjustedDamage(MultiPartEntityPart pt, DamageSource src, float damage) {
    if (src.getSourceOfDamage() instanceof net.minecraft.entity.projectile.EntityArrow) {
      src.getSourceOfDamage().setFire(1);
      return 0.0F;
    } 
    return super.getAdjustedDamage(pt, src, damage);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\boss\dragon\phase\PhaseSittingBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */