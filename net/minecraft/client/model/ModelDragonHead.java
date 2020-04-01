package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelDragonHead extends ModelBase {
  private final ModelRenderer head;
  
  private final ModelRenderer jaw;
  
  public ModelDragonHead(float p_i46588_1_) {
    this.textureWidth = 256;
    this.textureHeight = 256;
    setTextureOffset("body.body", 0, 0);
    setTextureOffset("wing.skin", -56, 88);
    setTextureOffset("wingtip.skin", -56, 144);
    setTextureOffset("rearleg.main", 0, 0);
    setTextureOffset("rearfoot.main", 112, 0);
    setTextureOffset("rearlegtip.main", 196, 0);
    setTextureOffset("head.upperhead", 112, 30);
    setTextureOffset("wing.bone", 112, 88);
    setTextureOffset("head.upperlip", 176, 44);
    setTextureOffset("jaw.jaw", 176, 65);
    setTextureOffset("frontleg.main", 112, 104);
    setTextureOffset("wingtip.bone", 112, 136);
    setTextureOffset("frontfoot.main", 144, 104);
    setTextureOffset("neck.box", 192, 104);
    setTextureOffset("frontlegtip.main", 226, 138);
    setTextureOffset("body.scale", 220, 53);
    setTextureOffset("head.scale", 0, 0);
    setTextureOffset("neck.scale", 48, 0);
    setTextureOffset("head.nostril", 112, 0);
    float f = -16.0F;
    this.head = new ModelRenderer(this, "head");
    this.head.addBox("upperlip", -6.0F, -1.0F, -24.0F, 12, 5, 16);
    this.head.addBox("upperhead", -8.0F, -8.0F, -10.0F, 16, 16, 16);
    this.head.mirror = true;
    this.head.addBox("scale", -5.0F, -12.0F, -4.0F, 2, 4, 6);
    this.head.addBox("nostril", -5.0F, -3.0F, -22.0F, 2, 2, 4);
    this.head.mirror = false;
    this.head.addBox("scale", 3.0F, -12.0F, -4.0F, 2, 4, 6);
    this.head.addBox("nostril", 3.0F, -3.0F, -22.0F, 2, 2, 4);
    this.jaw = new ModelRenderer(this, "jaw");
    this.jaw.setRotationPoint(0.0F, 4.0F, -8.0F);
    this.jaw.addBox("jaw", -6.0F, 0.0F, -16.0F, 12, 4, 16);
    this.head.addChild(this.jaw);
  }
  
  public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    this.jaw.rotateAngleX = (float)(Math.sin((limbSwing * 3.1415927F * 0.2F)) + 1.0D) * 0.2F;
    this.head.rotateAngleY = netHeadYaw * 0.017453292F;
    this.head.rotateAngleX = headPitch * 0.017453292F;
    GlStateManager.translate(0.0F, -0.374375F, 0.0F);
    GlStateManager.scale(0.75F, 0.75F, 0.75F);
    this.head.render(scale);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\model\ModelDragonHead.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */