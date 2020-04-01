package net.minecraft.entity.item;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

public class EntityItemFrame extends EntityHanging {
  private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(EntityItemFrame.class, DataSerializers.OPTIONAL_ITEM_STACK);
  
  private static final DataParameter<Integer> ROTATION = EntityDataManager.createKey(EntityItemFrame.class, DataSerializers.VARINT);
  
  private float itemDropChance = 1.0F;
  
  public EntityItemFrame(World worldIn) {
    super(worldIn);
  }
  
  public EntityItemFrame(World worldIn, BlockPos p_i45852_2_, EnumFacing p_i45852_3_) {
    super(worldIn, p_i45852_2_);
    updateFacingWithBoundingBox(p_i45852_3_);
  }
  
  protected void entityInit() {
    getDataManager().register(ITEM, ItemStack.field_190927_a);
    getDataManager().register(ROTATION, Integer.valueOf(0));
  }
  
  public float getCollisionBorderSize() {
    return 0.0F;
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (isEntityInvulnerable(source))
      return false; 
    if (!source.isExplosion() && !getDisplayedItem().func_190926_b()) {
      if (!this.world.isRemote) {
        dropItemOrSelf(source.getEntity(), false);
        playSound(SoundEvents.ENTITY_ITEMFRAME_REMOVE_ITEM, 1.0F, 1.0F);
        setDisplayedItem(ItemStack.field_190927_a);
      } 
      return true;
    } 
    return super.attackEntityFrom(source, amount);
  }
  
  public int getWidthPixels() {
    return 12;
  }
  
  public int getHeightPixels() {
    return 12;
  }
  
  public boolean isInRangeToRenderDist(double distance) {
    double d0 = 16.0D;
    d0 = d0 * 64.0D * getRenderDistanceWeight();
    return (distance < d0 * d0);
  }
  
  public void onBroken(@Nullable Entity brokenEntity) {
    playSound(SoundEvents.ENTITY_ITEMFRAME_BREAK, 1.0F, 1.0F);
    dropItemOrSelf(brokenEntity, true);
  }
  
  public void playPlaceSound() {
    playSound(SoundEvents.ENTITY_ITEMFRAME_PLACE, 1.0F, 1.0F);
  }
  
  public void dropItemOrSelf(@Nullable Entity entityIn, boolean p_146065_2_) {
    if (this.world.getGameRules().getBoolean("doEntityDrops")) {
      ItemStack itemstack = getDisplayedItem();
      if (entityIn instanceof EntityPlayer) {
        EntityPlayer entityplayer = (EntityPlayer)entityIn;
        if (entityplayer.capabilities.isCreativeMode) {
          removeFrameFromMap(itemstack);
          return;
        } 
      } 
      if (p_146065_2_)
        entityDropItem(new ItemStack(Items.ITEM_FRAME), 0.0F); 
      if (!itemstack.func_190926_b() && this.rand.nextFloat() < this.itemDropChance) {
        itemstack = itemstack.copy();
        removeFrameFromMap(itemstack);
        entityDropItem(itemstack, 0.0F);
      } 
    } 
  }
  
  private void removeFrameFromMap(ItemStack stack) {
    if (!stack.func_190926_b()) {
      if (stack.getItem() == Items.FILLED_MAP) {
        MapData mapdata = ((ItemMap)stack.getItem()).getMapData(stack, this.world);
        mapdata.mapDecorations.remove("frame-" + getEntityId());
      } 
      stack.setItemFrame(null);
    } 
  }
  
  public ItemStack getDisplayedItem() {
    return (ItemStack)getDataManager().get(ITEM);
  }
  
  public void setDisplayedItem(ItemStack stack) {
    setDisplayedItemWithUpdate(stack, true);
  }
  
  private void setDisplayedItemWithUpdate(ItemStack stack, boolean p_174864_2_) {
    if (!stack.func_190926_b()) {
      stack = stack.copy();
      stack.func_190920_e(1);
      stack.setItemFrame(this);
    } 
    getDataManager().set(ITEM, stack);
    getDataManager().setDirty(ITEM);
    if (!stack.func_190926_b())
      playSound(SoundEvents.ENTITY_ITEMFRAME_ADD_ITEM, 1.0F, 1.0F); 
    if (p_174864_2_ && this.hangingPosition != null)
      this.world.updateComparatorOutputLevel(this.hangingPosition, Blocks.AIR); 
  }
  
  public void notifyDataManagerChange(DataParameter<?> key) {
    if (key.equals(ITEM)) {
      ItemStack itemstack = getDisplayedItem();
      if (!itemstack.func_190926_b() && itemstack.getItemFrame() != this)
        itemstack.setItemFrame(this); 
    } 
  }
  
  public int getRotation() {
    return ((Integer)getDataManager().get(ROTATION)).intValue();
  }
  
  public void setItemRotation(int rotationIn) {
    setRotation(rotationIn, true);
  }
  
  private void setRotation(int rotationIn, boolean p_174865_2_) {
    getDataManager().set(ROTATION, Integer.valueOf(rotationIn % 8));
    if (p_174865_2_ && this.hangingPosition != null)
      this.world.updateComparatorOutputLevel(this.hangingPosition, Blocks.AIR); 
  }
  
  public static void registerFixesItemFrame(DataFixer fixer) {
    fixer.registerWalker(FixTypes.ENTITY, (IDataWalker)new ItemStackData(EntityItemFrame.class, new String[] { "Item" }));
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    if (!getDisplayedItem().func_190926_b()) {
      compound.setTag("Item", (NBTBase)getDisplayedItem().writeToNBT(new NBTTagCompound()));
      compound.setByte("ItemRotation", (byte)getRotation());
      compound.setFloat("ItemDropChance", this.itemDropChance);
    } 
    super.writeEntityToNBT(compound);
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    NBTTagCompound nbttagcompound = compound.getCompoundTag("Item");
    if (nbttagcompound != null && !nbttagcompound.hasNoTags()) {
      setDisplayedItemWithUpdate(new ItemStack(nbttagcompound), false);
      setRotation(compound.getByte("ItemRotation"), false);
      if (compound.hasKey("ItemDropChance", 99))
        this.itemDropChance = compound.getFloat("ItemDropChance"); 
    } 
    super.readEntityFromNBT(compound);
  }
  
  public boolean processInitialInteract(EntityPlayer player, EnumHand stack) {
    ItemStack itemstack = player.getHeldItem(stack);
    if (!this.world.isRemote)
      if (getDisplayedItem().func_190926_b()) {
        if (!itemstack.func_190926_b()) {
          setDisplayedItem(itemstack);
          if (!player.capabilities.isCreativeMode)
            itemstack.func_190918_g(1); 
        } 
      } else {
        playSound(SoundEvents.ENTITY_ITEMFRAME_ROTATE_ITEM, 1.0F, 1.0F);
        setItemRotation(getRotation() + 1);
      }  
    return true;
  }
  
  public int getAnalogOutput() {
    return getDisplayedItem().func_190926_b() ? 0 : (getRotation() % 8 + 1);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\item\EntityItemFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */