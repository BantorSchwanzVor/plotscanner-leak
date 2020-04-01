package net.minecraft.item;

import javax.annotation.Nullable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemHangingEntity extends Item {
  private final Class<? extends EntityHanging> hangingEntityClass;
  
  public ItemHangingEntity(Class<? extends EntityHanging> entityClass) {
    this.hangingEntityClass = entityClass;
    setCreativeTab(CreativeTabs.DECORATIONS);
  }
  
  public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY) {
    ItemStack itemstack = stack.getHeldItem(pos);
    BlockPos blockpos = worldIn.offset(hand);
    if (hand != EnumFacing.DOWN && hand != EnumFacing.UP && stack.canPlayerEdit(blockpos, hand, itemstack)) {
      EntityHanging entityhanging = createEntity(playerIn, blockpos, hand);
      if (entityhanging != null && entityhanging.onValidSurface()) {
        if (!playerIn.isRemote) {
          entityhanging.playPlaceSound();
          playerIn.spawnEntityInWorld((Entity)entityhanging);
        } 
        itemstack.func_190918_g(1);
      } 
      return EnumActionResult.SUCCESS;
    } 
    return EnumActionResult.FAIL;
  }
  
  @Nullable
  private EntityHanging createEntity(World worldIn, BlockPos pos, EnumFacing clickedSide) {
    if (this.hangingEntityClass == EntityPainting.class)
      return (EntityHanging)new EntityPainting(worldIn, pos, clickedSide); 
    return (this.hangingEntityClass == EntityItemFrame.class) ? (EntityHanging)new EntityItemFrame(worldIn, pos, clickedSide) : null;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemHangingEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */