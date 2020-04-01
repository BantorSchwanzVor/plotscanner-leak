package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelSquid extends ModelBase {
  ModelRenderer squidBody;
  
  ModelRenderer[] squidTentacles = new ModelRenderer[8];
  
  public ModelSquid() {
    int i = -16;
    this.squidBody = new ModelRenderer(this, 0, 0);
    this.squidBody.addBox(-6.0F, -8.0F, -6.0F, 12, 16, 12);
    this.squidBody.rotationPointY += 8.0F;
    for (int j = 0; j < this.squidTentacles.length; j++) {
      this.squidTentacles[j] = new ModelRenderer(this, 48, 0);
      double d0 = j * Math.PI * 2.0D / this.squidTentacles.length;
      float f = (float)Math.cos(d0) * 5.0F;
      float f1 = (float)Math.sin(d0) * 5.0F;
      this.squidTentacles[j].addBox(-1.0F, 0.0F, -1.0F, 2, 18, 2);
      (this.squidTentacles[j]).rotationPointX = f;
      (this.squidTentacles[j]).rotationPointZ = f1;
      (this.squidTentacles[j]).rotationPointY = 15.0F;
      d0 = j * Math.PI * -2.0D / this.squidTentacles.length + 1.5707963267948966D;
      (this.squidTentacles[j]).rotateAngleY = (float)d0;
    } 
  }
  
  public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
    byte b;
    int i;
    ModelRenderer[] arrayOfModelRenderer;
    for (i = (arrayOfModelRenderer = this.squidTentacles).length, b = 0; b < i; ) {
      ModelRenderer modelrenderer = arrayOfModelRenderer[b];
      modelrenderer.rotateAngleX = ageInTicks;
      b++;
    } 
  }
  
  public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
    this.squidBody.render(scale);
    byte b;
    int i;
    ModelRenderer[] arrayOfModelRenderer;
    for (i = (arrayOfModelRenderer = this.squidTentacles).length, b = 0; b < i; ) {
      ModelRenderer modelrenderer = arrayOfModelRenderer[b];
      modelrenderer.render(scale);
      b++;
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\model\ModelSquid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */