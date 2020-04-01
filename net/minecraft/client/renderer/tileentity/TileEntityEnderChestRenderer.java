package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.ResourceLocation;

public class TileEntityEnderChestRenderer extends TileEntitySpecialRenderer<TileEntityEnderChest> {
  private static final ResourceLocation ENDER_CHEST_TEXTURE = new ResourceLocation("textures/entity/chest/ender.png");
  
  private final ModelChest modelChest = new ModelChest();
  
  public void func_192841_a(TileEntityEnderChest p_192841_1_, double p_192841_2_, double p_192841_4_, double p_192841_6_, float p_192841_8_, int p_192841_9_, float p_192841_10_) {
    int i = 0;
    if (p_192841_1_.hasWorldObj())
      i = p_192841_1_.getBlockMetadata(); 
    if (p_192841_9_ >= 0) {
      bindTexture(DESTROY_STAGES[p_192841_9_]);
      GlStateManager.matrixMode(5890);
      GlStateManager.pushMatrix();
      GlStateManager.scale(4.0F, 4.0F, 1.0F);
      GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
      GlStateManager.matrixMode(5888);
    } else {
      bindTexture(ENDER_CHEST_TEXTURE);
    } 
    GlStateManager.pushMatrix();
    GlStateManager.enableRescaleNormal();
    GlStateManager.color(1.0F, 1.0F, 1.0F, p_192841_10_);
    GlStateManager.translate((float)p_192841_2_, (float)p_192841_4_ + 1.0F, (float)p_192841_6_ + 1.0F);
    GlStateManager.scale(1.0F, -1.0F, -1.0F);
    GlStateManager.translate(0.5F, 0.5F, 0.5F);
    int j = 0;
    if (i == 2)
      j = 180; 
    if (i == 3)
      j = 0; 
    if (i == 4)
      j = 90; 
    if (i == 5)
      j = -90; 
    GlStateManager.rotate(j, 0.0F, 1.0F, 0.0F);
    GlStateManager.translate(-0.5F, -0.5F, -0.5F);
    float f = p_192841_1_.prevLidAngle + (p_192841_1_.lidAngle - p_192841_1_.prevLidAngle) * p_192841_8_;
    f = 1.0F - f;
    f = 1.0F - f * f * f;
    this.modelChest.chestLid.rotateAngleX = -(f * 1.5707964F);
    this.modelChest.renderAll();
    GlStateManager.disableRescaleNormal();
    GlStateManager.popMatrix();
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    if (p_192841_9_ >= 0) {
      GlStateManager.matrixMode(5890);
      GlStateManager.popMatrix();
      GlStateManager.matrixMode(5888);
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\tileentity\TileEntityEnderChestRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */