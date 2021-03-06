package net.minecraft.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiChest extends GuiContainer {
  private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
  
  private final IInventory upperChestInventory;
  
  private final IInventory lowerChestInventory;
  
  private final int inventoryRows;
  
  public GuiChest(IInventory upperInv, IInventory lowerInv) {
    super((Container)new ContainerChest(upperInv, lowerInv, (EntityPlayer)(Minecraft.getMinecraft()).player));
    this.upperChestInventory = upperInv;
    this.lowerChestInventory = lowerInv;
    this.allowUserInput = false;
    int i = 222;
    int j = 114;
    this.inventoryRows = lowerInv.getSizeInventory() / 9;
    this.ySize = 114 + this.inventoryRows * 18;
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);
    func_191948_b(mouseX, mouseY);
  }
  
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    this.fontRendererObj.drawString(this.lowerChestInventory.getDisplayName().getUnformattedText(), 8, 6, 4210752);
    this.fontRendererObj.drawString(this.upperChestInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
  }
  
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;
    drawTexturedModalRect(i, j, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
    drawTexturedModalRect(i, j + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\gui\inventory\GuiChest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */