package de.Hero.clickgui.elements.menu;

import de.Hero.clickgui.elements.Element;
import de.Hero.clickgui.elements.ModuleButton;
import de.Hero.clickgui.util.ColorUtil;
import de.Hero.clickgui.util.FontUtil;
import de.Hero.settings.Setting;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.SoundEvents;
import org.seltak.anubis.Anubis;

public class ElementComboBox extends Element {
  public ElementComboBox(ModuleButton iparent, Setting iset) {
    this.parent = iparent;
    this.set = iset;
    setup();
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    Color temp = ColorUtil.getClickGUIColor();
    int color = (new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 150)).getRGB();
    Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -15066598);
    FontUtil.drawTotalCenteredString(this.setstrg, this.x + this.width / 2.0D, this.y + 7.0D, -1);
    int clr1 = color;
    int clr2 = temp.getRGB();
    Gui.drawRect(this.x, this.y + 14.0D, this.x + this.width, this.y + 15.0D, 1996488704);
    if (this.comboextended) {
      Gui.drawRect(this.x, this.y + 15.0D, this.x + this.width, this.y + this.height, -1441656302);
      double ay = this.y + 15.0D;
      for (String sld : this.set.getOptions()) {
        String elementtitle = String.valueOf(sld.substring(0, 1).toUpperCase()) + sld.substring(1, sld.length());
        FontUtil.drawCenteredString(elementtitle, this.x + this.width / 2.0D, ay + 2.0D, -1);
        if (sld.equalsIgnoreCase(this.set.getValString()))
          Gui.drawRect(this.x, ay, this.x + 1.5D, ay + FontUtil.getFontHeight() + 2.0D, clr1); 
        if (mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= ay && mouseY < ay + FontUtil.getFontHeight() + 2.0D)
          Gui.drawRect(this.x + this.width - 1.2D, ay, this.x + this.width, ay + FontUtil.getFontHeight() + 2.0D, clr2); 
        ay += (FontUtil.getFontHeight() + 2);
      } 
    } 
  }
  
  public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
    if (mouseButton == 0) {
      if (isButtonHovered(mouseX, mouseY)) {
        this.comboextended = !this.comboextended;
        return true;
      } 
      if (!this.comboextended)
        return false; 
      double ay = this.y + 15.0D;
      for (String slcd : this.set.getOptions()) {
        if (mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= ay && mouseY <= ay + FontUtil.getFontHeight() + 2.0D) {
          if (Anubis.setmgr.getSettingByName("Sound").getValBoolean())
            (Minecraft.getMinecraft()).player.playSound(SoundEvents.BLOCK_PISTON_CONTRACT, 20.0F, 20.0F); 
          if (this.clickgui != null && this.clickgui.setmgr != null)
            this.clickgui.setmgr.getSettingByName(this.set.getName()).setValString(slcd.toLowerCase()); 
          return true;
        } 
        ay += (FontUtil.getFontHeight() + 2);
      } 
    } 
    return super.mouseClicked(mouseX, mouseY, mouseButton);
  }
  
  public boolean isButtonHovered(int mouseX, int mouseY) {
    return (mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + 15.0D);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\de\Hero\clickgui\elements\menu\ElementComboBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */