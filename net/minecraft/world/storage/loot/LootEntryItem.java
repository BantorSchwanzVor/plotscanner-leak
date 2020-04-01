package net.minecraft.world.storage.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Collection;
import java.util.Random;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.LootFunction;

public class LootEntryItem extends LootEntry {
  protected final Item item;
  
  protected final LootFunction[] functions;
  
  public LootEntryItem(Item itemIn, int weightIn, int qualityIn, LootFunction[] functionsIn, LootCondition[] conditionsIn) {
    super(weightIn, qualityIn, conditionsIn);
    this.item = itemIn;
    this.functions = functionsIn;
  }
  
  public void addLoot(Collection<ItemStack> stacks, Random rand, LootContext context) {
    ItemStack itemstack = new ItemStack(this.item);
    byte b;
    int i;
    LootFunction[] arrayOfLootFunction;
    for (i = (arrayOfLootFunction = this.functions).length, b = 0; b < i; ) {
      LootFunction lootfunction = arrayOfLootFunction[b];
      if (LootConditionManager.testAllConditions(lootfunction.getConditions(), rand, context))
        itemstack = lootfunction.apply(itemstack, rand, context); 
      b++;
    } 
    if (!itemstack.func_190926_b())
      if (itemstack.func_190916_E() < this.item.getItemStackLimit()) {
        stacks.add(itemstack);
      } else {
        int j = itemstack.func_190916_E();
        while (j > 0) {
          ItemStack itemstack1 = itemstack.copy();
          itemstack1.func_190920_e(Math.min(itemstack.getMaxStackSize(), j));
          j -= itemstack1.func_190916_E();
          stacks.add(itemstack1);
        } 
      }  
  }
  
  protected void serialize(JsonObject json, JsonSerializationContext context) {
    if (this.functions != null && this.functions.length > 0)
      json.add("functions", context.serialize(this.functions)); 
    ResourceLocation resourcelocation = (ResourceLocation)Item.REGISTRY.getNameForObject(this.item);
    if (resourcelocation == null)
      throw new IllegalArgumentException("Can't serialize unknown item " + this.item); 
    json.addProperty("name", resourcelocation.toString());
  }
  
  public static LootEntryItem deserialize(JsonObject object, JsonDeserializationContext deserializationContext, int weightIn, int qualityIn, LootCondition[] conditionsIn) {
    LootFunction[] alootfunction;
    Item item = JsonUtils.getItem(object, "name");
    if (object.has("functions")) {
      alootfunction = (LootFunction[])JsonUtils.deserializeClass(object, "functions", deserializationContext, LootFunction[].class);
    } else {
      alootfunction = new LootFunction[0];
    } 
    return new LootEntryItem(item, weightIn, qualityIn, alootfunction, conditionsIn);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\storage\loot\LootEntryItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */