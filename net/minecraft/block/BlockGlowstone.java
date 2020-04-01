package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public class BlockGlowstone extends Block {
  public BlockGlowstone(Material materialIn) {
    super(materialIn);
    setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
  }
  
  public int quantityDroppedWithBonus(int fortune, Random random) {
    return MathHelper.clamp(quantityDropped(random) + random.nextInt(fortune + 1), 1, 4);
  }
  
  public int quantityDropped(Random random) {
    return 2 + random.nextInt(3);
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Items.GLOWSTONE_DUST;
  }
  
  public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_) {
    return MapColor.SAND;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockGlowstone.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */