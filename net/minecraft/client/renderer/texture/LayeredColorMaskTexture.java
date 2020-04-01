package net.minecraft.client.renderer.texture;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LayeredColorMaskTexture extends AbstractTexture {
  private static final Logger LOG = LogManager.getLogger();
  
  private final ResourceLocation textureLocation;
  
  private final List<String> listTextures;
  
  private final List<EnumDyeColor> listDyeColors;
  
  public LayeredColorMaskTexture(ResourceLocation textureLocationIn, List<String> p_i46101_2_, List<EnumDyeColor> p_i46101_3_) {
    this.textureLocation = textureLocationIn;
    this.listTextures = p_i46101_2_;
    this.listDyeColors = p_i46101_3_;
  }
  
  public void loadTexture(IResourceManager resourceManager) throws IOException {
    BufferedImage bufferedimage;
    deleteGlTexture();
    IResource iresource = null;
    try {
      iresource = resourceManager.getResource(this.textureLocation);
      BufferedImage bufferedimage1 = TextureUtil.readBufferedImage(iresource.getInputStream());
      int i = bufferedimage1.getType();
      if (i == 0)
        i = 6; 
      bufferedimage = new BufferedImage(bufferedimage1.getWidth(), bufferedimage1.getHeight(), i);
      Graphics graphics = bufferedimage.getGraphics();
      graphics.drawImage(bufferedimage1, 0, 0, null);
      int j = 0;
      while (true) {
        if (j >= 17 || j >= this.listTextures.size() || j >= this.listDyeColors.size()) {
          IOUtils.closeQuietly((Closeable)iresource);
          break;
        } 
        IResource iresource1 = null;
        try {
          String s = this.listTextures.get(j);
          int k = ((EnumDyeColor)this.listDyeColors.get(j)).func_193350_e();
          if (s != null) {
            iresource1 = resourceManager.getResource(new ResourceLocation(s));
            BufferedImage bufferedimage2 = TextureUtil.readBufferedImage(iresource1.getInputStream());
            if (bufferedimage2.getWidth() == bufferedimage.getWidth() && bufferedimage2.getHeight() == bufferedimage.getHeight() && bufferedimage2.getType() == 6) {
              for (int l = 0; l < bufferedimage2.getHeight(); l++) {
                for (int i1 = 0; i1 < bufferedimage2.getWidth(); i1++) {
                  int j1 = bufferedimage2.getRGB(i1, l);
                  if ((j1 & 0xFF000000) != 0) {
                    int k1 = (j1 & 0xFF0000) << 8 & 0xFF000000;
                    int l1 = bufferedimage1.getRGB(i1, l);
                    int i2 = MathHelper.multiplyColor(l1, k) & 0xFFFFFF;
                    bufferedimage2.setRGB(i1, l, k1 | i2);
                  } 
                } 
              } 
              bufferedimage.getGraphics().drawImage(bufferedimage2, 0, 0, null);
            } 
          } 
        } finally {
          IOUtils.closeQuietly((Closeable)iresource1);
        } 
        j++;
      } 
    } catch (IOException ioexception) {
      LOG.error("Couldn't load layered image", ioexception);
    } finally {
      IOUtils.closeQuietly((Closeable)iresource);
    } 
    TextureUtil.uploadTextureImage(getGlTextureId(), bufferedimage);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\texture\LayeredColorMaskTexture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */