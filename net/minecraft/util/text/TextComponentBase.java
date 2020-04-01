package net.minecraft.util.text;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public abstract class TextComponentBase implements ITextComponent {
  protected List<ITextComponent> siblings;
  
  private Style style;
  
  public TextComponentBase() {
    this.siblings = Lists.newArrayList();
  }
  
  public ITextComponent appendSibling(ITextComponent component) {
    component.getStyle().setParentStyle(getStyle());
    this.siblings.add(component);
    return this;
  }
  
  public List<ITextComponent> getSiblings() {
    return this.siblings;
  }
  
  public ITextComponent appendText(String text) {
    return appendSibling(new TextComponentString(text));
  }
  
  public ITextComponent setStyle(Style style) {
    this.style = style;
    for (ITextComponent itextcomponent : this.siblings)
      itextcomponent.getStyle().setParentStyle(getStyle()); 
    return this;
  }
  
  public Style getStyle() {
    if (this.style == null) {
      this.style = new Style();
      for (ITextComponent itextcomponent : this.siblings)
        itextcomponent.getStyle().setParentStyle(this.style); 
    } 
    return this.style;
  }
  
  public Iterator<ITextComponent> iterator() {
    return Iterators.concat((Iterator)Iterators.forArray((Object[])new TextComponentBase[] { this }, ), createDeepCopyIterator(this.siblings));
  }
  
  public final String getUnformattedText() {
    StringBuilder stringbuilder = new StringBuilder();
    for (ITextComponent itextcomponent : this)
      stringbuilder.append(itextcomponent.getUnformattedComponentText()); 
    return stringbuilder.toString();
  }
  
  public final String getFormattedText() {
    StringBuilder stringbuilder = new StringBuilder();
    for (ITextComponent itextcomponent : this) {
      String s = itextcomponent.getUnformattedComponentText();
      if (!s.isEmpty()) {
        stringbuilder.append(itextcomponent.getStyle().getFormattingCode());
        stringbuilder.append(s);
        stringbuilder.append(TextFormatting.RESET);
      } 
    } 
    return stringbuilder.toString();
  }
  
  public static Iterator<ITextComponent> createDeepCopyIterator(Iterable<ITextComponent> components) {
    Iterator<ITextComponent> iterator = Iterators.concat(Iterators.transform(components.iterator(), new Function<ITextComponent, Iterator<ITextComponent>>() {
            public Iterator<ITextComponent> apply(@Nullable ITextComponent p_apply_1_) {
              return p_apply_1_.iterator();
            }
          }));
    iterator = Iterators.transform(iterator, new Function<ITextComponent, ITextComponent>() {
          public ITextComponent apply(@Nullable ITextComponent p_apply_1_) {
            ITextComponent itextcomponent = p_apply_1_.createCopy();
            itextcomponent.setStyle(itextcomponent.getStyle().createDeepCopy());
            return itextcomponent;
          }
        });
    return iterator;
  }
  
  public boolean equals(Object p_equals_1_) {
    if (this == p_equals_1_)
      return true; 
    if (!(p_equals_1_ instanceof TextComponentBase))
      return false; 
    TextComponentBase textcomponentbase = (TextComponentBase)p_equals_1_;
    return (this.siblings.equals(textcomponentbase.siblings) && getStyle().equals(textcomponentbase.getStyle()));
  }
  
  public int hashCode() {
    return 31 * this.style.hashCode() + this.siblings.hashCode();
  }
  
  public String toString() {
    return "BaseComponent{style=" + this.style + ", siblings=" + this.siblings + '}';
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\text\TextComponentBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */