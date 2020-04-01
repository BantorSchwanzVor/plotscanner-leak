package org.newdawn.slick.tests;

import java.io.IOException;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

public class PedigreeTest extends BasicGame {
  private Image image;
  
  private GameContainer container;
  
  private ParticleSystem trail;
  
  private ParticleSystem fire;
  
  private float rx;
  
  private float ry = 900.0F;
  
  public PedigreeTest() {
    super("Pedigree Test");
  }
  
  public void init(GameContainer container) throws SlickException {
    this.container = container;
    try {
      this.fire = ParticleIO.loadConfiguredSystem("testdata/system.xml");
      this.trail = ParticleIO.loadConfiguredSystem("testdata/smoketrail.xml");
    } catch (IOException e) {
      throw new SlickException("Failed to load particle systems", e);
    } 
    this.image = new Image("testdata/rocket.png");
    spawnRocket();
  }
  
  private void spawnRocket() {
    this.ry = 700.0F;
    this.rx = (float)(Math.random() * 600.0D + 100.0D);
  }
  
  public void render(GameContainer container, Graphics g) {
    ((ConfigurableEmitter)this.trail.getEmitter(0)).setPosition(this.rx + 14.0F, this.ry + 35.0F);
    this.trail.render();
    this.image.draw((int)this.rx, (int)this.ry);
    g.translate(400.0F, 300.0F);
    this.fire.render();
  }
  
  public void update(GameContainer container, int delta) {
    this.fire.update(delta);
    this.trail.update(delta);
    this.ry -= delta * 0.25F;
    if (this.ry < -100.0F)
      spawnRocket(); 
  }
  
  public void mousePressed(int button, int x, int y) {
    super.mousePressed(button, x, y);
    for (int i = 0; i < this.fire.getEmitterCount(); i++)
      ((ConfigurableEmitter)this.fire.getEmitter(i)).setPosition((x - 400), (y - 300), true); 
  }
  
  public static void main(String[] argv) {
    try {
      AppGameContainer container = new AppGameContainer((Game)new PedigreeTest());
      container.setDisplayMode(800, 600, false);
      container.start();
    } catch (SlickException e) {
      e.printStackTrace();
    } 
  }
  
  public void keyPressed(int key, char c) {
    if (key == 1)
      this.container.exit(); 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\tests\PedigreeTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */