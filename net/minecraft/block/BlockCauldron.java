package net.minecraft.block;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCauldron extends Block {
  public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 3);
  
  protected static final AxisAlignedBB AABB_LEGS = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D);
  
  protected static final AxisAlignedBB AABB_WALL_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.125D);
  
  protected static final AxisAlignedBB AABB_WALL_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D);
  
  protected static final AxisAlignedBB AABB_WALL_EAST = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
  
  protected static final AxisAlignedBB AABB_WALL_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 1.0D);
  
  public BlockCauldron() {
    super(Material.IRON, MapColor.STONE);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)LEVEL, Integer.valueOf(0)));
  }
  
  public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
    addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_LEGS);
    addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_WEST);
    addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_NORTH);
    addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_EAST);
    addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_SOUTH);
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return FULL_BLOCK_AABB;
  }
  
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
    int i = ((Integer)state.getValue((IProperty)LEVEL)).intValue();
    float f = pos.getY() + (6.0F + (3 * i)) / 16.0F;
    if (!worldIn.isRemote && entityIn.isBurning() && i > 0 && (entityIn.getEntityBoundingBox()).minY <= f) {
      entityIn.extinguish();
      setWaterLevel(worldIn, pos, state, i - 1);
    } 
  }
  
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
    ItemStack itemstack = playerIn.getHeldItem(hand);
    if (itemstack.func_190926_b())
      return true; 
    int i = ((Integer)state.getValue((IProperty)LEVEL)).intValue();
    Item item = itemstack.getItem();
    if (item == Items.WATER_BUCKET) {
      if (i < 3 && !worldIn.isRemote) {
        if (!playerIn.capabilities.isCreativeMode)
          playerIn.setHeldItem(hand, new ItemStack(Items.BUCKET)); 
        playerIn.addStat(StatList.CAULDRON_FILLED);
        setWaterLevel(worldIn, pos, state, 3);
        worldIn.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
      } 
      return true;
    } 
    if (item == Items.BUCKET) {
      if (i == 3 && !worldIn.isRemote) {
        if (!playerIn.capabilities.isCreativeMode) {
          itemstack.func_190918_g(1);
          if (itemstack.func_190926_b()) {
            playerIn.setHeldItem(hand, new ItemStack(Items.WATER_BUCKET));
          } else if (!playerIn.inventory.addItemStackToInventory(new ItemStack(Items.WATER_BUCKET))) {
            playerIn.dropItem(new ItemStack(Items.WATER_BUCKET), false);
          } 
        } 
        playerIn.addStat(StatList.CAULDRON_USED);
        setWaterLevel(worldIn, pos, state, 0);
        worldIn.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
      } 
      return true;
    } 
    if (item == Items.GLASS_BOTTLE) {
      if (i > 0 && !worldIn.isRemote) {
        if (!playerIn.capabilities.isCreativeMode) {
          ItemStack itemstack3 = PotionUtils.addPotionToItemStack(new ItemStack((Item)Items.POTIONITEM), PotionTypes.WATER);
          playerIn.addStat(StatList.CAULDRON_USED);
          itemstack.func_190918_g(1);
          if (itemstack.func_190926_b()) {
            playerIn.setHeldItem(hand, itemstack3);
          } else if (!playerIn.inventory.addItemStackToInventory(itemstack3)) {
            playerIn.dropItem(itemstack3, false);
          } else if (playerIn instanceof EntityPlayerMP) {
            ((EntityPlayerMP)playerIn).sendContainerToPlayer(playerIn.inventoryContainer);
          } 
        } 
        worldIn.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
        setWaterLevel(worldIn, pos, state, i - 1);
      } 
      return true;
    } 
    if (item == Items.POTIONITEM && PotionUtils.getPotionFromItem(itemstack) == PotionTypes.WATER) {
      if (i < 3 && !worldIn.isRemote) {
        if (!playerIn.capabilities.isCreativeMode) {
          ItemStack itemstack2 = new ItemStack(Items.GLASS_BOTTLE);
          playerIn.addStat(StatList.CAULDRON_USED);
          playerIn.setHeldItem(hand, itemstack2);
          if (playerIn instanceof EntityPlayerMP)
            ((EntityPlayerMP)playerIn).sendContainerToPlayer(playerIn.inventoryContainer); 
        } 
        worldIn.playSound(null, pos, SoundEvents.field_191241_J, SoundCategory.BLOCKS, 1.0F, 1.0F);
        setWaterLevel(worldIn, pos, state, i + 1);
      } 
      return true;
    } 
    if (i > 0 && item instanceof ItemArmor) {
      ItemArmor itemarmor = (ItemArmor)item;
      if (itemarmor.getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER && itemarmor.hasColor(itemstack) && !worldIn.isRemote) {
        itemarmor.removeColor(itemstack);
        setWaterLevel(worldIn, pos, state, i - 1);
        playerIn.addStat(StatList.ARMOR_CLEANED);
        return true;
      } 
    } 
    if (i > 0 && item instanceof net.minecraft.item.ItemBanner) {
      if (TileEntityBanner.getPatterns(itemstack) > 0 && !worldIn.isRemote) {
        ItemStack itemstack1 = itemstack.copy();
        itemstack1.func_190920_e(1);
        TileEntityBanner.removeBannerData(itemstack1);
        playerIn.addStat(StatList.BANNER_CLEANED);
        if (!playerIn.capabilities.isCreativeMode) {
          itemstack.func_190918_g(1);
          setWaterLevel(worldIn, pos, state, i - 1);
        } 
        if (itemstack.func_190926_b()) {
          playerIn.setHeldItem(hand, itemstack1);
        } else if (!playerIn.inventory.addItemStackToInventory(itemstack1)) {
          playerIn.dropItem(itemstack1, false);
        } else if (playerIn instanceof EntityPlayerMP) {
          ((EntityPlayerMP)playerIn).sendContainerToPlayer(playerIn.inventoryContainer);
        } 
      } 
      return true;
    } 
    return false;
  }
  
  public void setWaterLevel(World worldIn, BlockPos pos, IBlockState state, int level) {
    worldIn.setBlockState(pos, state.withProperty((IProperty)LEVEL, Integer.valueOf(MathHelper.clamp(level, 0, 3))), 2);
    worldIn.updateComparatorOutputLevel(pos, this);
  }
  
  public void fillWithRain(World worldIn, BlockPos pos) {
    if (worldIn.rand.nextInt(20) == 1) {
      float f = worldIn.getBiome(pos).getFloatTemperature(pos);
      if (worldIn.getBiomeProvider().getTemperatureAtHeight(f, pos.getY()) >= 0.15F) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        if (((Integer)iblockstate.getValue((IProperty)LEVEL)).intValue() < 3)
          worldIn.setBlockState(pos, iblockstate.cycleProperty((IProperty)LEVEL), 2); 
      } 
    } 
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Items.CAULDRON;
  }
  
  public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
    return new ItemStack(Items.CAULDRON);
  }
  
  public boolean hasComparatorInputOverride(IBlockState state) {
    return true;
  }
  
  public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
    return ((Integer)blockState.getValue((IProperty)LEVEL)).intValue();
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)LEVEL, Integer.valueOf(meta));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((Integer)state.getValue((IProperty)LEVEL)).intValue();
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)LEVEL });
  }
  
  public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
    return true;
  }
  
  public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    if (p_193383_4_ == EnumFacing.UP)
      return BlockFaceShape.BOWL; 
    return (p_193383_4_ == EnumFacing.DOWN) ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockCauldron.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */