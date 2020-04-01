package org.newdawn.slick.tests;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.util.Log;

public class GUITest extends BasicGame implements ComponentListener {
  private Image image;
  
  private MouseOverArea[] areas = new MouseOverArea[4];
  
  private GameContainer container;
  
  private String message = "Demo Menu System with stock images";
  
  private TextField field;
  
  private TextField field2;
  
  private Image background;
  
  private Font font;
  
  private AppGameContainer app;
  
  public GUITest() {
    super("GUI Test");
  }
  
  public void init(GameContainer container) throws SlickException {
    if (container instanceof AppGameContainer) {
      this.app = (AppGameContainer)container;
      this.app.setIcon("testdata/icon.tga");
    } 
    this.font = (Font)new AngelCodeFont("testdata/demo2.fnt", "testdata/demo2_00.tga");
    this.field = new TextField((GUIContext)container, this.font, 150, 20, 500, 35, new ComponentListener() {
          public void componentActivated(AbstractComponent source) {
            GUITest.this.message = "Entered1: " + GUITest.this.field.getText();
            GUITest.this.field2.setFocus(true);
          }
        });
    this.field2 = new TextField((GUIContext)container, this.font, 150, 70, 500, 35, new ComponentListener() {
          public void componentActivated(AbstractComponent source) {
            GUITest.this.message = "Entered2: " + GUITest.this.field2.getText();
            GUITest.this.field.setFocus(true);
          }
        });
    this.field2.setBorderColor(Color.red);
    this.container = container;
    this.image = new Image("testdata/logo.tga");
    this.background = new Image("testdata/dungeontiles.gif");
    container.setMouseCursor("testdata/cursor.tga", 0, 0);
    for (int i = 0; i < 4; i++) {
      this.areas[i] = new MouseOverArea((GUIContext)container, this.image, 300, 100 + i * 100, 200, 90, this);
      this.areas[i].setNormalColor(new Color(1.0F, 1.0F, 1.0F, 0.8F));
      this.areas[i].setMouseOverColor(new Color(1.0F, 1.0F, 1.0F, 0.9F));
    } 
  }
  
  public void render(GameContainer container, Graphics g) {
    this.background.draw(0.0F, 0.0F, 800.0F, 500.0F);
    for (int i = 0; i < 4; i++)
      this.areas[i].render((GUIContext)container, g); 
    this.field.render((GUIContext)container, g);
    this.field2.render((GUIContext)container, g);
    g.setFont(this.font);
    g.drawString(this.message, 200.0F, 550.0F);
  }
  
  public void update(GameContainer container, int delta) {}
  
  public void keyPressed(int key, char c) {
    if (key == 1)
      System.exit(0); 
    if (key == 60)
      this.app.setDefaultMouseCursor(); 
    if (key == 59 && 
      this.app != null)
      try {
        this.app.setDisplayMode(640, 480, false);
      } catch (SlickException e) {
        Log.error((Throwable)e);
      }  
  }
  
  public static void main(String[] argv) {
    try {
      AppGameContainer container = new AppGameContainer((Game)new GUITest());
      container.setDisplayMode(800, 600, false);
      container.start();
    } catch (SlickException e) {
      e.printStackTrace();
    } 
  }
  
  public void componentActivated(AbstractComponent source) {
    System.out.println("ACTIVL : " + source);
    for (int i = 0; i < 4; i++) {
      if (source == this.areas[i])
        this.message = "Option " + (i + 1) + " pressed!"; 
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\tests\GUITest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */