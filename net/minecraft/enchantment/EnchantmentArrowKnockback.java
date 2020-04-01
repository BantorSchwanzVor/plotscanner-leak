package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentArrowKnockback extends Enchantment {
  public EnchantmentArrowKnockback(Enchantment.Rarity rarityIn, EntityEquipmentSlot... slots) {
    super(rarityIn, EnumEnchantmentType.BOW, slots);
    setName("arrowKnockback");
  }
  
  public int getMinEnchantability(int enchantmentLevel) {
    return 12 + (enchantmentLevel - 1) * 20;
  }
  
  public int getMaxEnchantability(int enchantmentLevel) {
    return getMinEnchantability(enchantmentLevel) + 25;
  }
  
  public int getMaxLevel() {
    return 2;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\enchantment\EnchantmentArrowKnockback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */