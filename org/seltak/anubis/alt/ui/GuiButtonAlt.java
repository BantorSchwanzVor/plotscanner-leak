package org.seltak.anubis.alt.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtonAlt extends GuiButton {
  public GuiButtonAlt(int buttonID, int xPos, int yPos) {
    super(buttonID, xPos, yPos, 20, 20, "");
  }
  
  public void drawButton(Minecraft mc, int mouseX, int mouseY) {
    if (this.visible) {
      mc.getTextureManager().bindTexture(new ResourceLocation("minty/btn/alt.png"));
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      boolean flag = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
      int i = 106;
      if (flag)
        i += this.height; 
      drawTexturedModalRect(this.xPosition, this.yPosition, 0, i, this.width, this.height);
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\al\\ui\GuiButtonAlt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */