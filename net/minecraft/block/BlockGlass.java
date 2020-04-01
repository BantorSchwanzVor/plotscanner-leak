package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.BlockRenderLayer;

public class BlockGlass extends BlockBreakable {
  public BlockGlass(Material materialIn, boolean ignoreSimilarity) {
    super(materialIn, ignoreSimilarity);
    setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
  }
  
  public int quantityDropped(Random random) {
    return 0;
  }
  
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.CUTOUT;
  }
  
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  
  protected boolean canSilkHarvest() {
    return true;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockGlass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */