package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockTNT extends Block {
  public static final PropertyBool EXPLODE = PropertyBool.create("explode");
  
  public BlockTNT() {
    super(Material.TNT);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)EXPLODE, Boolean.valueOf(false)));
    setCreativeTab(CreativeTabs.REDSTONE);
  }
  
  public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    super.onBlockAdded(worldIn, pos, state);
    if (worldIn.isBlockPowered(pos)) {
      onBlockDestroyedByPlayer(worldIn, pos, state.withProperty((IProperty)EXPLODE, Boolean.valueOf(true)));
      worldIn.setBlockToAir(pos);
    } 
  }
  
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
    if (worldIn.isBlockPowered(pos)) {
      onBlockDestroyedByPlayer(worldIn, pos, state.withProperty((IProperty)EXPLODE, Boolean.valueOf(true)));
      worldIn.setBlockToAir(pos);
    } 
  }
  
  public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
    if (!worldIn.isRemote) {
      EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(worldIn, (pos.getX() + 0.5F), pos.getY(), (pos.getZ() + 0.5F), explosionIn.getExplosivePlacedBy());
      entitytntprimed.setFuse((short)(worldIn.rand.nextInt(entitytntprimed.getFuse() / 4) + entitytntprimed.getFuse() / 8));
      worldIn.spawnEntityInWorld((Entity)entitytntprimed);
    } 
  }
  
  public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
    explode(worldIn, pos, state, (EntityLivingBase)null);
  }
  
  public void explode(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase igniter) {
    if (!worldIn.isRemote)
      if (((Boolean)state.getValue((IProperty)EXPLODE)).booleanValue()) {
        EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(worldIn, (pos.getX() + 0.5F), pos.getY(), (pos.getZ() + 0.5F), igniter);
        worldIn.spawnEntityInWorld((Entity)entitytntprimed);
        worldIn.playSound(null, entitytntprimed.posX, entitytntprimed.posY, entitytntprimed.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
      }  
  }
  
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
    ItemStack itemstack = playerIn.getHeldItem(hand);
    if (!itemstack.func_190926_b() && (itemstack.getItem() == Items.FLINT_AND_STEEL || itemstack.getItem() == Items.FIRE_CHARGE)) {
      explode(worldIn, pos, state.withProperty((IProperty)EXPLODE, Boolean.valueOf(true)), (EntityLivingBase)playerIn);
      worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
      if (itemstack.getItem() == Items.FLINT_AND_STEEL) {
        itemstack.damageItem(1, (EntityLivingBase)playerIn);
      } else if (!playerIn.capabilities.isCreativeMode) {
        itemstack.func_190918_g(1);
      } 
      return true;
    } 
    return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY);
  }
  
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
    if (!worldIn.isRemote && entityIn instanceof EntityArrow) {
      EntityArrow entityarrow = (EntityArrow)entityIn;
      if (entityarrow.isBurning()) {
        explode(worldIn, pos, worldIn.getBlockState(pos).withProperty((IProperty)EXPLODE, Boolean.valueOf(true)), (entityarrow.shootingEntity instanceof EntityLivingBase) ? (EntityLivingBase)entityarrow.shootingEntity : null);
        worldIn.setBlockToAir(pos);
      } 
    } 
  }
  
  public boolean canDropFromExplosion(Explosion explosionIn) {
    return false;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)EXPLODE, Boolean.valueOf(((meta & 0x1) > 0)));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((Boolean)state.getValue((IProperty)EXPLODE)).booleanValue() ? 1 : 0;
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)EXPLODE });
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockTNT.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */