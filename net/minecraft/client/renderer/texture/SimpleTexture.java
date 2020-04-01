package net.minecraft.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shadersmod.client.ShadersTex;

public class SimpleTexture extends AbstractTexture {
  private static final Logger LOG = LogManager.getLogger();
  
  protected final ResourceLocation textureLocation;
  
  public SimpleTexture(ResourceLocation textureResourceLocation) {
    this.textureLocation = textureResourceLocation;
  }
  
  public void loadTexture(IResourceManager resourceManager) throws IOException {
    deleteGlTexture();
    IResource iresource = null;
    try {
      iresource = resourceManager.getResource(this.textureLocation);
      BufferedImage bufferedimage = TextureUtil.readBufferedImage(iresource.getInputStream());
      boolean flag = false;
      boolean flag1 = false;
      if (iresource.hasMetadata())
        try {
          TextureMetadataSection texturemetadatasection = (TextureMetadataSection)iresource.getMetadata("texture");
          if (texturemetadatasection != null) {
            flag = texturemetadatasection.getTextureBlur();
            flag1 = texturemetadatasection.getTextureClamp();
          } 
        } catch (RuntimeException runtimeexception1) {
          LOG.warn("Failed reading metadata of: {}", this.textureLocation, runtimeexception1);
        }  
      if (Config.isShaders()) {
        ShadersTex.loadSimpleTexture(getGlTextureId(), bufferedimage, flag, flag1, resourceManager, this.textureLocation, getMultiTexID());
      } else {
        TextureUtil.uploadTextureImageAllocate(getGlTextureId(), bufferedimage, flag, flag1);
      } 
    } finally {
      IOUtils.closeQuietly((Closeable)iresource);
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\texture\SimpleTexture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */