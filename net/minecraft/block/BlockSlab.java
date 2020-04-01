package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockSlab extends Block {
  public static final PropertyEnum<EnumBlockHalf> HALF = PropertyEnum.create("half", EnumBlockHalf.class);
  
  protected static final AxisAlignedBB AABB_BOTTOM_HALF = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
  
  protected static final AxisAlignedBB AABB_TOP_HALF = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
  
  public BlockSlab(Material materialIn) {
    this(materialIn, materialIn.getMaterialMapColor());
  }
  
  public BlockSlab(Material p_i47249_1_, MapColor p_i47249_2_) {
    super(p_i47249_1_, p_i47249_2_);
    this.fullBlock = isDouble();
    setLightOpacity(255);
  }
  
  protected boolean canSilkHarvest() {
    return false;
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    if (isDouble())
      return FULL_BLOCK_AABB; 
    return (state.getValue((IProperty)HALF) == EnumBlockHalf.TOP) ? AABB_TOP_HALF : AABB_BOTTOM_HALF;
  }
  
  public boolean isFullyOpaque(IBlockState state) {
    return !(!((BlockSlab)state.getBlock()).isDouble() && state.getValue((IProperty)HALF) != EnumBlockHalf.TOP);
  }
  
  public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    if (((BlockSlab)p_193383_2_.getBlock()).isDouble())
      return BlockFaceShape.SOLID; 
    if (p_193383_4_ == EnumFacing.UP && p_193383_2_.getValue((IProperty)HALF) == EnumBlockHalf.TOP)
      return BlockFaceShape.SOLID; 
    return (p_193383_4_ == EnumFacing.DOWN && p_193383_2_.getValue((IProperty)HALF) == EnumBlockHalf.BOTTOM) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
  }
  
  public boolean isOpaqueCube(IBlockState state) {
    return isDouble();
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    IBlockState iblockstate = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty((IProperty)HALF, EnumBlockHalf.BOTTOM);
    if (isDouble())
      return iblockstate; 
    return (facing != EnumFacing.DOWN && (facing == EnumFacing.UP || hitY <= 0.5D)) ? iblockstate : iblockstate.withProperty((IProperty)HALF, EnumBlockHalf.TOP);
  }
  
  public int quantityDropped(Random random) {
    return isDouble() ? 2 : 1;
  }
  
  public boolean isFullCube(IBlockState state) {
    return isDouble();
  }
  
  public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    if (isDouble())
      return super.shouldSideBeRendered(blockState, blockAccess, pos, side); 
    if (side != EnumFacing.UP && side != EnumFacing.DOWN && !super.shouldSideBeRendered(blockState, blockAccess, pos, side))
      return false; 
    IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
    boolean flag = (isHalfSlab(iblockstate) && iblockstate.getValue((IProperty)HALF) == EnumBlockHalf.TOP);
    boolean flag1 = (isHalfSlab(blockState) && blockState.getValue((IProperty)HALF) == EnumBlockHalf.TOP);
    if (flag1) {
      if (side == EnumFacing.DOWN)
        return true; 
      if (side == EnumFacing.UP && super.shouldSideBeRendered(blockState, blockAccess, pos, side))
        return true; 
      return !(isHalfSlab(iblockstate) && flag);
    } 
    if (side == EnumFacing.UP)
      return true; 
    if (side == EnumFacing.DOWN && super.shouldSideBeRendered(blockState, blockAccess, pos, side))
      return true; 
    return !(isHalfSlab(iblockstate) && !flag);
  }
  
  protected static boolean isHalfSlab(IBlockState state) {
    Block block = state.getBlock();
    return !(block != Blocks.STONE_SLAB && block != Blocks.WOODEN_SLAB && block != Blocks.STONE_SLAB2 && block != Blocks.PURPUR_SLAB);
  }
  
  public abstract String getUnlocalizedName(int paramInt);
  
  public abstract boolean isDouble();
  
  public abstract IProperty<?> getVariantProperty();
  
  public abstract Comparable<?> getTypeForItem(ItemStack paramItemStack);
  
  public enum EnumBlockHalf implements IStringSerializable {
    TOP("top"),
    BOTTOM("bottom");
    
    private final String name;
    
    EnumBlockHalf(String name) {
      this.name = name;
    }
    
    public String toString() {
      return this.name;
    }
    
    public String getName() {
      return this.name;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockSlab.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */