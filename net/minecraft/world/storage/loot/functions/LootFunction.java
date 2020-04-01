package net.minecraft.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public abstract class LootFunction {
  private final LootCondition[] conditions;
  
  protected LootFunction(LootCondition[] conditionsIn) {
    this.conditions = conditionsIn;
  }
  
  public abstract ItemStack apply(ItemStack paramItemStack, Random paramRandom, LootContext paramLootContext);
  
  public LootCondition[] getConditions() {
    return this.conditions;
  }
  
  public static abstract class Serializer<T extends LootFunction> {
    private final ResourceLocation lootTableLocation;
    
    private final Class<T> functionClass;
    
    protected Serializer(ResourceLocation location, Class<T> clazz) {
      this.lootTableLocation = location;
      this.functionClass = clazz;
    }
    
    public ResourceLocation getFunctionName() {
      return this.lootTableLocation;
    }
    
    public Class<T> getFunctionClass() {
      return this.functionClass;
    }
    
    public abstract void serialize(JsonObject param1JsonObject, T param1T, JsonSerializationContext param1JsonSerializationContext);
    
    public abstract T deserialize(JsonObject param1JsonObject, JsonDeserializationContext param1JsonDeserializationContext, LootCondition[] param1ArrayOfLootCondition);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\storage\loot\functions\LootFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */