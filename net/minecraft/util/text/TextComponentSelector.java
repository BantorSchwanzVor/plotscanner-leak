package net.minecraft.util.text;

public class TextComponentSelector extends TextComponentBase {
  private final String selector;
  
  public TextComponentSelector(String selectorIn) {
    this.selector = selectorIn;
  }
  
  public String getSelector() {
    return this.selector;
  }
  
  public String getUnformattedComponentText() {
    return this.selector;
  }
  
  public TextComponentSelector createCopy() {
    TextComponentSelector textcomponentselector = new TextComponentSelector(this.selector);
    textcomponentselector.setStyle(getStyle().createShallowCopy());
    for (ITextComponent itextcomponent : getSiblings())
      textcomponentselector.appendSibling(itextcomponent.createCopy()); 
    return textcomponentselector;
  }
  
  public boolean equals(Object p_equals_1_) {
    if (this == p_equals_1_)
      return true; 
    if (!(p_equals_1_ instanceof TextComponentSelector))
      return false; 
    TextComponentSelector textcomponentselector = (TextComponentSelector)p_equals_1_;
    return (this.selector.equals(textcomponentselector.selector) && super.equals(p_equals_1_));
  }
  
  public String toString() {
    return "SelectorComponent{pattern='" + this.selector + '\'' + ", siblings=" + this.siblings + ", style=" + getStyle() + '}';
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\text\TextComponentSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */