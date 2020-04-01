package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemLead extends Item {
  public ItemLead() {
    setCreativeTab(CreativeTabs.TOOLS);
  }
  
  public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY) {
    Block block = playerIn.getBlockState(worldIn).getBlock();
    if (!(block instanceof net.minecraft.block.BlockFence))
      return EnumActionResult.PASS; 
    if (!playerIn.isRemote)
      attachToFence(stack, playerIn, worldIn); 
    return EnumActionResult.SUCCESS;
  }
  
  public static boolean attachToFence(EntityPlayer player, World worldIn, BlockPos fence) {
    EntityLeashKnot entityleashknot = EntityLeashKnot.getKnotForPosition(worldIn, fence);
    boolean flag = false;
    double d0 = 7.0D;
    int i = fence.getX();
    int j = fence.getY();
    int k = fence.getZ();
    for (EntityLiving entityliving : worldIn.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(i - 7.0D, j - 7.0D, k - 7.0D, i + 7.0D, j + 7.0D, k + 7.0D))) {
      if (entityliving.getLeashed() && entityliving.getLeashedToEntity() == player) {
        if (entityleashknot == null)
          entityleashknot = EntityLeashKnot.createKnot(worldIn, fence); 
        entityliving.setLeashedToEntity((Entity)entityleashknot, true);
        flag = true;
      } 
    } 
    return flag;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemLead.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */