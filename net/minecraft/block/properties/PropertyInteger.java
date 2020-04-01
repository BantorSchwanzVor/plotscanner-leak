package net.minecraft.block.properties;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;

public class PropertyInteger extends PropertyHelper<Integer> {
  private final ImmutableSet<Integer> allowedValues;
  
  protected PropertyInteger(String name, int min, int max) {
    super(name, Integer.class);
    if (min < 0)
      throw new IllegalArgumentException("Min value of " + name + " must be 0 or greater"); 
    if (max <= min)
      throw new IllegalArgumentException("Max value of " + name + " must be greater than min (" + min + ")"); 
    Set<Integer> set = Sets.newHashSet();
    for (int i = min; i <= max; i++)
      set.add(Integer.valueOf(i)); 
    this.allowedValues = ImmutableSet.copyOf(set);
  }
  
  public Collection<Integer> getAllowedValues() {
    return (Collection<Integer>)this.allowedValues;
  }
  
  public boolean equals(Object p_equals_1_) {
    if (this == p_equals_1_)
      return true; 
    if (p_equals_1_ instanceof PropertyInteger && super.equals(p_equals_1_)) {
      PropertyInteger propertyinteger = (PropertyInteger)p_equals_1_;
      return this.allowedValues.equals(propertyinteger.allowedValues);
    } 
    return false;
  }
  
  public int hashCode() {
    return 31 * super.hashCode() + this.allowedValues.hashCode();
  }
  
  public static PropertyInteger create(String name, int min, int max) {
    return new PropertyInteger(name, min, max);
  }
  
  public Optional<Integer> parseValue(String value) {
    try {
      Integer integer = Integer.valueOf(value);
      return this.allowedValues.contains(integer) ? Optional.of(integer) : Optional.absent();
    } catch (NumberFormatException var3) {
      return Optional.absent();
    } 
  }
  
  public String getName(Integer value) {
    return value.toString();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\properties\PropertyInteger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */