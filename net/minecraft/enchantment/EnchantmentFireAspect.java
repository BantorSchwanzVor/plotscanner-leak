package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentFireAspect extends Enchantment {
  protected EnchantmentFireAspect(Enchantment.Rarity rarityIn, EntityEquipmentSlot... slots) {
    super(rarityIn, EnumEnchantmentType.WEAPON, slots);
    setName("fire");
  }
  
  public int getMinEnchantability(int enchantmentLevel) {
    return 10 + 20 * (enchantmentLevel - 1);
  }
  
  public int getMaxEnchantability(int enchantmentLevel) {
    return super.getMinEnchantability(enchantmentLevel) + 50;
  }
  
  public int getMaxLevel() {
    return 2;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\enchantment\EnchantmentFireAspect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */