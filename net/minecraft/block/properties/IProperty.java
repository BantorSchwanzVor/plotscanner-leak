package net.minecraft.block.properties;

import com.google.common.base.Optional;
import java.util.Collection;

public interface IProperty<T extends Comparable<T>> {
  String getName();
  
  Collection<T> getAllowedValues();
  
  Class<T> getValueClass();
  
  Optional<T> parseValue(String paramString);
  
  String getName(T paramT);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\properties\IProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */