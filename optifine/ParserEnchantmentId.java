package optifine;

import net.minecraft.enchantment.Enchantment;

public class ParserEnchantmentId implements IParserInt {
  public int parse(String p_parse_1_, int p_parse_2_) {
    Enchantment enchantment = Enchantment.getEnchantmentByLocation(p_parse_1_);
    return (enchantment == null) ? p_parse_2_ : Enchantment.getEnchantmentID(enchantment);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\optifine\ParserEnchantmentId.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */