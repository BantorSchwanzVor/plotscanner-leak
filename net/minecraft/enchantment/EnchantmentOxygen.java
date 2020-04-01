package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentOxygen extends Enchantment {
  public EnchantmentOxygen(Enchantment.Rarity rarityIn, EntityEquipmentSlot... slots) {
    super(rarityIn, EnumEnchantmentType.ARMOR_HEAD, slots);
    setName("oxygen");
  }
  
  public int getMinEnchantability(int enchantmentLevel) {
    return 10 * enchantmentLevel;
  }
  
  public int getMaxEnchantability(int enchantmentLevel) {
    return getMinEnchantability(enchantmentLevel) + 30;
  }
  
  public int getMaxLevel() {
    return 3;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\enchantment\EnchantmentOxygen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */