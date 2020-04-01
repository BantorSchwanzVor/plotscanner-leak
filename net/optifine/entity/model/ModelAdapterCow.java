package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.renderer.entity.RenderCow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityCow;

public class ModelAdapterCow extends ModelAdapterQuadruped {
  public ModelAdapterCow() {
    super(EntityCow.class, "cow", 0.7F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelCow();
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
    RenderCow rendercow = new RenderCow(rendermanager);
    rendercow.mainModel = modelBase;
    rendercow.shadowSize = shadowSize;
    return (IEntityRenderer)rendercow;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterCow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */