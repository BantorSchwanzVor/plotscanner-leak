package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockBone extends BlockRotatedPillar {
  public BlockBone() {
    super(Material.ROCK, MapColor.SAND);
    setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    setHardness(2.0F);
    setSoundType(SoundType.STONE);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockBone.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */