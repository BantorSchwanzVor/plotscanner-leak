package net.minecraft.block;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockStandingSign extends BlockSign {
  public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);
  
  public BlockStandingSign() {
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)ROTATION, Integer.valueOf(0)));
  }
  
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
    if (!worldIn.getBlockState(pos.down()).getMaterial().isSolid()) {
      dropBlockAsItem(worldIn, pos, state, 0);
      worldIn.setBlockToAir(pos);
    } 
    super.neighborChanged(state, worldIn, pos, blockIn, p_189540_5_);
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)ROTATION, Integer.valueOf(meta));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((Integer)state.getValue((IProperty)ROTATION)).intValue();
  }
  
  public IBlockState withRotation(IBlockState state, Rotation rot) {
    return state.withProperty((IProperty)ROTATION, Integer.valueOf(rot.rotate(((Integer)state.getValue((IProperty)ROTATION)).intValue(), 16)));
  }
  
  public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
    return state.withProperty((IProperty)ROTATION, Integer.valueOf(mirrorIn.mirrorRotation(((Integer)state.getValue((IProperty)ROTATION)).intValue(), 16)));
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)ROTATION });
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockStandingSign.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */