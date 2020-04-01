package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderMooshroom;
import net.minecraft.entity.passive.EntityMooshroom;

public class ModelAdapterMooshroom extends ModelAdapterQuadruped {
  public ModelAdapterMooshroom() {
    super(EntityMooshroom.class, "mooshroom", 0.7F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelCow();
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
    RenderMooshroom rendermooshroom = new RenderMooshroom(rendermanager);
    rendermooshroom.mainModel = modelBase;
    rendermooshroom.shadowSize = shadowSize;
    return (IEntityRenderer)rendermooshroom;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterMooshroom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */