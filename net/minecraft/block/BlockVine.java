package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockVine extends Block {
  public static final PropertyBool UP = PropertyBool.create("up");
  
  public static final PropertyBool NORTH = PropertyBool.create("north");
  
  public static final PropertyBool EAST = PropertyBool.create("east");
  
  public static final PropertyBool SOUTH = PropertyBool.create("south");
  
  public static final PropertyBool WEST = PropertyBool.create("west");
  
  public static final PropertyBool[] ALL_FACES = new PropertyBool[] { UP, NORTH, SOUTH, WEST, EAST };
  
  protected static final AxisAlignedBB UP_AABB = new AxisAlignedBB(0.0D, 0.9375D, 0.0D, 1.0D, 1.0D, 1.0D);
  
  protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D);
  
  protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
  
  protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0625D);
  
  protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D);
  
  public BlockVine() {
    super(Material.VINE);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)UP, Boolean.valueOf(false)).withProperty((IProperty)NORTH, Boolean.valueOf(false)).withProperty((IProperty)EAST, Boolean.valueOf(false)).withProperty((IProperty)SOUTH, Boolean.valueOf(false)).withProperty((IProperty)WEST, Boolean.valueOf(false)));
    setTickRandomly(true);
    setCreativeTab(CreativeTabs.DECORATIONS);
  }
  
  @Nullable
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
    return NULL_AABB;
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    state = state.getActualState(source, pos);
    int i = 0;
    AxisAlignedBB axisalignedbb = FULL_BLOCK_AABB;
    if (((Boolean)state.getValue((IProperty)UP)).booleanValue()) {
      axisalignedbb = UP_AABB;
      i++;
    } 
    if (((Boolean)state.getValue((IProperty)NORTH)).booleanValue()) {
      axisalignedbb = NORTH_AABB;
      i++;
    } 
    if (((Boolean)state.getValue((IProperty)EAST)).booleanValue()) {
      axisalignedbb = EAST_AABB;
      i++;
    } 
    if (((Boolean)state.getValue((IProperty)SOUTH)).booleanValue()) {
      axisalignedbb = SOUTH_AABB;
      i++;
    } 
    if (((Boolean)state.getValue((IProperty)WEST)).booleanValue()) {
      axisalignedbb = WEST_AABB;
      i++;
    } 
    return (i == 1) ? axisalignedbb : FULL_BLOCK_AABB;
  }
  
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    BlockPos blockpos = pos.up();
    return state.withProperty((IProperty)UP, Boolean.valueOf((worldIn.getBlockState(blockpos).func_193401_d(worldIn, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID)));
  }
  
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  
  public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
    return true;
  }
  
  public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
    return (side != EnumFacing.DOWN && side != EnumFacing.UP && func_193395_a(worldIn, pos, side));
  }
  
  public boolean func_193395_a(World p_193395_1_, BlockPos p_193395_2_, EnumFacing p_193395_3_) {
    Block block = p_193395_1_.getBlockState(p_193395_2_.up()).getBlock();
    return (func_193396_c(p_193395_1_, p_193395_2_.offset(p_193395_3_.getOpposite()), p_193395_3_) && (block == Blocks.AIR || block == Blocks.VINE || func_193396_c(p_193395_1_, p_193395_2_.up(), EnumFacing.UP)));
  }
  
  private boolean func_193396_c(World p_193396_1_, BlockPos p_193396_2_, EnumFacing p_193396_3_) {
    IBlockState iblockstate = p_193396_1_.getBlockState(p_193396_2_);
    return (iblockstate.func_193401_d((IBlockAccess)p_193396_1_, p_193396_2_, p_193396_3_) == BlockFaceShape.SOLID && !func_193397_e(iblockstate.getBlock()));
  }
  
  protected static boolean func_193397_e(Block p_193397_0_) {
    return !(!(p_193397_0_ instanceof BlockShulkerBox) && p_193397_0_ != Blocks.BEACON && p_193397_0_ != Blocks.CAULDRON && p_193397_0_ != Blocks.GLASS && p_193397_0_ != Blocks.STAINED_GLASS && p_193397_0_ != Blocks.PISTON && p_193397_0_ != Blocks.STICKY_PISTON && p_193397_0_ != Blocks.PISTON_HEAD && p_193397_0_ != Blocks.TRAPDOOR);
  }
  
  private boolean recheckGrownSides(World worldIn, BlockPos pos, IBlockState state) {
    IBlockState iblockstate = state;
    for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
      PropertyBool propertybool = getPropertyFor(enumfacing);
      if (((Boolean)state.getValue((IProperty)propertybool)).booleanValue() && !func_193395_a(worldIn, pos, enumfacing.getOpposite())) {
        IBlockState iblockstate1 = worldIn.getBlockState(pos.up());
        if (iblockstate1.getBlock() != this || !((Boolean)iblockstate1.getValue((IProperty)propertybool)).booleanValue())
          state = state.withProperty((IProperty)propertybool, Boolean.valueOf(false)); 
      } 
    } 
    if (getNumGrownFaces(state) == 0)
      return false; 
    if (iblockstate != state)
      worldIn.setBlockState(pos, state, 2); 
    return true;
  }
  
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
    if (!worldIn.isRemote && !recheckGrownSides(worldIn, pos, state)) {
      dropBlockAsItem(worldIn, pos, state, 0);
      worldIn.setBlockToAir(pos);
    } 
  }
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (!worldIn.isRemote)
      if (worldIn.rand.nextInt(4) == 0) {
        int i = 4;
        int j = 5;
        boolean flag = false;
        int k;
        label100: for (k = -4; k <= 4; k++) {
          for (int l = -4; l <= 4; l++) {
            for (int i1 = -1; i1 <= 1; i1++) {
              if (worldIn.getBlockState(pos.add(k, i1, l)).getBlock() == this) {
                j--;
                if (j <= 0) {
                  flag = true;
                  break label100;
                } 
              } 
            } 
          } 
        } 
        EnumFacing enumfacing1 = EnumFacing.random(rand);
        BlockPos blockpos2 = pos.up();
        if (enumfacing1 == EnumFacing.UP && pos.getY() < 255 && worldIn.isAirBlock(blockpos2)) {
          IBlockState iblockstate2 = state;
          for (EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL) {
            if (rand.nextBoolean() && func_193395_a(worldIn, blockpos2, enumfacing2.getOpposite())) {
              iblockstate2 = iblockstate2.withProperty((IProperty)getPropertyFor(enumfacing2), Boolean.valueOf(true));
              continue;
            } 
            iblockstate2 = iblockstate2.withProperty((IProperty)getPropertyFor(enumfacing2), Boolean.valueOf(false));
          } 
          if (((Boolean)iblockstate2.getValue((IProperty)NORTH)).booleanValue() || ((Boolean)iblockstate2.getValue((IProperty)EAST)).booleanValue() || ((Boolean)iblockstate2.getValue((IProperty)SOUTH)).booleanValue() || ((Boolean)iblockstate2.getValue((IProperty)WEST)).booleanValue())
            worldIn.setBlockState(blockpos2, iblockstate2, 2); 
        } else if (enumfacing1.getAxis().isHorizontal() && !((Boolean)state.getValue((IProperty)getPropertyFor(enumfacing1))).booleanValue()) {
          if (!flag) {
            BlockPos blockpos4 = pos.offset(enumfacing1);
            IBlockState iblockstate3 = worldIn.getBlockState(blockpos4);
            Block block1 = iblockstate3.getBlock();
            if (block1.blockMaterial == Material.AIR) {
              EnumFacing enumfacing3 = enumfacing1.rotateY();
              EnumFacing enumfacing4 = enumfacing1.rotateYCCW();
              boolean flag1 = ((Boolean)state.getValue((IProperty)getPropertyFor(enumfacing3))).booleanValue();
              boolean flag2 = ((Boolean)state.getValue((IProperty)getPropertyFor(enumfacing4))).booleanValue();
              BlockPos blockpos = blockpos4.offset(enumfacing3);
              BlockPos blockpos1 = blockpos4.offset(enumfacing4);
              if (flag1 && func_193395_a(worldIn, blockpos.offset(enumfacing3), enumfacing3)) {
                worldIn.setBlockState(blockpos4, getDefaultState().withProperty((IProperty)getPropertyFor(enumfacing3), Boolean.valueOf(true)), 2);
              } else if (flag2 && func_193395_a(worldIn, blockpos1.offset(enumfacing4), enumfacing4)) {
                worldIn.setBlockState(blockpos4, getDefaultState().withProperty((IProperty)getPropertyFor(enumfacing4), Boolean.valueOf(true)), 2);
              } else if (flag1 && worldIn.isAirBlock(blockpos) && func_193395_a(worldIn, blockpos, enumfacing1)) {
                worldIn.setBlockState(blockpos, getDefaultState().withProperty((IProperty)getPropertyFor(enumfacing1.getOpposite()), Boolean.valueOf(true)), 2);
              } else if (flag2 && worldIn.isAirBlock(blockpos1) && func_193395_a(worldIn, blockpos1, enumfacing1)) {
                worldIn.setBlockState(blockpos1, getDefaultState().withProperty((IProperty)getPropertyFor(enumfacing1.getOpposite()), Boolean.valueOf(true)), 2);
              } 
            } else if (iblockstate3.func_193401_d((IBlockAccess)worldIn, blockpos4, enumfacing1) == BlockFaceShape.SOLID) {
              worldIn.setBlockState(pos, state.withProperty((IProperty)getPropertyFor(enumfacing1), Boolean.valueOf(true)), 2);
            } 
          } 
        } else if (pos.getY() > 1) {
          BlockPos blockpos3 = pos.down();
          IBlockState iblockstate = worldIn.getBlockState(blockpos3);
          Block block = iblockstate.getBlock();
          if (block.blockMaterial == Material.AIR) {
            IBlockState iblockstate1 = state;
            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
              if (rand.nextBoolean())
                iblockstate1 = iblockstate1.withProperty((IProperty)getPropertyFor(enumfacing), Boolean.valueOf(false)); 
            } 
            if (((Boolean)iblockstate1.getValue((IProperty)NORTH)).booleanValue() || ((Boolean)iblockstate1.getValue((IProperty)EAST)).booleanValue() || ((Boolean)iblockstate1.getValue((IProperty)SOUTH)).booleanValue() || ((Boolean)iblockstate1.getValue((IProperty)WEST)).booleanValue())
              worldIn.setBlockState(blockpos3, iblockstate1, 2); 
          } else if (block == this) {
            IBlockState iblockstate4 = iblockstate;
            for (EnumFacing enumfacing5 : EnumFacing.Plane.HORIZONTAL) {
              PropertyBool propertybool = getPropertyFor(enumfacing5);
              if (rand.nextBoolean() && ((Boolean)state.getValue((IProperty)propertybool)).booleanValue())
                iblockstate4 = iblockstate4.withProperty((IProperty)propertybool, Boolean.valueOf(true)); 
            } 
            if (((Boolean)iblockstate4.getValue((IProperty)NORTH)).booleanValue() || ((Boolean)iblockstate4.getValue((IProperty)EAST)).booleanValue() || ((Boolean)iblockstate4.getValue((IProperty)SOUTH)).booleanValue() || ((Boolean)iblockstate4.getValue((IProperty)WEST)).booleanValue())
              worldIn.setBlockState(blockpos3, iblockstate4, 2); 
          } 
        } 
      }  
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    IBlockState iblockstate = getDefaultState().withProperty((IProperty)UP, Boolean.valueOf(false)).withProperty((IProperty)NORTH, Boolean.valueOf(false)).withProperty((IProperty)EAST, Boolean.valueOf(false)).withProperty((IProperty)SOUTH, Boolean.valueOf(false)).withProperty((IProperty)WEST, Boolean.valueOf(false));
    return facing.getAxis().isHorizontal() ? iblockstate.withProperty((IProperty)getPropertyFor(facing.getOpposite()), Boolean.valueOf(true)) : iblockstate;
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Items.field_190931_a;
  }
  
  public int quantityDropped(Random random) {
    return 0;
  }
  
  public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
    if (!worldIn.isRemote && stack.getItem() == Items.SHEARS) {
      player.addStat(StatList.getBlockStats(this));
      spawnAsEntity(worldIn, pos, new ItemStack(Blocks.VINE, 1, 0));
    } else {
      super.harvestBlock(worldIn, player, pos, state, te, stack);
    } 
  }
  
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.CUTOUT;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)SOUTH, Boolean.valueOf(((meta & 0x1) > 0))).withProperty((IProperty)WEST, Boolean.valueOf(((meta & 0x2) > 0))).withProperty((IProperty)NORTH, Boolean.valueOf(((meta & 0x4) > 0))).withProperty((IProperty)EAST, Boolean.valueOf(((meta & 0x8) > 0)));
  }
  
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    if (((Boolean)state.getValue((IProperty)SOUTH)).booleanValue())
      i |= 0x1; 
    if (((Boolean)state.getValue((IProperty)WEST)).booleanValue())
      i |= 0x2; 
    if (((Boolean)state.getValue((IProperty)NORTH)).booleanValue())
      i |= 0x4; 
    if (((Boolean)state.getValue((IProperty)EAST)).booleanValue())
      i |= 0x8; 
    return i;
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)UP, (IProperty)NORTH, (IProperty)EAST, (IProperty)SOUTH, (IProperty)WEST });
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
  
  public static PropertyBool getPropertyFor(EnumFacing side) {
    switch (side) {
      case UP:
        return UP;
      case NORTH:
        return NORTH;
      case SOUTH:
        return SOUTH;
      case WEST:
        return WEST;
      case EAST:
        return EAST;
    } 
    throw new IllegalArgumentException(side + " is an invalid choice");
  }
  
  public static int getNumGrownFaces(IBlockState state) {
    int i = 0;
    byte b;
    int j;
    PropertyBool[] arrayOfPropertyBool;
    for (j = (arrayOfPropertyBool = ALL_FACES).length, b = 0; b < j; ) {
      PropertyBool propertybool = arrayOfPropertyBool[b];
      if (((Boolean)state.getValue((IProperty)propertybool)).booleanValue())
        i++; 
      b++;
    } 
    return i;
  }
  
  public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    return BlockFaceShape.UNDEFINED;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockVine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */