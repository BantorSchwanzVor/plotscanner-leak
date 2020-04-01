package org.newdawn.slick.state.transition;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public class FadeOutTransition implements Transition {
  private Color color;
  
  private int fadeTime;
  
  public FadeOutTransition() {
    this(Color.black, 500);
  }
  
  public FadeOutTransition(Color color) {
    this(color, 500);
  }
  
  public FadeOutTransition(Color color, int fadeTime) {
    this.color = new Color(color);
    this.color.a = 0.0F;
    this.fadeTime = fadeTime;
  }
  
  public boolean isComplete() {
    return (this.color.a >= 1.0F);
  }
  
  public void postRender(StateBasedGame game, GameContainer container, Graphics g) {
    Color old = g.getColor();
    g.setColor(this.color);
    g.fillRect(0.0F, 0.0F, (container.getWidth() * 2), (container.getHeight() * 2));
    g.setColor(old);
  }
  
  public void update(StateBasedGame game, GameContainer container, int delta) {
    this.color.a += delta * 1.0F / this.fadeTime;
    if (this.color.a > 1.0F)
      this.color.a = 1.0F; 
  }
  
  public void preRender(StateBasedGame game, GameContainer container, Graphics g) {}
  
  public void init(GameState firstState, GameState secondState) {}
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\state\transition\FadeOutTransition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */