package net.minecraft.entity.item;

import com.google.common.base.Optional;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.end.DragonFightManager;

public class EntityEnderCrystal extends Entity {
  private static final DataParameter<Optional<BlockPos>> BEAM_TARGET = EntityDataManager.createKey(EntityEnderCrystal.class, DataSerializers.OPTIONAL_BLOCK_POS);
  
  private static final DataParameter<Boolean> SHOW_BOTTOM = EntityDataManager.createKey(EntityEnderCrystal.class, DataSerializers.BOOLEAN);
  
  public int innerRotation;
  
  public EntityEnderCrystal(World worldIn) {
    super(worldIn);
    this.preventEntitySpawning = true;
    setSize(2.0F, 2.0F);
    this.innerRotation = this.rand.nextInt(100000);
  }
  
  public EntityEnderCrystal(World worldIn, double x, double y, double z) {
    this(worldIn);
    setPosition(x, y, z);
  }
  
  protected boolean canTriggerWalking() {
    return false;
  }
  
  protected void entityInit() {
    getDataManager().register(BEAM_TARGET, Optional.absent());
    getDataManager().register(SHOW_BOTTOM, Boolean.valueOf(true));
  }
  
  public void onUpdate() {
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    this.innerRotation++;
    if (!this.world.isRemote) {
      BlockPos blockpos = new BlockPos(this);
      if (this.world.provider instanceof WorldProviderEnd && this.world.getBlockState(blockpos).getBlock() != Blocks.FIRE)
        this.world.setBlockState(blockpos, Blocks.FIRE.getDefaultState()); 
    } 
  }
  
  protected void writeEntityToNBT(NBTTagCompound compound) {
    if (getBeamTarget() != null)
      compound.setTag("BeamTarget", (NBTBase)NBTUtil.createPosTag(getBeamTarget())); 
    compound.setBoolean("ShowBottom", shouldShowBottom());
  }
  
  protected void readEntityFromNBT(NBTTagCompound compound) {
    if (compound.hasKey("BeamTarget", 10))
      setBeamTarget(NBTUtil.getPosFromTag(compound.getCompoundTag("BeamTarget"))); 
    if (compound.hasKey("ShowBottom", 1))
      setShowBottom(compound.getBoolean("ShowBottom")); 
  }
  
  public boolean canBeCollidedWith() {
    return true;
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (isEntityInvulnerable(source))
      return false; 
    if (source.getEntity() instanceof net.minecraft.entity.boss.EntityDragon)
      return false; 
    if (!this.isDead && !this.world.isRemote) {
      setDead();
      if (!this.world.isRemote) {
        if (!source.isExplosion())
          this.world.createExplosion(null, this.posX, this.posY, this.posZ, 6.0F, true); 
        onCrystalDestroyed(source);
      } 
    } 
    return true;
  }
  
  public void onKillCommand() {
    onCrystalDestroyed(DamageSource.generic);
    super.onKillCommand();
  }
  
  private void onCrystalDestroyed(DamageSource source) {
    if (this.world.provider instanceof WorldProviderEnd) {
      WorldProviderEnd worldproviderend = (WorldProviderEnd)this.world.provider;
      DragonFightManager dragonfightmanager = worldproviderend.getDragonFightManager();
      if (dragonfightmanager != null)
        dragonfightmanager.onCrystalDestroyed(this, source); 
    } 
  }
  
  public void setBeamTarget(@Nullable BlockPos beamTarget) {
    getDataManager().set(BEAM_TARGET, Optional.fromNullable(beamTarget));
  }
  
  @Nullable
  public BlockPos getBeamTarget() {
    return (BlockPos)((Optional)getDataManager().get(BEAM_TARGET)).orNull();
  }
  
  public void setShowBottom(boolean showBottom) {
    getDataManager().set(SHOW_BOTTOM, Boolean.valueOf(showBottom));
  }
  
  public boolean shouldShowBottom() {
    return ((Boolean)getDataManager().get(SHOW_BOTTOM)).booleanValue();
  }
  
  public boolean isInRangeToRenderDist(double distance) {
    return !(!super.isInRangeToRenderDist(distance) && getBeamTarget() == null);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\item\EntityEnderCrystal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */