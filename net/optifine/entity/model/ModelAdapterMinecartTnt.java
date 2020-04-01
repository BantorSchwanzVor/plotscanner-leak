package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderTntMinecart;
import net.minecraft.entity.item.EntityMinecartTNT;
import optifine.Config;
import optifine.Reflector;

public class ModelAdapterMinecartTnt extends ModelAdapterMinecart {
  public ModelAdapterMinecartTnt() {
    super(EntityMinecartTNT.class, "tnt_minecart", 0.5F);
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
    RenderTntMinecart rendertntminecart = new RenderTntMinecart(rendermanager);
    if (!Reflector.RenderMinecart_modelMinecart.exists()) {
      Config.warn("Field not found: RenderMinecart.modelMinecart");
      return null;
    } 
    Reflector.setFieldValue(rendertntminecart, Reflector.RenderMinecart_modelMinecart, modelBase);
    rendertntminecart.shadowSize = shadowSize;
    return (IEntityRenderer)rendertntminecart;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterMinecartTnt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */