package net.minecraft.block;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockOldLeaf extends BlockLeaves {
  public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class, new Predicate<BlockPlanks.EnumType>() {
        public boolean apply(@Nullable BlockPlanks.EnumType p_apply_1_) {
          return (p_apply_1_.getMetadata() < 4);
        }
      });
  
  public BlockOldLeaf() {
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)VARIANT, BlockPlanks.EnumType.OAK).withProperty((IProperty)CHECK_DECAY, Boolean.valueOf(true)).withProperty((IProperty)DECAYABLE, Boolean.valueOf(true)));
  }
  
  protected void dropApple(World worldIn, BlockPos pos, IBlockState state, int chance) {
    if (state.getValue((IProperty)VARIANT) == BlockPlanks.EnumType.OAK && worldIn.rand.nextInt(chance) == 0)
      spawnAsEntity(worldIn, pos, new ItemStack(Items.APPLE)); 
  }
  
  protected int getSaplingDropChance(IBlockState state) {
    return (state.getValue((IProperty)VARIANT) == BlockPlanks.EnumType.JUNGLE) ? 40 : super.getSaplingDropChance(state);
  }
  
  public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
    tab.add(new ItemStack(this, 1, BlockPlanks.EnumType.OAK.getMetadata()));
    tab.add(new ItemStack(this, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()));
    tab.add(new ItemStack(this, 1, BlockPlanks.EnumType.BIRCH.getMetadata()));
    tab.add(new ItemStack(this, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()));
  }
  
  protected ItemStack getSilkTouchDrop(IBlockState state) {
    return new ItemStack(Item.getItemFromBlock(this), 1, ((BlockPlanks.EnumType)state.getValue((IProperty)VARIANT)).getMetadata());
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)VARIANT, getWoodType(meta)).withProperty((IProperty)DECAYABLE, Boolean.valueOf(((meta & 0x4) == 0))).withProperty((IProperty)CHECK_DECAY, Boolean.valueOf(((meta & 0x8) > 0)));
  }
  
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    i |= ((BlockPlanks.EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
    if (!((Boolean)state.getValue((IProperty)DECAYABLE)).booleanValue())
      i |= 0x4; 
    if (((Boolean)state.getValue((IProperty)CHECK_DECAY)).booleanValue())
      i |= 0x8; 
    return i;
  }
  
  public BlockPlanks.EnumType getWoodType(int meta) {
    return BlockPlanks.EnumType.byMetadata((meta & 0x3) % 4);
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)VARIANT, (IProperty)CHECK_DECAY, (IProperty)DECAYABLE });
  }
  
  public int damageDropped(IBlockState state) {
    return ((BlockPlanks.EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
  }
  
  public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
    if (!worldIn.isRemote && stack.getItem() == Items.SHEARS) {
      player.addStat(StatList.getBlockStats(this));
      spawnAsEntity(worldIn, pos, new ItemStack(Item.getItemFromBlock(this), 1, ((BlockPlanks.EnumType)state.getValue((IProperty)VARIANT)).getMetadata()));
    } else {
      super.harvestBlock(worldIn, player, pos, state, te, stack);
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockOldLeaf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */