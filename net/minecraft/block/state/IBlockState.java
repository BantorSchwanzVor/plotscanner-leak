package net.minecraft.block.state;

import com.google.common.collect.ImmutableMap;
import java.util.Collection;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;

public interface IBlockState extends IBlockBehaviors, IBlockProperties {
  Collection<IProperty<?>> getPropertyNames();
  
  <T extends Comparable<T>> T getValue(IProperty<T> paramIProperty);
  
  <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> paramIProperty, V paramV);
  
  <T extends Comparable<T>> IBlockState cycleProperty(IProperty<T> paramIProperty);
  
  ImmutableMap<IProperty<?>, Comparable<?>> getProperties();
  
  Block getBlock();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\state\IBlockState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */