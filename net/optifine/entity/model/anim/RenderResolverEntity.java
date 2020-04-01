package net.optifine.entity.model.anim;

public class RenderResolverEntity implements IRenderResolver {
  public IExpression getParameter(String name) {
    EnumRenderParameterEntity enumrenderparameterentity = EnumRenderParameterEntity.parse(name);
    return enumrenderparameterentity;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\anim\RenderResolverEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */