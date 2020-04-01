package de.Hero.clickgui;

import de.Hero.clickgui.elements.ModuleButton;
import de.Hero.clickgui.util.ColorUtil;
import de.Hero.clickgui.util.FontUtil;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.gui.Gui;
import org.seltak.anubis.Anubis;

public class Panel {
  public String title;
  
  public double x;
  
  public double y;
  
  private double x2;
  
  private double y2;
  
  public double width;
  
  public double height;
  
  public boolean dragging;
  
  public boolean extended = true;
  
  public boolean visible;
  
  public ArrayList<ModuleButton> Elements = new ArrayList<>();
  
  public ClickGUI clickgui;
  
  public Panel(String ititle, double ix, double iy, double iwidth, double iheight, boolean iextended, ClickGUI parent) {
    this.title = ititle;
    this.x = ix;
    this.y = iy;
    this.width = iwidth;
    this.height = iheight;
    this.extended = iextended;
    this.dragging = false;
    this.visible = true;
    this.clickgui = parent;
    setup();
  }
  
  public void setup() {}
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    if (!this.visible)
      return; 
    if (this.dragging) {
      this.x = this.x2 + mouseX;
      this.y = this.y2 + mouseY;
    } 
    Color temp = ColorUtil.getClickGUIColor().darker();
    int outlineColor = (new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 170)).getRGB();
    Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -15592942);
    if (Anubis.setmgr.getSettingByName("Design").getValString().equalsIgnoreCase("New")) {
      Gui.drawRect(this.x - 2.0D, this.y, this.x, this.y + this.height, outlineColor);
      FontUtil.drawStringWithShadow(this.title, this.x + 2.0D, this.y + this.height / 2.0D - (FontUtil.getFontHeight() / 2), -1052689);
    } else if (Anubis.setmgr.getSettingByName("Design").getValString().equalsIgnoreCase("JellyLike")) {
      Gui.drawRect(this.x + 4.0D, this.y + 2.0D, this.x + 4.3D, this.y + this.height - 2.0D, -5592406);
      Gui.drawRect(this.x - 4.0D + this.width, this.y + 2.0D, this.x - 4.3D + this.width, this.y + this.height - 2.0D, -5592406);
      FontUtil.drawTotalCenteredStringWithShadow(this.title, this.x + this.width / 2.0D, this.y + this.height / 2.0D, -1052689);
    } 
    if (this.extended && !this.Elements.isEmpty()) {
      double startY = this.y + this.height;
      int epanelcolor = Anubis.setmgr.getSettingByName("Design").getValString().equalsIgnoreCase("New") ? -14474461 : (Anubis.setmgr.getSettingByName("Design").getValString().equalsIgnoreCase("JellyLike") ? -1156246251 : 0);
      for (ModuleButton et : this.Elements) {
        if (Anubis.setmgr.getSettingByName("Design").getValString().equalsIgnoreCase("New"))
          Gui.drawRect(this.x - 2.0D, startY, this.x + this.width, startY + et.height + 1.0D, outlineColor); 
        Gui.drawRect(this.x, startY, this.x + this.width, startY + et.height + 1.0D, epanelcolor);
        et.x = this.x + 2.0D;
        et.y = startY;
        et.width = this.width - 4.0D;
        et.drawScreen(mouseX, mouseY, partialTicks);
        startY += et.height + 1.0D;
      } 
      Gui.drawRect(this.x, startY + 1.0D, this.x + this.width, startY + 1.0D, epanelcolor);
    } 
  }
  
  public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
    if (!this.visible)
      return false; 
    if (mouseButton == 0 && isHovered(mouseX, mouseY)) {
      this.x2 = this.x - mouseX;
      this.y2 = this.y - mouseY;
      this.dragging = true;
      return true;
    } 
    if (mouseButton == 1 && isHovered(mouseX, mouseY)) {
      this.extended = !this.extended;
      return true;
    } 
    if (this.extended)
      for (ModuleButton et : this.Elements) {
        if (et.mouseClicked(mouseX, mouseY, mouseButton))
          return true; 
      }  
    return false;
  }
  
  public void mouseReleased(int mouseX, int mouseY, int state) {
    if (!this.visible)
      return; 
    if (state == 0)
      this.dragging = false; 
  }
  
  public boolean isHovered(int mouseX, int mouseY) {
    return (mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\de\Hero\clickgui\Panel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */