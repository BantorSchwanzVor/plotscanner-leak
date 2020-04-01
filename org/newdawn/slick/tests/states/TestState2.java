package org.newdawn.slick.tests.states;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.state.transition.Transition;

public class TestState2 extends BasicGameState {
  public static final int ID = 2;
  
  private Font font;
  
  private Image image;
  
  private float ang;
  
  private StateBasedGame game;
  
  public int getID() {
    return 2;
  }
  
  public void init(GameContainer container, StateBasedGame game) throws SlickException {
    this.game = game;
    this.font = (Font)new AngelCodeFont("testdata/demo2.fnt", "testdata/demo2_00.tga");
    this.image = new Image("testdata/logo.tga");
  }
  
  public void render(GameContainer container, StateBasedGame game, Graphics g) {
    g.setFont(this.font);
    g.setColor(Color.green);
    g.drawString("This is State 2", 200.0F, 50.0F);
    g.rotate(400.0F, 300.0F, this.ang);
    g.drawImage(this.image, (400 - this.image.getWidth() / 2), (300 - this.image.getHeight() / 2));
  }
  
  public void update(GameContainer container, StateBasedGame game, int delta) {
    this.ang += delta * 0.1F;
  }
  
  public void keyReleased(int key, char c) {
    if (key == 2)
      this.game.enterState(1, (Transition)new FadeOutTransition(Color.black), (Transition)new FadeInTransition(Color.black)); 
    if (key == 4)
      this.game.enterState(3, (Transition)new FadeOutTransition(Color.black), (Transition)new FadeInTransition(Color.black)); 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\tests\states\TestState2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */