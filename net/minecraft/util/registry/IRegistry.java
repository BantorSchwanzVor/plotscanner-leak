package net.minecraft.util.registry;

import java.util.Set;
import javax.annotation.Nullable;

public interface IRegistry<K, V> extends Iterable<V> {
  @Nullable
  V getObject(K paramK);
  
  void putObject(K paramK, V paramV);
  
  Set<K> getKeys();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\registry\IRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */