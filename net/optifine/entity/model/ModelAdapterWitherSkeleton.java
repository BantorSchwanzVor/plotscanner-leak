package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWitherSkeleton;
import net.minecraft.entity.monster.EntityWitherSkeleton;

public class ModelAdapterWitherSkeleton extends ModelAdapterBiped {
  public ModelAdapterWitherSkeleton() {
    super(EntityWitherSkeleton.class, "wither_skeleton", 0.7F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelSkeleton();
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
    RenderWitherSkeleton renderwitherskeleton = new RenderWitherSkeleton(rendermanager);
    renderwitherskeleton.mainModel = modelBase;
    renderwitherskeleton.shadowSize = shadowSize;
    return (IEntityRenderer)renderwitherskeleton;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterWitherSkeleton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */