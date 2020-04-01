package net.optifine.entity.model.anim;

public class Constant implements IExpression {
  private float value;
  
  public Constant(float value) {
    this.value = value;
  }
  
  public float eval() {
    return this.value;
  }
  
  public String toString() {
    return this.value;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\anim\Constant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */