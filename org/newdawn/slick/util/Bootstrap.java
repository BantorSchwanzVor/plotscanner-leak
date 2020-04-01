package org.newdawn.slick.util;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Game;

public class Bootstrap {
  public static void runAsApplication(Game game, int width, int height, boolean fullscreen) {
    try {
      AppGameContainer container = new AppGameContainer(game, width, height, fullscreen);
      container.start();
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slic\\util\Bootstrap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */