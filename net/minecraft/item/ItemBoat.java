package net.minecraft.item;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemBoat extends Item {
  private final EntityBoat.Type type;
  
  public ItemBoat(EntityBoat.Type typeIn) {
    this.type = typeIn;
    this.maxStackSize = 1;
    setCreativeTab(CreativeTabs.TRANSPORTATION);
    setUnlocalizedName("boat." + typeIn.getName());
  }
  
  public ActionResult<ItemStack> onItemRightClick(World itemStackIn, EntityPlayer worldIn, EnumHand playerIn) {
    ItemStack itemstack = worldIn.getHeldItem(playerIn);
    float f = 1.0F;
    float f1 = worldIn.prevRotationPitch + (worldIn.rotationPitch - worldIn.prevRotationPitch) * 1.0F;
    float f2 = worldIn.prevRotationYaw + (worldIn.rotationYaw - worldIn.prevRotationYaw) * 1.0F;
    double d0 = worldIn.prevPosX + (worldIn.posX - worldIn.prevPosX) * 1.0D;
    double d1 = worldIn.prevPosY + (worldIn.posY - worldIn.prevPosY) * 1.0D + worldIn.getEyeHeight();
    double d2 = worldIn.prevPosZ + (worldIn.posZ - worldIn.prevPosZ) * 1.0D;
    Vec3d vec3d = new Vec3d(d0, d1, d2);
    float f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
    float f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
    float f5 = -MathHelper.cos(-f1 * 0.017453292F);
    float f6 = MathHelper.sin(-f1 * 0.017453292F);
    float f7 = f4 * f5;
    float f8 = f3 * f5;
    double d3 = 5.0D;
    Vec3d vec3d1 = vec3d.addVector(f7 * 5.0D, f6 * 5.0D, f8 * 5.0D);
    RayTraceResult raytraceresult = itemStackIn.rayTraceBlocks(vec3d, vec3d1, true);
    if (raytraceresult == null)
      return new ActionResult(EnumActionResult.PASS, itemstack); 
    Vec3d vec3d2 = worldIn.getLook(1.0F);
    boolean flag = false;
    List<Entity> list = itemStackIn.getEntitiesWithinAABBExcludingEntity((Entity)worldIn, worldIn.getEntityBoundingBox().addCoord(vec3d2.xCoord * 5.0D, vec3d2.yCoord * 5.0D, vec3d2.zCoord * 5.0D).expandXyz(1.0D));
    for (int i = 0; i < list.size(); i++) {
      Entity entity = list.get(i);
      if (entity.canBeCollidedWith()) {
        AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expandXyz(entity.getCollisionBorderSize());
        if (axisalignedbb.isVecInside(vec3d))
          flag = true; 
      } 
    } 
    if (flag)
      return new ActionResult(EnumActionResult.PASS, itemstack); 
    if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK)
      return new ActionResult(EnumActionResult.PASS, itemstack); 
    Block block = itemStackIn.getBlockState(raytraceresult.getBlockPos()).getBlock();
    boolean flag1 = !(block != Blocks.WATER && block != Blocks.FLOWING_WATER);
    EntityBoat entityboat = new EntityBoat(itemStackIn, raytraceresult.hitVec.xCoord, flag1 ? (raytraceresult.hitVec.yCoord - 0.12D) : raytraceresult.hitVec.yCoord, raytraceresult.hitVec.zCoord);
    entityboat.setBoatType(this.type);
    entityboat.rotationYaw = worldIn.rotationYaw;
    if (!itemStackIn.getCollisionBoxes((Entity)entityboat, entityboat.getEntityBoundingBox().expandXyz(-0.1D)).isEmpty())
      return new ActionResult(EnumActionResult.FAIL, itemstack); 
    if (!itemStackIn.isRemote)
      itemStackIn.spawnEntityInWorld((Entity)entityboat); 
    if (!worldIn.capabilities.isCreativeMode)
      itemstack.func_190918_g(1); 
    worldIn.addStat(StatList.getObjectUseStats(this));
    return new ActionResult(EnumActionResult.SUCCESS, itemstack);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemBoat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */