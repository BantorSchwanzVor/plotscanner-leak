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
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockPurpurSlab extends BlockSlab {
  public static final PropertyEnum<Variant> VARIANT = PropertyEnum.create("variant", Variant.class);
  
  public BlockPurpurSlab() {
    super(Material.ROCK, MapColor.MAGENTA);
    IBlockState iblockstate = this.blockState.getBaseState();
    if (!isDouble())
      iblockstate = iblockstate.withProperty((IProperty)HALF, BlockSlab.EnumBlockHalf.BOTTOM); 
    setDefaultState(iblockstate.withProperty((IProperty)VARIANT, Variant.DEFAULT));
    setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Item.getItemFromBlock(Blocks.PURPUR_SLAB);
  }
  
  public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
    return new ItemStack(Blocks.PURPUR_SLAB);
  }
  
  public IBlockState getStateFromMeta(int meta) {
    IBlockState iblockstate = getDefaultState().withProperty((IProperty)VARIANT, Variant.DEFAULT);
    if (!isDouble())
      iblockstate = iblockstate.withProperty((IProperty)HALF, ((meta & 0x8) == 0) ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP); 
    return iblockstate;
  }
  
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    if (!isDouble() && state.getValue((IProperty)HALF) == BlockSlab.EnumBlockHalf.TOP)
      i |= 0x8; 
    return i;
  }
  
  protected BlockStateContainer createBlockState() {
    return isDouble() ? new BlockStateContainer(this, new IProperty[] { (IProperty)VARIANT }) : new BlockStateContainer(this, new IProperty[] { (IProperty)HALF, (IProperty)VARIANT });
  }
  
  public String getUnlocalizedName(int meta) {
    return getUnlocalizedName();
  }
  
  public IProperty<?> getVariantProperty() {
    return (IProperty<?>)VARIANT;
  }
  
  public Comparable<?> getTypeForItem(ItemStack stack) {
    return Variant.DEFAULT;
  }
  
  public static class Double extends BlockPurpurSlab {
    public boolean isDouble() {
      return true;
    }
  }
  
  public static class Half extends BlockPurpurSlab {
    public boolean isDouble() {
      return false;
    }
  }
  
  public enum Variant implements IStringSerializable {
    DEFAULT;
    
    public String getName() {
      return "default";
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockPurpurSlab.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */