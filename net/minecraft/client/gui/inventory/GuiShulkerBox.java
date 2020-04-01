package net.minecraft.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerShulkerBox;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiShulkerBox extends GuiContainer {
  private static final ResourceLocation field_190778_u = new ResourceLocation("textures/gui/container/shulker_box.png");
  
  private final IInventory field_190779_v;
  
  private final InventoryPlayer field_190780_w;
  
  public GuiShulkerBox(InventoryPlayer p_i47233_1_, IInventory p_i47233_2_) {
    super((Container)new ContainerShulkerBox(p_i47233_1_, p_i47233_2_, (EntityPlayer)(Minecraft.getMinecraft()).player));
    this.field_190780_w = p_i47233_1_;
    this.field_190779_v = p_i47233_2_;
    this.ySize++;
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);
    func_191948_b(mouseX, mouseY);
  }
  
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    this.fontRendererObj.drawString(this.field_190779_v.getDisplayName().getUnformattedText(), 8, 6, 4210752);
    this.fontRendererObj.drawString(this.field_190780_w.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
  }
  
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(field_190778_u);
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;
    drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\gui\inventory\GuiShulkerBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */