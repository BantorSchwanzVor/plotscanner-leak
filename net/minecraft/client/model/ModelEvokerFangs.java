package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelEvokerFangs extends ModelBase {
  private final ModelRenderer field_191213_a = new ModelRenderer(this, 0, 0);
  
  private final ModelRenderer field_191214_b;
  
  private final ModelRenderer field_191215_c;
  
  public ModelEvokerFangs() {
    this.field_191213_a.setRotationPoint(-5.0F, 22.0F, -5.0F);
    this.field_191213_a.addBox(0.0F, 0.0F, 0.0F, 10, 12, 10);
    this.field_191214_b = new ModelRenderer(this, 40, 0);
    this.field_191214_b.setRotationPoint(1.5F, 22.0F, -4.0F);
    this.field_191214_b.addBox(0.0F, 0.0F, 0.0F, 4, 14, 8);
    this.field_191215_c = new ModelRenderer(this, 40, 0);
    this.field_191215_c.setRotationPoint(-1.5F, 22.0F, 4.0F);
    this.field_191215_c.addBox(0.0F, 0.0F, 0.0F, 4, 14, 8);
  }
  
  public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    float f = limbSwing * 2.0F;
    if (f > 1.0F)
      f = 1.0F; 
    f = 1.0F - f * f * f;
    this.field_191214_b.rotateAngleZ = 3.1415927F - f * 0.35F * 3.1415927F;
    this.field_191215_c.rotateAngleZ = 3.1415927F + f * 0.35F * 3.1415927F;
    this.field_191215_c.rotateAngleY = 3.1415927F;
    float f1 = (limbSwing + MathHelper.sin(limbSwing * 2.7F)) * 0.6F * 12.0F;
    this.field_191214_b.rotationPointY = 24.0F - f1;
    this.field_191215_c.rotationPointY = this.field_191214_b.rotationPointY;
    this.field_191213_a.rotationPointY = this.field_191214_b.rotationPointY;
    this.field_191213_a.render(scale);
    this.field_191214_b.render(scale);
    this.field_191215_c.render(scale);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\model\ModelEvokerFangs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */