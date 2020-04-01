package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tests.states.TestState1;
import org.newdawn.slick.tests.states.TestState2;
import org.newdawn.slick.tests.states.TestState3;

public class StateBasedTest extends StateBasedGame {
  public StateBasedTest() {
    super("State Based Test");
  }
  
  public void initStatesList(GameContainer container) {
    addState((GameState)new TestState1());
    addState((GameState)new TestState2());
    addState((GameState)new TestState3());
  }
  
  public static void main(String[] argv) {
    try {
      AppGameContainer container = new AppGameContainer((Game)new StateBasedTest());
      container.setDisplayMode(800, 600, false);
      container.start();
    } catch (SlickException e) {
      e.printStackTrace();
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\tests\StateBasedTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */