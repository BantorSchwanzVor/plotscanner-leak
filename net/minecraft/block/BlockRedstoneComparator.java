package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityComparator;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneComparator extends BlockRedstoneDiode implements ITileEntityProvider {
  public static final PropertyBool POWERED = PropertyBool.create("powered");
  
  public static final PropertyEnum<Mode> MODE = PropertyEnum.create("mode", Mode.class);
  
  public BlockRedstoneComparator(boolean powered) {
    super(powered);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH).withProperty((IProperty)POWERED, Boolean.valueOf(false)).withProperty((IProperty)MODE, Mode.COMPARE));
    this.isBlockContainer = true;
  }
  
  public String getLocalizedName() {
    return I18n.translateToLocal("item.comparator.name");
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Items.COMPARATOR;
  }
  
  public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
    return new ItemStack(Items.COMPARATOR);
  }
  
  protected int getDelay(IBlockState state) {
    return 2;
  }
  
  protected IBlockState getPoweredState(IBlockState unpoweredState) {
    Boolean obool = (Boolean)unpoweredState.getValue((IProperty)POWERED);
    Mode blockredstonecomparator$mode = (Mode)unpoweredState.getValue((IProperty)MODE);
    EnumFacing enumfacing = (EnumFacing)unpoweredState.getValue((IProperty)FACING);
    return Blocks.POWERED_COMPARATOR.getDefaultState().withProperty((IProperty)FACING, (Comparable)enumfacing).withProperty((IProperty)POWERED, obool).withProperty((IProperty)MODE, blockredstonecomparator$mode);
  }
  
  protected IBlockState getUnpoweredState(IBlockState poweredState) {
    Boolean obool = (Boolean)poweredState.getValue((IProperty)POWERED);
    Mode blockredstonecomparator$mode = (Mode)poweredState.getValue((IProperty)MODE);
    EnumFacing enumfacing = (EnumFacing)poweredState.getValue((IProperty)FACING);
    return Blocks.UNPOWERED_COMPARATOR.getDefaultState().withProperty((IProperty)FACING, (Comparable)enumfacing).withProperty((IProperty)POWERED, obool).withProperty((IProperty)MODE, blockredstonecomparator$mode);
  }
  
  protected boolean isPowered(IBlockState state) {
    return !(!this.isRepeaterPowered && !((Boolean)state.getValue((IProperty)POWERED)).booleanValue());
  }
  
  protected int getActiveSignal(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
    TileEntity tileentity = worldIn.getTileEntity(pos);
    return (tileentity instanceof TileEntityComparator) ? ((TileEntityComparator)tileentity).getOutputSignal() : 0;
  }
  
  private int calculateOutput(World worldIn, BlockPos pos, IBlockState state) {
    return (state.getValue((IProperty)MODE) == Mode.SUBTRACT) ? Math.max(calculateInputStrength(worldIn, pos, state) - getPowerOnSides((IBlockAccess)worldIn, pos, state), 0) : calculateInputStrength(worldIn, pos, state);
  }
  
  protected boolean shouldBePowered(World worldIn, BlockPos pos, IBlockState state) {
    int i = calculateInputStrength(worldIn, pos, state);
    if (i >= 15)
      return true; 
    if (i == 0)
      return false; 
    int j = getPowerOnSides((IBlockAccess)worldIn, pos, state);
    if (j == 0)
      return true; 
    return (i >= j);
  }
  
  protected int calculateInputStrength(World worldIn, BlockPos pos, IBlockState state) {
    int i = super.calculateInputStrength(worldIn, pos, state);
    EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
    BlockPos blockpos = pos.offset(enumfacing);
    IBlockState iblockstate = worldIn.getBlockState(blockpos);
    if (iblockstate.hasComparatorInputOverride()) {
      i = iblockstate.getComparatorInputOverride(worldIn, blockpos);
    } else if (i < 15 && iblockstate.isNormalCube()) {
      blockpos = blockpos.offset(enumfacing);
      iblockstate = worldIn.getBlockState(blockpos);
      if (iblockstate.hasComparatorInputOverride()) {
        i = iblockstate.getComparatorInputOverride(worldIn, blockpos);
      } else if (iblockstate.getMaterial() == Material.AIR) {
        EntityItemFrame entityitemframe = findItemFrame(worldIn, enumfacing, blockpos);
        if (entityitemframe != null)
          i = entityitemframe.getAnalogOutput(); 
      } 
    } 
    return i;
  }
  
  @Nullable
  private EntityItemFrame findItemFrame(World worldIn, final EnumFacing facing, BlockPos pos) {
    List<EntityItemFrame> list = worldIn.getEntitiesWithinAABB(EntityItemFrame.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), (pos.getX() + 1), (pos.getY() + 1), (pos.getZ() + 1)), new Predicate<Entity>() {
          public boolean apply(@Nullable Entity p_apply_1_) {
            return (p_apply_1_ != null && p_apply_1_.getHorizontalFacing() == facing);
          }
        });
    return (list.size() == 1) ? list.get(0) : null;
  }
  
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
    if (!playerIn.capabilities.allowEdit)
      return false; 
    state = state.cycleProperty((IProperty)MODE);
    float f = (state.getValue((IProperty)MODE) == Mode.SUBTRACT) ? 0.55F : 0.5F;
    worldIn.playSound(playerIn, pos, SoundEvents.BLOCK_COMPARATOR_CLICK, SoundCategory.BLOCKS, 0.3F, f);
    worldIn.setBlockState(pos, state, 2);
    onStateChange(worldIn, pos, state);
    return true;
  }
  
  protected void updateState(World worldIn, BlockPos pos, IBlockState state) {
    if (!worldIn.isBlockTickPending(pos, this)) {
      int i = calculateOutput(worldIn, pos, state);
      TileEntity tileentity = worldIn.getTileEntity(pos);
      int j = (tileentity instanceof TileEntityComparator) ? ((TileEntityComparator)tileentity).getOutputSignal() : 0;
      if (i != j || isPowered(state) != shouldBePowered(worldIn, pos, state))
        if (isFacingTowardsRepeater(worldIn, pos, state)) {
          worldIn.updateBlockTick(pos, this, 2, -1);
        } else {
          worldIn.updateBlockTick(pos, this, 2, 0);
        }  
    } 
  }
  
  private void onStateChange(World worldIn, BlockPos pos, IBlockState state) {
    int i = calculateOutput(worldIn, pos, state);
    TileEntity tileentity = worldIn.getTileEntity(pos);
    int j = 0;
    if (tileentity instanceof TileEntityComparator) {
      TileEntityComparator tileentitycomparator = (TileEntityComparator)tileentity;
      j = tileentitycomparator.getOutputSignal();
      tileentitycomparator.setOutputSignal(i);
    } 
    if (j != i || state.getValue((IProperty)MODE) == Mode.COMPARE) {
      boolean flag1 = shouldBePowered(worldIn, pos, state);
      boolean flag = isPowered(state);
      if (flag && !flag1) {
        worldIn.setBlockState(pos, state.withProperty((IProperty)POWERED, Boolean.valueOf(false)), 2);
      } else if (!flag && flag1) {
        worldIn.setBlockState(pos, state.withProperty((IProperty)POWERED, Boolean.valueOf(true)), 2);
      } 
      notifyNeighbors(worldIn, pos, state);
    } 
  }
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (this.isRepeaterPowered)
      worldIn.setBlockState(pos, getUnpoweredState(state).withProperty((IProperty)POWERED, Boolean.valueOf(true)), 4); 
    onStateChange(worldIn, pos, state);
  }
  
  public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    super.onBlockAdded(worldIn, pos, state);
    worldIn.setTileEntity(pos, createNewTileEntity(worldIn, 0));
  }
  
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    super.breakBlock(worldIn, pos, state);
    worldIn.removeTileEntity(pos);
    notifyNeighbors(worldIn, pos, state);
  }
  
  public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
    super.eventReceived(state, worldIn, pos, id, param);
    TileEntity tileentity = worldIn.getTileEntity(pos);
    return (tileentity == null) ? false : tileentity.receiveClientEvent(id, param);
  }
  
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return (TileEntity)new TileEntityComparator();
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)FACING, (Comparable)EnumFacing.getHorizontal(meta)).withProperty((IProperty)POWERED, Boolean.valueOf(((meta & 0x8) > 0))).withProperty((IProperty)MODE, ((meta & 0x4) > 0) ? Mode.SUBTRACT : Mode.COMPARE);
  }
  
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    i |= ((EnumFacing)state.getValue((IProperty)FACING)).getHorizontalIndex();
    if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
      i |= 0x8; 
    if (state.getValue((IProperty)MODE) == Mode.SUBTRACT)
      i |= 0x4; 
    return i;
  }
  
  public IBlockState withRotation(IBlockState state, Rotation rot) {
    return state.withProperty((IProperty)FACING, (Comparable)rot.rotate((EnumFacing)state.getValue((IProperty)FACING)));
  }
  
  public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
    return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue((IProperty)FACING)));
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)FACING, (IProperty)MODE, (IProperty)POWERED });
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    return getDefaultState().withProperty((IProperty)FACING, (Comparable)placer.getHorizontalFacing().getOpposite()).withProperty((IProperty)POWERED, Boolean.valueOf(false)).withProperty((IProperty)MODE, Mode.COMPARE);
  }
  
  public enum Mode implements IStringSerializable {
    COMPARE("compare"),
    SUBTRACT("subtract");
    
    private final String name;
    
    Mode(String name) {
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


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockRedstoneComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */