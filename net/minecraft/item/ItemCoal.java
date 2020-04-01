package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.NonNullList;

public class ItemCoal extends Item {
  public ItemCoal() {
    setHasSubtypes(true);
    setMaxDamage(0);
    setCreativeTab(CreativeTabs.MATERIALS);
  }
  
  public String getUnlocalizedName(ItemStack stack) {
    return (stack.getMetadata() == 1) ? "item.charcoal" : "item.coal";
  }
  
  public void getSubItems(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
    if (func_194125_a(itemIn)) {
      tab.add(new ItemStack(this, 1, 0));
      tab.add(new ItemStack(this, 1, 1));
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemCoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */