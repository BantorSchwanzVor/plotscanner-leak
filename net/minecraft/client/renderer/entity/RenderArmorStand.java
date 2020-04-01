package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelArmorStand;
import net.minecraft.client.model.ModelArmorStandArmor;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderArmorStand extends RenderLivingBase<EntityArmorStand> {
  public static final ResourceLocation TEXTURE_ARMOR_STAND = new ResourceLocation("textures/entity/armorstand/wood.png");
  
  public RenderArmorStand(RenderManager manager) {
    super(manager, (ModelBase)new ModelArmorStand(), 0.0F);
    LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this) {
        protected void initArmor() {
          this.modelLeggings = (ModelBase)new ModelArmorStandArmor(0.5F);
          this.modelArmor = (ModelBase)new ModelArmorStandArmor(1.0F);
        }
      };
    addLayer(layerbipedarmor);
    addLayer(new LayerHeldItem(this));
    addLayer(new LayerElytra(this));
    addLayer(new LayerCustomHead((getMainModel()).bipedHead));
  }
  
  protected ResourceLocation getEntityTexture(EntityArmorStand entity) {
    return TEXTURE_ARMOR_STAND;
  }
  
  public ModelArmorStand getMainModel() {
    return (ModelArmorStand)super.getMainModel();
  }
  
  protected void rotateCorpse(EntityArmorStand entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks) {
    GlStateManager.rotate(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);
    float f = (float)(entityLiving.world.getTotalWorldTime() - entityLiving.punchCooldown) + partialTicks;
    if (f < 5.0F)
      GlStateManager.rotate(MathHelper.sin(f / 1.5F * 3.1415927F) * 3.0F, 0.0F, 1.0F, 0.0F); 
  }
  
  protected boolean canRenderName(EntityArmorStand entity) {
    return entity.getAlwaysRenderNameTag();
  }
  
  public void doRender(EntityArmorStand entity, double x, double y, double z, float entityYaw, float partialTicks) {
    if (entity.hasMarker())
      this.renderMarker = true; 
    super.doRender(entity, x, y, z, entityYaw, partialTicks);
    if (entity.hasMarker())
      this.renderMarker = false; 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderArmorStand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */