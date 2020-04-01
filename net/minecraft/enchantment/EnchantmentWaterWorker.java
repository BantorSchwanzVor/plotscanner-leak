package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentWaterWorker extends Enchantment {
  public EnchantmentWaterWorker(Enchantment.Rarity rarityIn, EntityEquipmentSlot... slots) {
    super(rarityIn, EnumEnchantmentType.ARMOR_HEAD, slots);
    setName("waterWorker");
  }
  
  public int getMinEnchantability(int enchantmentLevel) {
    return 1;
  }
  
  public int getMaxEnchantability(int enchantmentLevel) {
    return getMinEnchantability(enchantmentLevel) + 40;
  }
  
  public int getMaxLevel() {
    return 1;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\enchantment\EnchantmentWaterWorker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */