package net.minecraft.potion;

import net.minecraft.entity.ai.attributes.AttributeModifier;

public class PotionAttackDamage extends Potion {
  protected final double bonusPerLevel;
  
  protected PotionAttackDamage(boolean isBadEffectIn, int liquidColorIn, double bonusPerLevelIn) {
    super(isBadEffectIn, liquidColorIn);
    this.bonusPerLevel = bonusPerLevelIn;
  }
  
  public double getAttributeModifierAmount(int amplifier, AttributeModifier modifier) {
    return this.bonusPerLevel * (amplifier + 1);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\potion\PotionAttackDamage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */