package net.minecraft.block.state;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPistonStructureHelper {
  private final World world;
  
  private final BlockPos pistonPos;
  
  private final BlockPos blockToMove;
  
  private final EnumFacing moveDirection;
  
  private final List<BlockPos> toMove = Lists.newArrayList();
  
  private final List<BlockPos> toDestroy = Lists.newArrayList();
  
  public BlockPistonStructureHelper(World worldIn, BlockPos posIn, EnumFacing pistonFacing, boolean extending) {
    this.world = worldIn;
    this.pistonPos = posIn;
    if (extending) {
      this.moveDirection = pistonFacing;
      this.blockToMove = posIn.offset(pistonFacing);
    } else {
      this.moveDirection = pistonFacing.getOpposite();
      this.blockToMove = posIn.offset(pistonFacing, 2);
    } 
  }
  
  public boolean canMove() {
    this.toMove.clear();
    this.toDestroy.clear();
    IBlockState iblockstate = this.world.getBlockState(this.blockToMove);
    if (!BlockPistonBase.canPush(iblockstate, this.world, this.blockToMove, this.moveDirection, false, this.moveDirection)) {
      if (iblockstate.getMobilityFlag() == EnumPushReaction.DESTROY) {
        this.toDestroy.add(this.blockToMove);
        return true;
      } 
      return false;
    } 
    if (!addBlockLine(this.blockToMove, this.moveDirection))
      return false; 
    for (int i = 0; i < this.toMove.size(); i++) {
      BlockPos blockpos = this.toMove.get(i);
      if (this.world.getBlockState(blockpos).getBlock() == Blocks.SLIME_BLOCK && !addBranchingBlocks(blockpos))
        return false; 
    } 
    return true;
  }
  
  private boolean addBlockLine(BlockPos origin, EnumFacing p_177251_2_) {
    IBlockState iblockstate = this.world.getBlockState(origin);
    Block block = iblockstate.getBlock();
    if (iblockstate.getMaterial() == Material.AIR)
      return true; 
    if (!BlockPistonBase.canPush(iblockstate, this.world, origin, this.moveDirection, false, p_177251_2_))
      return true; 
    if (origin.equals(this.pistonPos))
      return true; 
    if (this.toMove.contains(origin))
      return true; 
    int i = 1;
    if (i + this.toMove.size() > 12)
      return false; 
    while (block == Blocks.SLIME_BLOCK) {
      BlockPos blockpos = origin.offset(this.moveDirection.getOpposite(), i);
      iblockstate = this.world.getBlockState(blockpos);
      block = iblockstate.getBlock();
      if (iblockstate.getMaterial() == Material.AIR || !BlockPistonBase.canPush(iblockstate, this.world, blockpos, this.moveDirection, false, this.moveDirection.getOpposite()) || blockpos.equals(this.pistonPos))
        break; 
      i++;
      if (i + this.toMove.size() > 12)
        return false; 
    } 
    int i1 = 0;
    for (int j = i - 1; j >= 0; j--) {
      this.toMove.add(origin.offset(this.moveDirection.getOpposite(), j));
      i1++;
    } 
    int j1 = 1;
    while (true) {
      BlockPos blockpos1 = origin.offset(this.moveDirection, j1);
      int k = this.toMove.indexOf(blockpos1);
      if (k > -1) {
        reorderListAtCollision(i1, k);
        for (int l = 0; l <= k + i1; l++) {
          BlockPos blockpos2 = this.toMove.get(l);
          if (this.world.getBlockState(blockpos2).getBlock() == Blocks.SLIME_BLOCK && !addBranchingBlocks(blockpos2))
            return false; 
        } 
        return true;
      } 
      iblockstate = this.world.getBlockState(blockpos1);
      if (iblockstate.getMaterial() == Material.AIR)
        return true; 
      if (!BlockPistonBase.canPush(iblockstate, this.world, blockpos1, this.moveDirection, true, this.moveDirection) || blockpos1.equals(this.pistonPos))
        return false; 
      if (iblockstate.getMobilityFlag() == EnumPushReaction.DESTROY) {
        this.toDestroy.add(blockpos1);
        return true;
      } 
      if (this.toMove.size() >= 12)
        return false; 
      this.toMove.add(blockpos1);
      i1++;
      j1++;
    } 
  }
  
  private void reorderListAtCollision(int p_177255_1_, int p_177255_2_) {
    List<BlockPos> list = Lists.newArrayList();
    List<BlockPos> list1 = Lists.newArrayList();
    List<BlockPos> list2 = Lists.newArrayList();
    list.addAll(this.toMove.subList(0, p_177255_2_));
    list1.addAll(this.toMove.subList(this.toMove.size() - p_177255_1_, this.toMove.size()));
    list2.addAll(this.toMove.subList(p_177255_2_, this.toMove.size() - p_177255_1_));
    this.toMove.clear();
    this.toMove.addAll(list);
    this.toMove.addAll(list1);
    this.toMove.addAll(list2);
  }
  
  private boolean addBranchingBlocks(BlockPos p_177250_1_) {
    byte b;
    int i;
    EnumFacing[] arrayOfEnumFacing;
    for (i = (arrayOfEnumFacing = EnumFacing.values()).length, b = 0; b < i; ) {
      EnumFacing enumfacing = arrayOfEnumFacing[b];
      if (enumfacing.getAxis() != this.moveDirection.getAxis() && !addBlockLine(p_177250_1_.offset(enumfacing), enumfacing))
        return false; 
      b++;
    } 
    return true;
  }
  
  public List<BlockPos> getBlocksToMove() {
    return this.toMove;
  }
  
  public List<BlockPos> getBlocksToDestroy() {
    return this.toDestroy;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\state\BlockPistonStructureHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */