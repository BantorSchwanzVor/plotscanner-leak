package net.minecraft.client.renderer.block.model.multipart;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

public interface ICondition {
  public static final ICondition TRUE = new ICondition() {
      public Predicate<IBlockState> getPredicate(BlockStateContainer blockState) {
        return new Predicate<IBlockState>() {
            public boolean apply(@Nullable IBlockState p_apply_1_) {
              return true;
            }
          };
      }
    };
  
  public static final ICondition FALSE = new ICondition() {
      public Predicate<IBlockState> getPredicate(BlockStateContainer blockState) {
        return new Predicate<IBlockState>() {
            public boolean apply(@Nullable IBlockState p_apply_1_) {
              return false;
            }
          };
      }
    };
  
  Predicate<IBlockState> getPredicate(BlockStateContainer paramBlockStateContainer);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\block\model\multipart\ICondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */