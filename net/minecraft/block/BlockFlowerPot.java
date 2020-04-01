package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class BlockFlowerPot extends BlockContainer {
  public static final PropertyInteger LEGACY_DATA = PropertyInteger.create("legacy_data", 0, 15);
  
  public static final PropertyEnum<EnumFlowerType> CONTENTS = PropertyEnum.create("contents", EnumFlowerType.class);
  
  protected static final AxisAlignedBB FLOWER_POT_AABB = new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.375D, 0.6875D);
  
  public BlockFlowerPot() {
    super(Material.CIRCUITS);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)CONTENTS, EnumFlowerType.EMPTY).withProperty((IProperty)LEGACY_DATA, Integer.valueOf(0)));
  }
  
  public String getLocalizedName() {
    return I18n.translateToLocal("item.flowerPot.name");
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return FLOWER_POT_AABB;
  }
  
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  
  public EnumBlockRenderType getRenderType(IBlockState state) {
    return EnumBlockRenderType.MODEL;
  }
  
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
    ItemStack itemstack = playerIn.getHeldItem(hand);
    TileEntityFlowerPot tileentityflowerpot = getTileEntity(worldIn, pos);
    if (tileentityflowerpot == null)
      return false; 
    ItemStack itemstack1 = tileentityflowerpot.getFlowerItemStack();
    if (itemstack1.func_190926_b()) {
      if (!func_190951_a(itemstack))
        return false; 
      tileentityflowerpot.func_190614_a(itemstack);
      playerIn.addStat(StatList.FLOWER_POTTED);
      if (!playerIn.capabilities.isCreativeMode)
        itemstack.func_190918_g(1); 
    } else {
      if (itemstack.func_190926_b()) {
        playerIn.setHeldItem(hand, itemstack1);
      } else if (!playerIn.func_191521_c(itemstack1)) {
        playerIn.dropItem(itemstack1, false);
      } 
      tileentityflowerpot.func_190614_a(ItemStack.field_190927_a);
    } 
    tileentityflowerpot.markDirty();
    worldIn.notifyBlockUpdate(pos, state, state, 3);
    return true;
  }
  
  private boolean func_190951_a(ItemStack p_190951_1_) {
    Block block = Block.getBlockFromItem(p_190951_1_.getItem());
    if (block != Blocks.YELLOW_FLOWER && block != Blocks.RED_FLOWER && block != Blocks.CACTUS && block != Blocks.BROWN_MUSHROOM && block != Blocks.RED_MUSHROOM && block != Blocks.SAPLING && block != Blocks.DEADBUSH) {
      int i = p_190951_1_.getMetadata();
      return (block == Blocks.TALLGRASS && i == BlockTallGrass.EnumType.FERN.getMeta());
    } 
    return true;
  }
  
  public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
    TileEntityFlowerPot tileentityflowerpot = getTileEntity(worldIn, pos);
    if (tileentityflowerpot != null) {
      ItemStack itemstack = tileentityflowerpot.getFlowerItemStack();
      if (!itemstack.func_190926_b())
        return itemstack; 
    } 
    return new ItemStack(Items.FLOWER_POT);
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    return (super.canPlaceBlockAt(worldIn, pos) && worldIn.getBlockState(pos.down()).isFullyOpaque());
  }
  
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
    if (!worldIn.getBlockState(pos.down()).isFullyOpaque()) {
      dropBlockAsItem(worldIn, pos, state, 0);
      worldIn.setBlockToAir(pos);
    } 
  }
  
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    TileEntityFlowerPot tileentityflowerpot = getTileEntity(worldIn, pos);
    if (tileentityflowerpot != null && tileentityflowerpot.getFlowerPotItem() != null)
      spawnAsEntity(worldIn, pos, new ItemStack(tileentityflowerpot.getFlowerPotItem(), 1, tileentityflowerpot.getFlowerPotData())); 
    super.breakBlock(worldIn, pos, state);
  }
  
  public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
    super.onBlockHarvested(worldIn, pos, state, player);
    if (player.capabilities.isCreativeMode) {
      TileEntityFlowerPot tileentityflowerpot = getTileEntity(worldIn, pos);
      if (tileentityflowerpot != null)
        tileentityflowerpot.func_190614_a(ItemStack.field_190927_a); 
    } 
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Items.FLOWER_POT;
  }
  
  @Nullable
  private TileEntityFlowerPot getTileEntity(World worldIn, BlockPos pos) {
    TileEntity tileentity = worldIn.getTileEntity(pos);
    return (tileentity instanceof TileEntityFlowerPot) ? (TileEntityFlowerPot)tileentity : null;
  }
  
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    Block block = null;
    int i = 0;
    switch (meta) {
      case 1:
        block = Blocks.RED_FLOWER;
        i = BlockFlower.EnumFlowerType.POPPY.getMeta();
        break;
      case 2:
        block = Blocks.YELLOW_FLOWER;
        break;
      case 3:
        block = Blocks.SAPLING;
        i = BlockPlanks.EnumType.OAK.getMetadata();
        break;
      case 4:
        block = Blocks.SAPLING;
        i = BlockPlanks.EnumType.SPRUCE.getMetadata();
        break;
      case 5:
        block = Blocks.SAPLING;
        i = BlockPlanks.EnumType.BIRCH.getMetadata();
        break;
      case 6:
        block = Blocks.SAPLING;
        i = BlockPlanks.EnumType.JUNGLE.getMetadata();
        break;
      case 7:
        block = Blocks.RED_MUSHROOM;
        break;
      case 8:
        block = Blocks.BROWN_MUSHROOM;
        break;
      case 9:
        block = Blocks.CACTUS;
        break;
      case 10:
        block = Blocks.DEADBUSH;
        break;
      case 11:
        block = Blocks.TALLGRASS;
        i = BlockTallGrass.EnumType.FERN.getMeta();
        break;
      case 12:
        block = Blocks.SAPLING;
        i = BlockPlanks.EnumType.ACACIA.getMetadata();
        break;
      case 13:
        block = Blocks.SAPLING;
        i = BlockPlanks.EnumType.DARK_OAK.getMetadata();
        break;
    } 
    return (TileEntity)new TileEntityFlowerPot(Item.getItemFromBlock(block), i);
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)CONTENTS, (IProperty)LEGACY_DATA });
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((Integer)state.getValue((IProperty)LEGACY_DATA)).intValue();
  }
  
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    EnumFlowerType blockflowerpot$enumflowertype = EnumFlowerType.EMPTY;
    TileEntity tileentity = (worldIn instanceof ChunkCache) ? ((ChunkCache)worldIn).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : worldIn.getTileEntity(pos);
    if (tileentity instanceof TileEntityFlowerPot) {
      TileEntityFlowerPot tileentityflowerpot = (TileEntityFlowerPot)tileentity;
      Item item = tileentityflowerpot.getFlowerPotItem();
      if (item instanceof net.minecraft.item.ItemBlock) {
        int i = tileentityflowerpot.getFlowerPotData();
        Block block = Block.getBlockFromItem(item);
        if (block == Blocks.SAPLING) {
          switch (BlockPlanks.EnumType.byMetadata(i)) {
            case OAK:
              blockflowerpot$enumflowertype = EnumFlowerType.OAK_SAPLING;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
            case SPRUCE:
              blockflowerpot$enumflowertype = EnumFlowerType.SPRUCE_SAPLING;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
            case BIRCH:
              blockflowerpot$enumflowertype = EnumFlowerType.BIRCH_SAPLING;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
            case JUNGLE:
              blockflowerpot$enumflowertype = EnumFlowerType.JUNGLE_SAPLING;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
            case null:
              blockflowerpot$enumflowertype = EnumFlowerType.ACACIA_SAPLING;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
            case DARK_OAK:
              blockflowerpot$enumflowertype = EnumFlowerType.DARK_OAK_SAPLING;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
          } 
          blockflowerpot$enumflowertype = EnumFlowerType.EMPTY;
        } else if (block == Blocks.TALLGRASS) {
          switch (i) {
            case 0:
              blockflowerpot$enumflowertype = EnumFlowerType.DEAD_BUSH;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
            case 2:
              blockflowerpot$enumflowertype = EnumFlowerType.FERN;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
          } 
          blockflowerpot$enumflowertype = EnumFlowerType.EMPTY;
        } else if (block == Blocks.YELLOW_FLOWER) {
          blockflowerpot$enumflowertype = EnumFlowerType.DANDELION;
        } else if (block == Blocks.RED_FLOWER) {
          switch (BlockFlower.EnumFlowerType.getType(BlockFlower.EnumFlowerColor.RED, i)) {
            case POPPY:
              blockflowerpot$enumflowertype = EnumFlowerType.POPPY;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
            case BLUE_ORCHID:
              blockflowerpot$enumflowertype = EnumFlowerType.BLUE_ORCHID;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
            case null:
              blockflowerpot$enumflowertype = EnumFlowerType.ALLIUM;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
            case HOUSTONIA:
              blockflowerpot$enumflowertype = EnumFlowerType.HOUSTONIA;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
            case RED_TULIP:
              blockflowerpot$enumflowertype = EnumFlowerType.RED_TULIP;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
            case ORANGE_TULIP:
              blockflowerpot$enumflowertype = EnumFlowerType.ORANGE_TULIP;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
            case WHITE_TULIP:
              blockflowerpot$enumflowertype = EnumFlowerType.WHITE_TULIP;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
            case PINK_TULIP:
              blockflowerpot$enumflowertype = EnumFlowerType.PINK_TULIP;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
            case OXEYE_DAISY:
              blockflowerpot$enumflowertype = EnumFlowerType.OXEYE_DAISY;
              return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
          } 
          blockflowerpot$enumflowertype = EnumFlowerType.EMPTY;
        } else if (block == Blocks.RED_MUSHROOM) {
          blockflowerpot$enumflowertype = EnumFlowerType.MUSHROOM_RED;
        } else if (block == Blocks.BROWN_MUSHROOM) {
          blockflowerpot$enumflowertype = EnumFlowerType.MUSHROOM_BROWN;
        } else if (block == Blocks.DEADBUSH) {
          blockflowerpot$enumflowertype = EnumFlowerType.DEAD_BUSH;
        } else if (block == Blocks.CACTUS) {
          blockflowerpot$enumflowertype = EnumFlowerType.CACTUS;
        } 
      } 
    } 
    return state.withProperty((IProperty)CONTENTS, blockflowerpot$enumflowertype);
  }
  
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.CUTOUT;
  }
  
  public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    return BlockFaceShape.UNDEFINED;
  }
  
  public enum EnumFlowerType implements IStringSerializable {
    EMPTY("empty"),
    POPPY("rose"),
    BLUE_ORCHID("blue_orchid"),
    ALLIUM("allium"),
    HOUSTONIA("houstonia"),
    RED_TULIP("red_tulip"),
    ORANGE_TULIP("orange_tulip"),
    WHITE_TULIP("white_tulip"),
    PINK_TULIP("pink_tulip"),
    OXEYE_DAISY("oxeye_daisy"),
    DANDELION("dandelion"),
    OAK_SAPLING("oak_sapling"),
    SPRUCE_SAPLING("spruce_sapling"),
    BIRCH_SAPLING("birch_sapling"),
    JUNGLE_SAPLING("jungle_sapling"),
    ACACIA_SAPLING("acacia_sapling"),
    DARK_OAK_SAPLING("dark_oak_sapling"),
    MUSHROOM_RED("mushroom_red"),
    MUSHROOM_BROWN("mushroom_brown"),
    DEAD_BUSH("dead_bush"),
    FERN("fern"),
    CACTUS("cactus");
    
    private final String name;
    
    EnumFlowerType(String name) {
      this.name = name;
    }
    
    public String toString() {
      return this.name;
    }
    
    public String getName() {
      return this.name;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockFlowerPot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */