package de.Hero.clickgui.elements;

import de.Hero.clickgui.Panel;
import de.Hero.clickgui.elements.menu.ElementCheckBox;
import de.Hero.clickgui.elements.menu.ElementComboBox;
import de.Hero.clickgui.elements.menu.ElementSlider;
import de.Hero.clickgui.util.ColorUtil;
import de.Hero.clickgui.util.FontUtil;
import de.Hero.settings.Setting;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.SoundEvents;
import org.seltak.anubis.Anubis;
import org.seltak.anubis.module.Module;

public class ModuleButton {
  public Module mod;
  
  public ArrayList<Element> menuelements;
  
  public Panel parent;
  
  public double x;
  
  public double y;
  
  public double width;
  
  public double height;
  
  public boolean extended = false;
  
  public boolean listening = false;
  
  public ModuleButton(Module imod, Panel pl) {
    this.mod = imod;
    this.height = ((Minecraft.getMinecraft()).fontRendererObj.FONT_HEIGHT + 2);
    this.parent = pl;
    this.menuelements = new ArrayList<>();
    if (Anubis.setmgr.getSettingsByMod(imod) != null)
      for (Setting s : Anubis.setmgr.getSettingsByMod(imod)) {
        if (s.isCheck()) {
          this.menuelements.add(new ElementCheckBox(this, s));
          continue;
        } 
        if (s.isSlider()) {
          this.menuelements.add(new ElementSlider(this, s));
          continue;
        } 
        if (s.isCombo())
          this.menuelements.add(new ElementComboBox(this, s)); 
      }  
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    Color temp = ColorUtil.getClickGUIColor();
    int color = (new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 150)).getRGB();
    int textcolor = -5263441;
    if (this.mod.isEnabled()) {
      Gui.drawRect(this.x - 2.0D, this.y, this.x + this.width + 2.0D, this.y + this.height + 1.0D, color);
      textcolor = -1052689;
    } 
    if (isHovered(mouseX, mouseY))
      Gui.drawRect(this.x - 2.0D, this.y, this.x + this.width + 2.0D, this.y + this.height + 1.0D, 1427181841); 
    FontUtil.drawTotalCenteredStringWithShadow(this.mod.getName(), this.x + this.width / 2.0D, this.y + 1.0D + this.height / 2.0D, textcolor);
  }
  
  public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
    if (!isHovered(mouseX, mouseY))
      return false; 
    if (mouseButton == 0) {
      this.mod.toggle();
      if (Anubis.setmgr.getSettingByName("Sound").getValBoolean())
        (Minecraft.getMinecraft()).player.playSound(SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, 0.5F, 0.5F); 
    } else if (mouseButton == 1) {
      if (this.menuelements != null && this.menuelements.size() > 0) {
        boolean b = !this.extended;
        Anubis.clickgui.closeAllSettings();
        this.extended = b;
        if (Anubis.setmgr.getSettingByName("Sound").getValBoolean())
          if (this.extended) {
            (Minecraft.getMinecraft()).player.playSound(SoundEvents.BLOCK_PISTON_EXTEND, 1.0F, 1.0F);
          } else {
            (Minecraft.getMinecraft()).player.playSound(SoundEvents.BLOCK_PISTON_CONTRACT, 1.0F, 1.0F);
          }  
      } 
    } else if (mouseButton == 2) {
      this.listening = true;
    } 
    return true;
  }
  
  public boolean keyTyped(char typedChar, int keyCode) throws IOException {
    if (this.listening) {
      if (keyCode != 1) {
        this.mod.setKeyCode(keyCode);
      } else {
        this.mod.setKeyCode(0);
      } 
      this.listening = false;
      return true;
    } 
    return false;
  }
  
  public boolean isHovered(int mouseX, int mouseY) {
    return (mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\de\Hero\clickgui\elements\ModuleButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */