package net.minecraft.world.storage.loot.properties;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.util.ResourceLocation;

public class EntityPropertyManager {
  private static final Map<ResourceLocation, EntityProperty.Serializer<?>> NAME_TO_SERIALIZER_MAP = Maps.newHashMap();
  
  private static final Map<Class<? extends EntityProperty>, EntityProperty.Serializer<?>> CLASS_TO_SERIALIZER_MAP = Maps.newHashMap();
  
  public static <T extends EntityProperty> void registerProperty(EntityProperty.Serializer<? extends T> p_186644_0_) {
    ResourceLocation resourcelocation = p_186644_0_.getName();
    Class<T> oclass = (Class)p_186644_0_.getPropertyClass();
    if (NAME_TO_SERIALIZER_MAP.containsKey(resourcelocation))
      throw new IllegalArgumentException("Can't re-register entity property name " + resourcelocation); 
    if (CLASS_TO_SERIALIZER_MAP.containsKey(oclass))
      throw new IllegalArgumentException("Can't re-register entity property class " + oclass.getName()); 
    NAME_TO_SERIALIZER_MAP.put(resourcelocation, p_186644_0_);
    CLASS_TO_SERIALIZER_MAP.put(oclass, p_186644_0_);
  }
  
  public static EntityProperty.Serializer<?> getSerializerForName(ResourceLocation p_186646_0_) {
    EntityProperty.Serializer<?> serializer = NAME_TO_SERIALIZER_MAP.get(p_186646_0_);
    if (serializer == null)
      throw new IllegalArgumentException("Unknown loot entity property '" + p_186646_0_ + "'"); 
    return serializer;
  }
  
  public static <T extends EntityProperty> EntityProperty.Serializer<T> getSerializerFor(T property) {
    EntityProperty.Serializer<?> serializer = CLASS_TO_SERIALIZER_MAP.get(property.getClass());
    if (serializer == null)
      throw new IllegalArgumentException("Unknown loot entity property " + property); 
    return (EntityProperty.Serializer)serializer;
  }
  
  static {
    registerProperty(new EntityOnFire.Serializer());
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\storage\loot\properties\EntityPropertyManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */