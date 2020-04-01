package net.minecraftforge.fml.common.registry;

import net.minecraft.util.ResourceLocation;

public interface RegistryDelegate<T> {
  T get();
  
  ResourceLocation name();
  
  Class<T> type();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraftforge\fml\common\registry\RegistryDelegate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */