package de.Hero.clickgui.elements.menu;

import de.Hero.clickgui.elements.Element;
import de.Hero.clickgui.elements.ModuleButton;
import de.Hero.clickgui.util.ColorUtil;
import de.Hero.clickgui.util.FontUtil;
import de.Hero.settings.Setting;
import java.awt.Color;
import net.minecraft.client.gui.Gui;

public class ElementCheckBox extends Element {
  public ElementCheckBox(ModuleButton iparent, Setting iset) {
    this.parent = iparent;
    this.set = iset;
    setup();
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    Color temp = ColorUtil.getClickGUIColor();
    int color = (new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 200)).getRGB();
    Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -15066598);
    FontUtil.drawString(this.setstrg, this.x + this.width - FontUtil.getStringWidth(this.setstrg), this.y + (FontUtil.getFontHeight() / 2) - 0.5D, -1);
    Gui.drawRect(this.x + 1.0D, this.y + 2.0D, this.x + 12.0D, this.y + 13.0D, this.set.getValBoolean() ? color : -16777216);
    if (isCheckHovered(mouseX, mouseY))
      Gui.drawRect(this.x + 1.0D, this.y + 2.0D, this.x + 12.0D, this.y + 13.0D, 1427181841); 
  }
  
  public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
    if (mouseButton == 0 && isCheckHovered(mouseX, mouseY)) {
      this.set.setValBoolean(!this.set.getValBoolean());
      return true;
    } 
    return super.mouseClicked(mouseX, mouseY, mouseButton);
  }
  
  public boolean isCheckHovered(int mouseX, int mouseY) {
    return (mouseX >= this.x + 1.0D && mouseX <= this.x + 12.0D && mouseY >= this.y + 2.0D && mouseY <= this.y + 13.0D);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\de\Hero\clickgui\elements\menu\ElementCheckBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */