package org.seltak.anubis;

import de.Hero.clickgui.ClickGUI;
import de.Hero.settings.Setting;
import de.Hero.settings.SettingsManager;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.seltak.anubis.alt.ui.AltManager;
import org.seltak.anubis.commandmanager.Command;
import org.seltak.anubis.commandmanager.CommandManager;
import org.seltak.anubis.module.Module;
import org.seltak.anubis.module.ModuleManager;
import org.seltak.anubis.ui.UIRenderer;

public class Anubis {
  public static final String CLIENT_NAME = "Anubis";
  
  public static final String CLIENT_VERSION = "0.1";
  
  static final File f = new File(
      (Minecraft.getMinecraft()).mcDataDir + "/Anubis/SaveSettings.txt");
  
  public static UIRenderer renderer;
  
  public static ModuleManager moduleManager;
  
  public static SettingsManager setmgr;
  
  public static ClickGUI clickgui;
  
  public static AltManager altManager;
  
  public static CommandManager cmdManager;
  
  public static int xGUI = 1;
  
  public static int yGUI = 1;
  
  public static ArrayList<String> friends = new ArrayList<>();
  
  public static void init() {
    cmdManager = new CommandManager();
    setmgr = new SettingsManager();
    renderer = new UIRenderer();
    moduleManager = new ModuleManager();
    cmdManager.init();
    moduleManager.init();
    altManager = new AltManager();
    clickgui = new ClickGUI();
    loadFile(f);
    (new Thread(new Runnable() {
          public void run() {
            String json = "";
            json = String.valueOf(json) + "[";
            System.out.println("JSONWRITER > Writing json...");
            for (IBlockState ibs : Block.BLOCK_STATE_IDS) {
              String name = ((ResourceLocation)Block.REGISTRY.getNameForObject(ibs.getBlock())).toString();
              int networkid = Block.BLOCK_STATE_IDS.get(ibs);
              int id = Block.REGISTRY.getIDForObject(ibs.getBlock());
              int subid = ibs.getBlock().getMetaFromState(ibs);
              Collection<IProperty<?>> props = ibs.getPropertyNames();
              json = String.valueOf(json) + "{\n";
              json = String.valueOf(json) + "\"Name\": \"" + name + "\",\n";
              json = String.valueOf(json) + "\"Id\": " + id + ",\n";
              json = String.valueOf(json) + "\"SubId\": " + subid + ",\n";
              json = String.valueOf(json) + "\"NetworkId\": " + networkid + ",\n";
              json = String.valueOf(json) + "\"States\": {\n";
              int current = 0;
              for (IProperty<?> prop : props) {
                String asd;
                if (current == props.size() - 1) {
                  asd = "";
                } else {
                  asd = ",";
                } 
                Object val = ibs.getValue(prop);
                if (val instanceof Enum) {
                  json = String.valueOf(json) + "\"" + prop.getName() + "\": \"" + val + "\"" + asd + "\n";
                } else {
                  json = String.valueOf(json) + "\"" + prop.getName() + "\": " + val + asd + "\n";
                } 
                current++;
              } 
              json = String.valueOf(json) + "}\n";
              json = String.valueOf(json) + "},\n";
            } 
            json = String.valueOf(json) + "]";
            try {
              File dir = new File((Minecraft.getMinecraft()).mcDataDir + "/Anubis/");
              dir.mkdir();
              File file = new File((Minecraft.getMinecraft()).mcDataDir + "/Anubis/JSON.json");
              FileOutputStream fos = new FileOutputStream(file);
              BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
              writer.write(json);
              System.out.println("JSONWRITER > Done.");
              writer.close();
            } catch (IOException e) {
              e.printStackTrace();
            } 
          }
        })).start();
  }
  
  public static void onGui() {
    renderer.draw();
    for (Module module : moduleManager.getEnabledModules())
      module.onGui(); 
  }
  
  public static void onRender() {
    for (Module module : moduleManager.getEnabledModules())
      module.onRender(); 
  }
  
  public static void onPreUpdate() {
    for (Module module : moduleManager.getEnabledModules())
      module.onPreUpdate(); 
  }
  
  public static void onTick() {
    for (Module module : moduleManager.getEnabledModules())
      module.onTick(); 
  }
  
  public static void onPostUpdate() {
    for (Module module : moduleManager.getEnabledModules())
      module.onPostUpdate(); 
  }
  
  public static void onKeyPressed(int keyCode) {
    for (Module module : moduleManager.moduleList)
      module.onKeyPressed(keyCode); 
  }
  
  public static void onCommand(String[] args) {
    for (Command cmd : cmdManager.commandList) {
      if (("." + cmd.getName()).equalsIgnoreCase(args[0])) {
        cmd.onCommand(args);
        return;
      } 
    } 
    sendMessage("This command could not be found!");
  }
  
  public static void sendMessage(String message) {
    (Minecraft.getMinecraft()).player.sendMessage(
        ITextComponent.Serializer.jsonToComponent("{\"text\":\"§6[§ePreview§6] §a" + message + "\"}"));
  }
  
  public static void saveFile() {
    try {
      saveKeybinds();
      File dir = new File((Minecraft.getMinecraft()).mcDataDir + "/Anubis/");
      dir.mkdir();
      File file = new File((Minecraft.getMinecraft()).mcDataDir + "/Anubis/SaveSettings.txt");
      FileOutputStream fos = new FileOutputStream(file);
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
      for (Setting set : setmgr.getSettings()) {
        writer.write(String.valueOf(String.valueOf(set.getName())) + ":" + set.getValBoolean() + ":" + set.getValDouble() + ":" + 
            set.getValString());
        writer.newLine();
      } 
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
  
  private static void saveKeybinds() {
    try {
      File dir = new File((Minecraft.getMinecraft()).mcDataDir + "/Anubis/");
      dir.mkdir();
      File file = new File((Minecraft.getMinecraft()).mcDataDir + "/Anubis/Keybinds.txt");
      FileOutputStream fos = new FileOutputStream(file);
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
      for (Module module : moduleManager.moduleList) {
        writer.write(String.valueOf(String.valueOf(module.getName())) + ":" + module.getKeybind());
        writer.newLine();
      } 
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
  
  public static void loadFile(File f) {
    try {
      loadKeybinds();
      BufferedReader reader = new BufferedReader(new FileReader(f));
      String line;
      while ((line = reader.readLine()) != null) {
        String[] arguments = line.split(":");
        if (arguments.length == 4) {
          Setting set = setmgr.getSettingByName(arguments[0]);
          if (set == null)
            continue; 
          set.setValBoolean(Boolean.parseBoolean(arguments[1]));
          set.setValDouble(Double.parseDouble(arguments[2]));
          set.setValString(arguments[3]);
        } 
      } 
      reader.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e2) {
      e2.printStackTrace();
    } 
  }
  
  public static void loadKeybinds() {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(
            (Minecraft.getMinecraft()).mcDataDir + "/Anubis/Keybinds.txt"));
      String line;
      while ((line = reader.readLine()) != null) {
        String[] arguments = line.split(":");
        if (arguments.length == 2 && 
          moduleManager.getModuleByNameMod(arguments[0]) != null)
          moduleManager.getModuleByNameMod(arguments[0]).setKeyCodeOnLoad(Integer.parseInt(arguments[1])); 
      } 
      reader.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e2) {
      e2.printStackTrace();
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\Anubis.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */