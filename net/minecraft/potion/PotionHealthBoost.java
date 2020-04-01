package net.minecraft.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;

public class PotionHealthBoost extends Potion {
  public PotionHealthBoost(boolean isBadEffectIn, int liquidColorIn) {
    super(isBadEffectIn, liquidColorIn);
  }
  
  public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier) {
    super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);
    if (entityLivingBaseIn.getHealth() > entityLivingBaseIn.getMaxHealth())
      entityLivingBaseIn.setHealth(entityLivingBaseIn.getMaxHealth()); 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\potion\PotionHealthBoost.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */