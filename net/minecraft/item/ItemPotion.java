package net.minecraft.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemPotion extends Item {
  public ItemPotion() {
    setMaxStackSize(1);
    setCreativeTab(CreativeTabs.BREWING);
  }
  
  public ItemStack func_190903_i() {
    return PotionUtils.addPotionToItemStack(super.func_190903_i(), PotionTypes.WATER);
  }
  
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
    EntityPlayer entityplayer = (entityLiving instanceof EntityPlayer) ? (EntityPlayer)entityLiving : null;
    if (entityplayer == null || !entityplayer.capabilities.isCreativeMode)
      stack.func_190918_g(1); 
    if (entityplayer instanceof EntityPlayerMP)
      CriteriaTriggers.field_193138_y.func_193148_a((EntityPlayerMP)entityplayer, stack); 
    if (!worldIn.isRemote)
      for (PotionEffect potioneffect : PotionUtils.getEffectsFromStack(stack)) {
        if (potioneffect.getPotion().isInstant()) {
          potioneffect.getPotion().affectEntity((Entity)entityplayer, (Entity)entityplayer, entityLiving, potioneffect.getAmplifier(), 1.0D);
          continue;
        } 
        entityLiving.addPotionEffect(new PotionEffect(potioneffect));
      }  
    if (entityplayer != null)
      entityplayer.addStat(StatList.getObjectUseStats(this)); 
    if (entityplayer == null || !entityplayer.capabilities.isCreativeMode) {
      if (stack.func_190926_b())
        return new ItemStack(Items.GLASS_BOTTLE); 
      if (entityplayer != null)
        entityplayer.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE)); 
    } 
    return stack;
  }
  
  public int getMaxItemUseDuration(ItemStack stack) {
    return 32;
  }
  
  public EnumAction getItemUseAction(ItemStack stack) {
    return EnumAction.DRINK;
  }
  
  public ActionResult<ItemStack> onItemRightClick(World itemStackIn, EntityPlayer worldIn, EnumHand playerIn) {
    worldIn.setActiveHand(playerIn);
    return new ActionResult(EnumActionResult.SUCCESS, worldIn.getHeldItem(playerIn));
  }
  
  public String getItemStackDisplayName(ItemStack stack) {
    return I18n.translateToLocal(PotionUtils.getPotionFromItem(stack).getNamePrefixed("potion.effect."));
  }
  
  public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
    PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);
  }
  
  public boolean hasEffect(ItemStack stack) {
    return !(!super.hasEffect(stack) && PotionUtils.getEffectsFromStack(stack).isEmpty());
  }
  
  public void getSubItems(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
    if (func_194125_a(itemIn))
      for (PotionType potiontype : PotionType.REGISTRY) {
        if (potiontype != PotionTypes.EMPTY)
          tab.add(PotionUtils.addPotionToItemStack(new ItemStack(this), potiontype)); 
      }  
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemPotion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */