package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ItemDoor extends Item {
  private final Block block;
  
  public ItemDoor(Block block) {
    this.block = block;
    setCreativeTab(CreativeTabs.REDSTONE);
  }
  
  public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY) {
    if (hand != EnumFacing.UP)
      return EnumActionResult.FAIL; 
    IBlockState iblockstate = playerIn.getBlockState(worldIn);
    Block block = iblockstate.getBlock();
    if (!block.isReplaceable((IBlockAccess)playerIn, worldIn))
      worldIn = worldIn.offset(hand); 
    ItemStack itemstack = stack.getHeldItem(pos);
    if (stack.canPlayerEdit(worldIn, hand, itemstack) && this.block.canPlaceBlockAt(playerIn, worldIn)) {
      EnumFacing enumfacing = EnumFacing.fromAngle(stack.rotationYaw);
      int i = enumfacing.getFrontOffsetX();
      int j = enumfacing.getFrontOffsetZ();
      boolean flag = !((i >= 0 || hitY >= 0.5F) && (i <= 0 || hitY <= 0.5F) && (j >= 0 || facing <= 0.5F) && (j <= 0 || facing >= 0.5F));
      placeDoor(playerIn, worldIn, enumfacing, this.block, flag);
      SoundType soundtype = this.block.getSoundType();
      playerIn.playSound(stack, worldIn, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
      itemstack.func_190918_g(1);
      return EnumActionResult.SUCCESS;
    } 
    return EnumActionResult.FAIL;
  }
  
  public static void placeDoor(World worldIn, BlockPos pos, EnumFacing facing, Block door, boolean isRightHinge) {
    BlockPos blockpos = pos.offset(facing.rotateY());
    BlockPos blockpos1 = pos.offset(facing.rotateYCCW());
    int i = (worldIn.getBlockState(blockpos1).isNormalCube() ? 1 : 0) + (worldIn.getBlockState(blockpos1.up()).isNormalCube() ? 1 : 0);
    int j = (worldIn.getBlockState(blockpos).isNormalCube() ? 1 : 0) + (worldIn.getBlockState(blockpos.up()).isNormalCube() ? 1 : 0);
    boolean flag = !(worldIn.getBlockState(blockpos1).getBlock() != door && worldIn.getBlockState(blockpos1.up()).getBlock() != door);
    boolean flag1 = !(worldIn.getBlockState(blockpos).getBlock() != door && worldIn.getBlockState(blockpos.up()).getBlock() != door);
    if ((!flag || flag1) && j <= i) {
      if ((flag1 && !flag) || j < i)
        isRightHinge = false; 
    } else {
      isRightHinge = true;
    } 
    BlockPos blockpos2 = pos.up();
    boolean flag2 = !(!worldIn.isBlockPowered(pos) && !worldIn.isBlockPowered(blockpos2));
    IBlockState iblockstate = door.getDefaultState().withProperty((IProperty)BlockDoor.FACING, (Comparable)facing).withProperty((IProperty)BlockDoor.HINGE, isRightHinge ? (Comparable)BlockDoor.EnumHingePosition.RIGHT : (Comparable)BlockDoor.EnumHingePosition.LEFT).withProperty((IProperty)BlockDoor.POWERED, Boolean.valueOf(flag2)).withProperty((IProperty)BlockDoor.OPEN, Boolean.valueOf(flag2));
    worldIn.setBlockState(pos, iblockstate.withProperty((IProperty)BlockDoor.HALF, (Comparable)BlockDoor.EnumDoorHalf.LOWER), 2);
    worldIn.setBlockState(blockpos2, iblockstate.withProperty((IProperty)BlockDoor.HALF, (Comparable)BlockDoor.EnumDoorHalf.UPPER), 2);
    worldIn.notifyNeighborsOfStateChange(pos, door, false);
    worldIn.notifyNeighborsOfStateChange(blockpos2, door, false);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemDoor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */