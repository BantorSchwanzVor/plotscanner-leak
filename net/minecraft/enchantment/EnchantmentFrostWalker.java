package net.minecraft.enchantment;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EnchantmentFrostWalker extends Enchantment {
  public EnchantmentFrostWalker(Enchantment.Rarity rarityIn, EntityEquipmentSlot... slots) {
    super(rarityIn, EnumEnchantmentType.ARMOR_FEET, slots);
    setName("frostWalker");
  }
  
  public int getMinEnchantability(int enchantmentLevel) {
    return enchantmentLevel * 10;
  }
  
  public int getMaxEnchantability(int enchantmentLevel) {
    return getMinEnchantability(enchantmentLevel) + 15;
  }
  
  public boolean isTreasureEnchantment() {
    return true;
  }
  
  public int getMaxLevel() {
    return 2;
  }
  
  public static void freezeNearby(EntityLivingBase living, World worldIn, BlockPos pos, int level) {
    if (living.onGround) {
      float f = Math.min(16, 2 + level);
      BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(0, 0, 0);
      for (BlockPos.MutableBlockPos blockpos$mutableblockpos1 : BlockPos.getAllInBoxMutable(pos.add(-f, -1.0D, -f), pos.add(f, -1.0D, f))) {
        if (blockpos$mutableblockpos1.distanceSqToCenter(living.posX, living.posY, living.posZ) <= (f * f)) {
          blockpos$mutableblockpos.setPos(blockpos$mutableblockpos1.getX(), blockpos$mutableblockpos1.getY() + 1, blockpos$mutableblockpos1.getZ());
          IBlockState iblockstate = worldIn.getBlockState((BlockPos)blockpos$mutableblockpos);
          if (iblockstate.getMaterial() == Material.AIR) {
            IBlockState iblockstate1 = worldIn.getBlockState((BlockPos)blockpos$mutableblockpos1);
            if (iblockstate1.getMaterial() == Material.WATER && ((Integer)iblockstate1.getValue((IProperty)BlockLiquid.LEVEL)).intValue() == 0 && worldIn.func_190527_a(Blocks.FROSTED_ICE, (BlockPos)blockpos$mutableblockpos1, false, EnumFacing.DOWN, null)) {
              worldIn.setBlockState((BlockPos)blockpos$mutableblockpos1, Blocks.FROSTED_ICE.getDefaultState());
              worldIn.scheduleUpdate(blockpos$mutableblockpos1.toImmutable(), Blocks.FROSTED_ICE, MathHelper.getInt(living.getRNG(), 60, 120));
            } 
          } 
        } 
      } 
    } 
  }
  
  public boolean canApplyTogether(Enchantment ench) {
    return (super.canApplyTogether(ench) && ench != Enchantments.DEPTH_STRIDER);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\enchantment\EnchantmentFrostWalker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */