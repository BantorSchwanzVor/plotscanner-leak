package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class BlockSponge extends Block {
  public static final PropertyBool WET = PropertyBool.create("wet");
  
  protected BlockSponge() {
    super(Material.SPONGE);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)WET, Boolean.valueOf(false)));
    setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
  }
  
  public String getLocalizedName() {
    return I18n.translateToLocal(String.valueOf(getUnlocalizedName()) + ".dry.name");
  }
  
  public int damageDropped(IBlockState state) {
    return ((Boolean)state.getValue((IProperty)WET)).booleanValue() ? 1 : 0;
  }
  
  public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    tryAbsorb(worldIn, pos, state);
  }
  
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
    tryAbsorb(worldIn, pos, state);
    super.neighborChanged(state, worldIn, pos, blockIn, p_189540_5_);
  }
  
  protected void tryAbsorb(World worldIn, BlockPos pos, IBlockState state) {
    if (!((Boolean)state.getValue((IProperty)WET)).booleanValue() && absorb(worldIn, pos)) {
      worldIn.setBlockState(pos, state.withProperty((IProperty)WET, Boolean.valueOf(true)), 2);
      worldIn.playEvent(2001, pos, Block.getIdFromBlock(Blocks.WATER));
    } 
  }
  
  private boolean absorb(World worldIn, BlockPos pos) {
    Queue<Tuple<BlockPos, Integer>> queue = Lists.newLinkedList();
    List<BlockPos> list = Lists.newArrayList();
    queue.add(new Tuple(pos, Integer.valueOf(0)));
    int i = 0;
    while (!queue.isEmpty()) {
      Tuple<BlockPos, Integer> tuple = queue.poll();
      BlockPos blockpos = (BlockPos)tuple.getFirst();
      int j = ((Integer)tuple.getSecond()).intValue();
      byte b;
      int k;
      EnumFacing[] arrayOfEnumFacing;
      for (k = (arrayOfEnumFacing = EnumFacing.values()).length, b = 0; b < k; ) {
        EnumFacing enumfacing = arrayOfEnumFacing[b];
        BlockPos blockpos1 = blockpos.offset(enumfacing);
        if (worldIn.getBlockState(blockpos1).getMaterial() == Material.WATER) {
          worldIn.setBlockState(blockpos1, Blocks.AIR.getDefaultState(), 2);
          list.add(blockpos1);
          i++;
          if (j < 6)
            queue.add(new Tuple(blockpos1, Integer.valueOf(j + 1))); 
        } 
        b++;
      } 
      if (i > 64)
        break; 
    } 
    for (BlockPos blockpos2 : list)
      worldIn.notifyNeighborsOfStateChange(blockpos2, Blocks.AIR, false); 
    return (i > 0);
  }
  
  public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
    tab.add(new ItemStack(this, 1, 0));
    tab.add(new ItemStack(this, 1, 1));
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)WET, Boolean.valueOf(((meta & 0x1) == 1)));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((Boolean)state.getValue((IProperty)WET)).booleanValue() ? 1 : 0;
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)WET });
  }
  
  public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
    if (((Boolean)stateIn.getValue((IProperty)WET)).booleanValue()) {
      EnumFacing enumfacing = EnumFacing.random(rand);
      if (enumfacing != EnumFacing.UP && !worldIn.getBlockState(pos.offset(enumfacing)).isFullyOpaque()) {
        double d0 = pos.getX();
        double d1 = pos.getY();
        double d2 = pos.getZ();
        if (enumfacing == EnumFacing.DOWN) {
          d1 -= 0.05D;
          d0 += rand.nextDouble();
          d2 += rand.nextDouble();
        } else {
          d1 += rand.nextDouble() * 0.8D;
          if (enumfacing.getAxis() == EnumFacing.Axis.X) {
            d2 += rand.nextDouble();
            if (enumfacing == EnumFacing.EAST) {
              d0++;
            } else {
              d0 += 0.05D;
            } 
          } else {
            d0 += rand.nextDouble();
            if (enumfacing == EnumFacing.SOUTH) {
              d2++;
            } else {
              d2 += 0.05D;
            } 
          } 
        } 
        worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
      } 
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockSponge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */