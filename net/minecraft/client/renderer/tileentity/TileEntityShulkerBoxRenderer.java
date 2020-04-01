package net.minecraft.client.renderer.tileentity;

import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelShulker;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderShulker;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.EnumFacing;

public class TileEntityShulkerBoxRenderer extends TileEntitySpecialRenderer<TileEntityShulkerBox> {
  private final ModelShulker field_191285_a;
  
  public TileEntityShulkerBoxRenderer(ModelShulker p_i47216_1_) {
    this.field_191285_a = p_i47216_1_;
  }
  
  public void func_192841_a(TileEntityShulkerBox p_192841_1_, double p_192841_2_, double p_192841_4_, double p_192841_6_, float p_192841_8_, int p_192841_9_, float p_192841_10_) {
    EnumFacing enumfacing = EnumFacing.UP;
    if (p_192841_1_.hasWorldObj()) {
      IBlockState iblockstate = getWorld().getBlockState(p_192841_1_.getPos());
      if (iblockstate.getBlock() instanceof BlockShulkerBox)
        enumfacing = (EnumFacing)iblockstate.getValue((IProperty)BlockShulkerBox.field_190957_a); 
    } 
    GlStateManager.enableDepth();
    GlStateManager.depthFunc(515);
    GlStateManager.depthMask(true);
    GlStateManager.disableCull();
    if (p_192841_9_ >= 0) {
      bindTexture(DESTROY_STAGES[p_192841_9_]);
      GlStateManager.matrixMode(5890);
      GlStateManager.pushMatrix();
      GlStateManager.scale(4.0F, 4.0F, 1.0F);
      GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
      GlStateManager.matrixMode(5888);
    } else {
      bindTexture(RenderShulker.SHULKER_ENDERGOLEM_TEXTURE[p_192841_1_.func_190592_s().getMetadata()]);
    } 
    GlStateManager.pushMatrix();
    GlStateManager.enableRescaleNormal();
    if (p_192841_9_ < 0)
      GlStateManager.color(1.0F, 1.0F, 1.0F, p_192841_10_); 
    GlStateManager.translate((float)p_192841_2_ + 0.5F, (float)p_192841_4_ + 1.5F, (float)p_192841_6_ + 0.5F);
    GlStateManager.scale(1.0F, -1.0F, -1.0F);
    GlStateManager.translate(0.0F, 1.0F, 0.0F);
    float f = 0.9995F;
    GlStateManager.scale(0.9995F, 0.9995F, 0.9995F);
    GlStateManager.translate(0.0F, -1.0F, 0.0F);
    switch (enumfacing) {
      case null:
        GlStateManager.translate(0.0F, 2.0F, 0.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        break;
      case NORTH:
        GlStateManager.translate(0.0F, 1.0F, 1.0F);
        GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        break;
      case SOUTH:
        GlStateManager.translate(0.0F, 1.0F, -1.0F);
        GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
        break;
      case WEST:
        GlStateManager.translate(-1.0F, 1.0F, 0.0F);
        GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
        break;
      case EAST:
        GlStateManager.translate(1.0F, 1.0F, 0.0F);
        GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
        break;
    } 
    this.field_191285_a.base.render(0.0625F);
    GlStateManager.translate(0.0F, -p_192841_1_.func_190585_a(p_192841_8_) * 0.5F, 0.0F);
    GlStateManager.rotate(270.0F * p_192841_1_.func_190585_a(p_192841_8_), 0.0F, 1.0F, 0.0F);
    this.field_191285_a.lid.render(0.0625F);
    GlStateManager.enableCull();
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


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\tileentity\TileEntityShulkerBoxRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */