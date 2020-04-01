package net.optifine.entity.model.anim;

public class Function implements IExpression {
  private EnumFunctionType enumFunction;
  
  private IExpression[] arguments;
  
  public Function(EnumFunctionType enumFunction, IExpression[] arguments) {
    this.enumFunction = enumFunction;
    this.arguments = arguments;
  }
  
  public float eval() {
    return this.enumFunction.eval(this.arguments);
  }
  
  public String toString() {
    return this.enumFunction + "()";
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\anim\Function.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */