package com.mysql.cj;

import java.util.Set;

public interface CacheAdapter<K, V> {
  V get(K paramK);
  
  void put(K paramK, V paramV);
  
  void invalidate(K paramK);
  
  void invalidateAll(Set<K> paramSet);
  
  void invalidateAll();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\CacheAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */