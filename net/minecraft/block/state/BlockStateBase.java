package net.minecraft.block.state;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.ResourceLocation;

public abstract class BlockStateBase implements IBlockState {
  public BlockStateBase() {
    this.blockId = -1;
    this.blockStateId = -1;
    this.metadata = -1;
    this.blockLocation = null;
  }
  
  private static final Joiner COMMA_JOINER = Joiner.on(',');
  
  private static final Function<Map.Entry<IProperty<?>, Comparable<?>>, String> MAP_ENTRY_TO_STRING = new Function<Map.Entry<IProperty<?>, Comparable<?>>, String>() {
      @Nullable
      public String apply(@Nullable Map.Entry<IProperty<?>, Comparable<?>> p_apply_1_) {
        if (p_apply_1_ == null)
          return "<NULL>"; 
        IProperty<?> iproperty = p_apply_1_.getKey();
        return String.valueOf(iproperty.getName()) + "=" + getPropertyName(iproperty, p_apply_1_.getValue());
      }
      
      private <T extends Comparable<T>> String getPropertyName(IProperty<T> property, Comparable<?> entry) {
        return property.getName(entry);
      }
    };
  
  private int blockId;
  
  private int blockStateId;
  
  private int metadata;
  
  private ResourceLocation blockLocation;
  
  public int getBlockId() {
    if (this.blockId < 0)
      this.blockId = Block.getIdFromBlock(getBlock()); 
    return this.blockId;
  }
  
  public int getBlockStateId() {
    if (this.blockStateId < 0)
      this.blockStateId = Block.getStateId(this); 
    return this.blockStateId;
  }
  
  public int getMetadata() {
    if (this.metadata < 0)
      this.metadata = getBlock().getMetaFromState(this); 
    return this.metadata;
  }
  
  public ResourceLocation getBlockLocation() {
    if (this.blockLocation == null)
      this.blockLocation = (ResourceLocation)Block.REGISTRY.getNameForObject(getBlock()); 
    return this.blockLocation;
  }
  
  public ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> getPropertyValueTable() {
    return null;
  }
  
  public <T extends Comparable<T>> IBlockState cycleProperty(IProperty<T> property) {
    return withProperty(property, cyclePropertyValue(property.getAllowedValues(), getValue(property)));
  }
  
  protected static <T> T cyclePropertyValue(Collection<T> values, T currentValue) {
    Iterator<T> iterator = values.iterator();
    while (iterator.hasNext()) {
      if (iterator.next().equals(currentValue)) {
        if (iterator.hasNext())
          return iterator.next(); 
        return values.iterator().next();
      } 
    } 
    return iterator.next();
  }
  
  public String toString() {
    StringBuilder stringbuilder = new StringBuilder();
    stringbuilder.append(Block.REGISTRY.getNameForObject(getBlock()));
    if (!getProperties().isEmpty()) {
      stringbuilder.append("[");
      COMMA_JOINER.appendTo(stringbuilder, Iterables.transform((Iterable)getProperties().entrySet(), MAP_ENTRY_TO_STRING));
      stringbuilder.append("]");
    } 
    return stringbuilder.toString();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\state\BlockStateBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */