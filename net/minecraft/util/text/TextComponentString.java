package net.minecraft.util.text;

public class TextComponentString extends TextComponentBase {
  private final String text;
  
  public TextComponentString(String msg) {
    this.text = msg;
  }
  
  public String getText() {
    return this.text;
  }
  
  public String getUnformattedComponentText() {
    return this.text;
  }
  
  public TextComponentString createCopy() {
    TextComponentString textcomponentstring = new TextComponentString(this.text);
    textcomponentstring.setStyle(getStyle().createShallowCopy());
    for (ITextComponent itextcomponent : getSiblings())
      textcomponentstring.appendSibling(itextcomponent.createCopy()); 
    return textcomponentstring;
  }
  
  public boolean equals(Object p_equals_1_) {
    if (this == p_equals_1_)
      return true; 
    if (!(p_equals_1_ instanceof TextComponentString))
      return false; 
    TextComponentString textcomponentstring = (TextComponentString)p_equals_1_;
    return (this.text.equals(textcomponentstring.getText()) && super.equals(p_equals_1_));
  }
  
  public String toString() {
    return "TextComponent{text='" + this.text + '\'' + ", siblings=" + this.siblings + ", style=" + getStyle() + '}';
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\text\TextComponentString.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */