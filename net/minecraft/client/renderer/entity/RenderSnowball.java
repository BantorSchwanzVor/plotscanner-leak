package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderSnowball<T extends Entity> extends Render<T> {
  protected final Item item;
  
  private final RenderItem itemRenderer;
  
  public RenderSnowball(RenderManager renderManagerIn, Item itemIn, RenderItem itemRendererIn) {
    super(renderManagerIn);
    this.item = itemIn;
    this.itemRenderer = itemRendererIn;
  }
  
  public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
    GlStateManager.pushMatrix();
    GlStateManager.translate((float)x, (float)y, (float)z);
    GlStateManager.enableRescaleNormal();
    GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotate(((this.renderManager.options.thirdPersonView == 2) ? -1 : true) * this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
    bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    if (this.renderOutlines) {
      GlStateManager.enableColorMaterial();
      GlStateManager.enableOutlineMode(getTeamColor(entity));
    } 
    this.itemRenderer.renderItem(getStackToRender(entity), ItemCameraTransforms.TransformType.GROUND);
    if (this.renderOutlines) {
      GlStateManager.disableOutlineMode();
      GlStateManager.disableColorMaterial();
    } 
    GlStateManager.disableRescaleNormal();
    GlStateManager.popMatrix();
    super.doRender(entity, x, y, z, entityYaw, partialTicks);
  }
  
  public ItemStack getStackToRender(T entityIn) {
    return new ItemStack(this.item);
  }
  
  protected ResourceLocation getEntityTexture(Entity entity) {
    return TextureMap.LOCATION_BLOCKS_TEXTURE;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderSnowball.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */