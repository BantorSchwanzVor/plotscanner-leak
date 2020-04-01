package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockPistonStructureHelper;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPistonBase extends BlockDirectional {
  public static final PropertyBool EXTENDED = PropertyBool.create("extended");
  
  protected static final AxisAlignedBB PISTON_BASE_EAST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 1.0D);
  
  protected static final AxisAlignedBB PISTON_BASE_WEST_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
  
  protected static final AxisAlignedBB PISTON_BASE_SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D);
  
  protected static final AxisAlignedBB PISTON_BASE_NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D);
  
  protected static final AxisAlignedBB PISTON_BASE_UP_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);
  
  protected static final AxisAlignedBB PISTON_BASE_DOWN_AABB = new AxisAlignedBB(0.0D, 0.25D, 0.0D, 1.0D, 1.0D, 1.0D);
  
  private final boolean isSticky;
  
  public BlockPistonBase(boolean isSticky) {
    super(Material.PISTON);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH).withProperty((IProperty)EXTENDED, Boolean.valueOf(false)));
    this.isSticky = isSticky;
    setSoundType(SoundType.STONE);
    setHardness(0.5F);
    setCreativeTab(CreativeTabs.REDSTONE);
  }
  
  public boolean causesSuffocation(IBlockState p_176214_1_) {
    return !((Boolean)p_176214_1_.getValue((IProperty)EXTENDED)).booleanValue();
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    if (((Boolean)state.getValue((IProperty)EXTENDED)).booleanValue()) {
      switch ((EnumFacing)state.getValue((IProperty)FACING)) {
        case null:
          return PISTON_BASE_DOWN_AABB;
        default:
          return PISTON_BASE_UP_AABB;
        case NORTH:
          return PISTON_BASE_NORTH_AABB;
        case SOUTH:
          return PISTON_BASE_SOUTH_AABB;
        case WEST:
          return PISTON_BASE_WEST_AABB;
        case EAST:
          break;
      } 
      return PISTON_BASE_EAST_AABB;
    } 
    return FULL_BLOCK_AABB;
  }
  
  public boolean isFullyOpaque(IBlockState state) {
    return !(((Boolean)state.getValue((IProperty)EXTENDED)).booleanValue() && state.getValue((IProperty)FACING) != EnumFacing.DOWN);
  }
  
  public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
    addCollisionBoxToList(pos, entityBox, collidingBoxes, state.getBoundingBox((IBlockAccess)worldIn, pos));
  }
  
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    worldIn.setBlockState(pos, state.withProperty((IProperty)FACING, (Comparable)EnumFacing.func_190914_a(pos, placer)), 2);
    if (!worldIn.isRemote)
      checkForMove(worldIn, pos, state); 
  }
  
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
    if (!worldIn.isRemote)
      checkForMove(worldIn, pos, state); 
  }
  
  public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    if (!worldIn.isRemote && worldIn.getTileEntity(pos) == null)
      checkForMove(worldIn, pos, state); 
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    return getDefaultState().withProperty((IProperty)FACING, (Comparable)EnumFacing.func_190914_a(pos, placer)).withProperty((IProperty)EXTENDED, Boolean.valueOf(false));
  }
  
  private void checkForMove(World worldIn, BlockPos pos, IBlockState state) {
    EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
    boolean flag = shouldBeExtended(worldIn, pos, enumfacing);
    if (flag && !((Boolean)state.getValue((IProperty)EXTENDED)).booleanValue()) {
      if ((new BlockPistonStructureHelper(worldIn, pos, enumfacing, true)).canMove())
        worldIn.addBlockEvent(pos, this, 0, enumfacing.getIndex()); 
    } else if (!flag && ((Boolean)state.getValue((IProperty)EXTENDED)).booleanValue()) {
      worldIn.addBlockEvent(pos, this, 1, enumfacing.getIndex());
    } 
  }
  
  private boolean shouldBeExtended(World worldIn, BlockPos pos, EnumFacing facing) {
    byte b;
    int i;
    EnumFacing[] arrayOfEnumFacing1;
    for (i = (arrayOfEnumFacing1 = EnumFacing.values()).length, b = 0; b < i; ) {
      EnumFacing enumfacing = arrayOfEnumFacing1[b];
      if (enumfacing != facing && worldIn.isSidePowered(pos.offset(enumfacing), enumfacing))
        return true; 
      b++;
    } 
    if (worldIn.isSidePowered(pos, EnumFacing.DOWN))
      return true; 
    BlockPos blockpos = pos.up();
    EnumFacing[] arrayOfEnumFacing2;
    for (int j = (arrayOfEnumFacing2 = EnumFacing.values()).length; i < j; ) {
      EnumFacing enumfacing1 = arrayOfEnumFacing2[i];
      if (enumfacing1 != EnumFacing.DOWN && worldIn.isSidePowered(blockpos.offset(enumfacing1), enumfacing1))
        return true; 
      i++;
    } 
    return false;
  }
  
  public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
    EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
    if (!worldIn.isRemote) {
      boolean flag = shouldBeExtended(worldIn, pos, enumfacing);
      if (flag && id == 1) {
        worldIn.setBlockState(pos, state.withProperty((IProperty)EXTENDED, Boolean.valueOf(true)), 2);
        return false;
      } 
      if (!flag && id == 0)
        return false; 
    } 
    if (id == 0) {
      if (!doMove(worldIn, pos, enumfacing, true))
        return false; 
      worldIn.setBlockState(pos, state.withProperty((IProperty)EXTENDED, Boolean.valueOf(true)), 3);
      worldIn.playSound(null, pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, worldIn.rand.nextFloat() * 0.25F + 0.6F);
    } else if (id == 1) {
      TileEntity tileentity1 = worldIn.getTileEntity(pos.offset(enumfacing));
      if (tileentity1 instanceof TileEntityPiston)
        ((TileEntityPiston)tileentity1).clearPistonTileEntity(); 
      worldIn.setBlockState(pos, Blocks.PISTON_EXTENSION.getDefaultState().withProperty((IProperty)BlockPistonMoving.FACING, (Comparable)enumfacing).withProperty((IProperty)BlockPistonMoving.TYPE, this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT), 3);
      worldIn.setTileEntity(pos, BlockPistonMoving.createTilePiston(getStateFromMeta(param), enumfacing, false, true));
      if (this.isSticky) {
        BlockPos blockpos = pos.add(enumfacing.getFrontOffsetX() * 2, enumfacing.getFrontOffsetY() * 2, enumfacing.getFrontOffsetZ() * 2);
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        boolean flag1 = false;
        if (block == Blocks.PISTON_EXTENSION) {
          TileEntity tileentity = worldIn.getTileEntity(blockpos);
          if (tileentity instanceof TileEntityPiston) {
            TileEntityPiston tileentitypiston = (TileEntityPiston)tileentity;
            if (tileentitypiston.getFacing() == enumfacing && tileentitypiston.isExtending()) {
              tileentitypiston.clearPistonTileEntity();
              flag1 = true;
            } 
          } 
        } 
        if (!flag1 && iblockstate.getMaterial() != Material.AIR && canPush(iblockstate, worldIn, blockpos, enumfacing.getOpposite(), false, enumfacing) && (iblockstate.getMobilityFlag() == EnumPushReaction.NORMAL || block == Blocks.PISTON || block == Blocks.STICKY_PISTON))
          doMove(worldIn, pos, enumfacing, false); 
      } else {
        worldIn.setBlockToAir(pos.offset(enumfacing));
      } 
      worldIn.playSound(null, pos, SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, worldIn.rand.nextFloat() * 0.15F + 0.6F);
    } 
    return true;
  }
  
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  
  @Nullable
  public static EnumFacing getFacing(int meta) {
    int i = meta & 0x7;
    return (i > 5) ? null : EnumFacing.getFront(i);
  }
  
  public static boolean canPush(IBlockState blockStateIn, World worldIn, BlockPos pos, EnumFacing facing, boolean destroyBlocks, EnumFacing p_185646_5_) {
    Block block = blockStateIn.getBlock();
    if (block == Blocks.OBSIDIAN)
      return false; 
    if (!worldIn.getWorldBorder().contains(pos))
      return false; 
    if (pos.getY() >= 0 && (facing != EnumFacing.DOWN || pos.getY() != 0)) {
      if (pos.getY() <= worldIn.getHeight() - 1 && (facing != EnumFacing.UP || pos.getY() != worldIn.getHeight() - 1)) {
        if (block != Blocks.PISTON && block != Blocks.STICKY_PISTON) {
          if (blockStateIn.getBlockHardness(worldIn, pos) == -1.0F)
            return false; 
          switch (blockStateIn.getMobilityFlag()) {
            case null:
              return false;
            case DESTROY:
              return destroyBlocks;
            case PUSH_ONLY:
              return (facing == p_185646_5_);
          } 
        } else if (((Boolean)blockStateIn.getValue((IProperty)EXTENDED)).booleanValue()) {
          return false;
        } 
        return !block.hasTileEntity();
      } 
      return false;
    } 
    return false;
  }
  
  private boolean doMove(World worldIn, BlockPos pos, EnumFacing direction, boolean extending) {
    if (!extending)
      worldIn.setBlockToAir(pos.offset(direction)); 
    BlockPistonStructureHelper blockpistonstructurehelper = new BlockPistonStructureHelper(worldIn, pos, direction, extending);
    if (!blockpistonstructurehelper.canMove())
      return false; 
    List<BlockPos> list = blockpistonstructurehelper.getBlocksToMove();
    List<IBlockState> list1 = Lists.newArrayList();
    for (int i = 0; i < list.size(); i++) {
      BlockPos blockpos = list.get(i);
      list1.add(worldIn.getBlockState(blockpos).getActualState((IBlockAccess)worldIn, blockpos));
    } 
    List<BlockPos> list2 = blockpistonstructurehelper.getBlocksToDestroy();
    int k = list.size() + list2.size();
    IBlockState[] aiblockstate = new IBlockState[k];
    EnumFacing enumfacing = extending ? direction : direction.getOpposite();
    for (int j = list2.size() - 1; j >= 0; j--) {
      BlockPos blockpos1 = list2.get(j);
      IBlockState iblockstate = worldIn.getBlockState(blockpos1);
      iblockstate.getBlock().dropBlockAsItem(worldIn, blockpos1, iblockstate, 0);
      worldIn.setBlockState(blockpos1, Blocks.AIR.getDefaultState(), 4);
      k--;
      aiblockstate[k] = iblockstate;
    } 
    for (int l = list.size() - 1; l >= 0; l--) {
      BlockPos blockpos3 = list.get(l);
      IBlockState iblockstate2 = worldIn.getBlockState(blockpos3);
      worldIn.setBlockState(blockpos3, Blocks.AIR.getDefaultState(), 2);
      blockpos3 = blockpos3.offset(enumfacing);
      worldIn.setBlockState(blockpos3, Blocks.PISTON_EXTENSION.getDefaultState().withProperty((IProperty)FACING, (Comparable)direction), 4);
      worldIn.setTileEntity(blockpos3, BlockPistonMoving.createTilePiston(list1.get(l), direction, extending, false));
      k--;
      aiblockstate[k] = iblockstate2;
    } 
    BlockPos blockpos2 = pos.offset(direction);
    if (extending) {
      BlockPistonExtension.EnumPistonType blockpistonextension$enumpistontype = this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT;
      IBlockState iblockstate3 = Blocks.PISTON_HEAD.getDefaultState().withProperty((IProperty)BlockPistonExtension.FACING, (Comparable)direction).withProperty((IProperty)BlockPistonExtension.TYPE, blockpistonextension$enumpistontype);
      IBlockState iblockstate1 = Blocks.PISTON_EXTENSION.getDefaultState().withProperty((IProperty)BlockPistonMoving.FACING, (Comparable)direction).withProperty((IProperty)BlockPistonMoving.TYPE, this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
      worldIn.setBlockState(blockpos2, iblockstate1, 4);
      worldIn.setTileEntity(blockpos2, BlockPistonMoving.createTilePiston(iblockstate3, direction, true, true));
    } 
    for (int i1 = list2.size() - 1; i1 >= 0; i1--)
      worldIn.notifyNeighborsOfStateChange(list2.get(i1), aiblockstate[k++].getBlock(), false); 
    for (int j1 = list.size() - 1; j1 >= 0; j1--)
      worldIn.notifyNeighborsOfStateChange(list.get(j1), aiblockstate[k++].getBlock(), false); 
    if (extending)
      worldIn.notifyNeighborsOfStateChange(blockpos2, Blocks.PISTON_HEAD, false); 
    return true;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)FACING, (Comparable)getFacing(meta)).withProperty((IProperty)EXTENDED, Boolean.valueOf(((meta & 0x8) > 0)));
  }
  
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    i |= ((EnumFacing)state.getValue((IProperty)FACING)).getIndex();
    if (((Boolean)state.getValue((IProperty)EXTENDED)).booleanValue())
      i |= 0x8; 
    return i;
  }
  
  public IBlockState withRotation(IBlockState state, Rotation rot) {
    return state.withProperty((IProperty)FACING, (Comparable)rot.rotate((EnumFacing)state.getValue((IProperty)FACING)));
  }
  
  public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
    return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue((IProperty)FACING)));
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)FACING, (IProperty)EXTENDED });
  }
  
  public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    p_193383_2_ = getActualState(p_193383_2_, p_193383_1_, p_193383_3_);
    return (p_193383_2_.getValue((IProperty)FACING) != p_193383_4_.getOpposite() && ((Boolean)p_193383_2_.getValue((IProperty)EXTENDED)).booleanValue()) ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockPistonBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */