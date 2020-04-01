package net.minecraft.util.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.util.IObjectIntIterable;
import net.minecraft.util.IntIdentityHashBiMap;

public class RegistryNamespaced<K, V> extends RegistrySimple<K, V> implements IObjectIntIterable<V> {
  protected final IntIdentityHashBiMap<V> underlyingIntegerMap = new IntIdentityHashBiMap(256);
  
  protected final Map<V, K> inverseObjectRegistry;
  
  public RegistryNamespaced() {
    this.inverseObjectRegistry = (Map<V, K>)((BiMap)this.registryObjects).inverse();
  }
  
  public void register(int id, K key, V value) {
    this.underlyingIntegerMap.put(value, id);
    putObject(key, value);
  }
  
  protected Map<K, V> createUnderlyingMap() {
    return (Map<K, V>)HashBiMap.create();
  }
  
  @Nullable
  public V getObject(@Nullable K name) {
    return super.getObject(name);
  }
  
  @Nullable
  public K getNameForObject(V value) {
    return this.inverseObjectRegistry.get(value);
  }
  
  public boolean containsKey(K key) {
    return super.containsKey(key);
  }
  
  public int getIDForObject(@Nullable V value) {
    return this.underlyingIntegerMap.getId(value);
  }
  
  @Nullable
  public V getObjectById(int id) {
    return (V)this.underlyingIntegerMap.get(id);
  }
  
  public Iterator<V> iterator() {
    return this.underlyingIntegerMap.iterator();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\registry\RegistryNamespaced.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */