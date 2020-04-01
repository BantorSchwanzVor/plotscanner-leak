package net.minecraft.world.storage.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.properties.EntityProperty;
import net.minecraft.world.storage.loot.properties.EntityPropertyManager;

public class EntityHasProperty implements LootCondition {
  private final EntityProperty[] properties;
  
  private final LootContext.EntityTarget target;
  
  public EntityHasProperty(EntityProperty[] propertiesIn, LootContext.EntityTarget targetIn) {
    this.properties = propertiesIn;
    this.target = targetIn;
  }
  
  public boolean testCondition(Random rand, LootContext context) {
    Entity entity = context.getEntity(this.target);
    if (entity == null)
      return false; 
    byte b;
    int i;
    EntityProperty[] arrayOfEntityProperty;
    for (i = (arrayOfEntityProperty = this.properties).length, b = 0; b < i; ) {
      EntityProperty entityproperty = arrayOfEntityProperty[b];
      if (!entityproperty.testProperty(rand, entity))
        return false; 
      b++;
    } 
    return true;
  }
  
  public static class Serializer extends LootCondition.Serializer<EntityHasProperty> {
    protected Serializer() {
      super(new ResourceLocation("entity_properties"), EntityHasProperty.class);
    }
    
    public void serialize(JsonObject json, EntityHasProperty value, JsonSerializationContext context) {
      JsonObject jsonobject = new JsonObject();
      byte b;
      int i;
      EntityProperty[] arrayOfEntityProperty;
      for (i = (arrayOfEntityProperty = value.properties).length, b = 0; b < i; ) {
        EntityProperty entityproperty = arrayOfEntityProperty[b];
        EntityProperty.Serializer<EntityProperty> serializer = EntityPropertyManager.getSerializerFor(entityproperty);
        jsonobject.add(serializer.getName().toString(), serializer.serialize(entityproperty, context));
        b++;
      } 
      json.add("properties", (JsonElement)jsonobject);
      json.add("entity", context.serialize(value.target));
    }
    
    public EntityHasProperty deserialize(JsonObject json, JsonDeserializationContext context) {
      Set<Map.Entry<String, JsonElement>> set = JsonUtils.getJsonObject(json, "properties").entrySet();
      EntityProperty[] aentityproperty = new EntityProperty[set.size()];
      int i = 0;
      for (Map.Entry<String, JsonElement> entry : set)
        aentityproperty[i++] = EntityPropertyManager.getSerializerForName(new ResourceLocation(entry.getKey())).deserialize(entry.getValue(), context); 
      return new EntityHasProperty(aentityproperty, (LootContext.EntityTarget)JsonUtils.deserializeClass(json, "entity", context, LootContext.EntityTarget.class));
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\storage\loot\conditions\EntityHasProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */