package net.minecraft.entity.passive;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public abstract class EntityAnimal extends EntityAgeable implements IAnimals {
  protected Block spawnableBlock = (Block)Blocks.GRASS;
  
  private int inLove;
  
  private UUID playerInLove;
  
  public EntityAnimal(World worldIn) {
    super(worldIn);
  }
  
  protected void updateAITasks() {
    if (getGrowingAge() != 0)
      this.inLove = 0; 
    super.updateAITasks();
  }
  
  public void onLivingUpdate() {
    super.onLivingUpdate();
    if (getGrowingAge() != 0)
      this.inLove = 0; 
    if (this.inLove > 0) {
      this.inLove--;
      if (this.inLove % 10 == 0) {
        double d0 = this.rand.nextGaussian() * 0.02D;
        double d1 = this.rand.nextGaussian() * 0.02D;
        double d2 = this.rand.nextGaussian() * 0.02D;
        this.world.spawnParticle(EnumParticleTypes.HEART, this.posX + (this.rand.nextFloat() * this.width * 2.0F) - this.width, this.posY + 0.5D + (this.rand.nextFloat() * this.height), this.posZ + (this.rand.nextFloat() * this.width * 2.0F) - this.width, d0, d1, d2, new int[0]);
      } 
    } 
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (isEntityInvulnerable(source))
      return false; 
    this.inLove = 0;
    return super.attackEntityFrom(source, amount);
  }
  
  public float getBlockPathWeight(BlockPos pos) {
    return (this.world.getBlockState(pos.down()).getBlock() == this.spawnableBlock) ? 10.0F : (this.world.getLightBrightness(pos) - 0.5F);
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    compound.setInteger("InLove", this.inLove);
    if (this.playerInLove != null)
      compound.setUniqueId("LoveCause", this.playerInLove); 
  }
  
  public double getYOffset() {
    return 0.14D;
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    this.inLove = compound.getInteger("InLove");
    this.playerInLove = compound.hasUniqueId("LoveCause") ? compound.getUniqueId("LoveCause") : null;
  }
  
  public boolean getCanSpawnHere() {
    int i = MathHelper.floor(this.posX);
    int j = MathHelper.floor((getEntityBoundingBox()).minY);
    int k = MathHelper.floor(this.posZ);
    BlockPos blockpos = new BlockPos(i, j, k);
    return (this.world.getBlockState(blockpos.down()).getBlock() == this.spawnableBlock && this.world.getLight(blockpos) > 8 && super.getCanSpawnHere());
  }
  
  public int getTalkInterval() {
    return 120;
  }
  
  protected boolean canDespawn() {
    return false;
  }
  
  protected int getExperiencePoints(EntityPlayer player) {
    return 1 + this.world.rand.nextInt(3);
  }
  
  public boolean isBreedingItem(ItemStack stack) {
    return (stack.getItem() == Items.WHEAT);
  }
  
  public boolean processInteract(EntityPlayer player, EnumHand hand) {
    ItemStack itemstack = player.getHeldItem(hand);
    if (!itemstack.func_190926_b()) {
      if (isBreedingItem(itemstack) && getGrowingAge() == 0 && this.inLove <= 0) {
        consumeItemFromStack(player, itemstack);
        setInLove(player);
        return true;
      } 
      if (isChild() && isBreedingItem(itemstack)) {
        consumeItemFromStack(player, itemstack);
        ageUp((int)((-getGrowingAge() / 20) * 0.1F), true);
        return true;
      } 
    } 
    return super.processInteract(player, hand);
  }
  
  protected void consumeItemFromStack(EntityPlayer player, ItemStack stack) {
    if (!player.capabilities.isCreativeMode)
      stack.func_190918_g(1); 
  }
  
  public void setInLove(@Nullable EntityPlayer player) {
    this.inLove = 600;
    if (player != null)
      this.playerInLove = player.getUniqueID(); 
    this.world.setEntityState((Entity)this, (byte)18);
  }
  
  @Nullable
  public EntityPlayerMP func_191993_do() {
    if (this.playerInLove == null)
      return null; 
    EntityPlayer entityplayer = this.world.getPlayerEntityByUUID(this.playerInLove);
    return (entityplayer instanceof EntityPlayerMP) ? (EntityPlayerMP)entityplayer : null;
  }
  
  public boolean isInLove() {
    return (this.inLove > 0);
  }
  
  public void resetInLove() {
    this.inLove = 0;
  }
  
  public boolean canMateWith(EntityAnimal otherAnimal) {
    if (otherAnimal == this)
      return false; 
    if (otherAnimal.getClass() != getClass())
      return false; 
    return (isInLove() && otherAnimal.isInLove());
  }
  
  public void handleStatusUpdate(byte id) {
    if (id == 18) {
      for (int i = 0; i < 7; i++) {
        double d0 = this.rand.nextGaussian() * 0.02D;
        double d1 = this.rand.nextGaussian() * 0.02D;
        double d2 = this.rand.nextGaussian() * 0.02D;
        this.world.spawnParticle(EnumParticleTypes.HEART, this.posX + (this.rand.nextFloat() * this.width * 2.0F) - this.width, this.posY + 0.5D + (this.rand.nextFloat() * this.height), this.posZ + (this.rand.nextFloat() * this.width * 2.0F) - this.width, d0, d1, d2, new int[0]);
      } 
    } else {
      super.handleStatusUpdate(id);
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\passive\EntityAnimal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */