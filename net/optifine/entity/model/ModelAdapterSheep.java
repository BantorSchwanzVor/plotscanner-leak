package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.entity.passive.EntitySheep;

public class ModelAdapterSheep extends ModelAdapterQuadruped {
  public ModelAdapterSheep() {
    super(EntitySheep.class, "sheep", 0.7F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelSheep2();
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
    RenderSheep rendersheep = new RenderSheep(rendermanager);
    rendersheep.mainModel = modelBase;
    rendersheep.shadowSize = shadowSize;
    return (IEntityRenderer)rendersheep;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterSheep.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */