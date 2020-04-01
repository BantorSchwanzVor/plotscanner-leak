package net.minecraft.client.renderer.color;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockStem;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;

public class BlockColors {
  private final ObjectIntIdentityMap<IBlockColor> mapBlockColors = new ObjectIntIdentityMap(32);
  
  public static BlockColors init() {
    final BlockColors blockcolors = new BlockColors();
    blockcolors.registerBlockColorHandler(new IBlockColor() {
          public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
            BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = (BlockDoublePlant.EnumPlantType)state.getValue((IProperty)BlockDoublePlant.VARIANT);
            return (worldIn != null && pos != null && (blockdoubleplant$enumplanttype == BlockDoublePlant.EnumPlantType.GRASS || blockdoubleplant$enumplanttype == BlockDoublePlant.EnumPlantType.FERN)) ? BiomeColorHelper.getGrassColorAtPos(worldIn, (state.getValue((IProperty)BlockDoublePlant.HALF) == BlockDoublePlant.EnumBlockHalf.UPPER) ? pos.down() : pos) : -1;
          }
        }new Block[] { (Block)Blocks.DOUBLE_PLANT });
    blockcolors.registerBlockColorHandler(new IBlockColor() {
          public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
            if (worldIn != null && pos != null) {
              TileEntity tileentity = worldIn.getTileEntity(pos);
              if (tileentity instanceof TileEntityFlowerPot) {
                Item item = ((TileEntityFlowerPot)tileentity).getFlowerPotItem();
                IBlockState iblockstate = Block.getBlockFromItem(item).getDefaultState();
                return blockcolors.colorMultiplier(iblockstate, worldIn, pos, tintIndex);
              } 
              return -1;
            } 
            return -1;
          }
        }new Block[] { Blocks.FLOWER_POT });
    blockcolors.registerBlockColorHandler(new IBlockColor() {
          public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
            return (worldIn != null && pos != null) ? BiomeColorHelper.getGrassColorAtPos(worldIn, pos) : ColorizerGrass.getGrassColor(0.5D, 1.0D);
          }
        }new Block[] { (Block)Blocks.GRASS });
    blockcolors.registerBlockColorHandler(new IBlockColor() {
          public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
            BlockPlanks.EnumType blockplanks$enumtype = (BlockPlanks.EnumType)state.getValue((IProperty)BlockOldLeaf.VARIANT);
            if (blockplanks$enumtype == BlockPlanks.EnumType.SPRUCE)
              return ColorizerFoliage.getFoliageColorPine(); 
            if (blockplanks$enumtype == BlockPlanks.EnumType.BIRCH)
              return ColorizerFoliage.getFoliageColorBirch(); 
            return (worldIn != null && pos != null) ? BiomeColorHelper.getFoliageColorAtPos(worldIn, pos) : ColorizerFoliage.getFoliageColorBasic();
          }
        }new Block[] { (Block)Blocks.LEAVES });
    blockcolors.registerBlockColorHandler(new IBlockColor() {
          public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
            return (worldIn != null && pos != null) ? BiomeColorHelper.getFoliageColorAtPos(worldIn, pos) : ColorizerFoliage.getFoliageColorBasic();
          }
        }new Block[] { (Block)Blocks.LEAVES2 });
    blockcolors.registerBlockColorHandler(new IBlockColor() {
          public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
            return (worldIn != null && pos != null) ? BiomeColorHelper.getWaterColorAtPos(worldIn, pos) : -1;
          }
        }new Block[] { (Block)Blocks.WATER, (Block)Blocks.FLOWING_WATER });
    blockcolors.registerBlockColorHandler(new IBlockColor() {
          public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
            return BlockRedstoneWire.colorMultiplier(((Integer)state.getValue((IProperty)BlockRedstoneWire.POWER)).intValue());
          }
        },  new Block[] { (Block)Blocks.REDSTONE_WIRE });
    blockcolors.registerBlockColorHandler(new IBlockColor() {
          public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
            return (worldIn != null && pos != null) ? BiomeColorHelper.getGrassColorAtPos(worldIn, pos) : -1;
          }
        }new Block[] { (Block)Blocks.REEDS });
    blockcolors.registerBlockColorHandler(new IBlockColor() {
          public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
            int i = ((Integer)state.getValue((IProperty)BlockStem.AGE)).intValue();
            int j = i * 32;
            int k = 255 - i * 8;
            int l = i * 4;
            return j << 16 | k << 8 | l;
          }
        },  new Block[] { Blocks.MELON_STEM, Blocks.PUMPKIN_STEM });
    blockcolors.registerBlockColorHandler(new IBlockColor() {
          public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
            if (worldIn != null && pos != null)
              return BiomeColorHelper.getGrassColorAtPos(worldIn, pos); 
            return (state.getValue((IProperty)BlockTallGrass.TYPE) == BlockTallGrass.EnumType.DEAD_BUSH) ? 16777215 : ColorizerGrass.getGrassColor(0.5D, 1.0D);
          }
        }new Block[] { (Block)Blocks.TALLGRASS });
    blockcolors.registerBlockColorHandler(new IBlockColor() {
          public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
            return (worldIn != null && pos != null) ? BiomeColorHelper.getFoliageColorAtPos(worldIn, pos) : ColorizerFoliage.getFoliageColorBasic();
          }
        }new Block[] { Blocks.VINE });
    blockcolors.registerBlockColorHandler(new IBlockColor() {
          public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
            return (worldIn != null && pos != null) ? 2129968 : 7455580;
          }
        },  new Block[] { Blocks.WATERLILY });
    return blockcolors;
  }
  
  public int getColor(IBlockState p_189991_1_, World p_189991_2_, BlockPos p_189991_3_) {
    IBlockColor iblockcolor = (IBlockColor)this.mapBlockColors.getByValue(Block.getIdFromBlock(p_189991_1_.getBlock()));
    if (iblockcolor != null)
      return iblockcolor.colorMultiplier(p_189991_1_, null, null, 0); 
    MapColor mapcolor = p_189991_1_.getMapColor((IBlockAccess)p_189991_2_, p_189991_3_);
    return (mapcolor != null) ? mapcolor.colorValue : -1;
  }
  
  public int colorMultiplier(IBlockState state, @Nullable IBlockAccess blockAccess, @Nullable BlockPos pos, int renderPass) {
    IBlockColor iblockcolor = (IBlockColor)this.mapBlockColors.getByValue(Block.getIdFromBlock(state.getBlock()));
    return (iblockcolor == null) ? -1 : iblockcolor.colorMultiplier(state, blockAccess, pos, renderPass);
  }
  
  public void registerBlockColorHandler(IBlockColor blockColor, Block... blocksIn) {
    byte b;
    int i;
    Block[] arrayOfBlock;
    for (i = (arrayOfBlock = blocksIn).length, b = 0; b < i; ) {
      Block block = arrayOfBlock[b];
      this.mapBlockColors.put(blockColor, Block.getIdFromBlock(block));
      b++;
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\color\BlockColors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */