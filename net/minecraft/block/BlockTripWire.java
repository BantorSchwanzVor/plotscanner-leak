package net.minecraft.block;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTripWire extends Block {
  public static final PropertyBool POWERED = PropertyBool.create("powered");
  
  public static final PropertyBool ATTACHED = PropertyBool.create("attached");
  
  public static final PropertyBool DISARMED = PropertyBool.create("disarmed");
  
  public static final PropertyBool NORTH = PropertyBool.create("north");
  
  public static final PropertyBool EAST = PropertyBool.create("east");
  
  public static final PropertyBool SOUTH = PropertyBool.create("south");
  
  public static final PropertyBool WEST = PropertyBool.create("west");
  
  protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0625D, 0.0D, 1.0D, 0.15625D, 1.0D);
  
  protected static final AxisAlignedBB TRIP_WRITE_ATTACHED_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
  
  public BlockTripWire() {
    super(Material.CIRCUITS);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)POWERED, Boolean.valueOf(false)).withProperty((IProperty)ATTACHED, Boolean.valueOf(false)).withProperty((IProperty)DISARMED, Boolean.valueOf(false)).withProperty((IProperty)NORTH, Boolean.valueOf(false)).withProperty((IProperty)EAST, Boolean.valueOf(false)).withProperty((IProperty)SOUTH, Boolean.valueOf(false)).withProperty((IProperty)WEST, Boolean.valueOf(false)));
    setTickRandomly(true);
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return !((Boolean)state.getValue((IProperty)ATTACHED)).booleanValue() ? TRIP_WRITE_ATTACHED_AABB : AABB;
  }
  
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    return state.withProperty((IProperty)NORTH, Boolean.valueOf(isConnectedTo(worldIn, pos, state, EnumFacing.NORTH))).withProperty((IProperty)EAST, Boolean.valueOf(isConnectedTo(worldIn, pos, state, EnumFacing.EAST))).withProperty((IProperty)SOUTH, Boolean.valueOf(isConnectedTo(worldIn, pos, state, EnumFacing.SOUTH))).withProperty((IProperty)WEST, Boolean.valueOf(isConnectedTo(worldIn, pos, state, EnumFacing.WEST)));
  }
  
  @Nullable
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
    return NULL_AABB;
  }
  
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Items.STRING;
  }
  
  public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
    return new ItemStack(Items.STRING);
  }
  
  public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    worldIn.setBlockState(pos, state, 3);
    notifyHook(worldIn, pos, state);
  }
  
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    notifyHook(worldIn, pos, state.withProperty((IProperty)POWERED, Boolean.valueOf(true)));
  }
  
  public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
    if (!worldIn.isRemote)
      if (!player.getHeldItemMainhand().func_190926_b() && player.getHeldItemMainhand().getItem() == Items.SHEARS)
        worldIn.setBlockState(pos, state.withProperty((IProperty)DISARMED, Boolean.valueOf(true)), 4);  
  }
  
  private void notifyHook(World worldIn, BlockPos pos, IBlockState state) {
    byte b;
    int i;
    EnumFacing[] arrayOfEnumFacing;
    for (i = (arrayOfEnumFacing = new EnumFacing[] { EnumFacing.SOUTH, EnumFacing.WEST }, ).length, b = 0; b < i; ) {
      EnumFacing enumfacing = arrayOfEnumFacing[b];
      for (int j = 1; j < 42; j++) {
        BlockPos blockpos = pos.offset(enumfacing, j);
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        if (iblockstate.getBlock() == Blocks.TRIPWIRE_HOOK) {
          if (iblockstate.getValue((IProperty)BlockTripWireHook.FACING) == enumfacing.getOpposite())
            Blocks.TRIPWIRE_HOOK.calculateState(worldIn, blockpos, iblockstate, false, true, j, state); 
          break;
        } 
        if (iblockstate.getBlock() != Blocks.TRIPWIRE)
          break; 
      } 
      b++;
    } 
  }
  
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
    if (!worldIn.isRemote)
      if (!((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
        updateState(worldIn, pos);  
  }
  
  public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {}
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (!worldIn.isRemote)
      if (((Boolean)worldIn.getBlockState(pos).getValue((IProperty)POWERED)).booleanValue())
        updateState(worldIn, pos);  
  }
  
  private void updateState(World worldIn, BlockPos pos) {
    IBlockState iblockstate = worldIn.getBlockState(pos);
    boolean flag = ((Boolean)iblockstate.getValue((IProperty)POWERED)).booleanValue();
    boolean flag1 = false;
    List<? extends Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(null, iblockstate.getBoundingBox((IBlockAccess)worldIn, pos).offset(pos));
    if (!list.isEmpty())
      for (Entity entity : list) {
        if (!entity.doesEntityNotTriggerPressurePlate()) {
          flag1 = true;
          break;
        } 
      }  
    if (flag1 != flag) {
      iblockstate = iblockstate.withProperty((IProperty)POWERED, Boolean.valueOf(flag1));
      worldIn.setBlockState(pos, iblockstate, 3);
      notifyHook(worldIn, pos, iblockstate);
    } 
    if (flag1)
      worldIn.scheduleUpdate(new BlockPos((Vec3i)pos), this, tickRate(worldIn)); 
  }
  
  public static boolean isConnectedTo(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing direction) {
    BlockPos blockpos = pos.offset(direction);
    IBlockState iblockstate = worldIn.getBlockState(blockpos);
    Block block = iblockstate.getBlock();
    if (block == Blocks.TRIPWIRE_HOOK) {
      EnumFacing enumfacing = direction.getOpposite();
      return (iblockstate.getValue((IProperty)BlockTripWireHook.FACING) == enumfacing);
    } 
    return (block == Blocks.TRIPWIRE);
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)POWERED, Boolean.valueOf(((meta & 0x1) > 0))).withProperty((IProperty)ATTACHED, Boolean.valueOf(((meta & 0x4) > 0))).withProperty((IProperty)DISARMED, Boolean.valueOf(((meta & 0x8) > 0)));
  }
  
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
      i |= 0x1; 
    if (((Boolean)state.getValue((IProperty)ATTACHED)).booleanValue())
      i |= 0x4; 
    if (((Boolean)state.getValue((IProperty)DISARMED)).booleanValue())
      i |= 0x8; 
    return i;
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
    return new BlockStateContainer(this, new IProperty[] { (IProperty)POWERED, (IProperty)ATTACHED, (IProperty)DISARMED, (IProperty)NORTH, (IProperty)EAST, (IProperty)WEST, (IProperty)SOUTH });
  }
  
  public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    return BlockFaceShape.UNDEFINED;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockTripWire.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */