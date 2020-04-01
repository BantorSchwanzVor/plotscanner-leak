package net.minecraft.util;

public class Tuple<A, B> {
  private A a;
  
  private B b;
  
  public Tuple(A aIn, B bIn) {
    this.a = aIn;
    this.b = bIn;
  }
  
  public A getFirst() {
    return this.a;
  }
  
  public B getSecond() {
    return this.b;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\Tuple.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */