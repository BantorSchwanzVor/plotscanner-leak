package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderHusk;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityHusk;

public class ModelAdapterHusk extends ModelAdapterBiped {
  public ModelAdapterHusk() {
    super(EntityHusk.class, "husk", 0.5F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelZombie();
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
    RenderHusk renderhusk = new RenderHusk(rendermanager);
    renderhusk.mainModel = modelBase;
    renderhusk.shadowSize = shadowSize;
    return (IEntityRenderer)renderhusk;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterHusk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */