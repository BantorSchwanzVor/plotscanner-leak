package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;

public class LayerBipedArmor extends LayerArmorBase<ModelBiped> {
  public LayerBipedArmor(RenderLivingBase<?> rendererIn) {
    super(rendererIn);
  }
  
  protected void initArmor() {
    this.modelLeggings = new ModelBiped(0.5F);
    this.modelArmor = new ModelBiped(1.0F);
  }
  
  protected void setModelSlotVisible(ModelBiped p_188359_1_, EntityEquipmentSlot slotIn) {
    setModelVisible(p_188359_1_);
    switch (slotIn) {
      case HEAD:
        p_188359_1_.bipedHead.showModel = true;
        p_188359_1_.bipedHeadwear.showModel = true;
        break;
      case null:
        p_188359_1_.bipedBody.showModel = true;
        p_188359_1_.bipedRightArm.showModel = true;
        p_188359_1_.bipedLeftArm.showModel = true;
        break;
      case LEGS:
        p_188359_1_.bipedBody.showModel = true;
        p_188359_1_.bipedRightLeg.showModel = true;
        p_188359_1_.bipedLeftLeg.showModel = true;
        break;
      case FEET:
        p_188359_1_.bipedRightLeg.showModel = true;
        p_188359_1_.bipedLeftLeg.showModel = true;
        break;
    } 
  }
  
  protected void setModelVisible(ModelBiped model) {
    model.setInvisible(false);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\layers\LayerBipedArmor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */