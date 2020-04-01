package net.minecraft.block.state;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockWorldState {
  private final World world;
  
  private final BlockPos pos;
  
  private final boolean forceLoad;
  
  private IBlockState state;
  
  private TileEntity tileEntity;
  
  private boolean tileEntityInitialized;
  
  public BlockWorldState(World worldIn, BlockPos posIn, boolean forceLoadIn) {
    this.world = worldIn;
    this.pos = posIn;
    this.forceLoad = forceLoadIn;
  }
  
  public IBlockState getBlockState() {
    if (this.state == null && (this.forceLoad || this.world.isBlockLoaded(this.pos)))
      this.state = this.world.getBlockState(this.pos); 
    return this.state;
  }
  
  @Nullable
  public TileEntity getTileEntity() {
    if (this.tileEntity == null && !this.tileEntityInitialized) {
      this.tileEntity = this.world.getTileEntity(this.pos);
      this.tileEntityInitialized = true;
    } 
    return this.tileEntity;
  }
  
  public BlockPos getPos() {
    return this.pos;
  }
  
  public static Predicate<BlockWorldState> hasState(final Predicate<IBlockState> predicatesIn) {
    return new Predicate<BlockWorldState>() {
        public boolean apply(@Nullable BlockWorldState p_apply_1_) {
          return (p_apply_1_ != null && predicatesIn.apply(p_apply_1_.getBlockState()));
        }
      };
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\state\BlockWorldState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */