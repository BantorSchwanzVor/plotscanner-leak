package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelIllager;
import net.minecraft.client.renderer.entity.RenderEvoker;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityEvoker;

public class ModelAdapterEvoker extends ModelAdapterIllager {
  public ModelAdapterEvoker() {
    super(EntityEvoker.class, "evocation_illager", 0.5F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelIllager(0.0F, 0.0F, 64, 64);
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
    RenderEvoker renderevoker = new RenderEvoker(rendermanager);
    renderevoker.mainModel = modelBase;
    renderevoker.shadowSize = shadowSize;
    return (IEntityRenderer)renderevoker;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterEvoker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */