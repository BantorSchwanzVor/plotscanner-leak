package net.optifine.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public abstract class ModelAdapter {
  private Class entityClass;
  
  private String name;
  
  private float shadowSize;
  
  public ModelAdapter(Class entityClass, String name, float shadowSize) {
    this.entityClass = entityClass;
    this.name = name;
    this.shadowSize = shadowSize;
  }
  
  public Class getEntityClass() {
    return this.entityClass;
  }
  
  public String getName() {
    return this.name;
  }
  
  public float getShadowSize() {
    return this.shadowSize;
  }
  
  public abstract ModelBase makeModel();
  
  public abstract ModelRenderer getModelRenderer(ModelBase paramModelBase, String paramString);
  
  public abstract IEntityRenderer makeEntityRender(ModelBase paramModelBase, float paramFloat);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */