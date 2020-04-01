package net.minecraft.client.audio;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistrySimple;

public class SoundRegistry extends RegistrySimple<ResourceLocation, SoundEventAccessor> {
  private Map<ResourceLocation, SoundEventAccessor> soundRegistry;
  
  protected Map<ResourceLocation, SoundEventAccessor> createUnderlyingMap() {
    this.soundRegistry = Maps.newHashMap();
    return this.soundRegistry;
  }
  
  public void add(SoundEventAccessor accessor) {
    putObject(accessor.getLocation(), accessor);
  }
  
  public void clearMap() {
    this.soundRegistry.clear();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\audio\SoundRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */