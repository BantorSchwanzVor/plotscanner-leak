package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;

public class RenderZombie extends RenderBiped<EntityZombie> {
  private static final ResourceLocation ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/zombie.png");
  
  public RenderZombie(RenderManager renderManagerIn) {
    super(renderManagerIn, (ModelBiped)new ModelZombie(), 0.5F);
    LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this) {
        protected void initArmor() {
          this.modelLeggings = (ModelBase)new ModelZombie(0.5F, true);
          this.modelArmor = (ModelBase)new ModelZombie(1.0F, true);
        }
      };
    addLayer(layerbipedarmor);
  }
  
  protected ResourceLocation getEntityTexture(EntityZombie entity) {
    return ZOMBIE_TEXTURES;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderZombie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */