package net.minecraft.util.registry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RegistryDefaulted<K, V> extends RegistrySimple<K, V> {
  private final V defaultObject;
  
  public RegistryDefaulted(V defaultObjectIn) {
    this.defaultObject = defaultObjectIn;
  }
  
  @Nonnull
  public V getObject(@Nullable K name) {
    V v = super.getObject(name);
    return (v == null) ? this.defaultObject : v;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\registry\RegistryDefaulted.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */