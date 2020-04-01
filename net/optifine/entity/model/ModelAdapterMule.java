package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderAbstractHorse;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityMule;

public class ModelAdapterMule extends ModelAdapterHorse {
  public ModelAdapterMule() {
    super(EntityMule.class, "mule", 0.75F);
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
    RenderAbstractHorse renderabstracthorse = new RenderAbstractHorse(rendermanager);
    renderabstracthorse.mainModel = modelBase;
    renderabstracthorse.shadowSize = shadowSize;
    return (IEntityRenderer)renderabstracthorse;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterMule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */