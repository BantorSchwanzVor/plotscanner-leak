package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemShears extends Item {
  public ItemShears() {
    setMaxStackSize(1);
    setMaxDamage(238);
    setCreativeTab(CreativeTabs.TOOLS);
  }
  
  public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
    if (!worldIn.isRemote)
      stack.damageItem(1, entityLiving); 
    Block block = state.getBlock();
    return (state.getMaterial() != Material.LEAVES && block != Blocks.WEB && block != Blocks.TALLGRASS && block != Blocks.VINE && block != Blocks.TRIPWIRE && block != Blocks.WOOL) ? super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving) : true;
  }
  
  public boolean canHarvestBlock(IBlockState blockIn) {
    Block block = blockIn.getBlock();
    return !(block != Blocks.WEB && block != Blocks.REDSTONE_WIRE && block != Blocks.TRIPWIRE);
  }
  
  public float getStrVsBlock(ItemStack stack, IBlockState state) {
    Block block = state.getBlock();
    if (block != Blocks.WEB && state.getMaterial() != Material.LEAVES)
      return (block == Blocks.WOOL) ? 5.0F : super.getStrVsBlock(stack, state); 
    return 15.0F;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemShears.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */