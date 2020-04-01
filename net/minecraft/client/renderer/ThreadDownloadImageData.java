package net.minecraft.client.renderer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import optifine.CapeImageBuffer;
import optifine.Config;
import optifine.HttpPipeline;
import optifine.HttpRequest;
import optifine.HttpResponse;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadDownloadImageData extends SimpleTexture {
  private static final Logger LOGGER = LogManager.getLogger();
  
  private static final AtomicInteger TEXTURE_DOWNLOADER_THREAD_ID = new AtomicInteger(0);
  
  @Nullable
  private final File cacheFile;
  
  private final String imageUrl;
  
  @Nullable
  private final IImageBuffer imageBuffer;
  
  @Nullable
  private BufferedImage bufferedImage;
  
  @Nullable
  private Thread imageThread;
  
  private boolean textureUploaded;
  
  public Boolean imageFound = null;
  
  public boolean pipeline = false;
  
  public ThreadDownloadImageData(@Nullable File cacheFileIn, String imageUrlIn, ResourceLocation textureResourceLocation, @Nullable IImageBuffer imageBufferIn) {
    super(textureResourceLocation);
    this.cacheFile = cacheFileIn;
    this.imageUrl = imageUrlIn;
    this.imageBuffer = imageBufferIn;
  }
  
  private void checkTextureUploaded() {
    if (!this.textureUploaded && this.bufferedImage != null) {
      this.textureUploaded = true;
      if (this.textureLocation != null)
        deleteGlTexture(); 
      TextureUtil.uploadTextureImage(super.getGlTextureId(), this.bufferedImage);
    } 
  }
  
  public int getGlTextureId() {
    checkTextureUploaded();
    return super.getGlTextureId();
  }
  
  public void setBufferedImage(BufferedImage bufferedImageIn) {
    this.bufferedImage = bufferedImageIn;
    if (this.imageBuffer != null)
      this.imageBuffer.skinAvailable(); 
    this.imageFound = Boolean.valueOf((this.bufferedImage != null));
  }
  
  public void loadTexture(IResourceManager resourceManager) throws IOException {
    if (this.bufferedImage == null && this.textureLocation != null)
      super.loadTexture(resourceManager); 
    if (this.imageThread == null)
      if (this.cacheFile != null && this.cacheFile.isFile()) {
        LOGGER.debug("Loading http texture from local cache ({})", this.cacheFile);
        try {
          this.bufferedImage = ImageIO.read(this.cacheFile);
          if (this.imageBuffer != null)
            setBufferedImage(this.imageBuffer.parseUserSkin(this.bufferedImage)); 
          loadingFinished();
        } catch (IOException ioexception) {
          LOGGER.error("Couldn't load skin {}", this.cacheFile, ioexception);
          loadTextureFromServer();
        } 
      } else {
        loadTextureFromServer();
      }  
  }
  
  protected void loadTextureFromServer() {
    this.imageThread = new Thread("Texture Downloader #" + TEXTURE_DOWNLOADER_THREAD_ID.incrementAndGet()) {
        public void run() {
          HttpURLConnection httpurlconnection = null;
          ThreadDownloadImageData.LOGGER.debug("Downloading http texture from {} to {}", ThreadDownloadImageData.this.imageUrl, ThreadDownloadImageData.this.cacheFile);
          if (ThreadDownloadImageData.this.shouldPipeline()) {
            ThreadDownloadImageData.this.loadPipelined();
          } else {
            try {
              BufferedImage bufferedimage;
              httpurlconnection = (HttpURLConnection)(new URL(ThreadDownloadImageData.this.imageUrl)).openConnection(Minecraft.getMinecraft().getProxy());
              httpurlconnection.setDoInput(true);
              httpurlconnection.setDoOutput(false);
              httpurlconnection.connect();
              if (httpurlconnection.getResponseCode() / 100 != 2) {
                if (httpurlconnection.getErrorStream() != null)
                  Config.readAll(httpurlconnection.getErrorStream()); 
                return;
              } 
              if (ThreadDownloadImageData.this.cacheFile != null) {
                FileUtils.copyInputStreamToFile(httpurlconnection.getInputStream(), ThreadDownloadImageData.this.cacheFile);
                bufferedimage = ImageIO.read(ThreadDownloadImageData.this.cacheFile);
              } else {
                bufferedimage = TextureUtil.readBufferedImage(httpurlconnection.getInputStream());
              } 
            } catch (Exception exception1) {
              ThreadDownloadImageData.LOGGER.error("Couldn't download http texture: " + exception1.getMessage());
              return;
            } finally {
              if (httpurlconnection != null)
                httpurlconnection.disconnect(); 
              ThreadDownloadImageData.this.loadingFinished();
            } 
            if (httpurlconnection != null)
              httpurlconnection.disconnect(); 
            ThreadDownloadImageData.this.loadingFinished();
          } 
        }
      };
    this.imageThread.setDaemon(true);
    this.imageThread.start();
  }
  
  private boolean shouldPipeline() {
    if (!this.pipeline)
      return false; 
    Proxy proxy = Minecraft.getMinecraft().getProxy();
    if (proxy.type() != Proxy.Type.DIRECT && proxy.type() != Proxy.Type.SOCKS)
      return false; 
    return this.imageUrl.startsWith("http://");
  }
  
  private void loadPipelined() {
    try {
      BufferedImage bufferedimage;
      HttpRequest httprequest = HttpPipeline.makeRequest(this.imageUrl, Minecraft.getMinecraft().getProxy());
      HttpResponse httpresponse = HttpPipeline.executeRequest(httprequest);
      if (httpresponse.getStatus() / 100 != 2)
        return; 
      byte[] abyte = httpresponse.getBody();
      ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(abyte);
      if (this.cacheFile != null) {
        FileUtils.copyInputStreamToFile(bytearrayinputstream, this.cacheFile);
        bufferedimage = ImageIO.read(this.cacheFile);
      } else {
        bufferedimage = TextureUtil.readBufferedImage(bytearrayinputstream);
      } 
      if (this.imageBuffer != null)
        bufferedimage = this.imageBuffer.parseUserSkin(bufferedimage); 
      setBufferedImage(bufferedimage);
    } catch (Exception exception) {
      LOGGER.error("Couldn't download http texture: " + exception.getClass().getName() + ": " + exception.getMessage());
      return;
    } finally {
      loadingFinished();
    } 
  }
  
  private void loadingFinished() {
    this.imageFound = Boolean.valueOf((this.bufferedImage != null));
    if (this.imageBuffer instanceof CapeImageBuffer) {
      CapeImageBuffer capeimagebuffer = (CapeImageBuffer)this.imageBuffer;
      capeimagebuffer.cleanup();
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\ThreadDownloadImageData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */