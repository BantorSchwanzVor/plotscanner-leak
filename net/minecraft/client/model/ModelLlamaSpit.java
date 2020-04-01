package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelLlamaSpit extends ModelBase {
  public ModelLlamaSpit() {
    this(0.0F);
  }
  
  private final ModelRenderer field_191225_a = new ModelRenderer(this);
  
  public ModelLlamaSpit(float p_i47225_1_) {
    int i = 2;
    this.field_191225_a.setTextureOffset(0, 0).addBox(-4.0F, 0.0F, 0.0F, 2, 2, 2, p_i47225_1_);
    this.field_191225_a.setTextureOffset(0, 0).addBox(0.0F, -4.0F, 0.0F, 2, 2, 2, p_i47225_1_);
    this.field_191225_a.setTextureOffset(0, 0).addBox(0.0F, 0.0F, -4.0F, 2, 2, 2, p_i47225_1_);
    this.field_191225_a.setTextureOffset(0, 0).addBox(0.0F, 0.0F, 0.0F, 2, 2, 2, p_i47225_1_);
    this.field_191225_a.setTextureOffset(0, 0).addBox(2.0F, 0.0F, 0.0F, 2, 2, 2, p_i47225_1_);
    this.field_191225_a.setTextureOffset(0, 0).addBox(0.0F, 2.0F, 0.0F, 2, 2, 2, p_i47225_1_);
    this.field_191225_a.setTextureOffset(0, 0).addBox(0.0F, 0.0F, 2.0F, 2, 2, 2, p_i47225_1_);
    this.field_191225_a.setRotationPoint(0.0F, 0.0F, 0.0F);
  }
  
  public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
    this.field_191225_a.render(scale);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\model\ModelLlamaSpit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */