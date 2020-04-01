package org.newdawn.slick.state.transition;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public abstract class CrossStateTransition implements Transition {
  private GameState secondState;
  
  public CrossStateTransition(GameState secondState) {
    this.secondState = secondState;
  }
  
  public abstract boolean isComplete();
  
  public void postRender(StateBasedGame game, GameContainer container, Graphics g) throws SlickException {
    preRenderSecondState(game, container, g);
    this.secondState.render(container, game, g);
    postRenderSecondState(game, container, g);
  }
  
  public void preRender(StateBasedGame game, GameContainer container, Graphics g) throws SlickException {
    preRenderFirstState(game, container, g);
  }
  
  public void update(StateBasedGame game, GameContainer container, int delta) throws SlickException {}
  
  public void preRenderFirstState(StateBasedGame game, GameContainer container, Graphics g) throws SlickException {}
  
  public void preRenderSecondState(StateBasedGame game, GameContainer container, Graphics g) throws SlickException {}
  
  public void postRenderSecondState(StateBasedGame game, GameContainer container, Graphics g) throws SlickException {}
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\state\transition\CrossStateTransition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */