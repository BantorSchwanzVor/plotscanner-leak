package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class BlockSnowBlock extends Block {
  protected BlockSnowBlock() {
    super(Material.CRAFTED_SNOW);
    setTickRandomly(true);
    setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Items.SNOWBALL;
  }
  
  public int quantityDropped(Random random) {
    return 4;
  }
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (worldIn.getLightFor(EnumSkyBlock.BLOCK, pos) > 11) {
      dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
      worldIn.setBlockToAir(pos);
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockSnowBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */