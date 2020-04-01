package de.Hero.clickgui.elements;

import de.Hero.clickgui.ClickGUI;
import de.Hero.clickgui.util.FontUtil;
import de.Hero.settings.Setting;

public class Element {
  public ClickGUI clickgui;
  
  public ModuleButton parent;
  
  public Setting set;
  
  public double offset;
  
  public double x;
  
  public double y;
  
  public double width;
  
  public double height;
  
  public String setstrg;
  
  public boolean comboextended;
  
  public void setup() {
    this.clickgui = this.parent.parent.clickgui;
  }
  
  public void update() {
    this.x = this.parent.x + this.parent.width + 2.0D;
    this.y = this.parent.y + this.offset;
    this.width = this.parent.width + 10.0D;
    this.height = 15.0D;
    String sname = this.set.getName();
    if (this.set.isCheck()) {
      this.setstrg = String.valueOf(sname.substring(0, 1).toUpperCase()) + sname.substring(1, sname.length());
      double textx = this.x + this.width - FontUtil.getStringWidth(this.setstrg);
      if (textx < this.x + 13.0D)
        this.width += this.x + 13.0D - textx + 1.0D; 
    } else if (this.set.isCombo()) {
      this.height = (this.comboextended ? (this.set.getOptions().size() * (FontUtil.getFontHeight() + 2) + 15) : 15);
      this.setstrg = String.valueOf(sname.substring(0, 1).toUpperCase()) + sname.substring(1, sname.length());
      int longest = FontUtil.getStringWidth(this.setstrg);
      for (String s : this.set.getOptions()) {
        int temp = FontUtil.getStringWidth(s);
        if (temp > longest)
          longest = temp; 
      } 
      double textx = this.x + this.width - longest;
      if (textx < this.x)
        this.width += this.x - textx + 1.0D; 
    } else if (this.set.isSlider()) {
      this.setstrg = String.valueOf(sname.substring(0, 1).toUpperCase()) + sname.substring(1, sname.length());
      double d1 = Math.round(this.set.getValDouble() * 100.0D) / 100.0D;
      double d2 = Math.round(this.set.getMax() * 100.0D) / 100.0D;
      double textx = this.x + this.width - FontUtil.getStringWidth(this.setstrg) - FontUtil.getStringWidth(d2) - 4.0D;
      if (textx < this.x)
        this.width += this.x - textx + 1.0D; 
    } 
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {}
  
  public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
    return isHovered(mouseX, mouseY);
  }
  
  public void mouseReleased(int mouseX, int mouseY, int state) {}
  
  public boolean isHovered(int mouseX, int mouseY) {
    return (mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\de\Hero\clickgui\elements\Element.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */