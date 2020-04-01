package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Music;
import org.newdawn.slick.MusicListener;
import org.newdawn.slick.SlickException;

public class MusicListenerTest extends BasicGame implements MusicListener {
  private boolean musicEnded = false;
  
  private boolean musicSwapped = false;
  
  private Music music;
  
  private Music stream;
  
  public MusicListenerTest() {
    super("Music Listener Test");
  }
  
  public void init(GameContainer container) throws SlickException {
    this.music = new Music("testdata/restart.ogg", false);
    this.stream = new Music("testdata/restart.ogg", false);
    this.music.addListener(this);
    this.stream.addListener(this);
  }
  
  public void update(GameContainer container, int delta) throws SlickException {}
  
  public void musicEnded(Music music) {
    this.musicEnded = true;
  }
  
  public void musicSwapped(Music music, Music newMusic) {
    this.musicSwapped = true;
  }
  
  public void render(GameContainer container, Graphics g) throws SlickException {
    g.drawString("Press M to play music", 100.0F, 100.0F);
    g.drawString("Press S to stream music", 100.0F, 150.0F);
    if (this.musicEnded)
      g.drawString("Music Ended", 100.0F, 200.0F); 
    if (this.musicSwapped)
      g.drawString("Music Swapped", 100.0F, 250.0F); 
  }
  
  public void keyPressed(int key, char c) {
    if (key == 50) {
      this.musicEnded = false;
      this.musicSwapped = false;
      this.music.play();
    } 
    if (key == 31) {
      this.musicEnded = false;
      this.musicSwapped = false;
      this.stream.play();
    } 
  }
  
  public static void main(String[] argv) {
    try {
      AppGameContainer container = new AppGameContainer((Game)new MusicListenerTest());
      container.setDisplayMode(800, 600, false);
      container.start();
    } catch (SlickException e) {
      e.printStackTrace();
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\tests\MusicListenerTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */