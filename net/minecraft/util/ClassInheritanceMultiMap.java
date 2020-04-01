package net.minecraft.util;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassInheritanceMultiMap<T> extends AbstractSet<T> {
  private static final Set<Class<?>> ALL_KNOWN = Sets.newHashSet();
  
  private final Map<Class<?>, List<T>> map = Maps.newHashMap();
  
  private final Set<Class<?>> knownKeys = Sets.newIdentityHashSet();
  
  private final Class<T> baseClass;
  
  private final List<T> values = Lists.newArrayList();
  
  public ClassInheritanceMultiMap(Class<T> baseClassIn) {
    this.baseClass = baseClassIn;
    this.knownKeys.add(baseClassIn);
    this.map.put(baseClassIn, this.values);
    for (Class<?> oclass : ALL_KNOWN)
      createLookup(oclass); 
  }
  
  protected void createLookup(Class<?> clazz) {
    ALL_KNOWN.add(clazz);
    for (T t : this.values) {
      if (clazz.isAssignableFrom(t.getClass()))
        addForClass(t, clazz); 
    } 
    this.knownKeys.add(clazz);
  }
  
  protected Class<?> initializeClassLookup(Class<?> clazz) {
    if (this.baseClass.isAssignableFrom(clazz)) {
      if (!this.knownKeys.contains(clazz))
        createLookup(clazz); 
      return clazz;
    } 
    throw new IllegalArgumentException("Don't know how to search for " + clazz);
  }
  
  public boolean add(T p_add_1_) {
    for (Class<?> oclass : this.knownKeys) {
      if (oclass.isAssignableFrom(p_add_1_.getClass()))
        addForClass(p_add_1_, oclass); 
    } 
    return true;
  }
  
  private void addForClass(T value, Class<?> parentClass) {
    List<T> list = this.map.get(parentClass);
    if (list == null) {
      this.map.put(parentClass, Lists.newArrayList(new Object[] { value }));
    } else {
      list.add(value);
    } 
  }
  
  public boolean remove(Object p_remove_1_) {
    T t = (T)p_remove_1_;
    boolean flag = false;
    for (Class<?> oclass : this.knownKeys) {
      if (oclass.isAssignableFrom(t.getClass())) {
        List<T> list = this.map.get(oclass);
        if (list != null && list.remove(t))
          flag = true; 
      } 
    } 
    return flag;
  }
  
  public boolean contains(Object p_contains_1_) {
    return Iterators.contains(getByClass(p_contains_1_.getClass()).iterator(), p_contains_1_);
  }
  
  public <S> Iterable<S> getByClass(final Class<S> clazz) {
    return new Iterable<S>() {
        public Iterator<S> iterator() {
          List<T> list = (List<T>)ClassInheritanceMultiMap.this.map.get(ClassInheritanceMultiMap.this.initializeClassLookup(clazz));
          if (list == null)
            return Collections.emptyIterator(); 
          Iterator<T> iterator = list.iterator();
          return (Iterator<S>)Iterators.filter(iterator, clazz);
        }
      };
  }
  
  public Iterator<T> iterator() {
    return this.values.isEmpty() ? Collections.<T>emptyIterator() : (Iterator<T>)Iterators.unmodifiableIterator(this.values.iterator());
  }
  
  public int size() {
    return this.values.size();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\ClassInheritanceMultiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */