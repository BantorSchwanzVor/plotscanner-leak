package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;

public class ModelBiped extends ModelBase {
  public ModelRenderer bipedHead;
  
  public ModelRenderer bipedHeadwear;
  
  public ModelRenderer bipedBody;
  
  public ModelRenderer bipedRightArm;
  
  public ModelRenderer bipedLeftArm;
  
  public ModelRenderer bipedRightLeg;
  
  public ModelRenderer bipedLeftLeg;
  
  public ArmPose leftArmPose;
  
  public ArmPose rightArmPose;
  
  public boolean isSneak;
  
  public ModelBiped() {
    this(0.0F);
  }
  
  public ModelBiped(float modelSize) {
    this(modelSize, 0.0F, 64, 32);
  }
  
  public ModelBiped(float modelSize, float p_i1149_2_, int textureWidthIn, int textureHeightIn) {
    this.leftArmPose = ArmPose.EMPTY;
    this.rightArmPose = ArmPose.EMPTY;
    this.textureWidth = textureWidthIn;
    this.textureHeight = textureHeightIn;
    this.bipedHead = new ModelRenderer(this, 0, 0);
    this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize);
    this.bipedHead.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
    this.bipedHeadwear = new ModelRenderer(this, 32, 0);
    this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize + 0.5F);
    this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
    this.bipedBody = new ModelRenderer(this, 16, 16);
    this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelSize);
    this.bipedBody.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
    this.bipedRightArm = new ModelRenderer(this, 40, 16);
    this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
    this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + p_i1149_2_, 0.0F);
    this.bipedLeftArm = new ModelRenderer(this, 40, 16);
    this.bipedLeftArm.mirror = true;
    this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
    this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + p_i1149_2_, 0.0F);
    this.bipedRightLeg = new ModelRenderer(this, 0, 16);
    this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
    this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + p_i1149_2_, 0.0F);
    this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
    this.bipedLeftLeg.mirror = true;
    this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
    this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + p_i1149_2_, 0.0F);
  }
  
  public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
    GlStateManager.pushMatrix();
    if (this.isChild) {
      float f = 2.0F;
      GlStateManager.scale(0.75F, 0.75F, 0.75F);
      GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
      this.bipedHead.render(scale);
      GlStateManager.popMatrix();
      GlStateManager.pushMatrix();
      GlStateManager.scale(0.5F, 0.5F, 0.5F);
      GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
      this.bipedBody.render(scale);
      this.bipedRightArm.render(scale);
      this.bipedLeftArm.render(scale);
      this.bipedRightLeg.render(scale);
      this.bipedLeftLeg.render(scale);
      this.bipedHeadwear.render(scale);
    } else {
      if (entityIn.isSneaking())
        GlStateManager.translate(0.0F, 0.2F, 0.0F); 
      this.bipedHead.render(scale);
      this.bipedBody.render(scale);
      this.bipedRightArm.render(scale);
      this.bipedLeftArm.render(scale);
      this.bipedRightLeg.render(scale);
      this.bipedLeftLeg.render(scale);
      this.bipedHeadwear.render(scale);
    } 
    GlStateManager.popMatrix();
  }
  
  public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
    boolean flag = (entityIn instanceof EntityLivingBase && ((EntityLivingBase)entityIn).getTicksElytraFlying() > 4);
    this.bipedHead.rotateAngleY = netHeadYaw * 0.017453292F;
    if (flag) {
      this.bipedHead.rotateAngleX = -0.7853982F;
    } else {
      this.bipedHead.rotateAngleX = headPitch * 0.017453292F;
    } 
    this.bipedBody.rotateAngleY = 0.0F;
    this.bipedRightArm.rotationPointZ = 0.0F;
    this.bipedRightArm.rotationPointX = -5.0F;
    this.bipedLeftArm.rotationPointZ = 0.0F;
    this.bipedLeftArm.rotationPointX = 5.0F;
    float f = 1.0F;
    if (flag) {
      f = (float)(entityIn.motionX * entityIn.motionX + entityIn.motionY * entityIn.motionY + entityIn.motionZ * entityIn.motionZ);
      f /= 0.2F;
      f = f * f * f;
    } 
    if (f < 1.0F)
      f = 1.0F; 
    this.bipedRightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 2.0F * limbSwingAmount * 0.5F / f;
    this.bipedLeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
    this.bipedRightArm.rotateAngleZ = 0.0F;
    this.bipedLeftArm.rotateAngleZ = 0.0F;
    this.bipedRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
    this.bipedLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount / f;
    this.bipedRightLeg.rotateAngleY = 0.0F;
    this.bipedLeftLeg.rotateAngleY = 0.0F;
    this.bipedRightLeg.rotateAngleZ = 0.0F;
    this.bipedLeftLeg.rotateAngleZ = 0.0F;
    if (this.isRiding) {
      this.bipedRightArm.rotateAngleX += -0.62831855F;
      this.bipedLeftArm.rotateAngleX += -0.62831855F;
      this.bipedRightLeg.rotateAngleX = -1.4137167F;
      this.bipedRightLeg.rotateAngleY = 0.31415927F;
      this.bipedRightLeg.rotateAngleZ = 0.07853982F;
      this.bipedLeftLeg.rotateAngleX = -1.4137167F;
      this.bipedLeftLeg.rotateAngleY = -0.31415927F;
      this.bipedLeftLeg.rotateAngleZ = -0.07853982F;
    } 
    this.bipedRightArm.rotateAngleY = 0.0F;
    this.bipedRightArm.rotateAngleZ = 0.0F;
    switch (this.leftArmPose) {
      case EMPTY:
        this.bipedLeftArm.rotateAngleY = 0.0F;
        break;
      case null:
        this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - 0.9424779F;
        this.bipedLeftArm.rotateAngleY = 0.5235988F;
        break;
      case ITEM:
        this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - 0.31415927F;
        this.bipedLeftArm.rotateAngleY = 0.0F;
        break;
    } 
    switch (this.rightArmPose) {
      case EMPTY:
        this.bipedRightArm.rotateAngleY = 0.0F;
        break;
      case null:
        this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - 0.9424779F;
        this.bipedRightArm.rotateAngleY = -0.5235988F;
        break;
      case ITEM:
        this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - 0.31415927F;
        this.bipedRightArm.rotateAngleY = 0.0F;
        break;
    } 
    if (this.swingProgress > 0.0F) {
      EnumHandSide enumhandside = getMainHand(entityIn);
      ModelRenderer modelrenderer = getArmForSide(enumhandside);
      float f1 = this.swingProgress;
      this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt(f1) * 6.2831855F) * 0.2F;
      if (enumhandside == EnumHandSide.LEFT)
        this.bipedBody.rotateAngleY *= -1.0F; 
      this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
      this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
      this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
      this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
      this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
      this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
      this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
      f1 = 1.0F - this.swingProgress;
      f1 *= f1;
      f1 *= f1;
      f1 = 1.0F - f1;
      float f2 = MathHelper.sin(f1 * 3.1415927F);
      float f3 = MathHelper.sin(this.swingProgress * 3.1415927F) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
      modelrenderer.rotateAngleX = (float)(modelrenderer.rotateAngleX - f2 * 1.2D + f3);
      modelrenderer.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
      modelrenderer.rotateAngleZ += MathHelper.sin(this.swingProgress * 3.1415927F) * -0.4F;
    } 
    if (this.isSneak) {
      this.bipedBody.rotateAngleX = 0.5F;
      this.bipedRightArm.rotateAngleX += 0.4F;
      this.bipedLeftArm.rotateAngleX += 0.4F;
      this.bipedRightLeg.rotationPointZ = 4.0F;
      this.bipedLeftLeg.rotationPointZ = 4.0F;
      this.bipedRightLeg.rotationPointY = 9.0F;
      this.bipedLeftLeg.rotationPointY = 9.0F;
      this.bipedHead.rotationPointY = 1.0F;
    } else {
      this.bipedBody.rotateAngleX = 0.0F;
      this.bipedRightLeg.rotationPointZ = 0.1F;
      this.bipedLeftLeg.rotationPointZ = 0.1F;
      this.bipedRightLeg.rotationPointY = 12.0F;
      this.bipedLeftLeg.rotationPointY = 12.0F;
      this.bipedHead.rotationPointY = 0.0F;
    } 
    this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
    this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
    this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
    this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
    if (this.rightArmPose == ArmPose.BOW_AND_ARROW) {
      this.bipedRightArm.rotateAngleY = -0.1F + this.bipedHead.rotateAngleY;
      this.bipedLeftArm.rotateAngleY = 0.1F + this.bipedHead.rotateAngleY + 0.4F;
      this.bipedRightArm.rotateAngleX = -1.5707964F + this.bipedHead.rotateAngleX;
      this.bipedLeftArm.rotateAngleX = -1.5707964F + this.bipedHead.rotateAngleX;
    } else if (this.leftArmPose == ArmPose.BOW_AND_ARROW) {
      this.bipedRightArm.rotateAngleY = -0.1F + this.bipedHead.rotateAngleY - 0.4F;
      this.bipedLeftArm.rotateAngleY = 0.1F + this.bipedHead.rotateAngleY;
      this.bipedRightArm.rotateAngleX = -1.5707964F + this.bipedHead.rotateAngleX;
      this.bipedLeftArm.rotateAngleX = -1.5707964F + this.bipedHead.rotateAngleX;
    } 
    copyModelAngles(this.bipedHead, this.bipedHeadwear);
  }
  
  public void setModelAttributes(ModelBase model) {
    super.setModelAttributes(model);
    if (model instanceof ModelBiped) {
      ModelBiped modelbiped = (ModelBiped)model;
      this.leftArmPose = modelbiped.leftArmPose;
      this.rightArmPose = modelbiped.rightArmPose;
      this.isSneak = modelbiped.isSneak;
    } 
  }
  
  public void setInvisible(boolean invisible) {
    this.bipedHead.showModel = invisible;
    this.bipedHeadwear.showModel = invisible;
    this.bipedBody.showModel = invisible;
    this.bipedRightArm.showModel = invisible;
    this.bipedLeftArm.showModel = invisible;
    this.bipedRightLeg.showModel = invisible;
    this.bipedLeftLeg.showModel = invisible;
  }
  
  public void postRenderArm(float scale, EnumHandSide side) {
    getArmForSide(side).postRender(scale);
  }
  
  protected ModelRenderer getArmForSide(EnumHandSide side) {
    return (side == EnumHandSide.LEFT) ? this.bipedLeftArm : this.bipedRightArm;
  }
  
  protected EnumHandSide getMainHand(Entity entityIn) {
    if (entityIn instanceof EntityLivingBase) {
      EntityLivingBase entitylivingbase = (EntityLivingBase)entityIn;
      EnumHandSide enumhandside = entitylivingbase.getPrimaryHand();
      return (entitylivingbase.swingingHand == EnumHand.MAIN_HAND) ? enumhandside : enumhandside.opposite();
    } 
    return EnumHandSide.RIGHT;
  }
  
  public enum ArmPose {
    EMPTY, ITEM, BLOCK, BOW_AND_ARROW;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\model\ModelBiped.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */