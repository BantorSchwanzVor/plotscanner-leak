package net.optifine.entity.model;

import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerSheepWool;
import net.minecraft.entity.passive.EntitySheep;
import optifine.Config;

public class ModelAdapterSheepWool extends ModelAdapterQuadruped {
  public ModelAdapterSheepWool() {
    super(EntitySheep.class, "sheep_wool", 0.7F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelSheep1();
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    RenderSheep renderSheep1;
    RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
    Render render = (Render)rendermanager.getEntityRenderMap().get(EntitySheep.class);
    if (!(render instanceof RenderSheep)) {
      Config.warn("Not a RenderSheep: " + render);
      return null;
    } 
    if (render.getEntityClass() == null) {
      RenderSheep rendersheep = new RenderSheep(rendermanager);
      rendersheep.mainModel = (ModelBase)new ModelSheep2();
      rendersheep.shadowSize = 0.7F;
      renderSheep1 = rendersheep;
    } 
    RenderSheep rendersheep1 = renderSheep1;
    List<LayerRenderer<EntitySheep>> list = rendersheep1.getLayerRenderers();
    Iterator<LayerRenderer<EntitySheep>> iterator = list.iterator();
    while (iterator.hasNext()) {
      LayerRenderer layerrenderer = iterator.next();
      if (layerrenderer instanceof LayerSheepWool)
        iterator.remove(); 
    } 
    LayerSheepWool layersheepwool = new LayerSheepWool(rendersheep1);
    layersheepwool.sheepModel = (ModelSheep1)modelBase;
    rendersheep1.addLayer((LayerRenderer)layersheepwool);
    return (IEntityRenderer)rendersheep1;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterSheepWool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */