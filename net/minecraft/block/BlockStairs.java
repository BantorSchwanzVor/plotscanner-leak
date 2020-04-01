package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStairs extends Block {
  public static final PropertyDirection FACING = BlockHorizontal.FACING;
  
  public static final PropertyEnum<EnumHalf> HALF = PropertyEnum.create("half", EnumHalf.class);
  
  public static final PropertyEnum<EnumShape> SHAPE = PropertyEnum.create("shape", EnumShape.class);
  
  protected static final AxisAlignedBB AABB_SLAB_TOP = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
  
  protected static final AxisAlignedBB AABB_QTR_TOP_WEST = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 1.0D);
  
  protected static final AxisAlignedBB AABB_QTR_TOP_EAST = new AxisAlignedBB(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
  
  protected static final AxisAlignedBB AABB_QTR_TOP_NORTH = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D);
  
  protected static final AxisAlignedBB AABB_QTR_TOP_SOUTH = new AxisAlignedBB(0.0D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D);
  
  protected static final AxisAlignedBB AABB_OCT_TOP_NW = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 0.5D);
  
  protected static final AxisAlignedBB AABB_OCT_TOP_NE = new AxisAlignedBB(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D);
  
  protected static final AxisAlignedBB AABB_OCT_TOP_SW = new AxisAlignedBB(0.0D, 0.5D, 0.5D, 0.5D, 1.0D, 1.0D);
  
  protected static final AxisAlignedBB AABB_OCT_TOP_SE = new AxisAlignedBB(0.5D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D);
  
  protected static final AxisAlignedBB AABB_SLAB_BOTTOM = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
  
  protected static final AxisAlignedBB AABB_QTR_BOT_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 1.0D);
  
  protected static final AxisAlignedBB AABB_QTR_BOT_EAST = new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
  
  protected static final AxisAlignedBB AABB_QTR_BOT_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
  
  protected static final AxisAlignedBB AABB_QTR_BOT_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
  
  protected static final AxisAlignedBB AABB_OCT_BOT_NW = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 0.5D);
  
  protected static final AxisAlignedBB AABB_OCT_BOT_NE = new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
  
  protected static final AxisAlignedBB AABB_OCT_BOT_SW = new AxisAlignedBB(0.0D, 0.0D, 0.5D, 0.5D, 0.5D, 1.0D);
  
  protected static final AxisAlignedBB AABB_OCT_BOT_SE = new AxisAlignedBB(0.5D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
  
  private final Block modelBlock;
  
  private final IBlockState modelState;
  
  protected BlockStairs(IBlockState modelState) {
    super((modelState.getBlock()).blockMaterial);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH).withProperty((IProperty)HALF, EnumHalf.BOTTOM).withProperty((IProperty)SHAPE, EnumShape.STRAIGHT));
    this.modelBlock = modelState.getBlock();
    this.modelState = modelState;
    setHardness(this.modelBlock.blockHardness);
    setResistance(this.modelBlock.blockResistance / 3.0F);
    setSoundType(this.modelBlock.blockSoundType);
    setLightOpacity(255);
    setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
  }
  
  public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
    if (!p_185477_7_)
      state = getActualState(state, (IBlockAccess)worldIn, pos); 
    for (AxisAlignedBB axisalignedbb : getCollisionBoxList(state))
      addCollisionBoxToList(pos, entityBox, collidingBoxes, axisalignedbb); 
  }
  
  private static List<AxisAlignedBB> getCollisionBoxList(IBlockState bstate) {
    List<AxisAlignedBB> list = Lists.newArrayList();
    boolean flag = (bstate.getValue((IProperty)HALF) == EnumHalf.TOP);
    list.add(flag ? AABB_SLAB_TOP : AABB_SLAB_BOTTOM);
    EnumShape blockstairs$enumshape = (EnumShape)bstate.getValue((IProperty)SHAPE);
    if (blockstairs$enumshape == EnumShape.STRAIGHT || blockstairs$enumshape == EnumShape.INNER_LEFT || blockstairs$enumshape == EnumShape.INNER_RIGHT)
      list.add(getCollQuarterBlock(bstate)); 
    if (blockstairs$enumshape != EnumShape.STRAIGHT)
      list.add(getCollEighthBlock(bstate)); 
    return list;
  }
  
  private static AxisAlignedBB getCollQuarterBlock(IBlockState bstate) {
    boolean flag = (bstate.getValue((IProperty)HALF) == EnumHalf.TOP);
    switch ((EnumFacing)bstate.getValue((IProperty)FACING)) {
      default:
        return flag ? AABB_QTR_BOT_NORTH : AABB_QTR_TOP_NORTH;
      case SOUTH:
        return flag ? AABB_QTR_BOT_SOUTH : AABB_QTR_TOP_SOUTH;
      case WEST:
        return flag ? AABB_QTR_BOT_WEST : AABB_QTR_TOP_WEST;
      case EAST:
        break;
    } 
    return flag ? AABB_QTR_BOT_EAST : AABB_QTR_TOP_EAST;
  }
  
  private static AxisAlignedBB getCollEighthBlock(IBlockState bstate) {
    EnumFacing enumfacing1, enumfacing = (EnumFacing)bstate.getValue((IProperty)FACING);
    switch ((EnumShape)bstate.getValue((IProperty)SHAPE)) {
      default:
        enumfacing1 = enumfacing;
        break;
      case OUTER_RIGHT:
        enumfacing1 = enumfacing.rotateY();
        break;
      case INNER_RIGHT:
        enumfacing1 = enumfacing.getOpposite();
        break;
      case null:
        enumfacing1 = enumfacing.rotateYCCW();
        break;
    } 
    boolean flag = (bstate.getValue((IProperty)HALF) == EnumHalf.TOP);
    switch (enumfacing1) {
      default:
        return flag ? AABB_OCT_BOT_NW : AABB_OCT_TOP_NW;
      case SOUTH:
        return flag ? AABB_OCT_BOT_SE : AABB_OCT_TOP_SE;
      case WEST:
        return flag ? AABB_OCT_BOT_SW : AABB_OCT_TOP_SW;
      case EAST:
        break;
    } 
    return flag ? AABB_OCT_BOT_NE : AABB_OCT_TOP_NE;
  }
  
  public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    p_193383_2_ = getActualState(p_193383_2_, p_193383_1_, p_193383_3_);
    if (p_193383_4_.getAxis() == EnumFacing.Axis.Y)
      return (((p_193383_4_ == EnumFacing.UP) ? true : false) == ((p_193383_2_.getValue((IProperty)HALF) == EnumHalf.TOP) ? true : false)) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED; 
    EnumShape blockstairs$enumshape = (EnumShape)p_193383_2_.getValue((IProperty)SHAPE);
    if (blockstairs$enumshape != EnumShape.OUTER_LEFT && blockstairs$enumshape != EnumShape.OUTER_RIGHT) {
      EnumFacing enumfacing = (EnumFacing)p_193383_2_.getValue((IProperty)FACING);
      switch (blockstairs$enumshape) {
        case INNER_RIGHT:
          return (enumfacing != p_193383_4_ && enumfacing != p_193383_4_.rotateYCCW()) ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
        case null:
          return (enumfacing != p_193383_4_ && enumfacing != p_193383_4_.rotateY()) ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
        case STRAIGHT:
          return (enumfacing == p_193383_4_) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
      } 
      return BlockFaceShape.UNDEFINED;
    } 
    return BlockFaceShape.UNDEFINED;
  }
  
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  
  public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
    this.modelBlock.randomDisplayTick(stateIn, worldIn, pos, rand);
  }
  
  public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
    this.modelBlock.onBlockClicked(worldIn, pos, playerIn);
  }
  
  public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
    this.modelBlock.onBlockDestroyedByPlayer(worldIn, pos, state);
  }
  
  public int getPackedLightmapCoords(IBlockState state, IBlockAccess source, BlockPos pos) {
    return this.modelState.getPackedLightmapCoords(source, pos);
  }
  
  public float getExplosionResistance(Entity exploder) {
    return this.modelBlock.getExplosionResistance(exploder);
  }
  
  public BlockRenderLayer getBlockLayer() {
    return this.modelBlock.getBlockLayer();
  }
  
  public int tickRate(World worldIn) {
    return this.modelBlock.tickRate(worldIn);
  }
  
  public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
    return this.modelState.getSelectedBoundingBox(worldIn, pos);
  }
  
  public Vec3d modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3d motion) {
    return this.modelBlock.modifyAcceleration(worldIn, pos, entityIn, motion);
  }
  
  public boolean isCollidable() {
    return this.modelBlock.isCollidable();
  }
  
  public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) {
    return this.modelBlock.canCollideCheck(state, hitIfLiquid);
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    return this.modelBlock.canPlaceBlockAt(worldIn, pos);
  }
  
  public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    this.modelState.neighborChanged(worldIn, pos, Blocks.AIR, pos);
    this.modelBlock.onBlockAdded(worldIn, pos, this.modelState);
  }
  
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    this.modelBlock.breakBlock(worldIn, pos, this.modelState);
  }
  
  public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
    this.modelBlock.onEntityWalk(worldIn, pos, entityIn);
  }
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    this.modelBlock.updateTick(worldIn, pos, state, rand);
  }
  
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
    return this.modelBlock.onBlockActivated(worldIn, pos, this.modelState, playerIn, hand, EnumFacing.DOWN, 0.0F, 0.0F, 0.0F);
  }
  
  public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
    this.modelBlock.onBlockDestroyedByExplosion(worldIn, pos, explosionIn);
  }
  
  public boolean isFullyOpaque(IBlockState state) {
    return (state.getValue((IProperty)HALF) == EnumHalf.TOP);
  }
  
  public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_) {
    return this.modelBlock.getMapColor(this.modelState, p_180659_2_, p_180659_3_);
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    IBlockState iblockstate = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
    iblockstate = iblockstate.withProperty((IProperty)FACING, (Comparable)placer.getHorizontalFacing()).withProperty((IProperty)SHAPE, EnumShape.STRAIGHT);
    return (facing != EnumFacing.DOWN && (facing == EnumFacing.UP || hitY <= 0.5D)) ? iblockstate.withProperty((IProperty)HALF, EnumHalf.BOTTOM) : iblockstate.withProperty((IProperty)HALF, EnumHalf.TOP);
  }
  
  @Nullable
  public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
    List<RayTraceResult> list = Lists.newArrayList();
    for (AxisAlignedBB axisalignedbb : getCollisionBoxList(getActualState(blockState, (IBlockAccess)worldIn, pos)))
      list.add(rayTrace(pos, start, end, axisalignedbb)); 
    RayTraceResult raytraceresult1 = null;
    double d1 = 0.0D;
    for (RayTraceResult raytraceresult : list) {
      if (raytraceresult != null) {
        double d0 = raytraceresult.hitVec.squareDistanceTo(end);
        if (d0 > d1) {
          raytraceresult1 = raytraceresult;
          d1 = d0;
        } 
      } 
    } 
    return raytraceresult1;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    IBlockState iblockstate = getDefaultState().withProperty((IProperty)HALF, ((meta & 0x4) > 0) ? EnumHalf.TOP : EnumHalf.BOTTOM);
    iblockstate = iblockstate.withProperty((IProperty)FACING, (Comparable)EnumFacing.getFront(5 - (meta & 0x3)));
    return iblockstate;
  }
  
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    if (state.getValue((IProperty)HALF) == EnumHalf.TOP)
      i |= 0x4; 
    i |= 5 - ((EnumFacing)state.getValue((IProperty)FACING)).getIndex();
    return i;
  }
  
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    return state.withProperty((IProperty)SHAPE, getStairsShape(state, worldIn, pos));
  }
  
  private static EnumShape getStairsShape(IBlockState p_185706_0_, IBlockAccess p_185706_1_, BlockPos p_185706_2_) {
    EnumFacing enumfacing = (EnumFacing)p_185706_0_.getValue((IProperty)FACING);
    IBlockState iblockstate = p_185706_1_.getBlockState(p_185706_2_.offset(enumfacing));
    if (isBlockStairs(iblockstate) && p_185706_0_.getValue((IProperty)HALF) == iblockstate.getValue((IProperty)HALF)) {
      EnumFacing enumfacing1 = (EnumFacing)iblockstate.getValue((IProperty)FACING);
      if (enumfacing1.getAxis() != ((EnumFacing)p_185706_0_.getValue((IProperty)FACING)).getAxis() && isDifferentStairs(p_185706_0_, p_185706_1_, p_185706_2_, enumfacing1.getOpposite())) {
        if (enumfacing1 == enumfacing.rotateYCCW())
          return EnumShape.OUTER_LEFT; 
        return EnumShape.OUTER_RIGHT;
      } 
    } 
    IBlockState iblockstate1 = p_185706_1_.getBlockState(p_185706_2_.offset(enumfacing.getOpposite()));
    if (isBlockStairs(iblockstate1) && p_185706_0_.getValue((IProperty)HALF) == iblockstate1.getValue((IProperty)HALF)) {
      EnumFacing enumfacing2 = (EnumFacing)iblockstate1.getValue((IProperty)FACING);
      if (enumfacing2.getAxis() != ((EnumFacing)p_185706_0_.getValue((IProperty)FACING)).getAxis() && isDifferentStairs(p_185706_0_, p_185706_1_, p_185706_2_, enumfacing2)) {
        if (enumfacing2 == enumfacing.rotateYCCW())
          return EnumShape.INNER_LEFT; 
        return EnumShape.INNER_RIGHT;
      } 
    } 
    return EnumShape.STRAIGHT;
  }
  
  private static boolean isDifferentStairs(IBlockState p_185704_0_, IBlockAccess p_185704_1_, BlockPos p_185704_2_, EnumFacing p_185704_3_) {
    IBlockState iblockstate = p_185704_1_.getBlockState(p_185704_2_.offset(p_185704_3_));
    return !(isBlockStairs(iblockstate) && iblockstate.getValue((IProperty)FACING) == p_185704_0_.getValue((IProperty)FACING) && iblockstate.getValue((IProperty)HALF) == p_185704_0_.getValue((IProperty)HALF));
  }
  
  public static boolean isBlockStairs(IBlockState state) {
    return state.getBlock() instanceof BlockStairs;
  }
  
  public IBlockState withRotation(IBlockState state, Rotation rot) {
    return state.withProperty((IProperty)FACING, (Comparable)rot.rotate((EnumFacing)state.getValue((IProperty)FACING)));
  }
  
  public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
    EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
    EnumShape blockstairs$enumshape = (EnumShape)state.getValue((IProperty)SHAPE);
    switch (mirrorIn) {
      case LEFT_RIGHT:
        if (enumfacing.getAxis() == EnumFacing.Axis.Z) {
          switch (blockstairs$enumshape) {
            case OUTER_LEFT:
              return state.withRotation(Rotation.CLOCKWISE_180).withProperty((IProperty)SHAPE, EnumShape.OUTER_RIGHT);
            case OUTER_RIGHT:
              return state.withRotation(Rotation.CLOCKWISE_180).withProperty((IProperty)SHAPE, EnumShape.OUTER_LEFT);
            case INNER_RIGHT:
              return state.withRotation(Rotation.CLOCKWISE_180).withProperty((IProperty)SHAPE, EnumShape.INNER_LEFT);
            case null:
              return state.withRotation(Rotation.CLOCKWISE_180).withProperty((IProperty)SHAPE, EnumShape.INNER_RIGHT);
          } 
          return state.withRotation(Rotation.CLOCKWISE_180);
        } 
        break;
      case null:
        if (enumfacing.getAxis() == EnumFacing.Axis.X)
          switch (blockstairs$enumshape) {
            case OUTER_LEFT:
              return state.withRotation(Rotation.CLOCKWISE_180).withProperty((IProperty)SHAPE, EnumShape.OUTER_RIGHT);
            case OUTER_RIGHT:
              return state.withRotation(Rotation.CLOCKWISE_180).withProperty((IProperty)SHAPE, EnumShape.OUTER_LEFT);
            case INNER_RIGHT:
              return state.withRotation(Rotation.CLOCKWISE_180).withProperty((IProperty)SHAPE, EnumShape.INNER_RIGHT);
            case null:
              return state.withRotation(Rotation.CLOCKWISE_180).withProperty((IProperty)SHAPE, EnumShape.INNER_LEFT);
            case STRAIGHT:
              return state.withRotation(Rotation.CLOCKWISE_180);
          }  
        break;
    } 
    return super.withMirror(state, mirrorIn);
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)FACING, (IProperty)HALF, (IProperty)SHAPE });
  }
  
  public enum EnumHalf implements IStringSerializable {
    TOP("top"),
    BOTTOM("bottom");
    
    private final String name;
    
    EnumHalf(String name) {
      this.name = name;
    }
    
    public String toString() {
      return this.name;
    }
    
    public String getName() {
      return this.name;
    }
  }
  
  public enum EnumShape implements IStringSerializable {
    STRAIGHT("straight"),
    INNER_LEFT("inner_left"),
    INNER_RIGHT("inner_right"),
    OUTER_LEFT("outer_left"),
    OUTER_RIGHT("outer_right");
    
    private final String name;
    
    EnumShape(String name) {
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


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockStairs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */