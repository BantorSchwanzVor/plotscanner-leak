package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDoublePlant extends BlockBush implements IGrowable {
  public static final PropertyEnum<EnumPlantType> VARIANT = PropertyEnum.create("variant", EnumPlantType.class);
  
  public static final PropertyEnum<EnumBlockHalf> HALF = PropertyEnum.create("half", EnumBlockHalf.class);
  
  public static final PropertyEnum<EnumFacing> FACING = (PropertyEnum<EnumFacing>)BlockHorizontal.FACING;
  
  public BlockDoublePlant() {
    super(Material.VINE);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)VARIANT, EnumPlantType.SUNFLOWER).withProperty((IProperty)HALF, EnumBlockHalf.LOWER).withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH));
    setHardness(0.0F);
    setSoundType(SoundType.PLANT);
    setUnlocalizedName("doublePlant");
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return FULL_BLOCK_AABB;
  }
  
  private EnumPlantType getType(IBlockAccess blockAccess, BlockPos pos, IBlockState state) {
    if (state.getBlock() == this) {
      state = state.getActualState(blockAccess, pos);
      return (EnumPlantType)state.getValue((IProperty)VARIANT);
    } 
    return EnumPlantType.FERN;
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    return (super.canPlaceBlockAt(worldIn, pos) && worldIn.isAirBlock(pos.up()));
  }
  
  public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
    IBlockState iblockstate = worldIn.getBlockState(pos);
    if (iblockstate.getBlock() != this)
      return true; 
    EnumPlantType blockdoubleplant$enumplanttype = (EnumPlantType)iblockstate.getActualState(worldIn, pos).getValue((IProperty)VARIANT);
    return !(blockdoubleplant$enumplanttype != EnumPlantType.FERN && blockdoubleplant$enumplanttype != EnumPlantType.GRASS);
  }
  
  protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
    if (!canBlockStay(worldIn, pos, state)) {
      boolean flag = (state.getValue((IProperty)HALF) == EnumBlockHalf.UPPER);
      BlockPos blockpos = flag ? pos : pos.up();
      BlockPos blockpos1 = flag ? pos.down() : pos;
      Block block = flag ? this : worldIn.getBlockState(blockpos).getBlock();
      Block block1 = flag ? worldIn.getBlockState(blockpos1).getBlock() : this;
      if (block == this)
        worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 2); 
      if (block1 == this) {
        worldIn.setBlockState(blockpos1, Blocks.AIR.getDefaultState(), 3);
        if (!flag)
          dropBlockAsItem(worldIn, blockpos1, state, 0); 
      } 
    } 
  }
  
  public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
    if (state.getValue((IProperty)HALF) == EnumBlockHalf.UPPER)
      return (worldIn.getBlockState(pos.down()).getBlock() == this); 
    IBlockState iblockstate = worldIn.getBlockState(pos.up());
    return (iblockstate.getBlock() == this && super.canBlockStay(worldIn, pos, iblockstate));
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    if (state.getValue((IProperty)HALF) == EnumBlockHalf.UPPER)
      return Items.field_190931_a; 
    EnumPlantType blockdoubleplant$enumplanttype = (EnumPlantType)state.getValue((IProperty)VARIANT);
    if (blockdoubleplant$enumplanttype == EnumPlantType.FERN)
      return Items.field_190931_a; 
    if (blockdoubleplant$enumplanttype == EnumPlantType.GRASS)
      return (rand.nextInt(8) == 0) ? Items.WHEAT_SEEDS : Items.field_190931_a; 
    return super.getItemDropped(state, rand, fortune);
  }
  
  public int damageDropped(IBlockState state) {
    return (state.getValue((IProperty)HALF) != EnumBlockHalf.UPPER && state.getValue((IProperty)VARIANT) != EnumPlantType.GRASS) ? ((EnumPlantType)state.getValue((IProperty)VARIANT)).getMeta() : 0;
  }
  
  public void placeAt(World worldIn, BlockPos lowerPos, EnumPlantType variant, int flags) {
    worldIn.setBlockState(lowerPos, getDefaultState().withProperty((IProperty)HALF, EnumBlockHalf.LOWER).withProperty((IProperty)VARIANT, variant), flags);
    worldIn.setBlockState(lowerPos.up(), getDefaultState().withProperty((IProperty)HALF, EnumBlockHalf.UPPER), flags);
  }
  
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    worldIn.setBlockState(pos.up(), getDefaultState().withProperty((IProperty)HALF, EnumBlockHalf.UPPER), 2);
  }
  
  public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
    if (worldIn.isRemote || stack.getItem() != Items.SHEARS || state.getValue((IProperty)HALF) != EnumBlockHalf.LOWER || !onHarvest(worldIn, pos, state, player))
      super.harvestBlock(worldIn, player, pos, state, te, stack); 
  }
  
  public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
    if (state.getValue((IProperty)HALF) == EnumBlockHalf.UPPER) {
      if (worldIn.getBlockState(pos.down()).getBlock() == this)
        if (player.capabilities.isCreativeMode) {
          worldIn.setBlockToAir(pos.down());
        } else {
          IBlockState iblockstate = worldIn.getBlockState(pos.down());
          EnumPlantType blockdoubleplant$enumplanttype = (EnumPlantType)iblockstate.getValue((IProperty)VARIANT);
          if (blockdoubleplant$enumplanttype != EnumPlantType.FERN && blockdoubleplant$enumplanttype != EnumPlantType.GRASS) {
            worldIn.destroyBlock(pos.down(), true);
          } else if (worldIn.isRemote) {
            worldIn.setBlockToAir(pos.down());
          } else if (!player.getHeldItemMainhand().func_190926_b() && player.getHeldItemMainhand().getItem() == Items.SHEARS) {
            onHarvest(worldIn, pos, iblockstate, player);
            worldIn.setBlockToAir(pos.down());
          } else {
            worldIn.destroyBlock(pos.down(), true);
          } 
        }  
    } else if (worldIn.getBlockState(pos.up()).getBlock() == this) {
      worldIn.setBlockState(pos.up(), Blocks.AIR.getDefaultState(), 2);
    } 
    super.onBlockHarvested(worldIn, pos, state, player);
  }
  
  private boolean onHarvest(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
    EnumPlantType blockdoubleplant$enumplanttype = (EnumPlantType)state.getValue((IProperty)VARIANT);
    if (blockdoubleplant$enumplanttype != EnumPlantType.FERN && blockdoubleplant$enumplanttype != EnumPlantType.GRASS)
      return false; 
    player.addStat(StatList.getBlockStats(this));
    int i = ((blockdoubleplant$enumplanttype == EnumPlantType.GRASS) ? BlockTallGrass.EnumType.GRASS : BlockTallGrass.EnumType.FERN).getMeta();
    spawnAsEntity(worldIn, pos, new ItemStack(Blocks.TALLGRASS, 2, i));
    return true;
  }
  
  public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
    byte b;
    int i;
    EnumPlantType[] arrayOfEnumPlantType;
    for (i = (arrayOfEnumPlantType = EnumPlantType.values()).length, b = 0; b < i; ) {
      EnumPlantType blockdoubleplant$enumplanttype = arrayOfEnumPlantType[b];
      tab.add(new ItemStack(this, 1, blockdoubleplant$enumplanttype.getMeta()));
      b++;
    } 
  }
  
  public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
    return new ItemStack(this, 1, getType((IBlockAccess)worldIn, pos, state).getMeta());
  }
  
  public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
    EnumPlantType blockdoubleplant$enumplanttype = getType((IBlockAccess)worldIn, pos, state);
    return (blockdoubleplant$enumplanttype != EnumPlantType.GRASS && blockdoubleplant$enumplanttype != EnumPlantType.FERN);
  }
  
  public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
    return true;
  }
  
  public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
    spawnAsEntity(worldIn, pos, new ItemStack(this, 1, getType((IBlockAccess)worldIn, pos, state).getMeta()));
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return ((meta & 0x8) > 0) ? getDefaultState().withProperty((IProperty)HALF, EnumBlockHalf.UPPER) : getDefaultState().withProperty((IProperty)HALF, EnumBlockHalf.LOWER).withProperty((IProperty)VARIANT, EnumPlantType.byMetadata(meta & 0x7));
  }
  
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    if (state.getValue((IProperty)HALF) == EnumBlockHalf.UPPER) {
      IBlockState iblockstate = worldIn.getBlockState(pos.down());
      if (iblockstate.getBlock() == this)
        state = state.withProperty((IProperty)VARIANT, iblockstate.getValue((IProperty)VARIANT)); 
    } 
    return state;
  }
  
  public int getMetaFromState(IBlockState state) {
    return (state.getValue((IProperty)HALF) == EnumBlockHalf.UPPER) ? (0x8 | ((EnumFacing)state.getValue((IProperty)FACING)).getHorizontalIndex()) : ((EnumPlantType)state.getValue((IProperty)VARIANT)).getMeta();
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)HALF, (IProperty)VARIANT, (IProperty)FACING });
  }
  
  public Block.EnumOffsetType getOffsetType() {
    return Block.EnumOffsetType.XZ;
  }
  
  public enum EnumBlockHalf implements IStringSerializable {
    UPPER, LOWER;
    
    public String toString() {
      return getName();
    }
    
    public String getName() {
      return (this == UPPER) ? "upper" : "lower";
    }
  }
  
  public enum EnumPlantType implements IStringSerializable {
    SUNFLOWER(0, "sunflower"),
    SYRINGA(1, "syringa"),
    GRASS(2, "double_grass", "grass"),
    FERN(3, "double_fern", "fern"),
    ROSE(4, "double_rose", "rose"),
    PAEONIA(5, "paeonia");
    
    private static final EnumPlantType[] META_LOOKUP = new EnumPlantType[(values()).length];
    
    private final int meta;
    
    private final String name;
    
    private final String unlocalizedName;
    
    static {
      byte b;
      int i;
      EnumPlantType[] arrayOfEnumPlantType;
      for (i = (arrayOfEnumPlantType = values()).length, b = 0; b < i; ) {
        EnumPlantType blockdoubleplant$enumplanttype = arrayOfEnumPlantType[b];
        META_LOOKUP[blockdoubleplant$enumplanttype.getMeta()] = blockdoubleplant$enumplanttype;
        b++;
      } 
    }
    
    EnumPlantType(int meta, String name, String unlocalizedName) {
      this.meta = meta;
      this.name = name;
      this.unlocalizedName = unlocalizedName;
    }
    
    public int getMeta() {
      return this.meta;
    }
    
    public String toString() {
      return this.name;
    }
    
    public static EnumPlantType byMetadata(int meta) {
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


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockDoublePlant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */