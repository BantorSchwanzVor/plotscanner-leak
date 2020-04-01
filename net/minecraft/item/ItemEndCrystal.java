package net.minecraft.item;

import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.end.DragonFightManager;

public class ItemEndCrystal extends Item {
  public ItemEndCrystal() {
    setUnlocalizedName("end_crystal");
    setCreativeTab(CreativeTabs.DECORATIONS);
  }
  
  public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY) {
    IBlockState iblockstate = playerIn.getBlockState(worldIn);
    if (iblockstate.getBlock() != Blocks.OBSIDIAN && iblockstate.getBlock() != Blocks.BEDROCK)
      return EnumActionResult.FAIL; 
    BlockPos blockpos = worldIn.up();
    ItemStack itemstack = stack.getHeldItem(pos);
    if (!stack.canPlayerEdit(blockpos, hand, itemstack))
      return EnumActionResult.FAIL; 
    BlockPos blockpos1 = blockpos.up();
    boolean flag = (!playerIn.isAirBlock(blockpos) && !playerIn.getBlockState(blockpos).getBlock().isReplaceable((IBlockAccess)playerIn, blockpos));
    int i = flag | ((!playerIn.isAirBlock(blockpos1) && !playerIn.getBlockState(blockpos1).getBlock().isReplaceable((IBlockAccess)playerIn, blockpos1)) ? 1 : 0);
    if (i != 0)
      return EnumActionResult.FAIL; 
    double d0 = blockpos.getX();
    double d1 = blockpos.getY();
    double d2 = blockpos.getZ();
    List<Entity> list = playerIn.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(d0, d1, d2, d0 + 1.0D, d1 + 2.0D, d2 + 1.0D));
    if (!list.isEmpty())
      return EnumActionResult.FAIL; 
    if (!playerIn.isRemote) {
      EntityEnderCrystal entityendercrystal = new EntityEnderCrystal(playerIn, (worldIn.getX() + 0.5F), (worldIn.getY() + 1), (worldIn.getZ() + 0.5F));
      entityendercrystal.setShowBottom(false);
      playerIn.spawnEntityInWorld((Entity)entityendercrystal);
      if (playerIn.provider instanceof WorldProviderEnd) {
        DragonFightManager dragonfightmanager = ((WorldProviderEnd)playerIn.provider).getDragonFightManager();
        dragonfightmanager.respawnDragon();
      } 
    } 
    itemstack.func_190918_g(1);
    return EnumActionResult.SUCCESS;
  }
  
  public boolean hasEffect(ItemStack stack) {
    return true;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemEndCrystal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */