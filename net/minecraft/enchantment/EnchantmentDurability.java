package net.minecraft.enchantment;

import java.util.Random;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class EnchantmentDurability extends Enchantment {
  protected EnchantmentDurability(Enchantment.Rarity rarityIn, EntityEquipmentSlot... slots) {
    super(rarityIn, EnumEnchantmentType.BREAKABLE, slots);
    setName("durability");
  }
  
  public int getMinEnchantability(int enchantmentLevel) {
    return 5 + (enchantmentLevel - 1) * 8;
  }
  
  public int getMaxEnchantability(int enchantmentLevel) {
    return super.getMinEnchantability(enchantmentLevel) + 50;
  }
  
  public int getMaxLevel() {
    return 3;
  }
  
  public boolean canApply(ItemStack stack) {
    return stack.isItemStackDamageable() ? true : super.canApply(stack);
  }
  
  public static boolean negateDamage(ItemStack p_92097_0_, int p_92097_1_, Random p_92097_2_) {
    if (p_92097_0_.getItem() instanceof net.minecraft.item.ItemArmor && p_92097_2_.nextFloat() < 0.6F)
      return false; 
    return (p_92097_2_.nextInt(p_92097_1_ + 1) > 0);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\enchantment\EnchantmentDurability.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */