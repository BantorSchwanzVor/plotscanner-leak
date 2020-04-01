package net.minecraft.client.model;

public class ModelPig extends ModelQuadruped {
  public ModelPig() {
    this(0.0F);
  }
  
  public ModelPig(float scale) {
    super(6, scale);
    this.head.setTextureOffset(16, 16).addBox(-2.0F, 0.0F, -9.0F, 4, 3, 1, scale);
    this.childYOffset = 4.0F;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\model\ModelPig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */