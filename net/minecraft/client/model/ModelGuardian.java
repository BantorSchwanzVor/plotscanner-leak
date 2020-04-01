package net.minecraft.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ModelGuardian extends ModelBase {
  private final ModelRenderer guardianBody;
  
  private final ModelRenderer guardianEye;
  
  private final ModelRenderer[] guardianSpines;
  
  private final ModelRenderer[] guardianTail;
  
  public ModelGuardian() {
    this.textureWidth = 64;
    this.textureHeight = 64;
    this.guardianSpines = new ModelRenderer[12];
    this.guardianBody = new ModelRenderer(this);
    this.guardianBody.setTextureOffset(0, 0).addBox(-6.0F, 10.0F, -8.0F, 12, 12, 16);
    this.guardianBody.setTextureOffset(0, 28).addBox(-8.0F, 10.0F, -6.0F, 2, 12, 12);
    this.guardianBody.setTextureOffset(0, 28).addBox(6.0F, 10.0F, -6.0F, 2, 12, 12, true);
    this.guardianBody.setTextureOffset(16, 40).addBox(-6.0F, 8.0F, -6.0F, 12, 2, 12);
    this.guardianBody.setTextureOffset(16, 40).addBox(-6.0F, 22.0F, -6.0F, 12, 2, 12);
    for (int i = 0; i < this.guardianSpines.length; i++) {
      this.guardianSpines[i] = new ModelRenderer(this, 0, 0);
      this.guardianSpines[i].addBox(-1.0F, -4.5F, -1.0F, 2, 9, 2);
      this.guardianBody.addChild(this.guardianSpines[i]);
    } 
    this.guardianEye = new ModelRenderer(this, 8, 0);
    this.guardianEye.addBox(-1.0F, 15.0F, 0.0F, 2, 2, 1);
    this.guardianBody.addChild(this.guardianEye);
    this.guardianTail = new ModelRenderer[3];
    this.guardianTail[0] = new ModelRenderer(this, 40, 0);
    this.guardianTail[0].addBox(-2.0F, 14.0F, 7.0F, 4, 4, 8);
    this.guardianTail[1] = new ModelRenderer(this, 0, 54);
    this.guardianTail[1].addBox(0.0F, 14.0F, 0.0F, 3, 3, 7);
    this.guardianTail[2] = new ModelRenderer(this);
    this.guardianTail[2].setTextureOffset(41, 32).addBox(0.0F, 14.0F, 0.0F, 2, 2, 6);
    this.guardianTail[2].setTextureOffset(25, 19).addBox(1.0F, 10.5F, 3.0F, 1, 9, 9);
    this.guardianBody.addChild(this.guardianTail[0]);
    this.guardianTail[0].addChild(this.guardianTail[1]);
    this.guardianTail[1].addChild(this.guardianTail[2]);
  }
  
  public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
    this.guardianBody.render(scale);
  }
  
  public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
    EntityLivingBase entityLivingBase;
    EntityGuardian entityguardian = (EntityGuardian)entityIn;
    float f = ageInTicks - entityguardian.ticksExisted;
    this.guardianBody.rotateAngleY = netHeadYaw * 0.017453292F;
    this.guardianBody.rotateAngleX = headPitch * 0.017453292F;
    float[] afloat = { 
        1.75F, 0.25F, 0.0F, 0.0F, 0.5F, 0.5F, 0.5F, 0.5F, 1.25F, 0.75F, 
        0.0F, 0.0F };
    float[] afloat1 = { 
        0.0F, 0.0F, 0.0F, 0.0F, 0.25F, 1.75F, 1.25F, 0.75F, 0.0F, 0.0F, 
        0.0F, 0.0F };
    float[] afloat2 = { 
        0.0F, 0.0F, 0.25F, 1.75F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 
        0.75F, 1.25F };
    float[] afloat3 = { 
        0.0F, 0.0F, 8.0F, -8.0F, -8.0F, 8.0F, 8.0F, -8.0F, 0.0F, 0.0F, 
        8.0F, -8.0F };
    float[] afloat4 = { 
        -8.0F, -8.0F, -8.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.0F, 8.0F, 8.0F, 
        8.0F, 8.0F };
    float[] afloat5 = { 
        8.0F, -8.0F, 0.0F, 0.0F, -8.0F, -8.0F, 8.0F, 8.0F, 8.0F, -8.0F, 
        0.0F, 0.0F };
    float f1 = (1.0F - entityguardian.getSpikesAnimation(f)) * 0.55F;
    for (int i = 0; i < 12; i++) {
      (this.guardianSpines[i]).rotateAngleX = 3.1415927F * afloat[i];
      (this.guardianSpines[i]).rotateAngleY = 3.1415927F * afloat1[i];
      (this.guardianSpines[i]).rotateAngleZ = 3.1415927F * afloat2[i];
      (this.guardianSpines[i]).rotationPointX = afloat3[i] * (1.0F + MathHelper.cos(ageInTicks * 1.5F + i) * 0.01F - f1);
      (this.guardianSpines[i]).rotationPointY = 16.0F + afloat4[i] * (1.0F + MathHelper.cos(ageInTicks * 1.5F + i) * 0.01F - f1);
      (this.guardianSpines[i]).rotationPointZ = afloat5[i] * (1.0F + MathHelper.cos(ageInTicks * 1.5F + i) * 0.01F - f1);
    } 
    this.guardianEye.rotationPointZ = -8.25F;
    Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
    if (entityguardian.hasTargetedEntity())
      entityLivingBase = entityguardian.getTargetedEntity(); 
    if (entityLivingBase != null) {
      Vec3d vec3d = entityLivingBase.getPositionEyes(0.0F);
      Vec3d vec3d1 = entityIn.getPositionEyes(0.0F);
      double d0 = vec3d.yCoord - vec3d1.yCoord;
      if (d0 > 0.0D) {
        this.guardianEye.rotationPointY = 0.0F;
      } else {
        this.guardianEye.rotationPointY = 1.0F;
      } 
      Vec3d vec3d2 = entityIn.getLook(0.0F);
      vec3d2 = new Vec3d(vec3d2.xCoord, 0.0D, vec3d2.zCoord);
      Vec3d vec3d3 = (new Vec3d(vec3d1.xCoord - vec3d.xCoord, 0.0D, vec3d1.zCoord - vec3d.zCoord)).normalize().rotateYaw(1.5707964F);
      double d1 = vec3d2.dotProduct(vec3d3);
      this.guardianEye.rotationPointX = MathHelper.sqrt((float)Math.abs(d1)) * 2.0F * (float)Math.signum(d1);
    } 
    this.guardianEye.showModel = true;
    float f2 = entityguardian.getTailAnimation(f);
    (this.guardianTail[0]).rotateAngleY = MathHelper.sin(f2) * 3.1415927F * 0.05F;
    (this.guardianTail[1]).rotateAngleY = MathHelper.sin(f2) * 3.1415927F * 0.1F;
    (this.guardianTail[1]).rotationPointX = -1.5F;
    (this.guardianTail[1]).rotationPointY = 0.5F;
    (this.guardianTail[1]).rotationPointZ = 14.0F;
    (this.guardianTail[2]).rotateAngleY = MathHelper.sin(f2) * 3.1415927F * 0.15F;
    (this.guardianTail[2]).rotationPointX = 0.5F;
    (this.guardianTail[2]).rotationPointY = 0.5F;
    (this.guardianTail[2]).rotationPointZ = 6.0F;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\model\ModelGuardian.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */