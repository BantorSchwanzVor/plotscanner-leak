package net.minecraft.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.IntBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.ClickEvent;
import optifine.Config;
import optifine.Reflector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;

public class ScreenShotHelper {
  private static final Logger LOGGER = LogManager.getLogger();
  
  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
  
  private static IntBuffer pixelBuffer;
  
  private static int[] pixelValues;
  
  public static ITextComponent saveScreenshot(File gameDirectory, int width, int height, Framebuffer buffer) {
    return saveScreenshot(gameDirectory, null, width, height, buffer);
  }
  
  public static ITextComponent saveScreenshot(File gameDirectory, @Nullable String screenshotName, int width, int height, Framebuffer buffer) {
    try {
      File file1 = new File(gameDirectory, "screenshots");
      file1.mkdir();
      Minecraft minecraft = Minecraft.getMinecraft();
      int i = (Config.getGameSettings()).guiScale;
      ScaledResolution scaledresolution = new ScaledResolution(minecraft);
      int j = scaledresolution.getScaleFactor();
      int k = Config.getScreenshotSize();
      boolean flag = (OpenGlHelper.isFramebufferEnabled() && k > 1);
      if (flag) {
        (Config.getGameSettings()).guiScale = j * k;
        resize(width * k, height * k);
        GlStateManager.pushMatrix();
        GlStateManager.clear(16640);
        minecraft.getFramebuffer().bindFramebuffer(true);
        minecraft.entityRenderer.updateCameraAndRender(minecraft.getRenderPartialTicks(), System.nanoTime());
      } 
      BufferedImage bufferedimage = createScreenshot(width, height, buffer);
      if (flag) {
        minecraft.getFramebuffer().unbindFramebuffer();
        GlStateManager.popMatrix();
        (Config.getGameSettings()).guiScale = i;
        resize(width, height);
      } 
      if (screenshotName == null) {
        file2 = getTimestampedPNGFileForDirectory(file1);
      } else {
        file2 = new File(file1, screenshotName);
      } 
      File file2 = file2.getCanonicalFile();
      Object object = null;
      if (Reflector.ForgeHooksClient_onScreenshot.exists()) {
        object = Reflector.call(Reflector.ForgeHooksClient_onScreenshot, new Object[] { bufferedimage, file2 });
        if (Reflector.callBoolean(object, Reflector.Event_isCanceled, new Object[0]))
          return (ITextComponent)Reflector.call(object, Reflector.ScreenshotEvent_getCancelMessage, new Object[0]); 
        file2 = (File)Reflector.call(object, Reflector.ScreenshotEvent_getScreenshotFile, new Object[0]);
      } 
      ImageIO.write(bufferedimage, "png", file2);
      TextComponentString textComponentString = new TextComponentString(file2.getName());
      textComponentString.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file2.getAbsolutePath()));
      textComponentString.getStyle().setUnderlined(Boolean.valueOf(true));
      if (object != null) {
        ITextComponent itextcomponent1 = (ITextComponent)Reflector.call(object, Reflector.ScreenshotEvent_getResultMessage, new Object[0]);
        if (itextcomponent1 != null)
          return itextcomponent1; 
      } 
      return (ITextComponent)new TextComponentTranslation("screenshot.success", new Object[] { textComponentString });
    } catch (Exception exception1) {
      LOGGER.warn("Couldn't save screenshot", exception1);
      return (ITextComponent)new TextComponentTranslation("screenshot.failure", new Object[] { exception1.getMessage() });
    } 
  }
  
  public static BufferedImage createScreenshot(int width, int height, Framebuffer framebufferIn) {
    if (OpenGlHelper.isFramebufferEnabled()) {
      width = framebufferIn.framebufferTextureWidth;
      height = framebufferIn.framebufferTextureHeight;
    } 
    int i = width * height;
    if (pixelBuffer == null || pixelBuffer.capacity() < i) {
      pixelBuffer = BufferUtils.createIntBuffer(i);
      pixelValues = new int[i];
    } 
    GlStateManager.glPixelStorei(3333, 1);
    GlStateManager.glPixelStorei(3317, 1);
    pixelBuffer.clear();
    if (OpenGlHelper.isFramebufferEnabled()) {
      GlStateManager.bindTexture(framebufferIn.framebufferTexture);
      GlStateManager.glGetTexImage(3553, 0, 32993, 33639, pixelBuffer);
    } else {
      GlStateManager.glReadPixels(0, 0, width, height, 32993, 33639, pixelBuffer);
    } 
    pixelBuffer.get(pixelValues);
    TextureUtil.processPixelValues(pixelValues, width, height);
    BufferedImage bufferedimage = new BufferedImage(width, height, 1);
    bufferedimage.setRGB(0, 0, width, height, pixelValues, 0, width);
    return bufferedimage;
  }
  
  private static File getTimestampedPNGFileForDirectory(File gameDirectory) {
    String s = DATE_FORMAT.format(new Date()).toString();
    int i = 1;
    while (true) {
      File file1 = new File(gameDirectory, String.valueOf(s) + ((i == 1) ? "" : ("_" + i)) + ".png");
      if (!file1.exists())
        return file1; 
      i++;
    } 
  }
  
  private static void resize(int p_resize_0_, int p_resize_1_) {
    Minecraft minecraft = Minecraft.getMinecraft();
    minecraft.displayWidth = Math.max(1, p_resize_0_);
    minecraft.displayHeight = Math.max(1, p_resize_1_);
    if (minecraft.currentScreen != null) {
      ScaledResolution scaledresolution = new ScaledResolution(minecraft);
      minecraft.currentScreen.onResize(minecraft, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight());
    } 
    updateFramebufferSize();
  }
  
  private static void updateFramebufferSize() {
    Minecraft minecraft = Minecraft.getMinecraft();
    minecraft.getFramebuffer().createBindFramebuffer(minecraft.displayWidth, minecraft.displayHeight);
    if (minecraft.entityRenderer != null)
      minecraft.entityRenderer.updateShaderGroupSize(minecraft.displayWidth, minecraft.displayHeight); 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\ScreenShotHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */