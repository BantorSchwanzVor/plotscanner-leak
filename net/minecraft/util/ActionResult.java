package net.minecraft.util;

public class ActionResult<T> {
  private final EnumActionResult type;
  
  private final T result;
  
  public ActionResult(EnumActionResult typeIn, T resultIn) {
    this.type = typeIn;
    this.result = resultIn;
  }
  
  public EnumActionResult getType() {
    return this.type;
  }
  
  public T getResult() {
    return this.result;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\ActionResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */