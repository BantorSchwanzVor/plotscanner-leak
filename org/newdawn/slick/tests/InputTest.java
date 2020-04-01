package org.newdawn.slick.tests;

import java.util.ArrayList;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

public class InputTest extends BasicGame {
  private String message = "Press any key, mouse button, or drag the mouse";
  
  private ArrayList lines = new ArrayList();
  
  private boolean buttonDown;
  
  private float x;
  
  private float y;
  
  private Color[] cols = new Color[] { Color.red, Color.green, Color.blue, Color.white, Color.magenta, Color.cyan };
  
  private int index;
  
  private Input input;
  
  private int ypos;
  
  private AppGameContainer app;
  
  private boolean space;
  
  private boolean lshift;
  
  private boolean rshift;
  
  public InputTest() {
    super("Input Test");
  }
  
  public void init(GameContainer container) throws SlickException {
    if (container instanceof AppGameContainer)
      this.app = (AppGameContainer)container; 
    this.input = container.getInput();
    this.x = 300.0F;
    this.y = 300.0F;
  }
  
  public void render(GameContainer container, Graphics g) {
    g.drawString("left shift down: " + this.lshift, 100.0F, 240.0F);
    g.drawString("right shift down: " + this.rshift, 100.0F, 260.0F);
    g.drawString("space down: " + this.space, 100.0F, 280.0F);
    g.setColor(Color.white);
    g.drawString(this.message, 10.0F, 50.0F);
    g.drawString(container.getInput().getMouseY(), 10.0F, 400.0F);
    g.drawString("Use the primary gamepad to control the blob, and hit a gamepad button to change the color", 10.0F, 90.0F);
    for (int i = 0; i < this.lines.size(); i++) {
      Line line = this.lines.get(i);
      line.draw(g);
    } 
    g.setColor(this.cols[this.index]);
    g.fillOval((int)this.x, (int)this.y, 50.0F, 50.0F);
    g.setColor(Color.yellow);
    g.fillRect(50.0F, (200 + this.ypos), 40.0F, 40.0F);
  }
  
  public void update(GameContainer container, int delta) {
    this.lshift = container.getInput().isKeyDown(42);
    this.rshift = container.getInput().isKeyDown(54);
    this.space = container.getInput().isKeyDown(57);
    if (this.controllerLeft[0])
      this.x -= delta * 0.1F; 
    if (this.controllerRight[0])
      this.x += delta * 0.1F; 
    if (this.controllerUp[0])
      this.y -= delta * 0.1F; 
    if (this.controllerDown[0])
      this.y += delta * 0.1F; 
  }
  
  public void keyPressed(int key, char c) {
    if (key == 1)
      System.exit(0); 
    if (key == 59 && 
      this.app != null)
      try {
        this.app.setDisplayMode(600, 600, false);
        this.app.reinit();
      } catch (Exception e) {
        Log.error(e);
      }  
  }
  
  public void keyReleased(int key, char c) {
    this.message = "You pressed key code " + key + " (character = " + c + ")";
  }
  
  public void mousePressed(int button, int x, int y) {
    if (button == 0)
      this.buttonDown = true; 
    this.message = "Mouse pressed " + button + " " + x + "," + y;
  }
  
  public void mouseReleased(int button, int x, int y) {
    if (button == 0)
      this.buttonDown = false; 
    this.message = "Mouse released " + button + " " + x + "," + y;
  }
  
  public void mouseClicked(int button, int x, int y, int clickCount) {
    System.out.println("CLICKED:" + x + "," + y + " " + clickCount);
  }
  
  public void mouseWheelMoved(int change) {
    this.message = "Mouse wheel moved: " + change;
    if (change < 0)
      this.ypos -= 10; 
    if (change > 0)
      this.ypos += 10; 
  }
  
  public void mouseMoved(int oldx, int oldy, int newx, int newy) {
    if (this.buttonDown)
      this.lines.add(new Line(oldx, oldy, newx, newy)); 
  }
  
  private class Line {
    private int oldx;
    
    private int oldy;
    
    private int newx;
    
    private int newy;
    
    public Line(int oldx, int oldy, int newx, int newy) {
      this.oldx = oldx;
      this.oldy = oldy;
      this.newx = newx;
      this.newy = newy;
    }
    
    public void draw(Graphics g) {
      g.drawLine(this.oldx, this.oldy, this.newx, this.newy);
    }
  }
  
  public void controllerButtonPressed(int controller, int button) {
    super.controllerButtonPressed(controller, button);
    this.index++;
    this.index %= this.cols.length;
  }
  
  public static void main(String[] argv) {
    try {
      AppGameContainer container = new AppGameContainer((Game)new InputTest());
      container.setDisplayMode(800, 600, false);
      container.start();
    } catch (SlickException e) {
      e.printStackTrace();
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\tests\InputTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */