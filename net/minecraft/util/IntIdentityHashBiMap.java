package net.minecraft.util;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import java.util.Arrays;
import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.util.math.MathHelper;

public class IntIdentityHashBiMap<K> implements IObjectIntIterable<K> {
  private static final Object EMPTY = null;
  
  private K[] values;
  
  private int[] intKeys;
  
  private K[] byId;
  
  private int nextFreeIndex;
  
  private int mapSize;
  
  public IntIdentityHashBiMap(int initialCapacity) {
    initialCapacity = (int)(initialCapacity / 0.8F);
    this.values = (K[])new Object[initialCapacity];
    this.intKeys = new int[initialCapacity];
    this.byId = (K[])new Object[initialCapacity];
  }
  
  public int getId(@Nullable K p_186815_1_) {
    return getValue(getIndex(p_186815_1_, hashObject(p_186815_1_)));
  }
  
  @Nullable
  public K get(int idIn) {
    return (idIn >= 0 && idIn < this.byId.length) ? this.byId[idIn] : null;
  }
  
  private int getValue(int p_186805_1_) {
    return (p_186805_1_ == -1) ? -1 : this.intKeys[p_186805_1_];
  }
  
  public int add(K objectIn) {
    int i = nextId();
    put(objectIn, i);
    return i;
  }
  
  private int nextId() {
    while (this.nextFreeIndex < this.byId.length && this.byId[this.nextFreeIndex] != null)
      this.nextFreeIndex++; 
    return this.nextFreeIndex;
  }
  
  private void grow(int capacity) {
    K[] arrayOfK = this.values;
    int[] aint = this.intKeys;
    this.values = (K[])new Object[capacity];
    this.intKeys = new int[capacity];
    this.byId = (K[])new Object[capacity];
    this.nextFreeIndex = 0;
    this.mapSize = 0;
    for (int i = 0; i < arrayOfK.length; i++) {
      if (arrayOfK[i] != null)
        put(arrayOfK[i], aint[i]); 
    } 
  }
  
  public void put(K objectIn, int intKey) {
    int i = Math.max(intKey, this.mapSize + 1);
    if (i >= this.values.length * 0.8F) {
      int j;
      for (j = this.values.length << 1; j < intKey; j <<= 1);
      grow(j);
    } 
    int k = findEmpty(hashObject(objectIn));
    this.values[k] = objectIn;
    this.intKeys[k] = intKey;
    this.byId[intKey] = objectIn;
    this.mapSize++;
    if (intKey == this.nextFreeIndex)
      this.nextFreeIndex++; 
  }
  
  private int hashObject(@Nullable K obectIn) {
    return (MathHelper.hash(System.identityHashCode(obectIn)) & Integer.MAX_VALUE) % this.values.length;
  }
  
  private int getIndex(@Nullable K objectIn, int p_186816_2_) {
    for (int i = p_186816_2_; i < this.values.length; i++) {
      if (this.values[i] == objectIn)
        return i; 
      if (this.values[i] == EMPTY)
        return -1; 
    } 
    for (int j = 0; j < p_186816_2_; j++) {
      if (this.values[j] == objectIn)
        return j; 
      if (this.values[j] == EMPTY)
        return -1; 
    } 
    return -1;
  }
  
  private int findEmpty(int p_186806_1_) {
    for (int i = p_186806_1_; i < this.values.length; i++) {
      if (this.values[i] == EMPTY)
        return i; 
    } 
    for (int j = 0; j < p_186806_1_; j++) {
      if (this.values[j] == EMPTY)
        return j; 
    } 
    throw new RuntimeException("Overflowed :(");
  }
  
  public Iterator<K> iterator() {
    return (Iterator<K>)Iterators.filter((Iterator)Iterators.forArray((Object[])this.byId), Predicates.notNull());
  }
  
  public void clear() {
    Arrays.fill((Object[])this.values, (Object)null);
    Arrays.fill((Object[])this.byId, (Object)null);
    this.nextFreeIndex = 0;
    this.mapSize = 0;
  }
  
  public int size() {
    return this.mapSize;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\IntIdentityHashBiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */