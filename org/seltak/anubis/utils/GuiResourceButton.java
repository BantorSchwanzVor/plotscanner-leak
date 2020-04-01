package org.seltak.anubis.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class GuiResourceButton extends GuiButton {
  private ResourceLocation resLoc;
  
  private ResourceLocation resLoc2;
  
  private int textColor;
  
  private int hoversize;
  
  public void drawButton2(Minecraft mc, int mouseX, int mouseY) {
    boolean isOverButton = (mouseX >= this.xPosition && mouseX <= this.xPosition + this.width && mouseY >= this.yPosition && mouseY <= this.yPosition + this.height);
    if (this.visible) {
      if (isOverButton && this.hoversize <= 1) {
        this.hoversize++;
      } else if (!isOverButton && this.hoversize >= 0) {
        this.hoversize--;
      } 
      if (isOverButton)
        mc.fontRendererObj.drawStringWithShadow(this.displayString, (this.xPosition + this.width / 2 - mc.fontRendererObj.getStringWidth(this.displayString) / 2 + 1), (this.yPosition - this.height + 15), 16777215); 
      mc.getTextureManager().bindTexture(this.resLoc);
      Gui.drawScaledCustomSizeModalRect(this.xPosition, this.yPosition - this.hoversize, 0.0F, 0.0F, 30, this.height, this.width, this.height, this.width, this.height);
      if (!this.enabled)
        Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 1610612736); 
    } 
  }
  
  public GuiResourceButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, ResourceLocation resLoc) {
    super(buttonId, x, y, widthIn, heightIn, buttonText);
    this.resLoc = resLoc;
    this.textColor = -1;
  }
  
  public GuiResourceButton(int buttonId, int x, int y, String buttonText, ResourceLocation resLoc) {
    super(buttonId, x, y, buttonText);
    this.resLoc = resLoc;
    this.textColor = -1;
  }
  
  public GuiResourceButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, ResourceLocation resLoc, int color) {
    super(buttonId, x, y, widthIn, heightIn, buttonText);
    this.resLoc = resLoc;
    this.textColor = color;
  }
  
  public GuiResourceButton(int buttonId, int x, int y, String buttonText, ResourceLocation resLoc, int color) {
    super(buttonId, x, y, buttonText);
    this.resLoc = resLoc;
    this.textColor = color;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubi\\utils\GuiResourceButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */