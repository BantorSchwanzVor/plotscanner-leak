package org.seltak.anubis.module;

import net.minecraft.client.Minecraft;
import org.seltak.anubis.Anubis;

public class Module {
  private String name;
  
  private Category category;
  
  private String description;
  
  private int keyCode;
  
  private String[] modes;
  
  private boolean enabled;
  
  protected Minecraft mc = Minecraft.getMinecraft();
  
  public Module(String name, Category category) {
    this(name, category, "No description", 0, new String[] { "Default" });
  }
  
  public Module(String name, Category category, int keyCode) {
    this(name, category, "No description", keyCode, new String[] { "Default" });
  }
  
  public Module(String name, Category category, String description) {
    this(name, category, description, 0, new String[] { "Default" });
  }
  
  public Module(String name, Category category, String description, int keyCode) {
    this(name, category, description, keyCode, new String[] { "Default" });
  }
  
  public Module(String name, Category category, String description, int keyCode, String... modes) {
    this.name = name;
    this.category = category;
    this.description = description;
    this.keyCode = keyCode;
    this.modes = modes;
    setup();
  }
  
  public void onEnable() {}
  
  public void onDisable() {}
  
  public void setup() {}
  
  public boolean isEnabled() {
    return this.enabled;
  }
  
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
  
  public void onRender() {}
  
  public void onPreUpdate() {}
  
  public void onTick() {}
  
  public void onGui() {}
  
  public void onPostUpdate() {}
  
  public void onKeyPressed(int keyCode) {
    if (this.keyCode == keyCode)
      toggle(); 
  }
  
  public void toggle() {
    if (!isEnabled()) {
      onEnable();
    } else {
      onDisable();
    } 
    setEnabled(!isEnabled());
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public Category getCategory() {
    return this.category;
  }
  
  public int getKeybind() {
    return this.keyCode;
  }
  
  public void setKeyCode(int keyCode) {
    Anubis.saveFile();
    this.keyCode = keyCode;
  }
  
  public void setKeyCodeOnLoad(int keyCode) {
    this.keyCode = keyCode;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\module\Module.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */