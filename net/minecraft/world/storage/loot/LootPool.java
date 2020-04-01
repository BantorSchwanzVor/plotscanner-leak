package net.minecraft.world.storage.loot;

import com.google.common.collect.Lists;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import org.apache.commons.lang3.ArrayUtils;

public class LootPool {
  private final LootEntry[] lootEntries;
  
  private final LootCondition[] poolConditions;
  
  private final RandomValueRange rolls;
  
  private final RandomValueRange bonusRolls;
  
  public LootPool(LootEntry[] lootEntriesIn, LootCondition[] poolConditionsIn, RandomValueRange rollsIn, RandomValueRange bonusRollsIn) {
    this.lootEntries = lootEntriesIn;
    this.poolConditions = poolConditionsIn;
    this.rolls = rollsIn;
    this.bonusRolls = bonusRollsIn;
  }
  
  protected void createLootRoll(Collection<ItemStack> stacks, Random rand, LootContext context) {
    List<LootEntry> list = Lists.newArrayList();
    int i = 0;
    byte b;
    int j;
    LootEntry[] arrayOfLootEntry;
    for (j = (arrayOfLootEntry = this.lootEntries).length, b = 0; b < j; ) {
      LootEntry lootentry = arrayOfLootEntry[b];
      if (LootConditionManager.testAllConditions(lootentry.conditions, rand, context)) {
        int k = lootentry.getEffectiveWeight(context.getLuck());
        if (k > 0) {
          list.add(lootentry);
          i += k;
        } 
      } 
      b++;
    } 
    if (i != 0 && !list.isEmpty()) {
      int k = rand.nextInt(i);
      for (LootEntry lootentry1 : list) {
        k -= lootentry1.getEffectiveWeight(context.getLuck());
        if (k < 0) {
          lootentry1.addLoot(stacks, rand, context);
          return;
        } 
      } 
    } 
  }
  
  public void generateLoot(Collection<ItemStack> stacks, Random rand, LootContext context) {
    if (LootConditionManager.testAllConditions(this.poolConditions, rand, context)) {
      int i = this.rolls.generateInt(rand) + MathHelper.floor(this.bonusRolls.generateFloat(rand) * context.getLuck());
      for (int j = 0; j < i; j++)
        createLootRoll(stacks, rand, context); 
    } 
  }
  
  public static class Serializer implements JsonDeserializer<LootPool>, JsonSerializer<LootPool> {
    public LootPool deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
      JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "loot pool");
      LootEntry[] alootentry = (LootEntry[])JsonUtils.deserializeClass(jsonobject, "entries", p_deserialize_3_, LootEntry[].class);
      LootCondition[] alootcondition = (LootCondition[])JsonUtils.deserializeClass(jsonobject, "conditions", new LootCondition[0], p_deserialize_3_, LootCondition[].class);
      RandomValueRange randomvaluerange = (RandomValueRange)JsonUtils.deserializeClass(jsonobject, "rolls", p_deserialize_3_, RandomValueRange.class);
      RandomValueRange randomvaluerange1 = (RandomValueRange)JsonUtils.deserializeClass(jsonobject, "bonus_rolls", new RandomValueRange(0.0F, 0.0F), p_deserialize_3_, RandomValueRange.class);
      return new LootPool(alootentry, alootcondition, randomvaluerange, randomvaluerange1);
    }
    
    public JsonElement serialize(LootPool p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
      JsonObject jsonobject = new JsonObject();
      jsonobject.add("entries", p_serialize_3_.serialize(p_serialize_1_.lootEntries));
      jsonobject.add("rolls", p_serialize_3_.serialize(p_serialize_1_.rolls));
      if (p_serialize_1_.bonusRolls.getMin() != 0.0F && p_serialize_1_.bonusRolls.getMax() != 0.0F)
        jsonobject.add("bonus_rolls", p_serialize_3_.serialize(p_serialize_1_.bonusRolls)); 
      if (!ArrayUtils.isEmpty((Object[])p_serialize_1_.poolConditions))
        jsonobject.add("conditions", p_serialize_3_.serialize(p_serialize_1_.poolConditions)); 
      return (JsonElement)jsonobject;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\storage\loot\LootPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */