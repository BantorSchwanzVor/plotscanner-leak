package net.minecraft.client.util;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.stats.RecipeBook;

public class RecipeBookClient extends RecipeBook {
  public static final Map<CreativeTabs, List<RecipeList>> field_194086_e = Maps.newHashMap();
  
  public static final List<RecipeList> field_194087_f = Lists.newArrayList();
  
  private static RecipeList func_194082_a(CreativeTabs p_194082_0_) {
    RecipeList recipelist = new RecipeList();
    field_194087_f.add(recipelist);
    ((List<RecipeList>)field_194086_e.computeIfAbsent(p_194082_0_, p_194085_0_ -> new ArrayList()))
      
      .add(recipelist);
    ((List<RecipeList>)field_194086_e.computeIfAbsent(CreativeTabs.SEARCH, p_194083_0_ -> new ArrayList()))
      
      .add(recipelist);
    return recipelist;
  }
  
  private static CreativeTabs func_194084_a(ItemStack p_194084_0_) {
    CreativeTabs creativetabs = p_194084_0_.getItem().getCreativeTab();
    if (creativetabs != CreativeTabs.BUILDING_BLOCKS && creativetabs != CreativeTabs.TOOLS && creativetabs != CreativeTabs.REDSTONE)
      return (creativetabs == CreativeTabs.COMBAT) ? CreativeTabs.TOOLS : CreativeTabs.MISC; 
    return creativetabs;
  }
  
  static {
    HashBasedTable hashBasedTable = HashBasedTable.create();
    for (IRecipe irecipe : CraftingManager.field_193380_a) {
      if (!irecipe.func_192399_d()) {
        RecipeList recipelist1;
        CreativeTabs creativetabs = func_194084_a(irecipe.getRecipeOutput());
        String s = irecipe.func_193358_e();
        if (s.isEmpty()) {
          recipelist1 = func_194082_a(creativetabs);
        } else {
          recipelist1 = (RecipeList)hashBasedTable.get(creativetabs, s);
          if (recipelist1 == null) {
            recipelist1 = func_194082_a(creativetabs);
            hashBasedTable.put(creativetabs, s, recipelist1);
          } 
        } 
        recipelist1.func_192709_a(irecipe);
      } 
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\clien\\util\RecipeBookClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */