package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiHopper extends GuiContainer {
  private static final ResourceLocation HOPPER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/hopper.png");
  
  private final IInventory playerInventory;
  
  private final IInventory hopperInventory;
  
  public GuiHopper(InventoryPlayer playerInv, IInventory hopperInv) {
    super((Container)new ContainerHopper(playerInv, hopperInv, (EntityPlayer)(Minecraft.getMinecraft()).player));
    this.playerInventory = (IInventory)playerInv;
    this.hopperInventory = hopperInv;
    this.allowUserInput = false;
    this.ySize = 133;
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);
    func_191948_b(mouseX, mouseY);
  }
  
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    this.fontRendererObj.drawString(this.hopperInventory.getDisplayName().getUnformattedText(), 8, 6, 4210752);
    this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
  }
  
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(HOPPER_GUI_TEXTURE);
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;
    drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\gui\GuiHopper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */