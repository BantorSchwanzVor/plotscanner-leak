package net.minecraft.client.resources;

import com.google.gson.JsonParseException;
import java.io.IOException;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResourcePackListEntryServer extends ResourcePackListEntry {
  private static final Logger LOGGER = LogManager.getLogger();
  
  private final IResourcePack resourcePack;
  
  private final ResourceLocation resourcePackIcon;
  
  public ResourcePackListEntryServer(GuiScreenResourcePacks p_i46594_1_, IResourcePack p_i46594_2_) {
    super(p_i46594_1_);
    DynamicTexture dynamictexture;
    this.resourcePack = p_i46594_2_;
    try {
      dynamictexture = new DynamicTexture(p_i46594_2_.getPackImage());
    } catch (IOException var5) {
      dynamictexture = TextureUtil.MISSING_TEXTURE;
    } 
    this.resourcePackIcon = this.mc.getTextureManager().getDynamicTextureLocation("texturepackicon", dynamictexture);
  }
  
  protected int getResourcePackFormat() {
    return 3;
  }
  
  protected String getResourcePackDescription() {
    try {
      PackMetadataSection packmetadatasection = this.resourcePack.<PackMetadataSection>getPackMetadata((this.mc.getResourcePackRepository()).rprMetadataSerializer, "pack");
      if (packmetadatasection != null)
        return packmetadatasection.getPackDescription().getFormattedText(); 
    } catch (JsonParseException jsonparseexception) {
      LOGGER.error("Couldn't load metadata info", (Throwable)jsonparseexception);
    } catch (IOException ioexception) {
      LOGGER.error("Couldn't load metadata info", ioexception);
    } 
    return TextFormatting.RED + "Missing " + "pack.mcmeta" + " :(";
  }
  
  protected boolean canMoveRight() {
    return false;
  }
  
  protected boolean canMoveLeft() {
    return false;
  }
  
  protected boolean canMoveUp() {
    return false;
  }
  
  protected boolean canMoveDown() {
    return false;
  }
  
  protected String getResourcePackName() {
    return "Server";
  }
  
  protected void bindResourcePackIcon() {
    this.mc.getTextureManager().bindTexture(this.resourcePackIcon);
  }
  
  protected boolean showHoverOverlay() {
    return false;
  }
  
  public boolean isServerPack() {
    return true;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\resources\ResourcePackListEntryServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */