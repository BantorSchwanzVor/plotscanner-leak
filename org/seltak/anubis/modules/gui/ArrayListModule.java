package org.seltak.anubis.modules.gui;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import marraylist.Corner;
import marraylist.MArrayList;
import marraylist.MRenderer;
import marraylist.McRenderer;
import marraylist.Sorting;
import marraylist.Style;
import org.seltak.anubis.Anubis;
import org.seltak.anubis.module.Category;
import org.seltak.anubis.module.Module;

public class ArrayListModule extends Module {
  private MArrayList arrayList;
  
  public ArrayListModule() {
    super("HUD", Category.GUI);
  }
  
  public void init() {
    this.arrayList = new MArrayList(
        (MRenderer)new McRenderer(), 
        
        Corner.UPPER_RIGHT, 
        
        Sorting.WIDTH, 
        
        EnumSet.of(
          
          Style.RAINBOW_FLOW, 
          Style.LINES), 
        
        -16777216);
  }
  
  public void onRender() {
    init();
    List<String> modules = new ArrayList<>();
    for (Module module : Anubis.moduleManager.getEnabledModules()) {
      if (module.getCategory() != Category.GUI)
        modules.add(module.getName()); 
    } 
    this.arrayList.drawArrayList(modules);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\modules\gui\ArrayListModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */