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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LootTable {
  private static final Logger LOGGER = LogManager.getLogger();
  
  public static final LootTable EMPTY_LOOT_TABLE = new LootTable(new LootPool[0]);
  
  private final LootPool[] pools;
  
  public LootTable(LootPool[] poolsIn) {
    this.pools = poolsIn;
  }
  
  public List<ItemStack> generateLootForPools(Random rand, LootContext context) {
    List<ItemStack> list = Lists.newArrayList();
    if (context.addLootTable(this)) {
      byte b;
      int i;
      LootPool[] arrayOfLootPool;
      for (i = (arrayOfLootPool = this.pools).length, b = 0; b < i; ) {
        LootPool lootpool = arrayOfLootPool[b];
        lootpool.generateLoot(list, rand, context);
        b++;
      } 
      context.removeLootTable(this);
    } else {
      LOGGER.warn("Detected infinite loop in loot tables");
    } 
    return list;
  }
  
  public void fillInventory(IInventory inventory, Random rand, LootContext context) {
    List<ItemStack> list = generateLootForPools(rand, context);
    List<Integer> list1 = getEmptySlotsRandomized(inventory, rand);
    shuffleItems(list, list1.size(), rand);
    for (ItemStack itemstack : list) {
      if (list1.isEmpty()) {
        LOGGER.warn("Tried to over-fill a container");
        return;
      } 
      if (itemstack.func_190926_b()) {
        inventory.setInventorySlotContents(((Integer)list1.remove(list1.size() - 1)).intValue(), ItemStack.field_190927_a);
        continue;
      } 
      inventory.setInventorySlotContents(((Integer)list1.remove(list1.size() - 1)).intValue(), itemstack);
    } 
  }
  
  private void shuffleItems(List<ItemStack> stacks, int p_186463_2_, Random rand) {
    List<ItemStack> list = Lists.newArrayList();
    Iterator<ItemStack> iterator = stacks.iterator();
    while (iterator.hasNext()) {
      ItemStack itemstack = iterator.next();
      if (itemstack.func_190926_b()) {
        iterator.remove();
        continue;
      } 
      if (itemstack.func_190916_E() > 1) {
        list.add(itemstack);
        iterator.remove();
      } 
    } 
    p_186463_2_ -= stacks.size();
    while (p_186463_2_ > 0 && !list.isEmpty()) {
      ItemStack itemstack2 = list.remove(MathHelper.getInt(rand, 0, list.size() - 1));
      int i = MathHelper.getInt(rand, 1, itemstack2.func_190916_E() / 2);
      ItemStack itemstack1 = itemstack2.splitStack(i);
      if (itemstack2.func_190916_E() > 1 && rand.nextBoolean()) {
        list.add(itemstack2);
      } else {
        stacks.add(itemstack2);
      } 
      if (itemstack1.func_190916_E() > 1 && rand.nextBoolean()) {
        list.add(itemstack1);
        continue;
      } 
      stacks.add(itemstack1);
    } 
    stacks.addAll(list);
    Collections.shuffle(stacks, rand);
  }
  
  private List<Integer> getEmptySlotsRandomized(IInventory inventory, Random rand) {
    List<Integer> list = Lists.newArrayList();
    for (int i = 0; i < inventory.getSizeInventory(); i++) {
      if (inventory.getStackInSlot(i).func_190926_b())
        list.add(Integer.valueOf(i)); 
    } 
    Collections.shuffle(list, rand);
    return list;
  }
  
  public static class Serializer implements JsonDeserializer<LootTable>, JsonSerializer<LootTable> {
    public LootTable deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
      JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "loot table");
      LootPool[] alootpool = (LootPool[])JsonUtils.deserializeClass(jsonobject, "pools", new LootPool[0], p_deserialize_3_, LootPool[].class);
      return new LootTable(alootpool);
    }
    
    public JsonElement serialize(LootTable p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
      JsonObject jsonobject = new JsonObject();
      jsonobject.add("pools", p_serialize_3_.serialize(p_serialize_1_.pools));
      return (JsonElement)jsonobject;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\storage\loot\LootTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */