package org.newdawn.slick;

public interface Game {
  void init(GameContainer paramGameContainer) throws SlickException;
  
  void update(GameContainer paramGameContainer, int paramInt) throws SlickException;
  
  void render(GameContainer paramGameContainer, Graphics paramGraphics) throws SlickException;
  
  boolean closeRequested();
  
  String getTitle();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\Game.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */