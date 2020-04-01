package net.minecraft.util;

import javax.annotation.Nullable;

public class IntHashMap<V> {
  private transient Entry<V>[] slots = (Entry<V>[])new Entry[16];
  
  private transient int count;
  
  private int threshold = 12;
  
  private final float growFactor = 0.75F;
  
  private static int computeHash(int integer) {
    integer = integer ^ integer >>> 20 ^ integer >>> 12;
    return integer ^ integer >>> 7 ^ integer >>> 4;
  }
  
  private static int getSlotIndex(int hash, int slotCount) {
    return hash & slotCount - 1;
  }
  
  @Nullable
  public V lookup(int hashEntry) {
    int i = computeHash(hashEntry);
    for (Entry<V> entry = this.slots[getSlotIndex(i, this.slots.length)]; entry != null; entry = entry.nextEntry) {
      if (entry.hashEntry == hashEntry)
        return entry.valueEntry; 
    } 
    return null;
  }
  
  public boolean containsItem(int hashEntry) {
    return (lookupEntry(hashEntry) != null);
  }
  
  @Nullable
  final Entry<V> lookupEntry(int hashEntry) {
    int i = computeHash(hashEntry);
    for (Entry<V> entry = this.slots[getSlotIndex(i, this.slots.length)]; entry != null; entry = entry.nextEntry) {
      if (entry.hashEntry == hashEntry)
        return entry; 
    } 
    return null;
  }
  
  public void addKey(int hashEntry, V valueEntry) {
    int i = computeHash(hashEntry);
    int j = getSlotIndex(i, this.slots.length);
    for (Entry<V> entry = this.slots[j]; entry != null; entry = entry.nextEntry) {
      if (entry.hashEntry == hashEntry) {
        entry.valueEntry = valueEntry;
        return;
      } 
    } 
    insert(i, hashEntry, valueEntry, j);
  }
  
  private void grow(int p_76047_1_) {
    Entry<V>[] arrayOfEntry = this.slots;
    int i = arrayOfEntry.length;
    if (i == 1073741824) {
      this.threshold = Integer.MAX_VALUE;
    } else {
      Entry[] entry1 = new Entry[p_76047_1_];
      copyTo((Entry<V>[])entry1);
      this.slots = (Entry<V>[])entry1;
      this.threshold = (int)(p_76047_1_ * 0.75F);
    } 
  }
  
  private void copyTo(Entry[] p_76048_1_) {
    Entry<V>[] arrayOfEntry = this.slots;
    int i = p_76048_1_.length;
    for (int j = 0; j < arrayOfEntry.length; j++) {
      Entry<V> entry1 = arrayOfEntry[j];
      if (entry1 != null) {
        Entry<V> entry2;
        arrayOfEntry[j] = null;
        do {
          entry2 = entry1.nextEntry;
          int k = getSlotIndex(entry1.slotHash, i);
          entry1.nextEntry = p_76048_1_[k];
          p_76048_1_[k] = entry1;
          entry1 = entry2;
        } while (entry2 != null);
      } 
    } 
  }
  
  @Nullable
  public V removeObject(int p_76049_1_) {
    Entry<V> entry = removeEntry(p_76049_1_);
    return (entry == null) ? null : entry.valueEntry;
  }
  
  @Nullable
  final Entry<V> removeEntry(int p_76036_1_) {
    int i = computeHash(p_76036_1_);
    int j = getSlotIndex(i, this.slots.length);
    Entry<V> entry = this.slots[j];
    Entry<V> entry1;
    for (entry1 = entry; entry1 != null; entry1 = entry2) {
      Entry<V> entry2 = entry1.nextEntry;
      if (entry1.hashEntry == p_76036_1_) {
        this.count--;
        if (entry == entry1) {
          this.slots[j] = entry2;
        } else {
          entry.nextEntry = entry2;
        } 
        return entry1;
      } 
      entry = entry1;
    } 
    return entry1;
  }
  
  public void clearMap() {
    Entry<V>[] arrayOfEntry = this.slots;
    for (int i = 0; i < arrayOfEntry.length; i++)
      arrayOfEntry[i] = null; 
    this.count = 0;
  }
  
  private void insert(int p_76040_1_, int p_76040_2_, V p_76040_3_, int p_76040_4_) {
    Entry<V> entry = this.slots[p_76040_4_];
    this.slots[p_76040_4_] = new Entry<>(p_76040_1_, p_76040_2_, p_76040_3_, entry);
    if (this.count++ >= this.threshold)
      grow(2 * this.slots.length); 
  }
  
  static class Entry<V> {
    final int hashEntry;
    
    V valueEntry;
    
    Entry<V> nextEntry;
    
    final int slotHash;
    
    Entry(int p_i1552_1_, int p_i1552_2_, V p_i1552_3_, Entry<V> p_i1552_4_) {
      this.valueEntry = p_i1552_3_;
      this.nextEntry = p_i1552_4_;
      this.hashEntry = p_i1552_2_;
      this.slotHash = p_i1552_1_;
    }
    
    public final int getHash() {
      return this.hashEntry;
    }
    
    public final V getValue() {
      return this.valueEntry;
    }
    
    public final boolean equals(Object p_equals_1_) {
      if (!(p_equals_1_ instanceof Entry))
        return false; 
      Entry<V> entry = (Entry<V>)p_equals_1_;
      if (this.hashEntry == entry.hashEntry) {
        Object object = getValue();
        Object object1 = entry.getValue();
        if (object == object1 || (object != null && object.equals(object1)))
          return true; 
      } 
      return false;
    }
    
    public final int hashCode() {
      return IntHashMap.computeHash(this.hashEntry);
    }
    
    public final String toString() {
      return String.valueOf(getHash()) + "=" + getValue();
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\IntHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */