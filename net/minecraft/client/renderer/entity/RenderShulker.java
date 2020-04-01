package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelShulker;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class RenderShulker extends RenderLiving<EntityShulker> {
  public static final ResourceLocation[] SHULKER_ENDERGOLEM_TEXTURE = new ResourceLocation[] { 
      new ResourceLocation("textures/entity/shulker/shulker_white.png"), new ResourceLocation("textures/entity/shulker/shulker_orange.png"), new ResourceLocation("textures/entity/shulker/shulker_magenta.png"), new ResourceLocation("textures/entity/shulker/shulker_light_blue.png"), new ResourceLocation("textures/entity/shulker/shulker_yellow.png"), new ResourceLocation("textures/entity/shulker/shulker_lime.png"), new ResourceLocation("textures/entity/shulker/shulker_pink.png"), new ResourceLocation("textures/entity/shulker/shulker_gray.png"), new ResourceLocation("textures/entity/shulker/shulker_silver.png"), new ResourceLocation("textures/entity/shulker/shulker_cyan.png"), 
      new ResourceLocation("textures/entity/shulker/shulker_purple.png"), new ResourceLocation("textures/entity/shulker/shulker_blue.png"), new ResourceLocation("textures/entity/shulker/shulker_brown.png"), new ResourceLocation("textures/entity/shulker/shulker_green.png"), new ResourceLocation("textures/entity/shulker/shulker_red.png"), new ResourceLocation("textures/entity/shulker/shulker_black.png") };
  
  public RenderShulker(RenderManager p_i47194_1_) {
    super(p_i47194_1_, (ModelBase)new ModelShulker(), 0.0F);
    addLayer(new HeadLayer(null));
  }
  
  public ModelShulker getMainModel() {
    return (ModelShulker)super.getMainModel();
  }
  
  public void doRender(EntityShulker entity, double x, double y, double z, float entityYaw, float partialTicks) {
    int i = entity.getClientTeleportInterp();
    if (i > 0 && entity.isAttachedToBlock()) {
      BlockPos blockpos = entity.getAttachmentPos();
      BlockPos blockpos1 = entity.getOldAttachPos();
      double d0 = (i - partialTicks) / 6.0D;
      d0 *= d0;
      double d1 = (blockpos.getX() - blockpos1.getX()) * d0;
      double d2 = (blockpos.getY() - blockpos1.getY()) * d0;
      double d3 = (blockpos.getZ() - blockpos1.getZ()) * d0;
      super.doRender(entity, x - d1, y - d2, z - d3, entityYaw, partialTicks);
    } else {
      super.doRender(entity, x, y, z, entityYaw, partialTicks);
    } 
  }
  
  public boolean shouldRender(EntityShulker livingEntity, ICamera camera, double camX, double camY, double camZ) {
    if (super.shouldRender(livingEntity, camera, camX, camY, camZ))
      return true; 
    if (livingEntity.getClientTeleportInterp() > 0 && livingEntity.isAttachedToBlock()) {
      BlockPos blockpos = livingEntity.getOldAttachPos();
      BlockPos blockpos1 = livingEntity.getAttachmentPos();
      Vec3d vec3d = new Vec3d(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
      Vec3d vec3d1 = new Vec3d(blockpos.getX(), blockpos.getY(), blockpos.getZ());
      if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(vec3d1.xCoord, vec3d1.yCoord, vec3d1.zCoord, vec3d.xCoord, vec3d.yCoord, vec3d.zCoord)))
        return true; 
    } 
    return false;
  }
  
  protected ResourceLocation getEntityTexture(EntityShulker entity) {
    return SHULKER_ENDERGOLEM_TEXTURE[entity.func_190769_dn().getMetadata()];
  }
  
  protected void rotateCorpse(EntityShulker entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks) {
    super.rotateCorpse(entityLiving, p_77043_2_, p_77043_3_, partialTicks);
    switch (entityLiving.getAttachmentFacing()) {
      default:
        return;
      case EAST:
        GlStateManager.translate(0.5F, 0.5F, 0.0F);
        GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
      case WEST:
        GlStateManager.translate(-0.5F, 0.5F, 0.0F);
        GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
      case NORTH:
        GlStateManager.translate(0.0F, 0.5F, -0.5F);
        GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
      case SOUTH:
        GlStateManager.translate(0.0F, 0.5F, 0.5F);
        GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
      case UP:
        break;
    } 
    GlStateManager.translate(0.0F, 1.0F, 0.0F);
    GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
  }
  
  protected void preRenderCallback(EntityShulker entitylivingbaseIn, float partialTickTime) {
    float f = 0.999F;
    GlStateManager.scale(0.999F, 0.999F, 0.999F);
  }
  
  class HeadLayer implements LayerRenderer<EntityShulker> {
    private HeadLayer() {}
    
    public void doRenderLayer(EntityShulker entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
      GlStateManager.pushMatrix();
      switch (entitylivingbaseIn.getAttachmentFacing()) {
        case EAST:
          GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
          GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
          GlStateManager.translate(1.0F, -1.0F, 0.0F);
          GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
          break;
        case WEST:
          GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
          GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
          GlStateManager.translate(-1.0F, -1.0F, 0.0F);
          GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
          break;
        case NORTH:
          GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
          GlStateManager.translate(0.0F, -1.0F, -1.0F);
          break;
        case SOUTH:
          GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
          GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
          GlStateManager.translate(0.0F, -1.0F, 1.0F);
          break;
        case UP:
          GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
          GlStateManager.translate(0.0F, -2.0F, 0.0F);
          break;
      } 
      ModelRenderer modelrenderer = (RenderShulker.this.getMainModel()).head;
      modelrenderer.rotateAngleY = netHeadYaw * 0.017453292F;
      modelrenderer.rotateAngleX = headPitch * 0.017453292F;
      RenderShulker.this.bindTexture(RenderShulker.SHULKER_ENDERGOLEM_TEXTURE[entitylivingbaseIn.func_190769_dn().getMetadata()]);
      modelrenderer.render(scale);
      GlStateManager.popMatrix();
    }
    
    public boolean shouldCombineTextures() {
      return false;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderShulker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */