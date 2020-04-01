package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class BlockEmptyDrops extends Block {
  public BlockEmptyDrops(Material materialIn) {
    super(materialIn);
  }
  
  public int quantityDropped(Random random) {
    return 0;
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Items.field_190931_a;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockEmptyDrops.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */