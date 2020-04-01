package org.newdawn.slick.tests;

import java.io.IOException;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Font;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;

public class DeferredLoadingTest extends BasicGame {
  private Music music;
  
  private Sound sound;
  
  private Image image;
  
  private Font font;
  
  private DeferredResource nextResource;
  
  private boolean started;
  
  public DeferredLoadingTest() {
    super("Deferred Loading Test");
  }
  
  public void init(GameContainer container) throws SlickException {
    LoadingList.setDeferredLoading(true);
    this.sound = new Sound("testdata/restart.ogg");
    this.music = new Music("testdata/SMB-X.XM");
    this.image = new Image("testdata/logo.tga");
    this.font = (Font)new AngelCodeFont("testdata/demo.fnt", "testdata/demo_00.tga");
  }
  
  public void render(GameContainer container, Graphics g) {
    if (this.nextResource != null)
      g.drawString("Loading: " + this.nextResource.getDescription(), 100.0F, 100.0F); 
    int total = LoadingList.get().getTotalResources();
    int loaded = LoadingList.get().getTotalResources() - LoadingList.get().getRemainingResources();
    float bar = loaded / total;
    g.fillRect(100.0F, 150.0F, (loaded * 40), 20.0F);
    g.drawRect(100.0F, 150.0F, (total * 40), 20.0F);
    if (this.started) {
      this.image.draw(100.0F, 200.0F);
      this.font.drawString(100.0F, 500.0F, "LOADING COMPLETE");
    } 
  }
  
  public void update(GameContainer container, int delta) throws SlickException {
    if (this.nextResource != null) {
      try {
        this.nextResource.load();
        try {
          Thread.sleep(50L);
        } catch (Exception exception) {}
      } catch (IOException e) {
        throw new SlickException("Failed to load: " + this.nextResource.getDescription(), e);
      } 
      this.nextResource = null;
    } 
    if (LoadingList.get().getRemainingResources() > 0) {
      this.nextResource = LoadingList.get().getNext();
    } else if (!this.started) {
      this.started = true;
      this.music.loop();
      this.sound.play();
    } 
  }
  
  public static void main(String[] argv) {
    try {
      AppGameContainer container = new AppGameContainer((Game)new DeferredLoadingTest());
      container.setDisplayMode(800, 600, false);
      container.start();
    } catch (SlickException e) {
      e.printStackTrace();
    } 
  }
  
  public void keyPressed(int key, char c) {}
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\tests\DeferredLoadingTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */