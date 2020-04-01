package net.minecraft.block.state.pattern;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class BlockMatcher implements Predicate<IBlockState> {
  private final Block block;
  
  private BlockMatcher(Block blockType) {
    this.block = blockType;
  }
  
  public static BlockMatcher forBlock(Block blockType) {
    return new BlockMatcher(blockType);
  }
  
  public boolean apply(@Nullable IBlockState p_apply_1_) {
    return (p_apply_1_ != null && p_apply_1_.getBlock() == this.block);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\state\pattern\BlockMatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */