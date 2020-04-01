package net.minecraft.block;

import com.google.common.cache.LoadingCache;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPortal extends BlockBreakable {
  public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class, (Enum[])new EnumFacing.Axis[] { EnumFacing.Axis.X, EnumFacing.Axis.Z });
  
  protected static final AxisAlignedBB X_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D);
  
  protected static final AxisAlignedBB Z_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D);
  
  protected static final AxisAlignedBB Y_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D);
  
  public BlockPortal() {
    super(Material.PORTAL, false);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)AXIS, (Comparable)EnumFacing.Axis.X));
    setTickRandomly(true);
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    switch ((EnumFacing.Axis)state.getValue((IProperty)AXIS)) {
      case null:
        return X_AABB;
      default:
        return Y_AABB;
      case Z:
        break;
    } 
    return Z_AABB;
  }
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    super.updateTick(worldIn, pos, state, rand);
    if (worldIn.provider.isSurfaceWorld() && worldIn.getGameRules().getBoolean("doMobSpawning") && rand.nextInt(2000) < worldIn.getDifficulty().getDifficultyId()) {
      int i = pos.getY();
      BlockPos blockpos;
      for (blockpos = pos; !worldIn.getBlockState(blockpos).isFullyOpaque() && blockpos.getY() > 0; blockpos = blockpos.down());
      if (i > 0 && !worldIn.getBlockState(blockpos.up()).isNormalCube()) {
        Entity entity = ItemMonsterPlacer.spawnCreature(worldIn, EntityList.func_191306_a(EntityPigZombie.class), blockpos.getX() + 0.5D, blockpos.getY() + 1.1D, blockpos.getZ() + 0.5D);
        if (entity != null)
          entity.timeUntilPortal = entity.getPortalCooldown(); 
      } 
    } 
  }
  
  @Nullable
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
    return NULL_AABB;
  }
  
  public static int getMetaForAxis(EnumFacing.Axis axis) {
    if (axis == EnumFacing.Axis.X)
      return 1; 
    return (axis == EnumFacing.Axis.Z) ? 2 : 0;
  }
  
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  
  public boolean trySpawnPortal(World worldIn, BlockPos pos) {
    Size blockportal$size = new Size(worldIn, pos, EnumFacing.Axis.X);
    if (blockportal$size.isValid() && blockportal$size.portalBlockCount == 0) {
      blockportal$size.placePortalBlocks();
      return true;
    } 
    Size blockportal$size1 = new Size(worldIn, pos, EnumFacing.Axis.Z);
    if (blockportal$size1.isValid() && blockportal$size1.portalBlockCount == 0) {
      blockportal$size1.placePortalBlocks();
      return true;
    } 
    return false;
  }
  
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
    EnumFacing.Axis enumfacing$axis = (EnumFacing.Axis)state.getValue((IProperty)AXIS);
    if (enumfacing$axis == EnumFacing.Axis.X) {
      Size blockportal$size = new Size(worldIn, pos, EnumFacing.Axis.X);
      if (!blockportal$size.isValid() || blockportal$size.portalBlockCount < blockportal$size.width * blockportal$size.height)
        worldIn.setBlockState(pos, Blocks.AIR.getDefaultState()); 
    } else if (enumfacing$axis == EnumFacing.Axis.Z) {
      Size blockportal$size1 = new Size(worldIn, pos, EnumFacing.Axis.Z);
      if (!blockportal$size1.isValid() || blockportal$size1.portalBlockCount < blockportal$size1.width * blockportal$size1.height)
        worldIn.setBlockState(pos, Blocks.AIR.getDefaultState()); 
    } 
  }
  
  public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    pos = pos.offset(side);
    EnumFacing.Axis enumfacing$axis = null;
    if (blockState.getBlock() == this) {
      enumfacing$axis = (EnumFacing.Axis)blockState.getValue((IProperty)AXIS);
      if (enumfacing$axis == null)
        return false; 
      if (enumfacing$axis == EnumFacing.Axis.Z && side != EnumFacing.EAST && side != EnumFacing.WEST)
        return false; 
      if (enumfacing$axis == EnumFacing.Axis.X && side != EnumFacing.SOUTH && side != EnumFacing.NORTH)
        return false; 
    } 
    boolean flag = (blockAccess.getBlockState(pos.west()).getBlock() == this && blockAccess.getBlockState(pos.west(2)).getBlock() != this);
    boolean flag1 = (blockAccess.getBlockState(pos.east()).getBlock() == this && blockAccess.getBlockState(pos.east(2)).getBlock() != this);
    boolean flag2 = (blockAccess.getBlockState(pos.north()).getBlock() == this && blockAccess.getBlockState(pos.north(2)).getBlock() != this);
    boolean flag3 = (blockAccess.getBlockState(pos.south()).getBlock() == this && blockAccess.getBlockState(pos.south(2)).getBlock() != this);
    boolean flag4 = !(!flag && !flag1 && enumfacing$axis != EnumFacing.Axis.X);
    boolean flag5 = !(!flag2 && !flag3 && enumfacing$axis != EnumFacing.Axis.Z);
    if (flag4 && side == EnumFacing.WEST)
      return true; 
    if (flag4 && side == EnumFacing.EAST)
      return true; 
    if (flag5 && side == EnumFacing.NORTH)
      return true; 
    return (flag5 && side == EnumFacing.SOUTH);
  }
  
  public int quantityDropped(Random random) {
    return 0;
  }
  
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }
  
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
    if (!entityIn.isRiding() && !entityIn.isBeingRidden() && entityIn.isNonBoss())
      entityIn.setPortal(pos); 
  }
  
  public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
    if (rand.nextInt(100) == 0)
      worldIn.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS, 0.5F, rand.nextFloat() * 0.4F + 0.8F, false); 
    for (int i = 0; i < 4; i++) {
      double d0 = (pos.getX() + rand.nextFloat());
      double d1 = (pos.getY() + rand.nextFloat());
      double d2 = (pos.getZ() + rand.nextFloat());
      double d3 = (rand.nextFloat() - 0.5D) * 0.5D;
      double d4 = (rand.nextFloat() - 0.5D) * 0.5D;
      double d5 = (rand.nextFloat() - 0.5D) * 0.5D;
      int j = rand.nextInt(2) * 2 - 1;
      if (worldIn.getBlockState(pos.west()).getBlock() != this && worldIn.getBlockState(pos.east()).getBlock() != this) {
        d0 = pos.getX() + 0.5D + 0.25D * j;
        d3 = (rand.nextFloat() * 2.0F * j);
      } else {
        d2 = pos.getZ() + 0.5D + 0.25D * j;
        d5 = (rand.nextFloat() * 2.0F * j);
      } 
      worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5, new int[0]);
    } 
  }
  
  public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
    return ItemStack.field_190927_a;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)AXIS, ((meta & 0x3) == 2) ? (Comparable)EnumFacing.Axis.Z : (Comparable)EnumFacing.Axis.X);
  }
  
  public int getMetaFromState(IBlockState state) {
    return getMetaForAxis((EnumFacing.Axis)state.getValue((IProperty)AXIS));
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
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)AXIS });
  }
  
  public BlockPattern.PatternHelper createPatternHelper(World worldIn, BlockPos p_181089_2_) {
    EnumFacing.Axis enumfacing$axis = EnumFacing.Axis.Z;
    Size blockportal$size = new Size(worldIn, p_181089_2_, EnumFacing.Axis.X);
    LoadingCache<BlockPos, BlockWorldState> loadingcache = BlockPattern.createLoadingCache(worldIn, true);
    if (!blockportal$size.isValid()) {
      enumfacing$axis = EnumFacing.Axis.X;
      blockportal$size = new Size(worldIn, p_181089_2_, EnumFacing.Axis.Z);
    } 
    if (!blockportal$size.isValid())
      return new BlockPattern.PatternHelper(p_181089_2_, EnumFacing.NORTH, EnumFacing.UP, loadingcache, 1, 1, 1); 
    int[] aint = new int[(EnumFacing.AxisDirection.values()).length];
    EnumFacing enumfacing = blockportal$size.rightDir.rotateYCCW();
    BlockPos blockpos = blockportal$size.bottomLeft.up(blockportal$size.getHeight() - 1);
    byte b;
    int i;
    EnumFacing.AxisDirection[] arrayOfAxisDirection1;
    for (i = (arrayOfAxisDirection1 = EnumFacing.AxisDirection.values()).length, b = 0; b < i; ) {
      EnumFacing.AxisDirection enumfacing$axisdirection = arrayOfAxisDirection1[b];
      BlockPattern.PatternHelper blockpattern$patternhelper = new BlockPattern.PatternHelper((enumfacing.getAxisDirection() == enumfacing$axisdirection) ? blockpos : blockpos.offset(blockportal$size.rightDir, blockportal$size.getWidth() - 1), EnumFacing.getFacingFromAxis(enumfacing$axisdirection, enumfacing$axis), EnumFacing.UP, loadingcache, blockportal$size.getWidth(), blockportal$size.getHeight(), 1);
      for (int k = 0; k < blockportal$size.getWidth(); k++) {
        for (int m = 0; m < blockportal$size.getHeight(); m++) {
          BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(k, m, 1);
          if (blockworldstate.getBlockState() != null && blockworldstate.getBlockState().getMaterial() != Material.AIR)
            aint[enumfacing$axisdirection.ordinal()] = aint[enumfacing$axisdirection.ordinal()] + 1; 
        } 
      } 
      b++;
    } 
    EnumFacing.AxisDirection enumfacing$axisdirection1 = EnumFacing.AxisDirection.POSITIVE;
    EnumFacing.AxisDirection[] arrayOfAxisDirection2;
    for (int j = (arrayOfAxisDirection2 = EnumFacing.AxisDirection.values()).length; i < j; ) {
      EnumFacing.AxisDirection enumfacing$axisdirection2 = arrayOfAxisDirection2[i];
      if (aint[enumfacing$axisdirection2.ordinal()] < aint[enumfacing$axisdirection1.ordinal()])
        enumfacing$axisdirection1 = enumfacing$axisdirection2; 
      i++;
    } 
    return new BlockPattern.PatternHelper((enumfacing.getAxisDirection() == enumfacing$axisdirection1) ? blockpos : blockpos.offset(blockportal$size.rightDir, blockportal$size.getWidth() - 1), EnumFacing.getFacingFromAxis(enumfacing$axisdirection1, enumfacing$axis), EnumFacing.UP, loadingcache, blockportal$size.getWidth(), blockportal$size.getHeight(), 1);
  }
  
  public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    return BlockFaceShape.UNDEFINED;
  }
  
  public static class Size {
    private final World world;
    
    private final EnumFacing.Axis axis;
    
    private final EnumFacing rightDir;
    
    private final EnumFacing leftDir;
    
    private int portalBlockCount;
    
    private BlockPos bottomLeft;
    
    private int height;
    
    private int width;
    
    public Size(World worldIn, BlockPos p_i45694_2_, EnumFacing.Axis p_i45694_3_) {
      this.world = worldIn;
      this.axis = p_i45694_3_;
      if (p_i45694_3_ == EnumFacing.Axis.X) {
        this.leftDir = EnumFacing.EAST;
        this.rightDir = EnumFacing.WEST;
      } else {
        this.leftDir = EnumFacing.NORTH;
        this.rightDir = EnumFacing.SOUTH;
      } 
      for (BlockPos blockpos = p_i45694_2_; p_i45694_2_.getY() > blockpos.getY() - 21 && p_i45694_2_.getY() > 0 && isEmptyBlock(worldIn.getBlockState(p_i45694_2_.down()).getBlock()); p_i45694_2_ = p_i45694_2_.down());
      int i = getDistanceUntilEdge(p_i45694_2_, this.leftDir) - 1;
      if (i >= 0) {
        this.bottomLeft = p_i45694_2_.offset(this.leftDir, i);
        this.width = getDistanceUntilEdge(this.bottomLeft, this.rightDir);
        if (this.width < 2 || this.width > 21) {
          this.bottomLeft = null;
          this.width = 0;
        } 
      } 
      if (this.bottomLeft != null)
        this.height = calculatePortalHeight(); 
    }
    
    protected int getDistanceUntilEdge(BlockPos p_180120_1_, EnumFacing p_180120_2_) {
      int i;
      for (i = 0; i < 22; i++) {
        BlockPos blockpos = p_180120_1_.offset(p_180120_2_, i);
        if (!isEmptyBlock(this.world.getBlockState(blockpos).getBlock()) || this.world.getBlockState(blockpos.down()).getBlock() != Blocks.OBSIDIAN)
          break; 
      } 
      Block block = this.world.getBlockState(p_180120_1_.offset(p_180120_2_, i)).getBlock();
      return (block == Blocks.OBSIDIAN) ? i : 0;
    }
    
    public int getHeight() {
      return this.height;
    }
    
    public int getWidth() {
      return this.width;
    }
    
    protected int calculatePortalHeight() {
      label38: for (this.height = 0; this.height < 21; this.height++) {
        for (int i = 0; i < this.width; i++) {
          BlockPos blockpos = this.bottomLeft.offset(this.rightDir, i).up(this.height);
          Block block = this.world.getBlockState(blockpos).getBlock();
          if (!isEmptyBlock(block))
            break label38; 
          if (block == Blocks.PORTAL)
            this.portalBlockCount++; 
          if (i == 0) {
            block = this.world.getBlockState(blockpos.offset(this.leftDir)).getBlock();
            if (block != Blocks.OBSIDIAN)
              break label38; 
          } else if (i == this.width - 1) {
            block = this.world.getBlockState(blockpos.offset(this.rightDir)).getBlock();
            if (block != Blocks.OBSIDIAN)
              break label38; 
          } 
        } 
      } 
      for (int j = 0; j < this.width; j++) {
        if (this.world.getBlockState(this.bottomLeft.offset(this.rightDir, j).up(this.height)).getBlock() != Blocks.OBSIDIAN) {
          this.height = 0;
          break;
        } 
      } 
      if (this.height <= 21 && this.height >= 3)
        return this.height; 
      this.bottomLeft = null;
      this.width = 0;
      this.height = 0;
      return 0;
    }
    
    protected boolean isEmptyBlock(Block blockIn) {
      return !(blockIn.blockMaterial != Material.AIR && blockIn != Blocks.FIRE && blockIn != Blocks.PORTAL);
    }
    
    public boolean isValid() {
      return (this.bottomLeft != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21);
    }
    
    public void placePortalBlocks() {
      for (int i = 0; i < this.width; i++) {
        BlockPos blockpos = this.bottomLeft.offset(this.rightDir, i);
        for (int j = 0; j < this.height; j++)
          this.world.setBlockState(blockpos.up(j), Blocks.PORTAL.getDefaultState().withProperty((IProperty)BlockPortal.AXIS, (Comparable)this.axis), 2); 
      } 
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockPortal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */