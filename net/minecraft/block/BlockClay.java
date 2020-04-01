package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class BlockClay extends Block {
  public BlockClay() {
    super(Material.CLAY);
    setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Items.CLAY_BALL;
  }
  
  public int quantityDropped(Random random) {
    return 4;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockClay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */