package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRotatedPillar extends Block {
  public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);
  
  protected BlockRotatedPillar(Material materialIn) {
    super(materialIn, materialIn.getMaterialMapColor());
  }
  
  protected BlockRotatedPillar(Material materialIn, MapColor color) {
    super(materialIn, color);
  }
  
  public IBlockState withRotation(IBlockState state, Rotation rot) {
    switch (rot) {
      case CLOCKWISE_90:
      case COUNTERCLOCKWISE_90:
        switch ((EnumFacing.Axis)state.getValue((IProperty)AXIS)) {
          case null:
            return state.withProperty((IProperty)AXIS, (Comparable)EnumFacing.Axis.Z);
          case Z:
            return state.withProperty((IProperty)AXIS, (Comparable)EnumFacing.Axis.X);
        } 
        return state;
    } 
    return state;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    EnumFacing.Axis enumfacing$axis = EnumFacing.Axis.Y;
    int i = meta & 0xC;
    if (i == 4) {
      enumfacing$axis = EnumFacing.Axis.X;
    } else if (i == 8) {
      enumfacing$axis = EnumFacing.Axis.Z;
    } 
    return getDefaultState().withProperty((IProperty)AXIS, (Comparable)enumfacing$axis);
  }
  
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    EnumFacing.Axis enumfacing$axis = (EnumFacing.Axis)state.getValue((IProperty)AXIS);
    if (enumfacing$axis == EnumFacing.Axis.X) {
      i |= 0x4;
    } else if (enumfacing$axis == EnumFacing.Axis.Z) {
      i |= 0x8;
    } 
    return i;
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)AXIS });
  }
  
  protected ItemStack getSilkTouchDrop(IBlockState state) {
    return new ItemStack(Item.getItemFromBlock(this));
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty((IProperty)AXIS, (Comparable)facing.getAxis());
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockRotatedPillar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */