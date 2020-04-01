package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockFrostedIce extends BlockIce {
  public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 3);
  
  public BlockFrostedIce() {
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)AGE, Integer.valueOf(0)));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((Integer)state.getValue((IProperty)AGE)).intValue();
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)AGE, Integer.valueOf(MathHelper.clamp(meta, 0, 3)));
  }
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if ((rand.nextInt(3) == 0 || countNeighbors(worldIn, pos) < 4) && worldIn.getLightFromNeighbors(pos) > 11 - ((Integer)state.getValue((IProperty)AGE)).intValue() - state.getLightOpacity()) {
      slightlyMelt(worldIn, pos, state, rand, true);
    } else {
      worldIn.scheduleUpdate(pos, this, MathHelper.getInt(rand, 20, 40));
    } 
  }
  
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
    if (blockIn == this) {
      int i = countNeighbors(worldIn, pos);
      if (i < 2)
        turnIntoWater(worldIn, pos); 
    } 
  }
  
  private int countNeighbors(World p_185680_1_, BlockPos p_185680_2_) {
    int i = 0;
    byte b;
    int j;
    EnumFacing[] arrayOfEnumFacing;
    for (j = (arrayOfEnumFacing = EnumFacing.values()).length, b = 0; b < j; ) {
      EnumFacing enumfacing = arrayOfEnumFacing[b];
      if (p_185680_1_.getBlockState(p_185680_2_.offset(enumfacing)).getBlock() == this) {
        i++;
        if (i >= 4)
          return i; 
      } 
      b++;
    } 
    return i;
  }
  
  protected void slightlyMelt(World p_185681_1_, BlockPos p_185681_2_, IBlockState p_185681_3_, Random p_185681_4_, boolean p_185681_5_) {
    int i = ((Integer)p_185681_3_.getValue((IProperty)AGE)).intValue();
    if (i < 3) {
      p_185681_1_.setBlockState(p_185681_2_, p_185681_3_.withProperty((IProperty)AGE, Integer.valueOf(i + 1)), 2);
      p_185681_1_.scheduleUpdate(p_185681_2_, this, MathHelper.getInt(p_185681_4_, 20, 40));
    } else {
      turnIntoWater(p_185681_1_, p_185681_2_);
      if (p_185681_5_) {
        byte b;
        int j;
        EnumFacing[] arrayOfEnumFacing;
        for (j = (arrayOfEnumFacing = EnumFacing.values()).length, b = 0; b < j; ) {
          EnumFacing enumfacing = arrayOfEnumFacing[b];
          BlockPos blockpos = p_185681_2_.offset(enumfacing);
          IBlockState iblockstate = p_185681_1_.getBlockState(blockpos);
          if (iblockstate.getBlock() == this)
            slightlyMelt(p_185681_1_, blockpos, iblockstate, p_185681_4_, false); 
          b++;
        } 
      } 
    } 
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)AGE });
  }
  
  public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
    return ItemStack.field_190927_a;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockFrostedIce.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */