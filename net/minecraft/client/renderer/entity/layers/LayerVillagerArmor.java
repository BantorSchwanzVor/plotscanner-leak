package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.renderer.entity.RenderLivingBase;

public class LayerVillagerArmor extends LayerBipedArmor {
  public LayerVillagerArmor(RenderLivingBase<?> rendererIn) {
    super(rendererIn);
  }
  
  protected void initArmor() {
    this.modelLeggings = (ModelBiped)new ModelZombieVillager(0.5F, 0.0F, true);
    this.modelArmor = (ModelBiped)new ModelZombieVillager(1.0F, 0.0F, true);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\layers\LayerVillagerArmor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */