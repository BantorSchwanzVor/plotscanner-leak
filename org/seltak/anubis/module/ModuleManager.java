package org.seltak.anubis.module;

import java.util.ArrayList;
import org.seltak.anubis.Anubis;
import org.seltak.anubis.modules.combat.AntiBot;
import org.seltak.anubis.modules.combat.KillAura;
import org.seltak.anubis.modules.combat.Velocity;
import org.seltak.anubis.modules.gui.ArrayListModule;
import org.seltak.anubis.modules.gui.ClickGUI;
import org.seltak.anubis.modules.gui.ShowClosestPlayer;
import org.seltak.anubis.modules.movement.AutoJump;
import org.seltak.anubis.modules.movement.AutoSneak;
import org.seltak.anubis.modules.movement.AutoSprint;
import org.seltak.anubis.modules.movement.AutoWalk;
import org.seltak.anubis.modules.movement.Fastbridge;
import org.seltak.anubis.modules.movement.Fly;
import org.seltak.anubis.modules.movement.Jesus;
import org.seltak.anubis.modules.movement.SlowWalk;
import org.seltak.anubis.modules.movement.Speed;
import org.seltak.anubis.modules.player.AutoArmor;
import org.seltak.anubis.modules.player.AutoMine;
import org.seltak.anubis.modules.player.AutoRejoin;
import org.seltak.anubis.modules.player.AutoRespawn;
import org.seltak.anubis.modules.player.BCrasher;
import org.seltak.anubis.modules.render.BlockOutline;
import org.seltak.anubis.modules.render.ChestESP;
import org.seltak.anubis.modules.render.FakeName;
import org.seltak.anubis.modules.render.Fullbright;
import org.seltak.anubis.modules.render.GlowESP;
import org.seltak.anubis.modules.render.PlayerESP;
import org.seltak.anubis.modules.world.AutoPlace;
import org.seltak.anubis.modules.world.ChestStealer;
import org.seltak.anubis.modules.world.FastPlace;
import org.seltak.anubis.modules.world.ScaffoldWalk;

public class ModuleManager {
  public ArrayList<Module> moduleList = new ArrayList<>();
  
  public void init() {
    this.moduleList.add(new AutoSprint());
    this.moduleList.add(new AutoSneak());
    this.moduleList.add(new AutoJump());
    this.moduleList.add(new AutoWalk());
    this.moduleList.add(new AutoRespawn());
    this.moduleList.add(new AutoMine());
    this.moduleList.add(new ClickGUI());
    this.moduleList.add(new ArrayListModule());
    this.moduleList.add(new Fullbright());
    this.moduleList.add(new ChestStealer());
    this.moduleList.add(new AutoPlace());
    this.moduleList.add(new FastPlace());
    this.moduleList.add(new ShowClosestPlayer());
    this.moduleList.add(new FakeName());
    this.moduleList.add(new Velocity());
    this.moduleList.add(new KillAura());
    this.moduleList.add(new AntiBot());
    this.moduleList.add(new Speed());
    this.moduleList.add(new Fly());
    this.moduleList.add(new ScaffoldWalk());
    this.moduleList.add(new Jesus());
    this.moduleList.add(new PlayerESP());
    this.moduleList.add(new GlowESP());
    this.moduleList.add(new Fastbridge());
    this.moduleList.add(new AutoArmor());
    this.moduleList.add(new ChestESP());
    this.moduleList.add(new BlockOutline());
    this.moduleList.add(new SlowWalk());
    this.moduleList.add(new BCrasher());
    this.moduleList.add(new AutoRejoin());
  }
  
  public ArrayList<Module> getEnabledModules() {
    ArrayList<Module> enabledModules = new ArrayList<>();
    for (Module module : this.moduleList) {
      if (module.isEnabled())
        enabledModules.add(module); 
    } 
    return enabledModules;
  }
  
  public ArrayList<Module> getModules() {
    return this.moduleList;
  }
  
  public boolean getModuleByName(String name) {
    for (Module module : Anubis.moduleManager.getEnabledModules()) {
      if (module.getName().equalsIgnoreCase(name))
        return true; 
    } 
    return false;
  }
  
  public Module getModuleByNameMod(String name) {
    for (Module module : Anubis.moduleManager.getModules()) {
      if (module.getName().equalsIgnoreCase(name))
        return module; 
    } 
    return null;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\module\ModuleManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */