package net.minecraft.block.state.pattern;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

public class BlockStateMatcher implements Predicate<IBlockState> {
  public static final Predicate<IBlockState> ANY = new Predicate<IBlockState>() {
      public boolean apply(@Nullable IBlockState p_apply_1_) {
        return true;
      }
    };
  
  private final BlockStateContainer blockstate;
  
  private final Map<IProperty<?>, Predicate<?>> propertyPredicates = Maps.newHashMap();
  
  private BlockStateMatcher(BlockStateContainer blockStateIn) {
    this.blockstate = blockStateIn;
  }
  
  public static BlockStateMatcher forBlock(Block blockIn) {
    return new BlockStateMatcher(blockIn.getBlockState());
  }
  
  public boolean apply(@Nullable IBlockState p_apply_1_) {
    if (p_apply_1_ != null && p_apply_1_.getBlock().equals(this.blockstate.getBlock())) {
      if (this.propertyPredicates.isEmpty())
        return true; 
      for (Map.Entry<IProperty<?>, Predicate<?>> entry : this.propertyPredicates.entrySet()) {
        if (!matches(p_apply_1_, (IProperty<Comparable>)entry.getKey(), (Predicate<Comparable>)entry.getValue()))
          return false; 
      } 
      return true;
    } 
    return false;
  }
  
  protected <T extends Comparable<T>> boolean matches(IBlockState blockState, IProperty<T> property, Predicate<T> predicate) {
    return predicate.apply(blockState.getValue(property));
  }
  
  public <V extends Comparable<V>> BlockStateMatcher where(IProperty<V> property, Predicate<? extends V> is) {
    if (!this.blockstate.getProperties().contains(property))
      throw new IllegalArgumentException(this.blockstate + " cannot support property " + property); 
    this.propertyPredicates.put(property, is);
    return this;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\state\pattern\BlockStateMatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */