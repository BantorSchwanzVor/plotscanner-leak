package net.minecraft.entity.projectile;

import com.google.common.base.Predicate;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityPotion extends EntityThrowable {
  private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(EntityPotion.class, DataSerializers.OPTIONAL_ITEM_STACK);
  
  private static final Logger LOGGER = LogManager.getLogger();
  
  public static final Predicate<EntityLivingBase> field_190546_d = new Predicate<EntityLivingBase>() {
      public boolean apply(@Nullable EntityLivingBase p_apply_1_) {
        return EntityPotion.func_190544_c(p_apply_1_);
      }
    };
  
  public EntityPotion(World worldIn) {
    super(worldIn);
  }
  
  public EntityPotion(World worldIn, EntityLivingBase throwerIn, ItemStack potionDamageIn) {
    super(worldIn, throwerIn);
    setItem(potionDamageIn);
  }
  
  public EntityPotion(World worldIn, double x, double y, double z, ItemStack potionDamageIn) {
    super(worldIn, x, y, z);
    if (!potionDamageIn.func_190926_b())
      setItem(potionDamageIn); 
  }
  
  protected void entityInit() {
    getDataManager().register(ITEM, ItemStack.field_190927_a);
  }
  
  public ItemStack getPotion() {
    ItemStack itemstack = (ItemStack)getDataManager().get(ITEM);
    if (itemstack.getItem() != Items.SPLASH_POTION && itemstack.getItem() != Items.LINGERING_POTION) {
      if (this.world != null)
        LOGGER.error("ThrownPotion entity {} has no item?!", Integer.valueOf(getEntityId())); 
      return new ItemStack((Item)Items.SPLASH_POTION);
    } 
    return itemstack;
  }
  
  public void setItem(ItemStack stack) {
    getDataManager().set(ITEM, stack);
    getDataManager().setDirty(ITEM);
  }
  
  protected float getGravityVelocity() {
    return 0.05F;
  }
  
  protected void onImpact(RayTraceResult result) {
    if (!this.world.isRemote) {
      ItemStack itemstack = getPotion();
      PotionType potiontype = PotionUtils.getPotionFromItem(itemstack);
      List<PotionEffect> list = PotionUtils.getEffectsFromStack(itemstack);
      boolean flag = (potiontype == PotionTypes.WATER && list.isEmpty());
      if (result.typeOfHit == RayTraceResult.Type.BLOCK && flag) {
        BlockPos blockpos = result.getBlockPos().offset(result.sideHit);
        extinguishFires(blockpos, result.sideHit);
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
          extinguishFires(blockpos.offset(enumfacing), enumfacing); 
      } 
      if (flag) {
        func_190545_n();
      } else if (!list.isEmpty()) {
        if (isLingering()) {
          func_190542_a(itemstack, potiontype);
        } else {
          func_190543_a(result, list);
        } 
      } 
      int i = potiontype.hasInstantEffect() ? 2007 : 2002;
      this.world.playEvent(i, new BlockPos(this), PotionUtils.func_190932_c(itemstack));
      setDead();
    } 
  }
  
  private void func_190545_n() {
    AxisAlignedBB axisalignedbb = getEntityBoundingBox().expand(4.0D, 2.0D, 4.0D);
    List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb, field_190546_d);
    if (!list.isEmpty())
      for (EntityLivingBase entitylivingbase : list) {
        double d0 = getDistanceSqToEntity((Entity)entitylivingbase);
        if (d0 < 16.0D && func_190544_c(entitylivingbase))
          entitylivingbase.attackEntityFrom(DamageSource.drown, 1.0F); 
      }  
  }
  
  private void func_190543_a(RayTraceResult p_190543_1_, List<PotionEffect> p_190543_2_) {
    AxisAlignedBB axisalignedbb = getEntityBoundingBox().expand(4.0D, 2.0D, 4.0D);
    List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
    if (!list.isEmpty())
      for (EntityLivingBase entitylivingbase : list) {
        if (entitylivingbase.canBeHitWithPotion()) {
          double d0 = getDistanceSqToEntity((Entity)entitylivingbase);
          if (d0 < 16.0D) {
            double d1 = 1.0D - Math.sqrt(d0) / 4.0D;
            if (entitylivingbase == p_190543_1_.entityHit)
              d1 = 1.0D; 
            for (PotionEffect potioneffect : p_190543_2_) {
              Potion potion = potioneffect.getPotion();
              if (potion.isInstant()) {
                potion.affectEntity(this, (Entity)getThrower(), entitylivingbase, potioneffect.getAmplifier(), d1);
                continue;
              } 
              int i = (int)(d1 * potioneffect.getDuration() + 0.5D);
              if (i > 20)
                entitylivingbase.addPotionEffect(new PotionEffect(potion, i, potioneffect.getAmplifier(), potioneffect.getIsAmbient(), potioneffect.doesShowParticles())); 
            } 
          } 
        } 
      }  
  }
  
  private void func_190542_a(ItemStack p_190542_1_, PotionType p_190542_2_) {
    EntityAreaEffectCloud entityareaeffectcloud = new EntityAreaEffectCloud(this.world, this.posX, this.posY, this.posZ);
    entityareaeffectcloud.setOwner(getThrower());
    entityareaeffectcloud.setRadius(3.0F);
    entityareaeffectcloud.setRadiusOnUse(-0.5F);
    entityareaeffectcloud.setWaitTime(10);
    entityareaeffectcloud.setRadiusPerTick(-entityareaeffectcloud.getRadius() / entityareaeffectcloud.getDuration());
    entityareaeffectcloud.setPotion(p_190542_2_);
    for (PotionEffect potioneffect : PotionUtils.getFullEffectsFromItem(p_190542_1_))
      entityareaeffectcloud.addEffect(new PotionEffect(potioneffect)); 
    NBTTagCompound nbttagcompound = p_190542_1_.getTagCompound();
    if (nbttagcompound != null && nbttagcompound.hasKey("CustomPotionColor", 99))
      entityareaeffectcloud.setColor(nbttagcompound.getInteger("CustomPotionColor")); 
    this.world.spawnEntityInWorld((Entity)entityareaeffectcloud);
  }
  
  private boolean isLingering() {
    return (getPotion().getItem() == Items.LINGERING_POTION);
  }
  
  private void extinguishFires(BlockPos pos, EnumFacing p_184542_2_) {
    if (this.world.getBlockState(pos).getBlock() == Blocks.FIRE)
      this.world.extinguishFire(null, pos.offset(p_184542_2_), p_184542_2_.getOpposite()); 
  }
  
  public static void registerFixesPotion(DataFixer fixer) {
    EntityThrowable.registerFixesThrowable(fixer, "ThrownPotion");
    fixer.registerWalker(FixTypes.ENTITY, (IDataWalker)new ItemStackData(EntityPotion.class, new String[] { "Potion" }));
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    ItemStack itemstack = new ItemStack(compound.getCompoundTag("Potion"));
    if (itemstack.func_190926_b()) {
      setDead();
    } else {
      setItem(itemstack);
    } 
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    ItemStack itemstack = getPotion();
    if (!itemstack.func_190926_b())
      compound.setTag("Potion", (NBTBase)itemstack.writeToNBT(new NBTTagCompound())); 
  }
  
  private static boolean func_190544_c(EntityLivingBase p_190544_0_) {
    return !(!(p_190544_0_ instanceof net.minecraft.entity.monster.EntityEnderman) && !(p_190544_0_ instanceof net.minecraft.entity.monster.EntityBlaze));
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\projectile\EntityPotion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */