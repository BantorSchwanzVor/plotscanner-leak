package net.optifine.entity.model;

import net.minecraft.client.model.ModelRenderer;
import net.optifine.entity.model.anim.ModelUpdater;

public class CustomModelRenderer {
  private String modelPart;
  
  private boolean attach;
  
  private ModelRenderer modelRenderer;
  
  private ModelUpdater modelUpdater;
  
  public CustomModelRenderer(String modelPart, boolean attach, ModelRenderer modelRenderer, ModelUpdater modelUpdater) {
    this.modelPart = modelPart;
    this.attach = attach;
    this.modelRenderer = modelRenderer;
    this.modelUpdater = modelUpdater;
  }
  
  public ModelRenderer getModelRenderer() {
    return this.modelRenderer;
  }
  
  public String getModelPart() {
    return this.modelPart;
  }
  
  public boolean isAttach() {
    return this.attach;
  }
  
  public ModelUpdater getModelUpdater() {
    return this.modelUpdater;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\CustomModelRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */