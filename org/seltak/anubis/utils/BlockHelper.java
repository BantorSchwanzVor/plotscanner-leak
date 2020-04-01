package org.seltak.anubis.utils;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHelper {
  private static Minecraft mc = Minecraft.getMinecraft();
  
  public static float[] getRotationsForPosition(double x, double y, double z) {
    return getRotationsForPosition(x, y, z, mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
  }
  
  public static float[] getRotationsForPosition(double x, double y, double z, double sourceX, double sourceY, double sourceZ) {
    double deltaX = x - sourceX;
    double deltaY = y - sourceY;
    double deltaZ = z - sourceZ;
    if (deltaZ < 0.0D && deltaX < 0.0D) {
      yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
    } else if (deltaZ < 0.0D && deltaX > 0.0D) {
      yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
    } else {
      yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
    } 
    double distanceXZ = Math.sqrt(deltaX * deltaX + deltaZ * 
        deltaZ);
    double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
    double yawToEntity = wrapAngleTo180((float)yawToEntity);
    pitchToEntity = wrapAngleTo180((float)pitchToEntity);
    yawToEntity = Double.isNaN(yawToEntity) ? 0.0D : yawToEntity;
    pitchToEntity = Double.isNaN(pitchToEntity) ? 0.0D : pitchToEntity;
    return new float[] { (float)yawToEntity, (float)pitchToEntity };
  }
  
  public static float[] getBlockRotations(int x, int y, int z) {
    return getRotationsForPosition(x + 0.5D, y + 0.5D, z + 0.5D);
  }
  
  public static float[] getFacingRotations(int x, int y, int z, EnumFacing facing) {
    return getFacingRotations(x, y, z, facing, 1.0D);
  }
  
  public static float[] getFacingRotations(int x, int y, int z, EnumFacing facing, double width) {
    return getRotationsForPosition(x + 0.5D + facing.getDirectionVec().getX() * width / 2.0D, y + 0.5D + facing.getDirectionVec().getY() * width / 2.0D, z + 0.5D + facing.getDirectionVec().getZ() * width / 2.0D);
  }
  
  private static float wrapAngleTo180(float angle) {
    angle %= 360.0F;
    while (angle >= 180.0F)
      angle -= 360.0F; 
    while (angle < -180.0F)
      angle += 360.0F; 
    return angle;
  }
  
  public static boolean canSeeBlock(int x, int y, int z) {
    return (getFacing(new BlockPos(x, y, z)) != null);
  }
  
  public static Block getBlock(int x, int y, int z) {
    return mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
  }
  
  public static Block getBlock(double x, double y, double z) {
    return mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
  }
  
  public static boolean isInLiquid() {
    boolean inLiquid = false;
    int y = (int)((mc.player.getEntityBoundingBox()).minY + 1.1E-4D);
    for (int x = MathHelper.floor((mc.player.getEntityBoundingBox()).minX); x < 
      MathHelper.floor((mc.player.getEntityBoundingBox()).maxX) + 1; x++) {
      for (int z = MathHelper.floor((mc.player.getEntityBoundingBox()).minZ); z < 
        MathHelper.floor((mc.player.getEntityBoundingBox()).maxZ) + 1; z++) {
        Block block = getBlock(x, y, z);
        if (block != null && !(block instanceof net.minecraft.block.BlockAir)) {
          if (!(block instanceof net.minecraft.block.BlockLiquid))
            return false; 
          inLiquid = true;
        } 
      } 
    } 
    return inLiquid;
  }
  
  public static boolean isOnIce() {
    boolean onIce = false;
    int y = 
      (int)(mc.player.getEntityBoundingBox().offset(0.0D, -0.1D, 0.0D)).minY;
    for (int x = MathHelper.floor((mc.player.getEntityBoundingBox()).minX); x < 
      MathHelper.floor((mc.player.getEntityBoundingBox()).maxX) + 1; x++) {
      for (int z = MathHelper.floor((mc.player.getEntityBoundingBox()).minZ); z < 
        MathHelper.floor((mc.player.getEntityBoundingBox()).maxZ) + 1; z++) {
        Block block = getBlock(x, y, z);
        if (block != null && !(block instanceof net.minecraft.block.BlockAir) && (
          block instanceof net.minecraft.block.BlockPackedIce || 
          block instanceof net.minecraft.block.BlockIce))
          onIce = true; 
      } 
    } 
    return onIce;
  }
  
  public static boolean isOnFloor(double yOffset) {
    boolean onGround = false;
    int y = 
      (int)(mc.player.getEntityBoundingBox().offset(0.0D, yOffset, 0.0D)).minY;
    for (int x = MathHelper.floor((mc.player.getEntityBoundingBox()).minX); x < 
      MathHelper.floor((mc.player.getEntityBoundingBox()).maxX) + 1; x++) {
      for (int z = MathHelper.floor((mc.player.getEntityBoundingBox()).minZ); z < 
        MathHelper.floor((mc.player.getEntityBoundingBox()).maxZ) + 1; z++) {
        Block block = getBlock(x, y, z);
        AxisAlignedBB boundingBox = block
          .getCollisionBoundingBox((Minecraft.getMinecraft()).world.getBlockState(new BlockPos(x, y, z)), (IBlockAccess)mc.world, new BlockPos(x, y, z));
        List<AxisAlignedBB> boundingBoxList = new ArrayList<>();
        block.addCollisionBoxToList(mc.world.getBlockState(new BlockPos(x, y, z)), (World)mc.world, new BlockPos(x, y, z), mc.player.getEntityBoundingBox().offset(0.0D, yOffset, 0.0D).contract(0.0625D), boundingBoxList, (Entity)mc.player, false);
        if (!(block instanceof net.minecraft.block.BlockAir) && 
          block.isCollidable() && !boundingBoxList.isEmpty())
          onGround = true; 
      } 
    } 
    return onGround;
  }
  
  public static BlockPos getFloor() {
    AxisAlignedBB INFINITY_BB = new AxisAlignedBB(
        Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 
        Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    BlockPos highestPos = null;
    for (int i = 0; i <= Math.ceil(mc.player.posY); i++) {
      for (int x = MathHelper.floor((mc.player.getEntityBoundingBox()).minX); x < 
        MathHelper.floor((mc.player.getEntityBoundingBox()).maxX) + 1; x++) {
        for (int z = MathHelper.floor((mc.player.getEntityBoundingBox()).minZ); z < 
          MathHelper.floor((mc.player.getEntityBoundingBox()).maxZ) + 1; z++) {
          BlockPos pos = new BlockPos(x, mc.player.posY - 1.0D - i, z);
          IBlockState state = mc.world.getBlockState(pos);
          List<AxisAlignedBB> boundingBoxes = new ArrayList<>();
          state.getBlock().addCollisionBoxToList(state, (World)mc.world, pos, INFINITY_BB, boundingBoxes, (Entity)mc.player, true);
          if (!boundingBoxes.isEmpty()) {
            if (highestPos == null)
              highestPos = pos; 
            if (pos.getY() > highestPos.getY())
              highestPos = pos; 
          } 
        } 
      } 
    } 
    return highestPos;
  }
  
  public static boolean isOnLadder() {
    boolean onLadder = false;
    int y = 
      (int)(mc.player.getEntityBoundingBox().offset(0.0D, 1.0D, 0.0D)).minY;
    for (int x = MathHelper.floor((mc.player.getEntityBoundingBox()).minX); x < 
      MathHelper.floor((mc.player.getEntityBoundingBox()).maxX) + 1; x++) {
      for (int z = MathHelper.floor((mc.player.getEntityBoundingBox()).minZ); z < 
        MathHelper.floor((mc.player.getEntityBoundingBox()).maxZ) + 1; z++) {
        Block block = getBlock(x, y, z);
        if (block != null && !(block instanceof net.minecraft.block.BlockAir)) {
          if (!(block instanceof net.minecraft.block.BlockLadder))
            return false; 
          onLadder = true;
        } 
      } 
    } 
    return !(!onLadder && !mc.player.isOnLadder());
  }
  
  public static boolean isOnLiquid() {
    boolean onLiquid = false;
    int y = 
      (int)(mc.player.getEntityBoundingBox().offset(0.0D, -0.01D, 0.0D)).minY;
    for (int x = MathHelper.floor((mc.player.getEntityBoundingBox()).minX); x < 
      MathHelper.floor((mc.player.getEntityBoundingBox()).maxX) + 1; x++) {
      for (int z = MathHelper.floor((mc.player.getEntityBoundingBox()).minZ); z < 
        MathHelper.floor((mc.player.getEntityBoundingBox()).maxZ) + 1; z++) {
        Block block = getBlock(x, y, z);
        if (block != null && !(block instanceof net.minecraft.block.BlockAir)) {
          if (!(block instanceof net.minecraft.block.BlockLiquid))
            return false; 
          onLiquid = true;
        } 
      } 
    } 
    return onLiquid;
  }
  
  public static EnumFacing getFacing(BlockPos pos) {
    return getFacing(pos, 1.0D);
  }
  
  public static EnumFacing getFacing(BlockPos pos, double width) {
    EnumFacing[] orderedValues = { EnumFacing.UP, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.DOWN };
    byte b;
    int i;
    EnumFacing[] arrayOfEnumFacing1;
    for (i = (arrayOfEnumFacing1 = orderedValues).length, b = 0; b < i; ) {
      EnumFacing facing = arrayOfEnumFacing1[b];
      RayTraceResult objectHit = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), 
          new Vec3d(pos.getX() + 0.5D + facing.getDirectionVec().getX() * width / 2.0D, 
            pos.getY() + 0.5D + facing.getDirectionVec().getY() * width / 2.0D, 
            pos.getZ() + 0.5D + facing.getDirectionVec().getZ() * width / 2.0D), false, true, false);
      if (objectHit == null || (objectHit.typeOfHit == RayTraceResult.Type.BLOCK && objectHit.getBlockPos().equals(pos)))
        return facing; 
      b++;
    } 
    return null;
  }
  
  public static boolean isInsideBlock() {
    for (int x = MathHelper.floor((mc.player.getEntityBoundingBox()).minX); x < 
      MathHelper.floor((mc.player.getEntityBoundingBox()).maxX) + 1; x++) {
      for (int y = MathHelper.floor((mc.player.getEntityBoundingBox()).minY); y < 
        MathHelper.floor((mc.player.getEntityBoundingBox()).maxY) + 1; y++) {
        for (int z = MathHelper.floor((mc.player.getEntityBoundingBox()).minZ); z < 
          MathHelper.floor((mc.player.getEntityBoundingBox()).maxZ) + 1; z++) {
          Block block = (Minecraft.getMinecraft()).world
            .getBlockState(new BlockPos(x, y, z)).getBlock();
          if (block != null && !(block instanceof net.minecraft.block.BlockAir)) {
            AxisAlignedBB boundingBox = block.getCollisionBoundingBox((Minecraft.getMinecraft()).world.getBlockState(new BlockPos(x, y, z)), (IBlockAccess)mc.world, new BlockPos(x, y, z));
            if (block instanceof net.minecraft.block.BlockHopper)
              boundingBox = new AxisAlignedBB(x, y, z, (x + 1), (y + 1), (z + 1)); 
            if (boundingBox != null && 
              mc.player.getEntityBoundingBox().intersectsWith(boundingBox))
              return true; 
          } 
        } 
      } 
    } 
    return false;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubi\\utils\BlockHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */