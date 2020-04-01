package net.optifine.entity.model.anim;

import net.minecraft.client.model.ModelRenderer;

public class ModelVariable implements IExpression {
  private String name;
  
  private ModelRenderer modelRenderer;
  
  private EnumModelVariable enumModelVariable;
  
  public ModelVariable(String name, ModelRenderer modelRenderer, EnumModelVariable enumModelVariable) {
    this.name = name;
    this.modelRenderer = modelRenderer;
    this.enumModelVariable = enumModelVariable;
  }
  
  public float eval() {
    return getValue();
  }
  
  public float getValue() {
    return this.enumModelVariable.getFloat(this.modelRenderer);
  }
  
  public void setValue(float value) {
    this.enumModelVariable.setFloat(this.modelRenderer, value);
  }
  
  public String toString() {
    return this.name;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\anim\ModelVariable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */