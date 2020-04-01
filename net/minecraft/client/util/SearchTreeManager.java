package net.minecraft.client.util;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.item.ItemStack;

public class SearchTreeManager implements IResourceManagerReloadListener {
  public static final Key<ItemStack> field_194011_a = new Key<>();
  
  public static final Key<RecipeList> field_194012_b = new Key<>();
  
  private final Map<Key<?>, SearchTree<?>> field_194013_c = Maps.newHashMap();
  
  public void onResourceManagerReload(IResourceManager resourceManager) {
    for (SearchTree<?> searchtree : this.field_194013_c.values())
      searchtree.func_194040_a(); 
  }
  
  public <T> void func_194009_a(Key<T> p_194009_1_, SearchTree<T> p_194009_2_) {
    this.field_194013_c.put(p_194009_1_, p_194009_2_);
  }
  
  public <T> ISearchTree<T> func_194010_a(Key<T> p_194010_1_) {
    return (ISearchTree<T>)this.field_194013_c.get(p_194010_1_);
  }
  
  public static class Key<T> {}
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\clien\\util\SearchTreeManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */