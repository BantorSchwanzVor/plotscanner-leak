package de.Hero.clickgui.elements.menu;

import de.Hero.clickgui.elements.Element;
import de.Hero.clickgui.elements.ModuleButton;
import de.Hero.clickgui.util.ColorUtil;
import de.Hero.clickgui.util.FontUtil;
import de.Hero.settings.Setting;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.math.MathHelper;

public class ElementSlider extends Element {
  public boolean dragging;
  
  public ElementSlider(ModuleButton iparent, Setting iset) {
    this.parent = iparent;
    this.set = iset;
    this.dragging = false;
    setup();
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    double d1 = Math.round(this.set.getValDouble() * 100.0D) / 100.0D;
    boolean hoveredORdragged = !(!isSliderHovered(mouseX, mouseY) && !this.dragging);
    Color temp = ColorUtil.getClickGUIColor();
    int color = (new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), hoveredORdragged ? 250 : 200)).getRGB();
    int color2 = (new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), hoveredORdragged ? 255 : 230)).getRGB();
    double percentBar = (this.set.getValDouble() - this.set.getMin()) / (this.set.getMax() - this.set.getMin());
    Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -15066598);
    FontUtil.drawString(this.setstrg, this.x + 1.0D, this.y + 2.0D, -1);
    FontUtil.drawString(d1, this.x + this.width - FontUtil.getStringWidth(d1), this.y + 2.0D, -1);
    Gui.drawRect(this.x, this.y + 12.0D, this.x + this.width, this.y + 13.5D, -15724528);
    Gui.drawRect(this.x, this.y + 12.0D, this.x + percentBar * this.width, this.y + 13.5D, color);
    if (percentBar > 0.0D && percentBar < 1.0D)
      Gui.drawRect(this.x + percentBar * this.width - 1.0D, this.y + 12.0D, this.x + Math.min(percentBar * this.width, this.width), this.y + 13.5D, color2); 
    if (this.dragging) {
      double diff = this.set.getMax() - this.set.getMin();
      double val = this.set.getMin() + MathHelper.clamp((mouseX - this.x) / this.width, 0.0D, 1.0D) * diff;
      this.set.setValDouble(val);
    } 
  }
  
  public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
    if (mouseButton == 0 && isSliderHovered(mouseX, mouseY)) {
      this.dragging = true;
      return true;
    } 
    return super.mouseClicked(mouseX, mouseY, mouseButton);
  }
  
  public void mouseReleased(int mouseX, int mouseY, int state) {
    this.dragging = false;
  }
  
  public boolean isSliderHovered(int mouseX, int mouseY) {
    return (mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y + 11.0D && mouseY <= this.y + 14.0D);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\de\Hero\clickgui\elements\menu\ElementSlider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */