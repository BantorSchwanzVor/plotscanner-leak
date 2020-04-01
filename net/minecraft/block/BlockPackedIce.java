package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockPackedIce extends Block {
  public BlockPackedIce() {
    super(Material.PACKED_ICE);
    this.slipperiness = 0.98F;
    setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
  }
  
  public int quantityDropped(Random random) {
    return 0;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockPackedIce.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */