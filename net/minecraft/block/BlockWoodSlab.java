package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockWoodSlab extends BlockSlab {
  public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class);
  
  public BlockWoodSlab() {
    super(Material.WOOD);
    IBlockState iblockstate = this.blockState.getBaseState();
    if (!isDouble())
      iblockstate = iblockstate.withProperty((IProperty)HALF, BlockSlab.EnumBlockHalf.BOTTOM); 
    setDefaultState(iblockstate.withProperty((IProperty)VARIANT, BlockPlanks.EnumType.OAK));
    setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
  }
  
  public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_) {
    return ((BlockPlanks.EnumType)state.getValue((IProperty)VARIANT)).getMapColor();
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Item.getItemFromBlock(Blocks.WOODEN_SLAB);
  }
  
  public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
    return new ItemStack(Blocks.WOODEN_SLAB, 1, ((BlockPlanks.EnumType)state.getValue((IProperty)VARIANT)).getMetadata());
  }
  
  public String getUnlocalizedName(int meta) {
    return String.valueOf(getUnlocalizedName()) + "." + BlockPlanks.EnumType.byMetadata(meta).getUnlocalizedName();
  }
  
  public IProperty<?> getVariantProperty() {
    return (IProperty<?>)VARIANT;
  }
  
  public Comparable<?> getTypeForItem(ItemStack stack) {
    return BlockPlanks.EnumType.byMetadata(stack.getMetadata() & 0x7);
  }
  
  public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
    byte b;
    int i;
    BlockPlanks.EnumType[] arrayOfEnumType;
    for (i = (arrayOfEnumType = BlockPlanks.EnumType.values()).length, b = 0; b < i; ) {
      BlockPlanks.EnumType blockplanks$enumtype = arrayOfEnumType[b];
      tab.add(new ItemStack(this, 1, blockplanks$enumtype.getMetadata()));
      b++;
    } 
  }
  
  public IBlockState getStateFromMeta(int meta) {
    IBlockState iblockstate = getDefaultState().withProperty((IProperty)VARIANT, BlockPlanks.EnumType.byMetadata(meta & 0x7));
    if (!isDouble())
      iblockstate = iblockstate.withProperty((IProperty)HALF, ((meta & 0x8) == 0) ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP); 
    return iblockstate;
  }
  
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    i |= ((BlockPlanks.EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
    if (!isDouble() && state.getValue((IProperty)HALF) == BlockSlab.EnumBlockHalf.TOP)
      i |= 0x8; 
    return i;
  }
  
  protected BlockStateContainer createBlockState() {
    return isDouble() ? new BlockStateContainer(this, new IProperty[] { (IProperty)VARIANT }) : new BlockStateContainer(this, new IProperty[] { (IProperty)HALF, (IProperty)VARIANT });
  }
  
  public int damageDropped(IBlockState state) {
    return ((BlockPlanks.EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockWoodSlab.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */