package org.newdawn.slick.state.transition;

import java.util.ArrayList;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public class CombinedTransition implements Transition {
  private ArrayList transitions = new ArrayList();
  
  public void addTransition(Transition t) {
    this.transitions.add(t);
  }
  
  public boolean isComplete() {
    for (int i = 0; i < this.transitions.size(); i++) {
      if (!((Transition)this.transitions.get(i)).isComplete())
        return false; 
    } 
    return true;
  }
  
  public void postRender(StateBasedGame game, GameContainer container, Graphics g) throws SlickException {
    for (int i = this.transitions.size() - 1; i >= 0; i--)
      ((Transition)this.transitions.get(i)).postRender(game, container, g); 
  }
  
  public void preRender(StateBasedGame game, GameContainer container, Graphics g) throws SlickException {
    for (int i = 0; i < this.transitions.size(); i++)
      ((Transition)this.transitions.get(i)).postRender(game, container, g); 
  }
  
  public void update(StateBasedGame game, GameContainer container, int delta) throws SlickException {
    for (int i = 0; i < this.transitions.size(); i++) {
      Transition t = this.transitions.get(i);
      if (!t.isComplete())
        t.update(game, container, delta); 
    } 
  }
  
  public void init(GameState firstState, GameState secondState) {
    for (int i = this.transitions.size() - 1; i >= 0; i--)
      ((Transition)this.transitions.get(i)).init(firstState, secondState); 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\state\transition\CombinedTransition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */