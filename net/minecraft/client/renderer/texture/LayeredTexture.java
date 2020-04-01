package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LayeredTexture extends AbstractTexture {
  private static final Logger LOGGER = LogManager.getLogger();
  
  public final List<String> layeredTextureNames;
  
  public LayeredTexture(String... textureNames) {
    this.layeredTextureNames = Lists.newArrayList((Object[])textureNames);
  }
  
  public void loadTexture(IResourceManager resourceManager) throws IOException {
    deleteGlTexture();
    BufferedImage bufferedimage = null;
    for (String s : this.layeredTextureNames) {
      IResource iresource = null;
      try {
        if (s != null) {
          iresource = resourceManager.getResource(new ResourceLocation(s));
          BufferedImage bufferedimage1 = TextureUtil.readBufferedImage(iresource.getInputStream());
          if (bufferedimage == null)
            bufferedimage = new BufferedImage(bufferedimage1.getWidth(), bufferedimage1.getHeight(), 2); 
          bufferedimage.getGraphics().drawImage(bufferedimage1, 0, 0, null);
        } 
        continue;
      } catch (IOException ioexception) {
        LOGGER.error("Couldn't load layered image", ioexception);
      } finally {
        IOUtils.closeQuietly((Closeable)iresource);
      } 
      return;
    } 
    TextureUtil.uploadTextureImage(getGlTextureId(), bufferedimage);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\texture\LayeredTexture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */