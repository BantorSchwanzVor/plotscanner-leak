package net.minecraft.client.renderer.entity;

import com.google.common.collect.Lists;
import java.nio.FloatBuffer;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.optifine.entity.model.CustomEntityModels;
import optifine.Config;
import optifine.Reflector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shadersmod.client.Shaders;

public abstract class RenderLivingBase<T extends EntityLivingBase> extends Render<T> {
  private static final Logger LOGGER = LogManager.getLogger();
  
  private static final DynamicTexture TEXTURE_BRIGHTNESS = new DynamicTexture(16, 16);
  
  public ModelBase mainModel;
  
  protected FloatBuffer brightnessBuffer = GLAllocation.createDirectFloatBuffer(4);
  
  protected List<LayerRenderer<T>> layerRenderers = Lists.newArrayList();
  
  protected boolean renderMarker;
  
  public static float NAME_TAG_RANGE = 64.0F;
  
  public static float NAME_TAG_RANGE_SNEAK = 32.0F;
  
  public float renderLimbSwing;
  
  public float renderLimbSwingAmount;
  
  public float renderAgeInTicks;
  
  public float renderHeadYaw;
  
  public float renderHeadPitch;
  
  public float renderScaleFactor;
  
  public static final boolean animateModelLiving = Boolean.getBoolean("animate.model.living");
  
  public RenderLivingBase(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
    super(renderManagerIn);
    this.mainModel = modelBaseIn;
    this.shadowSize = shadowSizeIn;
  }
  
  public <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean addLayer(U layer) {
    return this.layerRenderers.add((LayerRenderer<T>)layer);
  }
  
  public ModelBase getMainModel() {
    return this.mainModel;
  }
  
  protected float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks) {
    float f;
    for (f = yawOffset - prevYawOffset; f < -180.0F; f += 360.0F);
    while (f >= 180.0F)
      f -= 360.0F; 
    return prevYawOffset + partialTicks * f;
  }
  
  public void transformHeldFull3DItemLayer() {}
  
  public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
    if (!Reflector.RenderLivingEvent_Pre_Constructor.exists() || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Pre_Constructor, new Object[] { entity, this, Float.valueOf(partialTicks), Double.valueOf(x), Double.valueOf(y), Double.valueOf(z) })) {
      if (animateModelLiving)
        ((EntityLivingBase)entity).limbSwingAmount = 1.0F; 
      GlStateManager.pushMatrix();
      GlStateManager.disableCull();
      this.mainModel.swingProgress = getSwingProgress(entity, partialTicks);
      this.mainModel.isRiding = entity.isRiding();
      if (Reflector.ForgeEntity_shouldRiderSit.exists())
        this.mainModel.isRiding = (entity.isRiding() && entity.getRidingEntity() != null && Reflector.callBoolean(entity.getRidingEntity(), Reflector.ForgeEntity_shouldRiderSit, new Object[0])); 
      this.mainModel.isChild = entity.isChild();
      try {
        float f = interpolateRotation(((EntityLivingBase)entity).prevRenderYawOffset, ((EntityLivingBase)entity).renderYawOffset, partialTicks);
        float f1 = interpolateRotation(((EntityLivingBase)entity).prevRotationYawHead, ((EntityLivingBase)entity).rotationYawHead, partialTicks);
        float f2 = f1 - f;
        if (this.mainModel.isRiding && entity.getRidingEntity() instanceof EntityLivingBase) {
          EntityLivingBase entitylivingbase = (EntityLivingBase)entity.getRidingEntity();
          f = interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
          f2 = f1 - f;
          float f3 = MathHelper.wrapDegrees(f2);
          if (f3 < -85.0F)
            f3 = -85.0F; 
          if (f3 >= 85.0F)
            f3 = 85.0F; 
          f = f1 - f3;
          if (f3 * f3 > 2500.0F)
            f += f3 * 0.2F; 
          f2 = f1 - f;
        } 
        float f7 = ((EntityLivingBase)entity).prevRotationPitch + (((EntityLivingBase)entity).rotationPitch - ((EntityLivingBase)entity).prevRotationPitch) * partialTicks;
        renderLivingAt(entity, x, y, z);
        float f8 = handleRotationFloat(entity, partialTicks);
        rotateCorpse(entity, f8, f, partialTicks);
        float f4 = prepareScale(entity, partialTicks);
        float f5 = 0.0F;
        float f6 = 0.0F;
        if (!entity.isRiding()) {
          f5 = ((EntityLivingBase)entity).prevLimbSwingAmount + (((EntityLivingBase)entity).limbSwingAmount - ((EntityLivingBase)entity).prevLimbSwingAmount) * partialTicks;
          f6 = ((EntityLivingBase)entity).limbSwing - ((EntityLivingBase)entity).limbSwingAmount * (1.0F - partialTicks);
          if (entity.isChild())
            f6 *= 3.0F; 
          if (f5 > 1.0F)
            f5 = 1.0F; 
        } 
        GlStateManager.enableAlpha();
        this.mainModel.setLivingAnimations((EntityLivingBase)entity, f6, f5, partialTicks);
        this.mainModel.setRotationAngles(f6, f5, f8, f2, f7, f4, (Entity)entity);
        if (CustomEntityModels.isActive()) {
          this.renderLimbSwing = f6;
          this.renderLimbSwingAmount = f5;
          this.renderAgeInTicks = f8;
          this.renderHeadYaw = f2;
          this.renderHeadPitch = f7;
          this.renderScaleFactor = f4;
        } 
        if (this.renderOutlines) {
          boolean flag1 = setScoreTeamColor(entity);
          GlStateManager.enableColorMaterial();
          GlStateManager.enableOutlineMode(getTeamColor(entity));
          if (!this.renderMarker)
            renderModel(entity, f6, f5, f8, f2, f7, f4); 
          if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).isSpectator())
            renderLayers(entity, f6, f5, partialTicks, f8, f2, f7, f4); 
          GlStateManager.disableOutlineMode();
          GlStateManager.disableColorMaterial();
          if (flag1)
            unsetScoreTeamColor(); 
        } else {
          boolean flag = setDoRenderBrightness(entity, partialTicks);
          renderModel(entity, f6, f5, f8, f2, f7, f4);
          if (flag)
            unsetBrightness(); 
          GlStateManager.depthMask(true);
          if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).isSpectator())
            renderLayers(entity, f6, f5, partialTicks, f8, f2, f7, f4); 
        } 
        GlStateManager.disableRescaleNormal();
      } catch (Exception exception1) {
        LOGGER.error("Couldn't render entity", exception1);
      } 
      GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GlStateManager.enableTexture2D();
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
      GlStateManager.enableCull();
      GlStateManager.popMatrix();
      super.doRender(entity, x, y, z, entityYaw, partialTicks);
      if (Reflector.RenderLivingEvent_Post_Constructor.exists())
        Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Post_Constructor, new Object[] { entity, this, Float.valueOf(partialTicks), Double.valueOf(x), Double.valueOf(y), Double.valueOf(z) }); 
    } 
  }
  
  public float prepareScale(T entitylivingbaseIn, float partialTicks) {
    GlStateManager.enableRescaleNormal();
    GlStateManager.scale(-1.0F, -1.0F, 1.0F);
    preRenderCallback(entitylivingbaseIn, partialTicks);
    float f = 0.0625F;
    GlStateManager.translate(0.0F, -1.501F, 0.0F);
    return 0.0625F;
  }
  
  protected boolean setScoreTeamColor(T entityLivingBaseIn) {
    GlStateManager.disableLighting();
    GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
    GlStateManager.disableTexture2D();
    GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    return true;
  }
  
  protected void unsetScoreTeamColor() {
    GlStateManager.enableLighting();
    GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
    GlStateManager.enableTexture2D();
    GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
  }
  
  protected void renderModel(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
    boolean flag = func_193115_c(entitylivingbaseIn);
    boolean flag1 = (!flag && !entitylivingbaseIn.isInvisibleToPlayer((EntityPlayer)(Minecraft.getMinecraft()).player));
    if (flag || flag1) {
      if (!bindEntityTexture(entitylivingbaseIn))
        return; 
      if (flag1)
        GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL); 
      this.mainModel.render((Entity)entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
      if (flag1)
        GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL); 
    } 
  }
  
  protected boolean func_193115_c(T p_193115_1_) {
    return !(p_193115_1_.isInvisible() && !this.renderOutlines);
  }
  
  protected boolean setDoRenderBrightness(T entityLivingBaseIn, float partialTicks) {
    return setBrightness(entityLivingBaseIn, partialTicks, true);
  }
  
  protected boolean setBrightness(T entitylivingbaseIn, float partialTicks, boolean combineTextures) {
    float f = entitylivingbaseIn.getBrightness();
    int i = getColorMultiplier(entitylivingbaseIn, f, partialTicks);
    boolean flag = ((i >> 24 & 0xFF) > 0);
    boolean flag1 = !(((EntityLivingBase)entitylivingbaseIn).hurtTime <= 0 && ((EntityLivingBase)entitylivingbaseIn).deathTime <= 0);
    if (!flag && !flag1)
      return false; 
    if (!flag && !combineTextures)
      return false; 
    GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    GlStateManager.enableTexture2D();
    GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
    GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
    GlStateManager.enableTexture2D();
    GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, OpenGlHelper.GL_INTERPOLATE);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_CONSTANT);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE2_RGB, OpenGlHelper.GL_CONSTANT);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND2_RGB, 770);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
    this.brightnessBuffer.position(0);
    if (flag1) {
      this.brightnessBuffer.put(1.0F);
      this.brightnessBuffer.put(0.0F);
      this.brightnessBuffer.put(0.0F);
      this.brightnessBuffer.put(0.3F);
      if (Config.isShaders())
        Shaders.setEntityColor(1.0F, 0.0F, 0.0F, 0.3F); 
    } else {
      float f1 = (i >> 24 & 0xFF) / 255.0F;
      float f2 = (i >> 16 & 0xFF) / 255.0F;
      float f3 = (i >> 8 & 0xFF) / 255.0F;
      float f4 = (i & 0xFF) / 255.0F;
      this.brightnessBuffer.put(f2);
      this.brightnessBuffer.put(f3);
      this.brightnessBuffer.put(f4);
      this.brightnessBuffer.put(1.0F - f1);
      if (Config.isShaders())
        Shaders.setEntityColor(f2, f3, f4, 1.0F - f1); 
    } 
    this.brightnessBuffer.flip();
    GlStateManager.glTexEnv(8960, 8705, this.brightnessBuffer);
    GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
    GlStateManager.enableTexture2D();
    GlStateManager.bindTexture(TEXTURE_BRIGHTNESS.getGlTextureId());
    GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_PREVIOUS);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.lightmapTexUnit);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
    GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    return true;
  }
  
  protected void unsetBrightness() {
    GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    GlStateManager.enableTexture2D();
    GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_ALPHA, OpenGlHelper.GL_PRIMARY_COLOR);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_ALPHA, 770);
    GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
    GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, 5890);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, 5890);
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
    GlStateManager.disableTexture2D();
    GlStateManager.bindTexture(0);
    GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, 5890);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
    GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, 5890);
    GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    if (Config.isShaders())
      Shaders.setEntityColor(0.0F, 0.0F, 0.0F, 0.0F); 
  }
  
  protected void renderLivingAt(T entityLivingBaseIn, double x, double y, double z) {
    GlStateManager.translate((float)x, (float)y, (float)z);
  }
  
  protected void rotateCorpse(T entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks) {
    GlStateManager.rotate(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);
    if (((EntityLivingBase)entityLiving).deathTime > 0) {
      float f = (((EntityLivingBase)entityLiving).deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
      f = MathHelper.sqrt(f);
      if (f > 1.0F)
        f = 1.0F; 
      GlStateManager.rotate(f * getDeathMaxRotation(entityLiving), 0.0F, 0.0F, 1.0F);
    } else {
      String s = TextFormatting.getTextWithoutFormattingCodes(entityLiving.getName());
      if (s != null && ("Dinnerbone".equals(s) || "Grumm".equals(s)) && (!(entityLiving instanceof EntityPlayer) || ((EntityPlayer)entityLiving).isWearing(EnumPlayerModelParts.CAPE))) {
        GlStateManager.translate(0.0F, ((EntityLivingBase)entityLiving).height + 0.1F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
      } 
    } 
  }
  
  protected float getSwingProgress(T livingBase, float partialTickTime) {
    return livingBase.getSwingProgress(partialTickTime);
  }
  
  protected float handleRotationFloat(T livingBase, float partialTicks) {
    return ((EntityLivingBase)livingBase).ticksExisted + partialTicks;
  }
  
  protected void renderLayers(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scaleIn) {
    for (LayerRenderer<T> layerrenderer : this.layerRenderers) {
      boolean flag = setBrightness(entitylivingbaseIn, partialTicks, layerrenderer.shouldCombineTextures());
      layerrenderer.doRenderLayer((EntityLivingBase)entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scaleIn);
      if (flag)
        unsetBrightness(); 
    } 
  }
  
  protected float getDeathMaxRotation(T entityLivingBaseIn) {
    return 90.0F;
  }
  
  protected int getColorMultiplier(T entitylivingbaseIn, float lightBrightness, float partialTickTime) {
    return 0;
  }
  
  protected void preRenderCallback(T entitylivingbaseIn, float partialTickTime) {}
  
  public void renderName(T entity, double x, double y, double z) {
    if (!Reflector.RenderLivingEvent_Specials_Pre_Constructor.exists() || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Specials_Pre_Constructor, new Object[] { entity, this, Double.valueOf(x), Double.valueOf(y), Double.valueOf(z) })) {
      if (canRenderName(entity)) {
        double d0 = entity.getDistanceSqToEntity(this.renderManager.renderViewEntity);
        float f = entity.isSneaking() ? NAME_TAG_RANGE_SNEAK : NAME_TAG_RANGE;
        if (d0 < (f * f)) {
          String s = entity.getDisplayName().getFormattedText();
          GlStateManager.alphaFunc(516, 0.1F);
          renderEntityName(entity, x, y, z, s, d0);
        } 
      } 
      if (Reflector.RenderLivingEvent_Specials_Post_Constructor.exists())
        Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Specials_Post_Constructor, new Object[] { entity, this, Double.valueOf(x), Double.valueOf(y), Double.valueOf(z) }); 
    } 
  }
  
  protected boolean canRenderName(T entity) {
    EntityPlayerSP entityplayersp = (Minecraft.getMinecraft()).player;
    boolean flag = !entity.isInvisibleToPlayer((EntityPlayer)entityplayersp);
    if (entity != entityplayersp) {
      Team team = entity.getTeam();
      Team team1 = entityplayersp.getTeam();
      if (team != null) {
        Team.EnumVisible team$enumvisible = team.getNameTagVisibility();
        switch (team$enumvisible) {
          case null:
            return flag;
          case NEVER:
            return false;
          case HIDE_FOR_OTHER_TEAMS:
            return (team1 == null) ? flag : ((team.isSameTeam(team1) && (team.getSeeFriendlyInvisiblesEnabled() || flag)));
          case HIDE_FOR_OWN_TEAM:
            return (team1 == null) ? flag : ((!team.isSameTeam(team1) && flag));
        } 
        return true;
      } 
    } 
    return (Minecraft.isGuiEnabled() && entity != this.renderManager.renderViewEntity && flag && !entity.isBeingRidden());
  }
  
  public List<LayerRenderer<T>> getLayerRenderers() {
    return this.layerRenderers;
  }
  
  static {
    int[] aint = TEXTURE_BRIGHTNESS.getTextureData();
    for (int i = 0; i < 256; i++)
      aint[i] = -1; 
    TEXTURE_BRIGHTNESS.updateDynamicTexture();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderLivingBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */