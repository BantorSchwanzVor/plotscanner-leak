package net.minecraft.item.crafting;

import com.google.common.base.Predicate;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.Comparator;
import javax.annotation.Nullable;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Ingredient implements Predicate<ItemStack> {
  public static final Ingredient field_193370_a = new Ingredient(new ItemStack[0]) {
      public boolean apply(@Nullable ItemStack p_apply_1_) {
        return p_apply_1_.func_190926_b();
      }
    };
  
  private final ItemStack[] field_193371_b;
  
  private IntList field_194140_c;
  
  private Ingredient(ItemStack... p_i47503_1_) {
    this.field_193371_b = p_i47503_1_;
  }
  
  public ItemStack[] func_193365_a() {
    return this.field_193371_b;
  }
  
  public boolean apply(@Nullable ItemStack p_apply_1_) {
    if (p_apply_1_ == null)
      return false; 
    byte b;
    int i;
    ItemStack[] arrayOfItemStack;
    for (i = (arrayOfItemStack = this.field_193371_b).length, b = 0; b < i; ) {
      ItemStack itemstack = arrayOfItemStack[b];
      if (itemstack.getItem() == p_apply_1_.getItem()) {
        int j = itemstack.getMetadata();
        if (j == 32767 || j == p_apply_1_.getMetadata())
          return true; 
      } 
      b++;
    } 
    return false;
  }
  
  public IntList func_194139_b() {
    if (this.field_194140_c == null) {
      this.field_194140_c = (IntList)new IntArrayList(this.field_193371_b.length);
      byte b;
      int i;
      ItemStack[] arrayOfItemStack;
      for (i = (arrayOfItemStack = this.field_193371_b).length, b = 0; b < i; ) {
        ItemStack itemstack = arrayOfItemStack[b];
        this.field_194140_c.add(RecipeItemHelper.func_194113_b(itemstack));
        b++;
      } 
      this.field_194140_c.sort((Comparator)IntComparators.NATURAL_COMPARATOR);
    } 
    return this.field_194140_c;
  }
  
  public static Ingredient func_193367_a(Item p_193367_0_) {
    return func_193369_a(new ItemStack[] { new ItemStack(p_193367_0_, 1, 32767) });
  }
  
  public static Ingredient func_193368_a(Item... p_193368_0_) {
    ItemStack[] aitemstack = new ItemStack[p_193368_0_.length];
    for (int i = 0; i < p_193368_0_.length; i++)
      aitemstack[i] = new ItemStack(p_193368_0_[i]); 
    return func_193369_a(aitemstack);
  }
  
  public static Ingredient func_193369_a(ItemStack... p_193369_0_) {
    if (p_193369_0_.length > 0) {
      byte b;
      int i;
      ItemStack[] arrayOfItemStack;
      for (i = (arrayOfItemStack = p_193369_0_).length, b = 0; b < i; ) {
        ItemStack itemstack = arrayOfItemStack[b];
        if (!itemstack.func_190926_b())
          return new Ingredient(p_193369_0_); 
        b++;
      } 
    } 
    return field_193370_a;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\crafting\Ingredient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */