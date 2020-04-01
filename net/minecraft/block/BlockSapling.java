package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenBirchTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BlockSapling extends BlockBush implements IGrowable {
  public static final PropertyEnum<BlockPlanks.EnumType> TYPE = PropertyEnum.create("type", BlockPlanks.EnumType.class);
  
  public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);
  
  protected static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);
  
  protected BlockSapling() {
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)TYPE, BlockPlanks.EnumType.OAK).withProperty((IProperty)STAGE, Integer.valueOf(0)));
    setCreativeTab(CreativeTabs.DECORATIONS);
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return SAPLING_AABB;
  }
  
  public String getLocalizedName() {
    return I18n.translateToLocal(String.valueOf(getUnlocalizedName()) + "." + BlockPlanks.EnumType.OAK.getUnlocalizedName() + ".name");
  }
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (!worldIn.isRemote) {
      super.updateTick(worldIn, pos, state, rand);
      if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0)
        grow(worldIn, pos, state, rand); 
    } 
  }
  
  public void grow(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (((Integer)state.getValue((IProperty)STAGE)).intValue() == 0) {
      worldIn.setBlockState(pos, state.cycleProperty((IProperty)STAGE), 4);
    } else {
      generateTree(worldIn, pos, state, rand);
    } 
  }
  
  public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    WorldGenBirchTree worldGenBirchTree;
    WorldGenSavannaTree worldGenSavannaTree;
    WorldGenCanopyTree worldGenCanopyTree;
    IBlockState iblockstate, iblockstate1;
    WorldGenerator worldgenerator = (rand.nextInt(10) == 0) ? (WorldGenerator)new WorldGenBigTree(true) : (WorldGenerator)new WorldGenTrees(true);
    int i = 0;
    int j = 0;
    boolean flag = false;
    switch ((BlockPlanks.EnumType)state.getValue((IProperty)TYPE)) {
      case SPRUCE:
        label68: for (i = 0; i >= -1; i--) {
          for (j = 0; j >= -1; j--) {
            if (isTwoByTwoOfType(worldIn, pos, i, j, BlockPlanks.EnumType.SPRUCE)) {
              WorldGenMegaPineTree worldGenMegaPineTree = new WorldGenMegaPineTree(false, rand.nextBoolean());
              flag = true;
              break label68;
            } 
          } 
        } 
        if (!flag) {
          i = 0;
          j = 0;
          WorldGenTaiga2 worldGenTaiga2 = new WorldGenTaiga2(true);
        } 
        break;
      case BIRCH:
        worldGenBirchTree = new WorldGenBirchTree(true, false);
        break;
      case JUNGLE:
        iblockstate = Blocks.LOG.getDefaultState().withProperty((IProperty)BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
        iblockstate1 = Blocks.LEAVES.getDefaultState().withProperty((IProperty)BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty((IProperty)BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
        label69: for (i = 0; i >= -1; i--) {
          for (j = 0; j >= -1; j--) {
            if (isTwoByTwoOfType(worldIn, pos, i, j, BlockPlanks.EnumType.JUNGLE)) {
              WorldGenMegaJungle worldGenMegaJungle = new WorldGenMegaJungle(true, 10, 20, iblockstate, iblockstate1);
              flag = true;
              break label69;
            } 
          } 
        } 
        if (!flag) {
          i = 0;
          j = 0;
          WorldGenTrees worldGenTrees = new WorldGenTrees(true, 4 + rand.nextInt(7), iblockstate, iblockstate1, false);
        } 
        break;
      case null:
        worldGenSavannaTree = new WorldGenSavannaTree(true);
        break;
      case DARK_OAK:
        label70: for (i = 0; i >= -1; i--) {
          for (j = 0; j >= -1; j--) {
            if (isTwoByTwoOfType(worldIn, pos, i, j, BlockPlanks.EnumType.DARK_OAK)) {
              worldGenCanopyTree = new WorldGenCanopyTree(true);
              flag = true;
              break label70;
            } 
          } 
        } 
        if (!flag)
          return; 
        break;
    } 
    IBlockState iblockstate2 = Blocks.AIR.getDefaultState();
    if (flag) {
      worldIn.setBlockState(pos.add(i, 0, j), iblockstate2, 4);
      worldIn.setBlockState(pos.add(i + 1, 0, j), iblockstate2, 4);
      worldIn.setBlockState(pos.add(i, 0, j + 1), iblockstate2, 4);
      worldIn.setBlockState(pos.add(i + 1, 0, j + 1), iblockstate2, 4);
    } else {
      worldIn.setBlockState(pos, iblockstate2, 4);
    } 
    if (!worldGenCanopyTree.generate(worldIn, rand, pos.add(i, 0, j)))
      if (flag) {
        worldIn.setBlockState(pos.add(i, 0, j), state, 4);
        worldIn.setBlockState(pos.add(i + 1, 0, j), state, 4);
        worldIn.setBlockState(pos.add(i, 0, j + 1), state, 4);
        worldIn.setBlockState(pos.add(i + 1, 0, j + 1), state, 4);
      } else {
        worldIn.setBlockState(pos, state, 4);
      }  
  }
  
  private boolean isTwoByTwoOfType(World worldIn, BlockPos pos, int p_181624_3_, int p_181624_4_, BlockPlanks.EnumType type) {
    return (isTypeAt(worldIn, pos.add(p_181624_3_, 0, p_181624_4_), type) && isTypeAt(worldIn, pos.add(p_181624_3_ + 1, 0, p_181624_4_), type) && isTypeAt(worldIn, pos.add(p_181624_3_, 0, p_181624_4_ + 1), type) && isTypeAt(worldIn, pos.add(p_181624_3_ + 1, 0, p_181624_4_ + 1), type));
  }
  
  public boolean isTypeAt(World worldIn, BlockPos pos, BlockPlanks.EnumType type) {
    IBlockState iblockstate = worldIn.getBlockState(pos);
    return (iblockstate.getBlock() == this && iblockstate.getValue((IProperty)TYPE) == type);
  }
  
  public int damageDropped(IBlockState state) {
    return ((BlockPlanks.EnumType)state.getValue((IProperty)TYPE)).getMetadata();
  }
  
  public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
    byte b;
    int i;
    BlockPlanks.EnumType[] arrayOfEnumType;
    for (i = (arrayOfEnumType = BlockPlanks.EnumType.values()).length, b = 0; b < i; ) {
      BlockPlanks.EnumType blockplanks$enumtype = arrayOfEnumType[b];
      tab.add(new ItemStack(this, 1, blockplanks$enumtype.getMetadata()));
      b++;
    } 
  }
  
  public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
    return true;
  }
  
  public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
    return (worldIn.rand.nextFloat() < 0.45D);
  }
  
  public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
    grow(worldIn, pos, state, rand);
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)TYPE, BlockPlanks.EnumType.byMetadata(meta & 0x7)).withProperty((IProperty)STAGE, Integer.valueOf((meta & 0x8) >> 3));
  }
  
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    i |= ((BlockPlanks.EnumType)state.getValue((IProperty)TYPE)).getMetadata();
    i |= ((Integer)state.getValue((IProperty)STAGE)).intValue() << 3;
    return i;
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)TYPE, (IProperty)STAGE });
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockSapling.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */