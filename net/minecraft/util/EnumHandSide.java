package net.minecraft.util;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public enum EnumHandSide {
  LEFT((ITextComponent)new TextComponentTranslation("options.mainHand.left", new Object[0])),
  RIGHT((ITextComponent)new TextComponentTranslation("options.mainHand.right", new Object[0]));
  
  private final ITextComponent handName;
  
  EnumHandSide(ITextComponent nameIn) {
    this.handName = nameIn;
  }
  
  public EnumHandSide opposite() {
    return (this == LEFT) ? RIGHT : LEFT;
  }
  
  public String toString() {
    return this.handName.getUnformattedText();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\EnumHandSide.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */