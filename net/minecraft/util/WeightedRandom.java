package net.minecraft.util;

import java.util.List;
import java.util.Random;

public class WeightedRandom {
  public static int getTotalWeight(List<? extends Item> collection) {
    int i = 0;
    int j = 0;
    for (int k = collection.size(); j < k; j++) {
      Item weightedrandom$item = collection.get(j);
      i += weightedrandom$item.itemWeight;
    } 
    return i;
  }
  
  public static <T extends Item> T getRandomItem(Random random, List<T> collection, int totalWeight) {
    if (totalWeight <= 0)
      throw new IllegalArgumentException(); 
    int i = random.nextInt(totalWeight);
    return getRandomItem(collection, i);
  }
  
  public static <T extends Item> T getRandomItem(List<T> collection, int weight) {
    int i = 0;
    for (int j = collection.size(); i < j; i++) {
      Item item = (Item)collection.get(i);
      weight -= item.itemWeight;
      if (weight < 0)
        return (T)item; 
    } 
    return null;
  }
  
  public static <T extends Item> T getRandomItem(Random random, List<T> collection) {
    return getRandomItem(random, collection, getTotalWeight(collection));
  }
  
  public static class Item {
    protected int itemWeight;
    
    public Item(int itemWeightIn) {
      this.itemWeight = itemWeightIn;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\WeightedRandom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */