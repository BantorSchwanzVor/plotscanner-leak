package net.minecraft.block;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRail extends BlockRailBase {
  public static final PropertyEnum<BlockRailBase.EnumRailDirection> SHAPE = PropertyEnum.create("shape", BlockRailBase.EnumRailDirection.class);
  
  protected BlockRail() {
    super(false);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH));
  }
  
  protected void updateState(IBlockState p_189541_1_, World p_189541_2_, BlockPos p_189541_3_, Block p_189541_4_) {
    if (p_189541_4_.getDefaultState().canProvidePower() && (new BlockRailBase.Rail(this, p_189541_2_, p_189541_3_, p_189541_1_)).countAdjacentRails() == 3)
      updateDir(p_189541_2_, p_189541_3_, p_189541_1_, false); 
  }
  
  public IProperty<BlockRailBase.EnumRailDirection> getShapeProperty() {
    return (IProperty<BlockRailBase.EnumRailDirection>)SHAPE;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.byMetadata(meta));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((BlockRailBase.EnumRailDirection)state.getValue((IProperty)SHAPE)).getMetadata();
  }
  
  public IBlockState withRotation(IBlockState state, Rotation rot) {
    switch (rot) {
      case null:
        switch ((BlockRailBase.EnumRailDirection)state.getValue((IProperty)SHAPE)) {
          case null:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);
          case ASCENDING_WEST:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);
          case ASCENDING_NORTH:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);
          case ASCENDING_SOUTH:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);
          case SOUTH_EAST:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);
          case SOUTH_WEST:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);
          case NORTH_WEST:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);
          case NORTH_EAST:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);
        } 
      case COUNTERCLOCKWISE_90:
        switch ((BlockRailBase.EnumRailDirection)state.getValue((IProperty)SHAPE)) {
          case null:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);
          case ASCENDING_WEST:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);
          case ASCENDING_NORTH:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);
          case ASCENDING_SOUTH:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);
          case SOUTH_EAST:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);
          case SOUTH_WEST:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);
          case NORTH_WEST:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);
          case NORTH_EAST:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);
          case NORTH_SOUTH:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.EAST_WEST);
          case EAST_WEST:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH);
        } 
      case CLOCKWISE_90:
        switch ((BlockRailBase.EnumRailDirection)state.getValue((IProperty)SHAPE)) {
          case null:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);
          case ASCENDING_WEST:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);
          case ASCENDING_NORTH:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);
          case ASCENDING_SOUTH:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);
          case SOUTH_EAST:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);
          case SOUTH_WEST:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);
          case NORTH_WEST:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);
          case NORTH_EAST:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);
          case NORTH_SOUTH:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.EAST_WEST);
          case EAST_WEST:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH);
        } 
        break;
    } 
    return state;
  }
  
  public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
    BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (BlockRailBase.EnumRailDirection)state.getValue((IProperty)SHAPE);
    switch (mirrorIn) {
      case LEFT_RIGHT:
        switch (blockrailbase$enumraildirection) {
          case ASCENDING_NORTH:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);
          case ASCENDING_SOUTH:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);
          case SOUTH_EAST:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);
          case SOUTH_WEST:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);
          case NORTH_WEST:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);
          case NORTH_EAST:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);
        } 
        return super.withMirror(state, mirrorIn);
      case null:
        switch (blockrailbase$enumraildirection) {
          case null:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);
          case ASCENDING_WEST:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);
          default:
            break;
          case SOUTH_EAST:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);
          case SOUTH_WEST:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);
          case NORTH_WEST:
            return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);
          case NORTH_EAST:
            break;
        } 
        return state.withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);
    } 
    return super.withMirror(state, mirrorIn);
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)SHAPE });
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockRail.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */