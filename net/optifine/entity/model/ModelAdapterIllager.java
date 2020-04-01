package net.optifine.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelIllager;
import net.minecraft.client.model.ModelRenderer;

public abstract class ModelAdapterIllager extends ModelAdapter {
  public ModelAdapterIllager(Class entityClass, String name, float shadowSize) {
    super(entityClass, name, shadowSize);
  }
  
  public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
    if (!(model instanceof ModelIllager))
      return null; 
    ModelIllager modelillager = (ModelIllager)model;
    if (modelPart.equals("head"))
      return modelillager.field_191217_a; 
    if (modelPart.equals("body"))
      return modelillager.field_191218_b; 
    if (modelPart.equals("arms"))
      return modelillager.field_191219_c; 
    if (modelPart.equals("left_leg"))
      return modelillager.field_191221_e; 
    if (modelPart.equals("right_leg"))
      return modelillager.field_191220_d; 
    if (modelPart.equals("nose"))
      return modelillager.field_191222_f; 
    if (modelPart.equals("left_arm"))
      return modelillager.field_191224_h; 
    return modelPart.equals("right_arm") ? modelillager.field_191223_g : null;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterIllager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */