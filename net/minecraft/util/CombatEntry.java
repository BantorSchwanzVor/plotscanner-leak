package net.minecraft.util;

import javax.annotation.Nullable;
import net.minecraft.util.text.ITextComponent;

public class CombatEntry {
  private final DamageSource damageSrc;
  
  private final int time;
  
  private final float damage;
  
  private final float health;
  
  private final String fallSuffix;
  
  private final float fallDistance;
  
  public CombatEntry(DamageSource damageSrcIn, int p_i1564_2_, float healthAmount, float damageAmount, String fallSuffixIn, float fallDistanceIn) {
    this.damageSrc = damageSrcIn;
    this.time = p_i1564_2_;
    this.damage = damageAmount;
    this.health = healthAmount;
    this.fallSuffix = fallSuffixIn;
    this.fallDistance = fallDistanceIn;
  }
  
  public DamageSource getDamageSrc() {
    return this.damageSrc;
  }
  
  public float getDamage() {
    return this.damage;
  }
  
  public boolean isLivingDamageSrc() {
    return this.damageSrc.getEntity() instanceof net.minecraft.entity.EntityLivingBase;
  }
  
  @Nullable
  public String getFallSuffix() {
    return this.fallSuffix;
  }
  
  @Nullable
  public ITextComponent getDamageSrcDisplayName() {
    return (getDamageSrc().getEntity() == null) ? null : getDamageSrc().getEntity().getDisplayName();
  }
  
  public float getDamageAmount() {
    return (this.damageSrc == DamageSource.outOfWorld) ? Float.MAX_VALUE : this.fallDistance;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\CombatEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */