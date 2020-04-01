package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentArrowFire extends Enchantment {
  public EnchantmentArrowFire(Enchantment.Rarity rarityIn, EntityEquipmentSlot... slots) {
    super(rarityIn, EnumEnchantmentType.BOW, slots);
    setName("arrowFire");
  }
  
  public int getMinEnchantability(int enchantmentLevel) {
    return 20;
  }
  
  public int getMaxEnchantability(int enchantmentLevel) {
    return 50;
  }
  
  public int getMaxLevel() {
    return 1;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\enchantment\EnchantmentArrowFire.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */