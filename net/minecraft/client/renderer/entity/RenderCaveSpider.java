package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.ResourceLocation;

public class RenderCaveSpider extends RenderSpider<EntityCaveSpider> {
  private static final ResourceLocation CAVE_SPIDER_TEXTURES = new ResourceLocation("textures/entity/spider/cave_spider.png");
  
  public RenderCaveSpider(RenderManager renderManagerIn) {
    super(renderManagerIn);
    this.shadowSize *= 0.7F;
  }
  
  protected void preRenderCallback(EntityCaveSpider entitylivingbaseIn, float partialTickTime) {
    GlStateManager.scale(0.7F, 0.7F, 0.7F);
  }
  
  protected ResourceLocation getEntityTexture(EntityCaveSpider entity) {
    return CAVE_SPIDER_TEXTURES;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderCaveSpider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */