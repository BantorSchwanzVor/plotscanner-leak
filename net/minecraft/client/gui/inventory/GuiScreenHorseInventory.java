package net.minecraft.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiScreenHorseInventory extends GuiContainer {
  private static final ResourceLocation HORSE_GUI_TEXTURES = new ResourceLocation("textures/gui/container/horse.png");
  
  private final IInventory playerInventory;
  
  private final IInventory horseInventory;
  
  private final AbstractHorse horseEntity;
  
  private float mousePosx;
  
  private float mousePosY;
  
  public GuiScreenHorseInventory(IInventory playerInv, IInventory horseInv, AbstractHorse horse) {
    super((Container)new ContainerHorseInventory(playerInv, horseInv, horse, (EntityPlayer)(Minecraft.getMinecraft()).player));
    this.playerInventory = playerInv;
    this.horseInventory = horseInv;
    this.horseEntity = horse;
    this.allowUserInput = false;
  }
  
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    this.fontRendererObj.drawString(this.horseInventory.getDisplayName().getUnformattedText(), 8, 6, 4210752);
    this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
  }
  
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(HORSE_GUI_TEXTURES);
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;
    drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    if (this.horseEntity instanceof AbstractChestHorse) {
      AbstractChestHorse abstractchesthorse = (AbstractChestHorse)this.horseEntity;
      if (abstractchesthorse.func_190695_dh())
        drawTexturedModalRect(i + 79, j + 17, 0, this.ySize, abstractchesthorse.func_190696_dl() * 18, 54); 
    } 
    if (this.horseEntity.func_190685_dA())
      drawTexturedModalRect(i + 7, j + 35 - 18, 18, this.ySize + 54, 18, 18); 
    if (this.horseEntity.func_190677_dK())
      if (this.horseEntity instanceof net.minecraft.entity.passive.EntityLlama) {
        drawTexturedModalRect(i + 7, j + 35, 36, this.ySize + 54, 18, 18);
      } else {
        drawTexturedModalRect(i + 7, j + 35, 0, this.ySize + 54, 18, 18);
      }  
    GuiInventory.drawEntityOnScreen(i + 51, j + 60, 17, (i + 51) - this.mousePosx, (j + 75 - 50) - this.mousePosY, (EntityLivingBase)this.horseEntity);
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();
    this.mousePosx = mouseX;
    this.mousePosY = mouseY;
    super.drawScreen(mouseX, mouseY, partialTicks);
    func_191948_b(mouseX, mouseY);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\gui\inventory\GuiScreenHorseInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */