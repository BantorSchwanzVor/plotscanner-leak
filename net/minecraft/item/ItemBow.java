package net.minecraft.item;

import javax.annotation.Nullable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemBow extends Item {
  public ItemBow() {
    this.maxStackSize = 1;
    setMaxDamage(384);
    setCreativeTab(CreativeTabs.COMBAT);
    addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter() {
          public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
            if (entityIn == null)
              return 0.0F; 
            return (entityIn.getActiveItemStack().getItem() != Items.BOW) ? 0.0F : ((stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0F);
          }
        });
    addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter() {
          public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
            return (entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack) ? 1.0F : 0.0F;
          }
        });
  }
  
  private ItemStack findAmmo(EntityPlayer player) {
    if (isArrow(player.getHeldItem(EnumHand.OFF_HAND)))
      return player.getHeldItem(EnumHand.OFF_HAND); 
    if (isArrow(player.getHeldItem(EnumHand.MAIN_HAND)))
      return player.getHeldItem(EnumHand.MAIN_HAND); 
    for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
      ItemStack itemstack = player.inventory.getStackInSlot(i);
      if (isArrow(itemstack))
        return itemstack; 
    } 
    return ItemStack.field_190927_a;
  }
  
  protected boolean isArrow(ItemStack stack) {
    return stack.getItem() instanceof ItemArrow;
  }
  
  public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
    if (entityLiving instanceof EntityPlayer) {
      EntityPlayer entityplayer = (EntityPlayer)entityLiving;
      boolean flag = !(!entityplayer.capabilities.isCreativeMode && EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) <= 0);
      ItemStack itemstack = findAmmo(entityplayer);
      if (!itemstack.func_190926_b() || flag) {
        if (itemstack.func_190926_b())
          itemstack = new ItemStack(Items.ARROW); 
        int i = getMaxItemUseDuration(stack) - timeLeft;
        float f = getArrowVelocity(i);
        if (f >= 0.1D) {
          boolean flag1 = (flag && itemstack.getItem() == Items.ARROW);
          if (!worldIn.isRemote) {
            ItemArrow itemarrow = (itemstack.getItem() instanceof ItemArrow) ? (ItemArrow)itemstack.getItem() : (ItemArrow)Items.ARROW;
            EntityArrow entityarrow = itemarrow.createArrow(worldIn, itemstack, (EntityLivingBase)entityplayer);
            entityarrow.setAim((Entity)entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, f * 3.0F, 1.0F);
            if (f == 1.0F)
              entityarrow.setIsCritical(true); 
            int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
            if (j > 0)
              entityarrow.setDamage(entityarrow.getDamage() + j * 0.5D + 0.5D); 
            int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
            if (k > 0)
              entityarrow.setKnockbackStrength(k); 
            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0)
              entityarrow.setFire(100); 
            stack.damageItem(1, (EntityLivingBase)entityplayer);
            if (flag1 || (entityplayer.capabilities.isCreativeMode && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)))
              entityarrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY; 
            worldIn.spawnEntityInWorld((Entity)entityarrow);
          } 
          worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
          if (!flag1 && !entityplayer.capabilities.isCreativeMode) {
            itemstack.func_190918_g(1);
            if (itemstack.func_190926_b())
              entityplayer.inventory.deleteStack(itemstack); 
          } 
          entityplayer.addStat(StatList.getObjectUseStats(this));
        } 
      } 
    } 
  }
  
  public static float getArrowVelocity(int charge) {
    float f = charge / 20.0F;
    f = (f * f + f * 2.0F) / 3.0F;
    if (f > 1.0F)
      f = 1.0F; 
    return f;
  }
  
  public int getMaxItemUseDuration(ItemStack stack) {
    return 72000;
  }
  
  public EnumAction getItemUseAction(ItemStack stack) {
    return EnumAction.BOW;
  }
  
  public ActionResult<ItemStack> onItemRightClick(World itemStackIn, EntityPlayer worldIn, EnumHand playerIn) {
    ItemStack itemstack = worldIn.getHeldItem(playerIn);
    boolean flag = !findAmmo(worldIn).func_190926_b();
    if (!worldIn.capabilities.isCreativeMode && !flag)
      return flag ? new ActionResult(EnumActionResult.PASS, itemstack) : new ActionResult(EnumActionResult.FAIL, itemstack); 
    worldIn.setActiveHand(playerIn);
    return new ActionResult(EnumActionResult.SUCCESS, itemstack);
  }
  
  public int getItemEnchantability() {
    return 1;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemBow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */