package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.particles.effects.FireEmitter;

public class ParticleTest extends BasicGame {
  private ParticleSystem system;
  
  private int mode = 2;
  
  public ParticleTest() {
    super("Particle Test");
  }
  
  public void init(GameContainer container) throws SlickException {
    Image image = new Image("testdata/particle.tga", true);
    this.system = new ParticleSystem(image);
    this.system.addEmitter((ParticleEmitter)new FireEmitter(400, 300, 45.0F));
    this.system.addEmitter((ParticleEmitter)new FireEmitter(200, 300, 60.0F));
    this.system.addEmitter((ParticleEmitter)new FireEmitter(600, 300, 30.0F));
  }
  
  public void render(GameContainer container, Graphics g) {
    for (int i = 0; i < 100; i++) {
      g.translate(1.0F, 1.0F);
      this.system.render();
    } 
    g.resetTransform();
    g.drawString("Press space to toggle blending mode", 200.0F, 500.0F);
    g.drawString("Particle Count: " + (this.system.getParticleCount() * 100), 200.0F, 520.0F);
  }
  
  public void update(GameContainer container, int delta) {
    this.system.update(delta);
  }
  
  public void keyPressed(int key, char c) {
    if (key == 1)
      System.exit(0); 
    if (key == 57) {
      this.mode = (1 == this.mode) ? 2 : 1;
      this.system.setBlendingMode(this.mode);
    } 
  }
  
  public static void main(String[] argv) {
    try {
      AppGameContainer container = new AppGameContainer((Game)new ParticleTest());
      container.setDisplayMode(800, 600, false);
      container.start();
    } catch (SlickException e) {
      e.printStackTrace();
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\tests\ParticleTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */