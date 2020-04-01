package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.util.ResourceLocation;

public class RenderPigZombie extends RenderBiped<EntityPigZombie> {
  private static final ResourceLocation ZOMBIE_PIGMAN_TEXTURE = new ResourceLocation("textures/entity/zombie_pigman.png");
  
  public RenderPigZombie(RenderManager renderManagerIn) {
    super(renderManagerIn, (ModelBiped)new ModelZombie(), 0.5F);
    addLayer(new LayerBipedArmor(this) {
          protected void initArmor() {
            this.modelLeggings = (ModelBase)new ModelZombie(0.5F, true);
            this.modelArmor = (ModelBase)new ModelZombie(1.0F, true);
          }
        });
  }
  
  protected ResourceLocation getEntityTexture(EntityPigZombie entity) {
    return ZOMBIE_PIGMAN_TEXTURE;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderPigZombie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */