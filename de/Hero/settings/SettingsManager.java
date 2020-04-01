package de.Hero.settings;

import java.util.ArrayList;
import org.seltak.anubis.module.Module;

public class SettingsManager {
  private ArrayList<Setting> settings = new ArrayList<>();
  
  public void rSetting(Setting in) {
    this.settings.add(in);
  }
  
  public ArrayList<Setting> getSettings() {
    return this.settings;
  }
  
  public ArrayList<Setting> getSettingsByMod(Module mod) {
    ArrayList<Setting> out = new ArrayList<>();
    for (Setting s : getSettings()) {
      if (s.getParentMod().equals(mod))
        out.add(s); 
    } 
    if (out.isEmpty())
      return null; 
    return out;
  }
  
  public Setting getSettingByName(String name) {
    for (Setting set : getSettings()) {
      if (set.getName().equalsIgnoreCase(name))
        return set; 
    } 
    System.err.println("[Anubis] Error Setting NOT found: '" + name + "'!");
    return null;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\de\Hero\settings\SettingsManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */