package net.minecraft.world.storage.loot.properties;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public interface EntityProperty {
  boolean testProperty(Random paramRandom, Entity paramEntity);
  
  public static abstract class Serializer<T extends EntityProperty> {
    private final ResourceLocation name;
    
    private final Class<T> propertyClass;
    
    protected Serializer(ResourceLocation nameIn, Class<T> propertyClassIn) {
      this.name = nameIn;
      this.propertyClass = propertyClassIn;
    }
    
    public ResourceLocation getName() {
      return this.name;
    }
    
    public Class<T> getPropertyClass() {
      return this.propertyClass;
    }
    
    public abstract JsonElement serialize(T param1T, JsonSerializationContext param1JsonSerializationContext);
    
    public abstract T deserialize(JsonElement param1JsonElement, JsonDeserializationContext param1JsonDeserializationContext);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\storage\loot\properties\EntityProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */