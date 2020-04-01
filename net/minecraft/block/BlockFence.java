package net.minecraft.block;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemLead;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFence extends Block {
  public static final PropertyBool NORTH = PropertyBool.create("north");
  
  public static final PropertyBool EAST = PropertyBool.create("east");
  
  public static final PropertyBool SOUTH = PropertyBool.create("south");
  
  public static final PropertyBool WEST = PropertyBool.create("west");
  
  protected static final AxisAlignedBB[] BOUNDING_BOXES = new AxisAlignedBB[] { 
      new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D), new AxisAlignedBB(0.0D, 0.0D, 0.375D, 0.625D, 1.0D, 1.0D), new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.625D, 1.0D, 0.625D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 1.0D, 1.0D, 1.0D), 
      new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D), new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.375D, 0.0D, 0.0D, 1.0D, 1.0D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.625D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D) };
  
  public static final AxisAlignedBB PILLAR_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.5D, 0.625D);
  
  public static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.625D, 0.625D, 1.5D, 1.0D);
  
  public static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 0.375D, 1.5D, 0.625D);
  
  public static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.5D, 0.375D);
  
  public static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.625D, 0.0D, 0.375D, 1.0D, 1.5D, 0.625D);
  
  public BlockFence(Material p_i46395_1_, MapColor p_i46395_2_) {
    super(p_i46395_1_, p_i46395_2_);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)NORTH, Boolean.valueOf(false)).withProperty((IProperty)EAST, Boolean.valueOf(false)).withProperty((IProperty)SOUTH, Boolean.valueOf(false)).withProperty((IProperty)WEST, Boolean.valueOf(false)));
    setCreativeTab(CreativeTabs.DECORATIONS);
  }
  
  public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
    if (!p_185477_7_)
      state = state.getActualState((IBlockAccess)worldIn, pos); 
    addCollisionBoxToList(pos, entityBox, collidingBoxes, PILLAR_AABB);
    if (((Boolean)state.getValue((IProperty)NORTH)).booleanValue())
      addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB); 
    if (((Boolean)state.getValue((IProperty)EAST)).booleanValue())
      addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB); 
    if (((Boolean)state.getValue((IProperty)SOUTH)).booleanValue())
      addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB); 
    if (((Boolean)state.getValue((IProperty)WEST)).booleanValue())
      addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB); 
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    state = getActualState(state, source, pos);
    return BOUNDING_BOXES[getBoundingBoxIdx(state)];
  }
  
  private static int getBoundingBoxIdx(IBlockState state) {
    int i = 0;
    if (((Boolean)state.getValue((IProperty)NORTH)).booleanValue())
      i |= 1 << EnumFacing.NORTH.getHorizontalIndex(); 
    if (((Boolean)state.getValue((IProperty)EAST)).booleanValue())
      i |= 1 << EnumFacing.EAST.getHorizontalIndex(); 
    if (((Boolean)state.getValue((IProperty)SOUTH)).booleanValue())
      i |= 1 << EnumFacing.SOUTH.getHorizontalIndex(); 
    if (((Boolean)state.getValue((IProperty)WEST)).booleanValue())
      i |= 1 << EnumFacing.WEST.getHorizontalIndex(); 
    return i;
  }
  
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  
  public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
    return false;
  }
  
  public boolean canConnectTo(IBlockAccess worldIn, BlockPos pos, EnumFacing p_176524_3_) {
    IBlockState iblockstate = worldIn.getBlockState(pos);
    BlockFaceShape blockfaceshape = iblockstate.func_193401_d(worldIn, pos, p_176524_3_);
    Block block = iblockstate.getBlock();
    boolean flag = (blockfaceshape == BlockFaceShape.MIDDLE_POLE && (iblockstate.getMaterial() == this.blockMaterial || block instanceof BlockFenceGate));
    return !((func_194142_e(block) || blockfaceshape != BlockFaceShape.SOLID) && !flag);
  }
  
  protected static boolean func_194142_e(Block p_194142_0_) {
    return !(!Block.func_193382_c(p_194142_0_) && p_194142_0_ != Blocks.BARRIER && p_194142_0_ != Blocks.MELON_BLOCK && p_194142_0_ != Blocks.PUMPKIN && p_194142_0_ != Blocks.LIT_PUMPKIN);
  }
  
  public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return true;
  }
  
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
    if (!worldIn.isRemote)
      return ItemLead.attachToFence(playerIn, worldIn, pos); 
    ItemStack itemstack = playerIn.getHeldItem(hand);
    return !(itemstack.getItem() != Items.LEAD && !itemstack.func_190926_b());
  }
  
  public int getMetaFromState(IBlockState state) {
    return 0;
  }
  
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    return state.withProperty((IProperty)NORTH, Boolean.valueOf(canConnectTo(worldIn, pos.north(), EnumFacing.SOUTH))).withProperty((IProperty)EAST, Boolean.valueOf(canConnectTo(worldIn, pos.east(), EnumFacing.WEST))).withProperty((IProperty)SOUTH, Boolean.valueOf(canConnectTo(worldIn, pos.south(), EnumFacing.NORTH))).withProperty((IProperty)WEST, Boolean.valueOf(canConnectTo(worldIn, pos.west(), EnumFacing.EAST)));
  }
  
  public IBlockState withRotation(IBlockState state, Rotation rot) {
    switch (rot) {
      case null:
        return state.withProperty((IProperty)NORTH, state.getValue((IProperty)SOUTH)).withProperty((IProperty)EAST, state.getValue((IProperty)WEST)).withProperty((IProperty)SOUTH, state.getValue((IProperty)NORTH)).withProperty((IProperty)WEST, state.getValue((IProperty)EAST));
      case COUNTERCLOCKWISE_90:
        return state.withProperty((IProperty)NORTH, state.getValue((IProperty)EAST)).withProperty((IProperty)EAST, state.getValue((IProperty)SOUTH)).withProperty((IProperty)SOUTH, state.getValue((IProperty)WEST)).withProperty((IProperty)WEST, state.getValue((IProperty)NORTH));
      case CLOCKWISE_90:
        return state.withProperty((IProperty)NORTH, state.getValue((IProperty)WEST)).withProperty((IProperty)EAST, state.getValue((IProperty)NORTH)).withProperty((IProperty)SOUTH, state.getValue((IProperty)EAST)).withProperty((IProperty)WEST, state.getValue((IProperty)SOUTH));
    } 
    return state;
  }
  
  public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
    switch (mirrorIn) {
      case LEFT_RIGHT:
        return state.withProperty((IProperty)NORTH, state.getValue((IProperty)SOUTH)).withProperty((IProperty)SOUTH, state.getValue((IProperty)NORTH));
      case null:
        return state.withProperty((IProperty)EAST, state.getValue((IProperty)WEST)).withProperty((IProperty)WEST, state.getValue((IProperty)EAST));
    } 
    return super.withMirror(state, mirrorIn);
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)NORTH, (IProperty)EAST, (IProperty)WEST, (IProperty)SOUTH });
  }
  
  public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    return (p_193383_4_ != EnumFacing.UP && p_193383_4_ != EnumFacing.DOWN) ? BlockFaceShape.MIDDLE_POLE : BlockFaceShape.CENTER;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockFence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */