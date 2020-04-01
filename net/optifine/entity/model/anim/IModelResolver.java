package net.optifine.entity.model.anim;

import net.minecraft.client.model.ModelRenderer;

public interface IModelResolver {
  ModelRenderer getModelRenderer(String paramString);
  
  ModelVariable getModelVariable(String paramString);
  
  IExpression getExpression(String paramString);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\anim\IModelResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */