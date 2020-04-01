package net.minecraft.item;

import javax.annotation.Nullable;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemElytra extends Item {
  public ItemElytra() {
    this.maxStackSize = 1;
    setMaxDamage(432);
    setCreativeTab(CreativeTabs.TRANSPORTATION);
    addPropertyOverride(new ResourceLocation("broken"), new IItemPropertyGetter() {
          public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
            return ItemElytra.isBroken(stack) ? 0.0F : 1.0F;
          }
        });
    BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR);
  }
  
  public static boolean isBroken(ItemStack stack) {
    return (stack.getItemDamage() < stack.getMaxDamage() - 1);
  }
  
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    return (repair.getItem() == Items.LEATHER);
  }
  
  public ActionResult<ItemStack> onItemRightClick(World itemStackIn, EntityPlayer worldIn, EnumHand playerIn) {
    ItemStack itemstack = worldIn.getHeldItem(playerIn);
    EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(itemstack);
    ItemStack itemstack1 = worldIn.getItemStackFromSlot(entityequipmentslot);
    if (itemstack1.func_190926_b()) {
      worldIn.setItemStackToSlot(entityequipmentslot, itemstack.copy());
      itemstack.func_190920_e(0);
      return new ActionResult(EnumActionResult.SUCCESS, itemstack);
    } 
    return new ActionResult(EnumActionResult.FAIL, itemstack);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemElytra.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */