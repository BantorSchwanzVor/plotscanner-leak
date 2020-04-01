package net.minecraft.client.renderer.block.model;

import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.registry.IRegistry;

public class ModelManager implements IResourceManagerReloadListener {
  private IRegistry<ModelResourceLocation, IBakedModel> modelRegistry;
  
  private final TextureMap texMap;
  
  private final BlockModelShapes modelProvider;
  
  private IBakedModel defaultModel;
  
  public ModelManager(TextureMap textures) {
    this.texMap = textures;
    this.modelProvider = new BlockModelShapes(this);
  }
  
  public void onResourceManagerReload(IResourceManager resourceManager) {
    ModelBakery modelbakery = new ModelBakery(resourceManager, this.texMap, this.modelProvider);
    this.modelRegistry = modelbakery.setupModelRegistry();
    this.defaultModel = (IBakedModel)this.modelRegistry.getObject(ModelBakery.MODEL_MISSING);
    this.modelProvider.reloadModels();
  }
  
  public IBakedModel getModel(ModelResourceLocation modelLocation) {
    if (modelLocation == null)
      return this.defaultModel; 
    IBakedModel ibakedmodel = (IBakedModel)this.modelRegistry.getObject(modelLocation);
    return (ibakedmodel == null) ? this.defaultModel : ibakedmodel;
  }
  
  public IBakedModel getMissingModel() {
    return this.defaultModel;
  }
  
  public TextureMap getTextureMap() {
    return this.texMap;
  }
  
  public BlockModelShapes getBlockModelShapes() {
    return this.modelProvider;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\block\model\ModelManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */