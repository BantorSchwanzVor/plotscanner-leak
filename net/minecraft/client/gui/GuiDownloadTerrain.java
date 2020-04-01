package net.minecraft.client.gui;

import net.minecraft.client.resources.I18n;

public class GuiDownloadTerrain extends GuiScreen {
  public void initGui() {
    this.buttonList.clear();
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawBackground(0);
    drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.downloadingTerrain", new Object[0]), this.width / 2, this.height / 2 - 50, 16777215);
    super.drawScreen(mouseX, mouseY, partialTicks);
  }
  
  public boolean doesGuiPauseGame() {
    return false;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\gui\GuiDownloadTerrain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */