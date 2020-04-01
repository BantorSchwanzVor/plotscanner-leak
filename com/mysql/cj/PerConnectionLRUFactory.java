package com.mysql.cj;

import com.mysql.cj.util.LRUCache;
import java.util.Set;

public class PerConnectionLRUFactory implements CacheAdapterFactory<String, ParseInfo> {
  public CacheAdapter<String, ParseInfo> getInstance(Object syncMutex, String url, int cacheMaxSize, int maxKeySize) {
    return new PerConnectionLRU(syncMutex, cacheMaxSize, maxKeySize);
  }
  
  class PerConnectionLRU implements CacheAdapter<String, ParseInfo> {
    private final int cacheSqlLimit;
    
    private final LRUCache<String, ParseInfo> cache;
    
    private final Object syncMutex;
    
    protected PerConnectionLRU(Object syncMutex, int cacheMaxSize, int maxKeySize) {
      int cacheSize = cacheMaxSize;
      this.cacheSqlLimit = maxKeySize;
      this.cache = new LRUCache(cacheSize);
      this.syncMutex = syncMutex;
    }
    
    public ParseInfo get(String key) {
      if (key == null || key.length() > this.cacheSqlLimit)
        return null; 
      synchronized (this.syncMutex) {
        return (ParseInfo)this.cache.get(key);
      } 
    }
    
    public void put(String key, ParseInfo value) {
      if (key == null || key.length() > this.cacheSqlLimit)
        return; 
      synchronized (this.syncMutex) {
        this.cache.put(key, value);
      } 
    }
    
    public void invalidate(String key) {
      synchronized (this.syncMutex) {
        this.cache.remove(key);
      } 
    }
    
    public void invalidateAll(Set<String> keys) {
      synchronized (this.syncMutex) {
        for (String key : keys)
          this.cache.remove(key); 
      } 
    }
    
    public void invalidateAll() {
      synchronized (this.syncMutex) {
        this.cache.clear();
      } 
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\PerConnectionLRUFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */