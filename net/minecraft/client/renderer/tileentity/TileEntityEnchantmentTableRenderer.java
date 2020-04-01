package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class TileEntityEnchantmentTableRenderer extends TileEntitySpecialRenderer<TileEntityEnchantmentTable> {
  private static final ResourceLocation TEXTURE_BOOK = new ResourceLocation("textures/entity/enchanting_table_book.png");
  
  private final ModelBook modelBook = new ModelBook();
  
  public void func_192841_a(TileEntityEnchantmentTable p_192841_1_, double p_192841_2_, double p_192841_4_, double p_192841_6_, float p_192841_8_, int p_192841_9_, float p_192841_10_) {
    GlStateManager.pushMatrix();
    GlStateManager.translate((float)p_192841_2_ + 0.5F, (float)p_192841_4_ + 0.75F, (float)p_192841_6_ + 0.5F);
    float f = p_192841_1_.tickCount + p_192841_8_;
    GlStateManager.translate(0.0F, 0.1F + MathHelper.sin(f * 0.1F) * 0.01F, 0.0F);
    float f1;
    for (f1 = p_192841_1_.bookRotation - p_192841_1_.bookRotationPrev; f1 >= 3.1415927F; f1 -= 6.2831855F);
    while (f1 < -3.1415927F)
      f1 += 6.2831855F; 
    float f2 = p_192841_1_.bookRotationPrev + f1 * p_192841_8_;
    GlStateManager.rotate(-f2 * 57.295776F, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotate(80.0F, 0.0F, 0.0F, 1.0F);
    bindTexture(TEXTURE_BOOK);
    float f3 = p_192841_1_.pageFlipPrev + (p_192841_1_.pageFlip - p_192841_1_.pageFlipPrev) * p_192841_8_ + 0.25F;
    float f4 = p_192841_1_.pageFlipPrev + (p_192841_1_.pageFlip - p_192841_1_.pageFlipPrev) * p_192841_8_ + 0.75F;
    f3 = (f3 - MathHelper.fastFloor(f3)) * 1.6F - 0.3F;
    f4 = (f4 - MathHelper.fastFloor(f4)) * 1.6F - 0.3F;
    if (f3 < 0.0F)
      f3 = 0.0F; 
    if (f4 < 0.0F)
      f4 = 0.0F; 
    if (f3 > 1.0F)
      f3 = 1.0F; 
    if (f4 > 1.0F)
      f4 = 1.0F; 
    float f5 = p_192841_1_.bookSpreadPrev + (p_192841_1_.bookSpread - p_192841_1_.bookSpreadPrev) * p_192841_8_;
    GlStateManager.enableCull();
    this.modelBook.render(null, f, f3, f4, f5, 0.0F, 0.0625F);
    GlStateManager.popMatrix();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\tileentity\TileEntityEnchantmentTableRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */