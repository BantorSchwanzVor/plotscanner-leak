package net.minecraft.entity.monster;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySilverfish extends EntityMob {
  private AISummonSilverfish summonSilverfish;
  
  public EntitySilverfish(World worldIn) {
    super(worldIn);
    setSize(0.4F, 0.3F);
  }
  
  public static void registerFixesSilverfish(DataFixer fixer) {
    EntityLiving.registerFixesMob(fixer, EntitySilverfish.class);
  }
  
  protected void initEntityAI() {
    this.summonSilverfish = new AISummonSilverfish(this);
    this.tasks.addTask(1, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
    this.tasks.addTask(3, this.summonSilverfish);
    this.tasks.addTask(4, (EntityAIBase)new EntityAIAttackMelee(this, 1.0D, false));
    this.tasks.addTask(5, (EntityAIBase)new AIHideInStone(this));
    this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget(this, true, new Class[0]));
    this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
  }
  
  public double getYOffset() {
    return 0.1D;
  }
  
  public float getEyeHeight() {
    return 0.1F;
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
    getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
  }
  
  protected boolean canTriggerWalking() {
    return false;
  }
  
  protected SoundEvent getAmbientSound() {
    return SoundEvents.ENTITY_SILVERFISH_AMBIENT;
  }
  
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return SoundEvents.ENTITY_SILVERFISH_HURT;
  }
  
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENTITY_SILVERFISH_DEATH;
  }
  
  protected void playStepSound(BlockPos pos, Block blockIn) {
    playSound(SoundEvents.ENTITY_SILVERFISH_STEP, 0.15F, 1.0F);
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (isEntityInvulnerable(source))
      return false; 
    if ((source instanceof net.minecraft.util.EntityDamageSource || source == DamageSource.magic) && this.summonSilverfish != null)
      this.summonSilverfish.notifyHurt(); 
    return super.attackEntityFrom(source, amount);
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    return LootTableList.ENTITIES_SILVERFISH;
  }
  
  public void onUpdate() {
    this.renderYawOffset = this.rotationYaw;
    super.onUpdate();
  }
  
  public void setRenderYawOffset(float offset) {
    this.rotationYaw = offset;
    super.setRenderYawOffset(offset);
  }
  
  public float getBlockPathWeight(BlockPos pos) {
    return (this.world.getBlockState(pos.down()).getBlock() == Blocks.STONE) ? 10.0F : super.getBlockPathWeight(pos);
  }
  
  protected boolean isValidLightLevel() {
    return true;
  }
  
  public boolean getCanSpawnHere() {
    if (super.getCanSpawnHere()) {
      EntityPlayer entityplayer = this.world.getNearestPlayerNotCreative((Entity)this, 5.0D);
      return (entityplayer == null);
    } 
    return false;
  }
  
  public EnumCreatureAttribute getCreatureAttribute() {
    return EnumCreatureAttribute.ARTHROPOD;
  }
  
  static class AIHideInStone extends EntityAIWander {
    private EnumFacing facing;
    
    private boolean doMerge;
    
    public AIHideInStone(EntitySilverfish silverfishIn) {
      super(silverfishIn, 1.0D, 10);
      setMutexBits(1);
    }
    
    public boolean shouldExecute() {
      if (this.entity.getAttackTarget() != null)
        return false; 
      if (!this.entity.getNavigator().noPath())
        return false; 
      Random random = this.entity.getRNG();
      if (this.entity.world.getGameRules().getBoolean("mobGriefing") && random.nextInt(10) == 0) {
        this.facing = EnumFacing.random(random);
        BlockPos blockpos = (new BlockPos(this.entity.posX, this.entity.posY + 0.5D, this.entity.posZ)).offset(this.facing);
        IBlockState iblockstate = this.entity.world.getBlockState(blockpos);
        if (BlockSilverfish.canContainSilverfish(iblockstate)) {
          this.doMerge = true;
          return true;
        } 
      } 
      this.doMerge = false;
      return super.shouldExecute();
    }
    
    public boolean continueExecuting() {
      return this.doMerge ? false : super.continueExecuting();
    }
    
    public void startExecuting() {
      if (!this.doMerge) {
        super.startExecuting();
      } else {
        World world = this.entity.world;
        BlockPos blockpos = (new BlockPos(this.entity.posX, this.entity.posY + 0.5D, this.entity.posZ)).offset(this.facing);
        IBlockState iblockstate = world.getBlockState(blockpos);
        if (BlockSilverfish.canContainSilverfish(iblockstate)) {
          world.setBlockState(blockpos, Blocks.MONSTER_EGG.getDefaultState().withProperty((IProperty)BlockSilverfish.VARIANT, (Comparable)BlockSilverfish.EnumType.forModelBlock(iblockstate)), 3);
          this.entity.spawnExplosionParticle();
          this.entity.setDead();
        } 
      } 
    }
  }
  
  static class AISummonSilverfish extends EntityAIBase {
    private final EntitySilverfish silverfish;
    
    private int lookForFriends;
    
    public AISummonSilverfish(EntitySilverfish silverfishIn) {
      this.silverfish = silverfishIn;
    }
    
    public void notifyHurt() {
      if (this.lookForFriends == 0)
        this.lookForFriends = 20; 
    }
    
    public boolean shouldExecute() {
      return (this.lookForFriends > 0);
    }
    
    public void updateTask() {
      this.lookForFriends--;
      if (this.lookForFriends <= 0) {
        World world = this.silverfish.world;
        Random random = this.silverfish.getRNG();
        BlockPos blockpos = new BlockPos((Entity)this.silverfish);
        for (int i = 0; i <= 5 && i >= -5; i = ((i <= 0) ? 1 : 0) - i) {
          for (int j = 0; j <= 10 && j >= -10; j = ((j <= 0) ? 1 : 0) - j) {
            for (int k = 0; k <= 10 && k >= -10; k = ((k <= 0) ? 1 : 0) - k) {
              BlockPos blockpos1 = blockpos.add(j, i, k);
              IBlockState iblockstate = world.getBlockState(blockpos1);
              if (iblockstate.getBlock() == Blocks.MONSTER_EGG) {
                if (world.getGameRules().getBoolean("mobGriefing")) {
                  world.destroyBlock(blockpos1, true);
                } else {
                  world.setBlockState(blockpos1, ((BlockSilverfish.EnumType)iblockstate.getValue((IProperty)BlockSilverfish.VARIANT)).getModelBlock(), 3);
                } 
                if (random.nextBoolean())
                  return; 
              } 
            } 
          } 
        } 
      } 
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\monster\EntitySilverfish.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */