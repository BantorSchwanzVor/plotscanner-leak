package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderZombieVillager;
import net.minecraft.entity.monster.EntityZombieVillager;

public class ModelAdapterZombieVillager extends ModelAdapterBiped {
  public ModelAdapterZombieVillager() {
    super(EntityZombieVillager.class, "zombie_villager", 0.5F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelZombieVillager();
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
    RenderZombieVillager renderzombievillager = new RenderZombieVillager(rendermanager);
    renderzombievillager.mainModel = modelBase;
    renderzombievillager.shadowSize = shadowSize;
    return (IEntityRenderer)renderzombievillager;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterZombieVillager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */