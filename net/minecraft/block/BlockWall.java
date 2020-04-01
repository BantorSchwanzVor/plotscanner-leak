package net.minecraft.block;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWall extends Block {
  public static final PropertyBool UP = PropertyBool.create("up");
  
  public static final PropertyBool NORTH = PropertyBool.create("north");
  
  public static final PropertyBool EAST = PropertyBool.create("east");
  
  public static final PropertyBool SOUTH = PropertyBool.create("south");
  
  public static final PropertyBool WEST = PropertyBool.create("west");
  
  public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
  
  protected static final AxisAlignedBB[] AABB_BY_INDEX = new AxisAlignedBB[] { 
      new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D), new AxisAlignedBB(0.25D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.3125D, 0.0D, 0.0D, 0.6875D, 0.875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 1.0D), new AxisAlignedBB(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D), 
      new AxisAlignedBB(0.0D, 0.0D, 0.3125D, 1.0D, 0.875D, 0.6875D), new AxisAlignedBB(0.0D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D) };
  
  protected static final AxisAlignedBB[] CLIP_AABB_BY_INDEX = new AxisAlignedBB[] { 
      AABB_BY_INDEX[0].setMaxY(1.5D), AABB_BY_INDEX[1].setMaxY(1.5D), AABB_BY_INDEX[2].setMaxY(1.5D), AABB_BY_INDEX[3].setMaxY(1.5D), AABB_BY_INDEX[4].setMaxY(1.5D), AABB_BY_INDEX[5].setMaxY(1.5D), AABB_BY_INDEX[6].setMaxY(1.5D), AABB_BY_INDEX[7].setMaxY(1.5D), AABB_BY_INDEX[8].setMaxY(1.5D), AABB_BY_INDEX[9].setMaxY(1.5D), 
      AABB_BY_INDEX[10].setMaxY(1.5D), AABB_BY_INDEX[11].setMaxY(1.5D), AABB_BY_INDEX[12].setMaxY(1.5D), AABB_BY_INDEX[13].setMaxY(1.5D), AABB_BY_INDEX[14].setMaxY(1.5D), AABB_BY_INDEX[15].setMaxY(1.5D) };
  
  public BlockWall(Block modelBlock) {
    super(modelBlock.blockMaterial);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)UP, Boolean.valueOf(false)).withProperty((IProperty)NORTH, Boolean.valueOf(false)).withProperty((IProperty)EAST, Boolean.valueOf(false)).withProperty((IProperty)SOUTH, Boolean.valueOf(false)).withProperty((IProperty)WEST, Boolean.valueOf(false)).withProperty((IProperty)VARIANT, EnumType.NORMAL));
    setHardness(modelBlock.blockHardness);
    setResistance(modelBlock.blockResistance / 3.0F);
    setSoundType(modelBlock.blockSoundType);
    setCreativeTab(CreativeTabs.DECORATIONS);
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    state = getActualState(state, source, pos);
    return AABB_BY_INDEX[getAABBIndex(state)];
  }
  
  public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
    if (!p_185477_7_)
      state = getActualState(state, (IBlockAccess)worldIn, pos); 
    addCollisionBoxToList(pos, entityBox, collidingBoxes, CLIP_AABB_BY_INDEX[getAABBIndex(state)]);
  }
  
  @Nullable
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
    blockState = getActualState(blockState, worldIn, pos);
    return CLIP_AABB_BY_INDEX[getAABBIndex(blockState)];
  }
  
  private static int getAABBIndex(IBlockState state) {
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
  
  public String getLocalizedName() {
    return I18n.translateToLocal(String.valueOf(getUnlocalizedName()) + "." + EnumType.NORMAL.getUnlocalizedName() + ".name");
  }
  
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  
  public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
    return false;
  }
  
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  
  private boolean canConnectTo(IBlockAccess worldIn, BlockPos pos, EnumFacing p_176253_3_) {
    IBlockState iblockstate = worldIn.getBlockState(pos);
    Block block = iblockstate.getBlock();
    BlockFaceShape blockfaceshape = iblockstate.func_193401_d(worldIn, pos, p_176253_3_);
    boolean flag = !(blockfaceshape != BlockFaceShape.MIDDLE_POLE_THICK && (blockfaceshape != BlockFaceShape.MIDDLE_POLE || !(block instanceof BlockFenceGate)));
    return !((func_194143_e(block) || blockfaceshape != BlockFaceShape.SOLID) && !flag);
  }
  
  protected static boolean func_194143_e(Block p_194143_0_) {
    return !(!Block.func_193382_c(p_194143_0_) && p_194143_0_ != Blocks.BARRIER && p_194143_0_ != Blocks.MELON_BLOCK && p_194143_0_ != Blocks.PUMPKIN && p_194143_0_ != Blocks.LIT_PUMPKIN);
  }
  
  public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
    byte b;
    int i;
    EnumType[] arrayOfEnumType;
    for (i = (arrayOfEnumType = EnumType.values()).length, b = 0; b < i; ) {
      EnumType blockwall$enumtype = arrayOfEnumType[b];
      tab.add(new ItemStack(this, 1, blockwall$enumtype.getMetadata()));
      b++;
    } 
  }
  
  public int damageDropped(IBlockState state) {
    return ((EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
  }
  
  public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return (side == EnumFacing.DOWN) ? super.shouldSideBeRendered(blockState, blockAccess, pos, side) : true;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)VARIANT, EnumType.byMetadata(meta));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
  }
  
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    boolean flag = canConnectTo(worldIn, pos.north(), EnumFacing.SOUTH);
    boolean flag1 = canConnectTo(worldIn, pos.east(), EnumFacing.WEST);
    boolean flag2 = canConnectTo(worldIn, pos.south(), EnumFacing.NORTH);
    boolean flag3 = canConnectTo(worldIn, pos.west(), EnumFacing.EAST);
    boolean flag4 = !((!flag || flag1 || !flag2 || flag3) && (flag || !flag1 || flag2 || !flag3));
    return state.withProperty((IProperty)UP, Boolean.valueOf(!(flag4 && worldIn.isAirBlock(pos.up())))).withProperty((IProperty)NORTH, Boolean.valueOf(flag)).withProperty((IProperty)EAST, Boolean.valueOf(flag1)).withProperty((IProperty)SOUTH, Boolean.valueOf(flag2)).withProperty((IProperty)WEST, Boolean.valueOf(flag3));
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)UP, (IProperty)NORTH, (IProperty)EAST, (IProperty)WEST, (IProperty)SOUTH, (IProperty)VARIANT });
  }
  
  public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    return (p_193383_4_ != EnumFacing.UP && p_193383_4_ != EnumFacing.DOWN) ? BlockFaceShape.MIDDLE_POLE_THICK : BlockFaceShape.CENTER_BIG;
  }
  
  public enum EnumType implements IStringSerializable {
    NORMAL(0, "cobblestone", "normal"),
    MOSSY(1, "mossy_cobblestone", "mossy");
    
    private static final EnumType[] META_LOOKUP = new EnumType[(values()).length];
    
    private final int meta;
    
    private final String name;
    
    private final String unlocalizedName;
    
    static {
      byte b;
      int i;
      EnumType[] arrayOfEnumType;
      for (i = (arrayOfEnumType = values()).length, b = 0; b < i; ) {
        EnumType blockwall$enumtype = arrayOfEnumType[b];
        META_LOOKUP[blockwall$enumtype.getMetadata()] = blockwall$enumtype;
        b++;
      } 
    }
    
    EnumType(int meta, String name, String unlocalizedName) {
      this.meta = meta;
      this.name = name;
      this.unlocalizedName = unlocalizedName;
    }
    
    public int getMetadata() {
      return this.meta;
    }
    
    public String toString() {
      return this.name;
    }
    
    public static EnumType byMetadata(int meta) {
      if (meta < 0 || meta >= META_LOOKUP.length)
        meta = 0; 
      return META_LOOKUP[meta];
    }
    
    public String getName() {
      return this.name;
    }
    
    public String getUnlocalizedName() {
      return this.unlocalizedName;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockWall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */