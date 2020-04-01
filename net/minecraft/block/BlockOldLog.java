package net.minecraft.block;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockOldLog extends BlockLog {
  public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class, new Predicate<BlockPlanks.EnumType>() {
        public boolean apply(@Nullable BlockPlanks.EnumType p_apply_1_) {
          return (p_apply_1_.getMetadata() < 4);
        }
      });
  
  public BlockOldLog() {
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)VARIANT, BlockPlanks.EnumType.OAK).withProperty((IProperty)LOG_AXIS, BlockLog.EnumAxis.Y));
  }
  
  public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_) {
    BlockPlanks.EnumType blockplanks$enumtype = (BlockPlanks.EnumType)state.getValue((IProperty)VARIANT);
    switch ((BlockLog.EnumAxis)state.getValue((IProperty)LOG_AXIS)) {
      default:
        switch (blockplanks$enumtype) {
          default:
            return BlockPlanks.EnumType.SPRUCE.getMapColor();
          case SPRUCE:
            return BlockPlanks.EnumType.DARK_OAK.getMapColor();
          case BIRCH:
            return MapColor.QUARTZ;
          case JUNGLE:
            break;
        } 
        return BlockPlanks.EnumType.SPRUCE.getMapColor();
      case Y:
        break;
    } 
    return blockplanks$enumtype.getMapColor();
  }
  
  public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
    tab.add(new ItemStack(this, 1, BlockPlanks.EnumType.OAK.getMetadata()));
    tab.add(new ItemStack(this, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()));
    tab.add(new ItemStack(this, 1, BlockPlanks.EnumType.BIRCH.getMetadata()));
    tab.add(new ItemStack(this, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()));
  }
  
  public IBlockState getStateFromMeta(int meta) {
    IBlockState iblockstate = getDefaultState().withProperty((IProperty)VARIANT, BlockPlanks.EnumType.byMetadata((meta & 0x3) % 4));
    switch (meta & 0xC) {
      case 0:
        iblockstate = iblockstate.withProperty((IProperty)LOG_AXIS, BlockLog.EnumAxis.Y);
        return iblockstate;
      case 4:
        iblockstate = iblockstate.withProperty((IProperty)LOG_AXIS, BlockLog.EnumAxis.X);
        return iblockstate;
      case 8:
        iblockstate = iblockstate.withProperty((IProperty)LOG_AXIS, BlockLog.EnumAxis.Z);
        return iblockstate;
    } 
    iblockstate = iblockstate.withProperty((IProperty)LOG_AXIS, BlockLog.EnumAxis.NONE);
    return iblockstate;
  }
  
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    i |= ((BlockPlanks.EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
    switch ((BlockLog.EnumAxis)state.getValue((IProperty)LOG_AXIS)) {
      case X:
        i |= 0x4;
        break;
      case Z:
        i |= 0x8;
        break;
      case null:
        i |= 0xC;
        break;
    } 
    return i;
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)VARIANT, (IProperty)LOG_AXIS });
  }
  
  protected ItemStack getSilkTouchDrop(IBlockState state) {
    return new ItemStack(Item.getItemFromBlock(this), 1, ((BlockPlanks.EnumType)state.getValue((IProperty)VARIANT)).getMetadata());
  }
  
  public int damageDropped(IBlockState state) {
    return ((BlockPlanks.EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockOldLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */