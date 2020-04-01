package optifine;

public class IntegerCache {
  private static final int CACHE_SIZE = 4096;
  
  private static final Integer[] cache = makeCache(4096);
  
  private static Integer[] makeCache(int p_makeCache_0_) {
    Integer[] ainteger = new Integer[p_makeCache_0_];
    for (int i = 0; i < p_makeCache_0_; i++)
      ainteger[i] = new Integer(i); 
    return ainteger;
  }
  
  public static Integer valueOf(int p_valueOf_0_) {
    return (p_valueOf_0_ >= 0 && p_valueOf_0_ < 4096) ? cache[p_valueOf_0_] : new Integer(p_valueOf_0_);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\optifine\IntegerCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */