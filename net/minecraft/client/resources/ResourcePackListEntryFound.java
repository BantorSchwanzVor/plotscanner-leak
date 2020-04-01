package net.minecraft.client.resources;

import net.minecraft.client.gui.GuiScreenResourcePacks;

public class ResourcePackListEntryFound extends ResourcePackListEntry {
  private final ResourcePackRepository.Entry resourcePackEntry;
  
  public ResourcePackListEntryFound(GuiScreenResourcePacks resourcePacksGUIIn, ResourcePackRepository.Entry entry) {
    super(resourcePacksGUIIn);
    this.resourcePackEntry = entry;
  }
  
  protected void bindResourcePackIcon() {
    this.resourcePackEntry.bindTexturePackIcon(this.mc.getTextureManager());
  }
  
  protected int getResourcePackFormat() {
    return this.resourcePackEntry.getPackFormat();
  }
  
  protected String getResourcePackDescription() {
    return this.resourcePackEntry.getTexturePackDescription();
  }
  
  protected String getResourcePackName() {
    return this.resourcePackEntry.getResourcePackName();
  }
  
  public ResourcePackRepository.Entry getResourcePackEntry() {
    return this.resourcePackEntry;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\resources\ResourcePackListEntryFound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */