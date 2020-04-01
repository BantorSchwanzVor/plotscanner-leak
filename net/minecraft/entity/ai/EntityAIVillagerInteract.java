package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class EntityAIVillagerInteract extends EntityAIWatchClosest2 {
  private int interactionDelay;
  
  private final EntityVillager villager;
  
  public EntityAIVillagerInteract(EntityVillager villagerIn) {
    super((EntityLiving)villagerIn, (Class)EntityVillager.class, 3.0F, 0.02F);
    this.villager = villagerIn;
  }
  
  public void startExecuting() {
    super.startExecuting();
    if (this.villager.canAbondonItems() && this.closestEntity instanceof EntityVillager && ((EntityVillager)this.closestEntity).wantsMoreFood()) {
      this.interactionDelay = 10;
    } else {
      this.interactionDelay = 0;
    } 
  }
  
  public void updateTask() {
    super.updateTask();
    if (this.interactionDelay > 0) {
      this.interactionDelay--;
      if (this.interactionDelay == 0) {
        InventoryBasic inventorybasic = this.villager.getVillagerInventory();
        for (int i = 0; i < inventorybasic.getSizeInventory(); i++) {
          ItemStack itemstack = inventorybasic.getStackInSlot(i);
          ItemStack itemstack1 = ItemStack.field_190927_a;
          if (!itemstack.func_190926_b()) {
            Item item = itemstack.getItem();
            if ((item == Items.BREAD || item == Items.POTATO || item == Items.CARROT || item == Items.BEETROOT) && itemstack.func_190916_E() > 3) {
              int l = itemstack.func_190916_E() / 2;
              itemstack.func_190918_g(l);
              itemstack1 = new ItemStack(item, l, itemstack.getMetadata());
            } else if (item == Items.WHEAT && itemstack.func_190916_E() > 5) {
              int j = itemstack.func_190916_E() / 2 / 3 * 3;
              int k = j / 3;
              itemstack.func_190918_g(j);
              itemstack1 = new ItemStack(Items.BREAD, k, 0);
            } 
            if (itemstack.func_190926_b())
              inventorybasic.setInventorySlotContents(i, ItemStack.field_190927_a); 
          } 
          if (!itemstack1.func_190926_b()) {
            double d0 = this.villager.posY - 0.30000001192092896D + this.villager.getEyeHeight();
            EntityItem entityitem = new EntityItem(this.villager.world, this.villager.posX, d0, this.villager.posZ, itemstack1);
            float f = 0.3F;
            float f1 = this.villager.rotationYawHead;
            float f2 = this.villager.rotationPitch;
            entityitem.motionX = (-MathHelper.sin(f1 * 0.017453292F) * MathHelper.cos(f2 * 0.017453292F) * 0.3F);
            entityitem.motionZ = (MathHelper.cos(f1 * 0.017453292F) * MathHelper.cos(f2 * 0.017453292F) * 0.3F);
            entityitem.motionY = (-MathHelper.sin(f2 * 0.017453292F) * 0.3F + 0.1F);
            entityitem.setDefaultPickupDelay();
            this.villager.world.spawnEntityInWorld((Entity)entityitem);
            break;
          } 
        } 
      } 
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\ai\EntityAIVillagerInteract.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */