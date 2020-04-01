package net.minecraft.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;

public class ChestRenderer {
  public void renderChestBrightness(Block blockIn, float color) {
    GlStateManager.color(color, color, color, 1.0F);
    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
    TileEntityItemStackRenderer.instance.renderByItem(new ItemStack(blockIn));
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\ChestRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */