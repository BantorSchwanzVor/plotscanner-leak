package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentMending extends Enchantment {
  public EnchantmentMending(Enchantment.Rarity rarityIn, EntityEquipmentSlot... slots) {
    super(rarityIn, EnumEnchantmentType.BREAKABLE, slots);
    setName("mending");
  }
  
  public int getMinEnchantability(int enchantmentLevel) {
    return enchantmentLevel * 25;
  }
  
  public int getMaxEnchantability(int enchantmentLevel) {
    return getMinEnchantability(enchantmentLevel) + 50;
  }
  
  public boolean isTreasureEnchantment() {
    return true;
  }
  
  public int getMaxLevel() {
    return 1;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\enchantment\EnchantmentMending.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */