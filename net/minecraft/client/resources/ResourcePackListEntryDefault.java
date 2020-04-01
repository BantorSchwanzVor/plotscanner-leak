package net.minecraft.client.resources;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenResourcePacks;

public class ResourcePackListEntryDefault extends ResourcePackListEntryServer {
  public ResourcePackListEntryDefault(GuiScreenResourcePacks resourcePacksGUIIn) {
    super(resourcePacksGUIIn, (Minecraft.getMinecraft().getResourcePackRepository()).rprDefaultResourcePack);
  }
  
  protected String getResourcePackName() {
    return "Default";
  }
  
  public boolean isServerPack() {
    return false;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\resources\ResourcePackListEntryDefault.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */