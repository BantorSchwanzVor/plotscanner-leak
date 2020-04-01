package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAIEatGrass extends EntityAIBase {
  private static final Predicate<IBlockState> IS_TALL_GRASS = (Predicate<IBlockState>)BlockStateMatcher.forBlock((Block)Blocks.TALLGRASS).where((IProperty)BlockTallGrass.TYPE, Predicates.equalTo(BlockTallGrass.EnumType.GRASS));
  
  private final EntityLiving grassEaterEntity;
  
  private final World entityWorld;
  
  int eatingGrassTimer;
  
  public EntityAIEatGrass(EntityLiving grassEaterEntityIn) {
    this.grassEaterEntity = grassEaterEntityIn;
    this.entityWorld = grassEaterEntityIn.world;
    setMutexBits(7);
  }
  
  public boolean shouldExecute() {
    if (this.grassEaterEntity.getRNG().nextInt(this.grassEaterEntity.isChild() ? 50 : 1000) != 0)
      return false; 
    BlockPos blockpos = new BlockPos(this.grassEaterEntity.posX, this.grassEaterEntity.posY, this.grassEaterEntity.posZ);
    if (IS_TALL_GRASS.apply(this.entityWorld.getBlockState(blockpos)))
      return true; 
    return (this.entityWorld.getBlockState(blockpos.down()).getBlock() == Blocks.GRASS);
  }
  
  public void startExecuting() {
    this.eatingGrassTimer = 40;
    this.entityWorld.setEntityState((Entity)this.grassEaterEntity, (byte)10);
    this.grassEaterEntity.getNavigator().clearPathEntity();
  }
  
  public void resetTask() {
    this.eatingGrassTimer = 0;
  }
  
  public boolean continueExecuting() {
    return (this.eatingGrassTimer > 0);
  }
  
  public int getEatingGrassTimer() {
    return this.eatingGrassTimer;
  }
  
  public void updateTask() {
    this.eatingGrassTimer = Math.max(0, this.eatingGrassTimer - 1);
    if (this.eatingGrassTimer == 4) {
      BlockPos blockpos = new BlockPos(this.grassEaterEntity.posX, this.grassEaterEntity.posY, this.grassEaterEntity.posZ);
      if (IS_TALL_GRASS.apply(this.entityWorld.getBlockState(blockpos))) {
        if (this.entityWorld.getGameRules().getBoolean("mobGriefing"))
          this.entityWorld.destroyBlock(blockpos, false); 
        this.grassEaterEntity.eatGrassBonus();
      } else {
        BlockPos blockpos1 = blockpos.down();
        if (this.entityWorld.getBlockState(blockpos1).getBlock() == Blocks.GRASS) {
          if (this.entityWorld.getGameRules().getBoolean("mobGriefing")) {
            this.entityWorld.playEvent(2001, blockpos1, Block.getIdFromBlock((Block)Blocks.GRASS));
            this.entityWorld.setBlockState(blockpos1, Blocks.DIRT.getDefaultState(), 2);
          } 
          this.grassEaterEntity.eatGrassBonus();
        } 
      } 
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\ai\EntityAIEatGrass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */