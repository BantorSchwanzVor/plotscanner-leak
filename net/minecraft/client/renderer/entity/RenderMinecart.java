package net.minecraft.client.renderer.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RenderMinecart<T extends EntityMinecart> extends Render<T> {
  private static final ResourceLocation MINECART_TEXTURES = new ResourceLocation("textures/entity/minecart.png");
  
  protected ModelBase modelMinecart = (ModelBase)new ModelMinecart();
  
  public RenderMinecart(RenderManager renderManagerIn) {
    super(renderManagerIn);
    this.shadowSize = 0.5F;
  }
  
  public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
    GlStateManager.pushMatrix();
    bindEntityTexture(entity);
    long i = entity.getEntityId() * 493286711L;
    i = i * i * 4392167121L + i * 98761L;
    float f = (((float)(i >> 16L & 0x7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
    float f1 = (((float)(i >> 20L & 0x7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
    float f2 = (((float)(i >> 24L & 0x7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
    GlStateManager.translate(f, f1, f2);
    double d0 = ((EntityMinecart)entity).lastTickPosX + (((EntityMinecart)entity).posX - ((EntityMinecart)entity).lastTickPosX) * partialTicks;
    double d1 = ((EntityMinecart)entity).lastTickPosY + (((EntityMinecart)entity).posY - ((EntityMinecart)entity).lastTickPosY) * partialTicks;
    double d2 = ((EntityMinecart)entity).lastTickPosZ + (((EntityMinecart)entity).posZ - ((EntityMinecart)entity).lastTickPosZ) * partialTicks;
    double d3 = 0.30000001192092896D;
    Vec3d vec3d = entity.getPos(d0, d1, d2);
    float f3 = ((EntityMinecart)entity).prevRotationPitch + (((EntityMinecart)entity).rotationPitch - ((EntityMinecart)entity).prevRotationPitch) * partialTicks;
    if (vec3d != null) {
      Vec3d vec3d1 = entity.getPosOffset(d0, d1, d2, 0.30000001192092896D);
      Vec3d vec3d2 = entity.getPosOffset(d0, d1, d2, -0.30000001192092896D);
      if (vec3d1 == null)
        vec3d1 = vec3d; 
      if (vec3d2 == null)
        vec3d2 = vec3d; 
      x += vec3d.xCoord - d0;
      y += (vec3d1.yCoord + vec3d2.yCoord) / 2.0D - d1;
      z += vec3d.zCoord - d2;
      Vec3d vec3d3 = vec3d2.addVector(-vec3d1.xCoord, -vec3d1.yCoord, -vec3d1.zCoord);
      if (vec3d3.lengthVector() != 0.0D) {
        vec3d3 = vec3d3.normalize();
        entityYaw = (float)(Math.atan2(vec3d3.zCoord, vec3d3.xCoord) * 180.0D / Math.PI);
        f3 = (float)(Math.atan(vec3d3.yCoord) * 73.0D);
      } 
    } 
    GlStateManager.translate((float)x, (float)y + 0.375F, (float)z);
    GlStateManager.rotate(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotate(-f3, 0.0F, 0.0F, 1.0F);
    float f5 = entity.getRollingAmplitude() - partialTicks;
    float f6 = entity.getDamage() - partialTicks;
    if (f6 < 0.0F)
      f6 = 0.0F; 
    if (f5 > 0.0F)
      GlStateManager.rotate(MathHelper.sin(f5) * f5 * f6 / 10.0F * entity.getRollingDirection(), 1.0F, 0.0F, 0.0F); 
    int j = entity.getDisplayTileOffset();
    if (this.renderOutlines) {
      GlStateManager.enableColorMaterial();
      GlStateManager.enableOutlineMode(getTeamColor(entity));
    } 
    IBlockState iblockstate = entity.getDisplayTile();
    if (iblockstate.getRenderType() != EnumBlockRenderType.INVISIBLE) {
      GlStateManager.pushMatrix();
      bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
      float f4 = 0.75F;
      GlStateManager.scale(0.75F, 0.75F, 0.75F);
      GlStateManager.translate(-0.5F, (j - 8) / 16.0F, 0.5F);
      renderCartContents(entity, partialTicks, iblockstate);
      GlStateManager.popMatrix();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      bindEntityTexture(entity);
    } 
    GlStateManager.scale(-1.0F, -1.0F, 1.0F);
    this.modelMinecart.render((Entity)entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
    GlStateManager.popMatrix();
    if (this.renderOutlines) {
      GlStateManager.disableOutlineMode();
      GlStateManager.disableColorMaterial();
    } 
    super.doRender(entity, x, y, z, entityYaw, partialTicks);
  }
  
  protected ResourceLocation getEntityTexture(T entity) {
    return MINECART_TEXTURES;
  }
  
  protected void renderCartContents(T p_188319_1_, float partialTicks, IBlockState p_188319_3_) {
    GlStateManager.pushMatrix();
    Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(p_188319_3_, p_188319_1_.getBrightness());
    GlStateManager.popMatrix();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderMinecart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */