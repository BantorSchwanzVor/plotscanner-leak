package net.minecraft.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;

public class PotionAbsorption extends Potion {
  protected PotionAbsorption(boolean isBadEffectIn, int liquidColorIn) {
    super(isBadEffectIn, liquidColorIn);
  }
  
  public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier) {
    entityLivingBaseIn.setAbsorptionAmount(entityLivingBaseIn.getAbsorptionAmount() - (4 * (amplifier + 1)));
    super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);
  }
  
  public void applyAttributesModifiersToEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier) {
    entityLivingBaseIn.setAbsorptionAmount(entityLivingBaseIn.getAbsorptionAmount() + (4 * (amplifier + 1)));
    super.applyAttributesModifiersToEntity(entityLivingBaseIn, attributeMapIn, amplifier);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\potion\PotionAbsorption.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */