package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelEnderMite extends ModelBase {
  private static final int[][] BODY_SIZES = new int[][] { { 4, 3, 2 }, { 6, 4, 5 }, { 3, 3, 1 }, { 1, 2, 1 } };
  
  private static final int[][] BODY_TEXS = new int[][] { new int[2], { 0, 5 }, { 0, 14 }, { 0, 18 } };
  
  private static final int BODY_COUNT = BODY_SIZES.length;
  
  private final ModelRenderer[] bodyParts = new ModelRenderer[BODY_COUNT];
  
  public ModelEnderMite() {
    float f = -3.5F;
    for (int i = 0; i < this.bodyParts.length; i++) {
      this.bodyParts[i] = new ModelRenderer(this, BODY_TEXS[i][0], BODY_TEXS[i][1]);
      this.bodyParts[i].addBox(BODY_SIZES[i][0] * -0.5F, 0.0F, BODY_SIZES[i][2] * -0.5F, BODY_SIZES[i][0], BODY_SIZES[i][1], BODY_SIZES[i][2]);
      this.bodyParts[i].setRotationPoint(0.0F, (24 - BODY_SIZES[i][1]), f);
      if (i < this.bodyParts.length - 1)
        f += (BODY_SIZES[i][2] + BODY_SIZES[i + 1][2]) * 0.5F; 
    } 
  }
  
  public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
    byte b;
    int i;
    ModelRenderer[] arrayOfModelRenderer;
    for (i = (arrayOfModelRenderer = this.bodyParts).length, b = 0; b < i; ) {
      ModelRenderer modelrenderer = arrayOfModelRenderer[b];
      modelrenderer.render(scale);
      b++;
    } 
  }
  
  public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
    for (int i = 0; i < this.bodyParts.length; i++) {
      (this.bodyParts[i]).rotateAngleY = MathHelper.cos(ageInTicks * 0.9F + i * 0.15F * 3.1415927F) * 3.1415927F * 0.01F * (1 + Math.abs(i - 2));
      (this.bodyParts[i]).rotationPointX = MathHelper.sin(ageInTicks * 0.9F + i * 0.15F * 3.1415927F) * 3.1415927F * 0.1F * Math.abs(i - 2);
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\model\ModelEnderMite.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */