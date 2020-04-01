package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.util.ResourceLoader;

public class SoundURLTest extends BasicGame {
  private Sound sound;
  
  private Sound charlie;
  
  private Sound burp;
  
  private Music music;
  
  private Music musica;
  
  private Music musicb;
  
  private Sound engine;
  
  private int volume = 1;
  
  public SoundURLTest() {
    super("Sound URL Test");
  }
  
  public void init(GameContainer container) throws SlickException {
    this.sound = new Sound(ResourceLoader.getResource("testdata/restart.ogg"));
    this.charlie = new Sound(ResourceLoader.getResource("testdata/cbrown01.wav"));
    this.engine = new Sound(ResourceLoader.getResource("testdata/engine.wav"));
    this.music = this.musica = new Music(ResourceLoader.getResource("testdata/restart.ogg"), false);
    this.musicb = new Music(ResourceLoader.getResource("testdata/kirby.ogg"), false);
    this.burp = new Sound(ResourceLoader.getResource("testdata/burp.aif"));
  }
  
  public void render(GameContainer container, Graphics g) {
    g.setColor(Color.white);
    g.drawString("The OGG loop is now streaming from the file, woot.", 100.0F, 60.0F);
    g.drawString("Press space for sound effect (OGG)", 100.0F, 100.0F);
    g.drawString("Press P to pause/resume music (XM)", 100.0F, 130.0F);
    g.drawString("Press E to pause/resume engine sound (WAV)", 100.0F, 190.0F);
    g.drawString("Press enter for charlie (WAV)", 100.0F, 160.0F);
    g.drawString("Press C to change music", 100.0F, 210.0F);
    g.drawString("Press B to burp (AIF)", 100.0F, 240.0F);
    g.drawString("Press + or - to change volume of music", 100.0F, 270.0F);
    g.setColor(Color.blue);
    g.drawString("Music Volume Level: " + (this.volume / 10.0F), 150.0F, 300.0F);
  }
  
  public void update(GameContainer container, int delta) {}
  
  public void keyPressed(int key, char c) {
    if (key == 1)
      System.exit(0); 
    if (key == 57)
      this.sound.play(); 
    if (key == 48)
      this.burp.play(); 
    if (key == 30)
      this.sound.playAt(-1.0F, 0.0F, 0.0F); 
    if (key == 38)
      this.sound.playAt(1.0F, 0.0F, 0.0F); 
    if (key == 28)
      this.charlie.play(1.0F, 1.0F); 
    if (key == 25)
      if (this.music.playing()) {
        this.music.pause();
      } else {
        this.music.resume();
      }  
    if (key == 46) {
      this.music.stop();
      if (this.music == this.musica) {
        this.music = this.musicb;
      } else {
        this.music = this.musica;
      } 
      this.music.loop();
    } 
    if (key == 18)
      if (this.engine.playing()) {
        this.engine.stop();
      } else {
        this.engine.loop();
      }  
    if (c == '+') {
      this.volume++;
      setVolume();
    } 
    if (c == '-') {
      this.volume--;
      setVolume();
    } 
  }
  
  private void setVolume() {
    if (this.volume > 10) {
      this.volume = 10;
    } else if (this.volume < 0) {
      this.volume = 0;
    } 
    this.music.setVolume(this.volume / 10.0F);
  }
  
  public static void main(String[] argv) {
    try {
      AppGameContainer container = new AppGameContainer((Game)new SoundURLTest());
      container.setDisplayMode(800, 600, false);
      container.start();
    } catch (SlickException e) {
      e.printStackTrace();
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\tests\SoundURLTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */