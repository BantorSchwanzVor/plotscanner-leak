package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentBindingCurse extends Enchantment {
  public EnchantmentBindingCurse(Enchantment.Rarity p_i47254_1_, EntityEquipmentSlot... p_i47254_2_) {
    super(p_i47254_1_, EnumEnchantmentType.WEARABLE, p_i47254_2_);
    setName("binding_curse");
  }
  
  public int getMinEnchantability(int enchantmentLevel) {
    return 25;
  }
  
  public int getMaxEnchantability(int enchantmentLevel) {
    return 50;
  }
  
  public int getMaxLevel() {
    return 1;
  }
  
  public boolean isTreasureEnchantment() {
    return true;
  }
  
  public boolean func_190936_d() {
    return true;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\enchantment\EnchantmentBindingCurse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */