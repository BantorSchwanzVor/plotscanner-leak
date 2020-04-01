package org.seltak.anubis.modules.world;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.seltak.anubis.module.Category;
import org.seltak.anubis.module.Module;

public class ScaffoldWalk extends Module {
  private static boolean cooldown = false;
  
  private float yawX;
  
  private float pitchX;
  
  public ScaffoldWalk() {
    super("ScaffoldWalk", Category.MOVEMENT, 21);
  }
  
  public void onPreUpdate() {
    this.yawX = this.mc.player.rotationYaw;
    this.pitchX = this.mc.player.rotationPitch;
    BlockPos playerBlock = new BlockPos(this.mc.player.posX, (this.mc.player.getEntityBoundingBox()).minY, this.mc.player.posZ);
    if (this.mc.world.isAirBlock(playerBlock.add(0, -1, 0)))
      if (isValidBlock(playerBlock.add(0, -2, 0))) {
        place(playerBlock.add(0, -1, 0), EnumFacing.UP);
      } else if (isValidBlock(playerBlock.add(-1, -1, 0))) {
        place(playerBlock.add(0, -1, 0), EnumFacing.EAST);
      } else if (isValidBlock(playerBlock.add(1, -1, 0))) {
        place(playerBlock.add(0, -1, 0), EnumFacing.WEST);
      } else if (isValidBlock(playerBlock.add(0, -1, -1))) {
        place(playerBlock.add(0, -1, 0), EnumFacing.SOUTH);
      } else if (isValidBlock(playerBlock.add(0, -1, 1))) {
        place(playerBlock.add(0, -1, 0), EnumFacing.NORTH);
      } else if (isValidBlock(playerBlock.add(1, -1, 1))) {
        if (isValidBlock(playerBlock.add(0, -1, 1)))
          place(playerBlock.add(0, -1, 1), EnumFacing.NORTH); 
        place(playerBlock.add(1, -1, 1), EnumFacing.EAST);
      } else if (isValidBlock(playerBlock.add(-1, -1, 1))) {
        if (isValidBlock(playerBlock.add(-1, -1, 0)))
          place(playerBlock.add(0, -1, 1), EnumFacing.WEST); 
        place(playerBlock.add(-1, -1, 1), EnumFacing.SOUTH);
      } else if (isValidBlock(playerBlock.add(-1, -1, -1))) {
        if (isValidBlock(playerBlock.add(0, -1, -1)))
          place(playerBlock.add(0, -1, -1), EnumFacing.SOUTH); 
        place(playerBlock.add(-1, -1, 1), EnumFacing.WEST);
      } else if (isValidBlock(playerBlock.add(1, -1, -1))) {
        if (isValidBlock(playerBlock.add(1, -1, 0)))
          place(playerBlock.add(1, -1, 0), EnumFacing.EAST); 
        place(playerBlock.add(1, -1, -1), EnumFacing.NORTH);
      }  
  }
  
  public void onPostUpdate() {
    this.mc.player.rotationYaw = this.yawX;
    this.mc.player.rotationPitch = this.pitchX;
    super.onPostUpdate();
  }
  
  private boolean isValidBlock(BlockPos pos) {
    Block b = this.mc.world.getBlockState(pos).getBlock();
    return (!(b instanceof net.minecraft.block.BlockLiquid) && b.getMaterial(this.mc.world.getBlockState(pos)) != Material.AIR);
  }
  
  private void place(BlockPos pos, EnumFacing face) {
    cooldown = true;
    if (face == EnumFacing.UP) {
      pos = pos.add(0, -1, 0);
    } else if (face == EnumFacing.NORTH) {
      pos = pos.add(0, 0, 1);
    } else if (face == EnumFacing.EAST) {
      pos = pos.add(-1, 0, 0);
    } else if (face == EnumFacing.SOUTH) {
      pos = pos.add(0, 0, -1);
    } else if (face == EnumFacing.WEST) {
      pos = pos.add(1, 0, 0);
    } 
    if (this.mc.player.getHeldItemMainhand() != null && this.mc.player.getHeldItemMainhand().getItem() instanceof net.minecraft.item.ItemBlock) {
      double var4 = pos.getX() + 0.25D - this.mc.player.posX;
      double var6 = pos.getZ() + 0.25D - this.mc.player.posZ;
      double var8 = pos.getY() + 0.25D - this.mc.player.posY + this.mc.player.getEyeHeight();
      double var14 = MathHelper.sqrt(var4 * var4 + var6 * var6);
      float yaw = (float)(Math.atan2(var6, var4) * 180.0D / 3.141592653689793D) - 90.0F;
      float pitch = (float)-(Math.atan2(var8, var14) * 180.0D / 3.141592653689793D);
      this.mc.player.rotationYaw = yaw;
      this.mc.player.rotationPitch = pitch;
      this.mc.player.swingArm(EnumHand.MAIN_HAND);
      this.mc.playerController.processRightClickBlock(this.mc.player, this.mc.world, pos, face, new Vec3d(0.5D, 0.5D, 0.5D), EnumHand.MAIN_HAND);
      int ticks = 0;
      ticks++;
      if (ticks >= 1000)
        ticks = 0; 
    } 
  }
  
  public void onEnable() {}
  
  public void onDisable() {}
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\modules\world\ScaffoldWalk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */