package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelLlama;
import net.minecraft.client.renderer.entity.RenderLlama;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityLlama;

public class ModelAdapterLlama extends ModelAdapterQuadruped {
  public ModelAdapterLlama() {
    super(EntityLlama.class, "llama", 0.7F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelLlama(0.0F);
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
    RenderLlama renderllama = new RenderLlama(rendermanager);
    renderllama.mainModel = modelBase;
    renderllama.shadowSize = shadowSize;
    return (IEntityRenderer)renderllama;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterLlama.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */