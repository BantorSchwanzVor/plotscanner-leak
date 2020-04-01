package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ItemSign extends Item {
  public ItemSign() {
    this.maxStackSize = 16;
    setCreativeTab(CreativeTabs.DECORATIONS);
  }
  
  public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY) {
    IBlockState iblockstate = playerIn.getBlockState(worldIn);
    boolean flag = iblockstate.getBlock().isReplaceable((IBlockAccess)playerIn, worldIn);
    if (hand != EnumFacing.DOWN && (iblockstate.getMaterial().isSolid() || flag) && (!flag || hand == EnumFacing.UP)) {
      worldIn = worldIn.offset(hand);
      ItemStack itemstack = stack.getHeldItem(pos);
      if (stack.canPlayerEdit(worldIn, hand, itemstack) && Blocks.STANDING_SIGN.canPlaceBlockAt(playerIn, worldIn)) {
        if (playerIn.isRemote)
          return EnumActionResult.SUCCESS; 
        worldIn = flag ? worldIn.down() : worldIn;
        if (hand == EnumFacing.UP) {
          int i = MathHelper.floor(((stack.rotationYaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 0xF;
          playerIn.setBlockState(worldIn, Blocks.STANDING_SIGN.getDefaultState().withProperty((IProperty)BlockStandingSign.ROTATION, Integer.valueOf(i)), 11);
        } else {
          playerIn.setBlockState(worldIn, Blocks.WALL_SIGN.getDefaultState().withProperty((IProperty)BlockWallSign.FACING, (Comparable)hand), 11);
        } 
        TileEntity tileentity = playerIn.getTileEntity(worldIn);
        if (tileentity instanceof TileEntitySign && !ItemBlock.setTileEntityNBT(playerIn, stack, worldIn, itemstack))
          stack.openEditSign((TileEntitySign)tileentity); 
        if (stack instanceof EntityPlayerMP)
          CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP)stack, worldIn, itemstack); 
        itemstack.func_190918_g(1);
        return EnumActionResult.SUCCESS;
      } 
      return EnumActionResult.FAIL;
    } 
    return EnumActionResult.FAIL;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemSign.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */