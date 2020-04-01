package net.minecraft.item;

import net.minecraft.util.text.TextFormatting;

public enum EnumRarity {
  COMMON(TextFormatting.WHITE, "Common"),
  UNCOMMON(TextFormatting.YELLOW, "Uncommon"),
  RARE(TextFormatting.AQUA, "Rare"),
  EPIC(TextFormatting.LIGHT_PURPLE, "Epic");
  
  public final TextFormatting rarityColor;
  
  public final String rarityName;
  
  EnumRarity(TextFormatting color, String name) {
    this.rarityColor = color;
    this.rarityName = name;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\EnumRarity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */