package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ItemSnow extends ItemBlock {
  public ItemSnow(Block block) {
    super(block);
    setMaxDamage(0);
  }
  
  public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY) {
    ItemStack itemstack = stack.getHeldItem(pos);
    if (!itemstack.func_190926_b() && stack.canPlayerEdit(worldIn, hand, itemstack)) {
      IBlockState iblockstate = playerIn.getBlockState(worldIn);
      Block block = iblockstate.getBlock();
      BlockPos blockpos = worldIn;
      if ((hand != EnumFacing.UP || block != this.block) && !block.isReplaceable((IBlockAccess)playerIn, worldIn)) {
        blockpos = worldIn.offset(hand);
        iblockstate = playerIn.getBlockState(blockpos);
        block = iblockstate.getBlock();
      } 
      if (block == this.block) {
        int i = ((Integer)iblockstate.getValue((IProperty)BlockSnow.LAYERS)).intValue();
        if (i < 8) {
          IBlockState iblockstate1 = iblockstate.withProperty((IProperty)BlockSnow.LAYERS, Integer.valueOf(i + 1));
          AxisAlignedBB axisalignedbb = iblockstate1.getCollisionBoundingBox((IBlockAccess)playerIn, blockpos);
          if (axisalignedbb != Block.NULL_AABB && playerIn.checkNoEntityCollision(axisalignedbb.offset(blockpos)) && playerIn.setBlockState(blockpos, iblockstate1, 10)) {
            SoundType soundtype = this.block.getSoundType();
            playerIn.playSound(stack, blockpos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
            if (stack instanceof EntityPlayerMP)
              CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP)stack, worldIn, itemstack); 
            itemstack.func_190918_g(1);
            return EnumActionResult.SUCCESS;
          } 
        } 
      } 
      return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY);
    } 
    return EnumActionResult.FAIL;
  }
  
  public int getMetadata(int damage) {
    return damage;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemSnow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */