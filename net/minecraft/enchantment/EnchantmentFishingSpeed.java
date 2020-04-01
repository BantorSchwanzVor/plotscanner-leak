package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentFishingSpeed extends Enchantment {
  protected EnchantmentFishingSpeed(Enchantment.Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot... slots) {
    super(rarityIn, typeIn, slots);
    setName("fishingSpeed");
  }
  
  public int getMinEnchantability(int enchantmentLevel) {
    return 15 + (enchantmentLevel - 1) * 9;
  }
  
  public int getMaxEnchantability(int enchantmentLevel) {
    return super.getMinEnchantability(enchantmentLevel) + 50;
  }
  
  public int getMaxLevel() {
    return 3;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\enchantment\EnchantmentFishingSpeed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */