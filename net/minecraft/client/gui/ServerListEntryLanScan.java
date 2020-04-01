package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class ServerListEntryLanScan implements GuiListExtended.IGuiListEntry {
  private final Minecraft mc = Minecraft.getMinecraft();
  
  public void func_192634_a(int p_192634_1_, int p_192634_2_, int p_192634_3_, int p_192634_4_, int p_192634_5_, int p_192634_6_, int p_192634_7_, boolean p_192634_8_, float p_192634_9_) {
    String s;
    int i = p_192634_3_ + p_192634_5_ / 2 - this.mc.fontRendererObj.FONT_HEIGHT / 2;
    this.mc.fontRendererObj.drawString(I18n.format("lanServer.scanning", new Object[0]), this.mc.currentScreen.width / 2 - this.mc.fontRendererObj.getStringWidth(I18n.format("lanServer.scanning", new Object[0])) / 2, i, 16777215);
    switch ((int)(Minecraft.getSystemTime() / 300L % 4L)) {
      default:
        s = "O o o";
        break;
      case 1:
      case 3:
        s = "o O o";
        break;
      case 2:
        s = "o o O";
        break;
    } 
    this.mc.fontRendererObj.drawString(s, this.mc.currentScreen.width / 2 - this.mc.fontRendererObj.getStringWidth(s) / 2, i + this.mc.fontRendererObj.FONT_HEIGHT, 8421504);
  }
  
  public void func_192633_a(int p_192633_1_, int p_192633_2_, int p_192633_3_, float p_192633_4_) {}
  
  public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
    return false;
  }
  
  public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {}
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\gui\ServerListEntryLanScan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */