package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RenderPotion extends RenderSnowball<EntityPotion> {
  public RenderPotion(RenderManager renderManagerIn, RenderItem itemRendererIn) {
    super(renderManagerIn, (Item)Items.POTIONITEM, itemRendererIn);
  }
  
  public ItemStack getStackToRender(EntityPotion entityIn) {
    return entityIn.getPotion();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderPotion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */