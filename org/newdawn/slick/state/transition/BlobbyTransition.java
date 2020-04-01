package org.newdawn.slick.state.transition;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.MaskUtil;

public class BlobbyTransition implements Transition {
  protected static SGL GL = Renderer.get();
  
  private GameState prev;
  
  private boolean finish;
  
  private Color background;
  
  private ArrayList blobs = new ArrayList();
  
  private int timer = 1000;
  
  private int blobCount = 10;
  
  public BlobbyTransition() {}
  
  public BlobbyTransition(Color background) {
    this.background = background;
  }
  
  public void init(GameState firstState, GameState secondState) {
    this.prev = secondState;
  }
  
  public boolean isComplete() {
    return this.finish;
  }
  
  public void postRender(StateBasedGame game, GameContainer container, Graphics g) throws SlickException {
    MaskUtil.resetMask();
  }
  
  public void preRender(StateBasedGame game, GameContainer container, Graphics g) throws SlickException {
    this.prev.render(container, game, g);
    MaskUtil.defineMask();
    for (int i = 0; i < this.blobs.size(); i++)
      ((Blob)this.blobs.get(i)).render(g); 
    MaskUtil.finishDefineMask();
    MaskUtil.drawOnMask();
    if (this.background != null) {
      Color c = g.getColor();
      g.setColor(this.background);
      g.fillRect(0.0F, 0.0F, container.getWidth(), container.getHeight());
      g.setColor(c);
    } 
  }
  
  public void update(StateBasedGame game, GameContainer container, int delta) throws SlickException {
    if (this.blobs.size() == 0)
      for (int j = 0; j < this.blobCount; j++)
        this.blobs.add(new Blob(container));  
    for (int i = 0; i < this.blobs.size(); i++)
      ((Blob)this.blobs.get(i)).update(delta); 
    this.timer -= delta;
    if (this.timer < 0)
      this.finish = true; 
  }
  
  private class Blob {
    private float x;
    
    private float y;
    
    private float growSpeed;
    
    private float rad;
    
    public Blob(GameContainer container) {
      this.x = (float)(Math.random() * container.getWidth());
      this.y = (float)(Math.random() * container.getWidth());
      this.growSpeed = (float)(1.0D + Math.random() * 1.0D);
    }
    
    public void update(int delta) {
      this.rad += this.growSpeed * delta * 0.4F;
    }
    
    public void render(Graphics g) {
      g.fillOval(this.x - this.rad, this.y - this.rad, this.rad * 2.0F, this.rad * 2.0F);
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\state\transition\BlobbyTransition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */