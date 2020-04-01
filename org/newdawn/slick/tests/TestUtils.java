package org.newdawn.slick.tests;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.Log;

public class TestUtils {
  private Texture texture;
  
  private Audio oggEffect;
  
  private Audio wavEffect;
  
  private Audio aifEffect;
  
  private Audio oggStream;
  
  private Audio modStream;
  
  private Font font;
  
  public void start() {
    initGL(800, 600);
    init();
    while (true) {
      update();
      GL11.glClear(16384);
      render();
      Display.update();
      Display.sync(100);
      if (Display.isCloseRequested())
        System.exit(0); 
    } 
  }
  
  private void initGL(int width, int height) {
    try {
      Display.setDisplayMode(new DisplayMode(width, height));
      Display.create();
      Display.setVSyncEnabled(true);
    } catch (LWJGLException e) {
      e.printStackTrace();
      System.exit(0);
    } 
    GL11.glEnable(3553);
    GL11.glShadeModel(7425);
    GL11.glDisable(2929);
    GL11.glDisable(2896);
    GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
    GL11.glClearDepth(1.0D);
    GL11.glEnable(3042);
    GL11.glBlendFunc(770, 771);
    GL11.glViewport(0, 0, width, height);
    GL11.glMatrixMode(5888);
    GL11.glMatrixMode(5889);
    GL11.glLoadIdentity();
    GL11.glOrtho(0.0D, width, height, 0.0D, 1.0D, -1.0D);
    GL11.glMatrixMode(5888);
  }
  
  public void init() {
    Log.setVerbose(false);
    Font awtFont = new Font("Times New Roman", 1, 16);
    this.font = (Font)new TrueTypeFont(awtFont, false);
    try {
      this.texture = TextureLoader.getTexture("PNG", new FileInputStream("testdata/rocks.png"));
      System.out.println("Texture loaded: " + this.texture);
      System.out.println(">> Image width: " + this.texture.getImageWidth());
      System.out.println(">> Image height: " + this.texture.getImageWidth());
      System.out.println(">> Texture width: " + this.texture.getTextureWidth());
      System.out.println(">> Texture height: " + this.texture.getTextureHeight());
      System.out.println(">> Texture ID: " + this.texture.getTextureID());
    } catch (IOException e) {
      e.printStackTrace();
    } 
    try {
      this.oggEffect = AudioLoader.getAudio("OGG", new FileInputStream("testdata/restart.ogg"));
      this.oggStream = AudioLoader.getStreamingAudio("OGG", (new File("testdata/bongos.ogg")).toURL());
      this.modStream = AudioLoader.getStreamingAudio("MOD", (new File("testdata/SMB-X.XM")).toURL());
      this.modStream.playAsMusic(1.0F, 1.0F, true);
      this.aifEffect = AudioLoader.getAudio("AIF", new FileInputStream("testdata/burp.aif"));
      this.wavEffect = AudioLoader.getAudio("WAV", new FileInputStream("testdata/cbrown01.wav"));
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
  
  public void update() {
    while (Keyboard.next()) {
      if (Keyboard.getEventKeyState()) {
        if (Keyboard.getEventKey() == 16)
          this.oggEffect.playAsSoundEffect(1.0F, 1.0F, false); 
        if (Keyboard.getEventKey() == 17)
          this.oggStream.playAsMusic(1.0F, 1.0F, true); 
        if (Keyboard.getEventKey() == 18)
          this.modStream.playAsMusic(1.0F, 1.0F, true); 
        if (Keyboard.getEventKey() == 19)
          this.aifEffect.playAsSoundEffect(1.0F, 1.0F, false); 
        if (Keyboard.getEventKey() == 20)
          this.wavEffect.playAsSoundEffect(1.0F, 1.0F, false); 
      } 
    } 
    SoundStore.get().poll(0);
  }
  
  public void render() {
    Color.white.bind();
    this.texture.bind();
    GL11.glBegin(7);
    GL11.glTexCoord2f(0.0F, 0.0F);
    GL11.glVertex2f(100.0F, 100.0F);
    GL11.glTexCoord2f(1.0F, 0.0F);
    GL11.glVertex2f((100 + this.texture.getTextureWidth()), 100.0F);
    GL11.glTexCoord2f(1.0F, 1.0F);
    GL11.glVertex2f((100 + this.texture.getTextureWidth()), (100 + this.texture.getTextureHeight()));
    GL11.glTexCoord2f(0.0F, 1.0F);
    GL11.glVertex2f(100.0F, (100 + this.texture.getTextureHeight()));
    GL11.glEnd();
    this.font.drawString(150.0F, 300.0F, "HELLO LWJGL WORLD", Color.yellow);
  }
  
  public static void main(String[] argv) {
    TestUtils utils = new TestUtils();
    utils.start();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\tests\TestUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */