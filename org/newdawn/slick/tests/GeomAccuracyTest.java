package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.geom.Shape;

public class GeomAccuracyTest extends BasicGame {
  private GameContainer container;
  
  private Color geomColor;
  
  private Color overlayColor;
  
  private boolean hideOverlay;
  
  private int colorIndex;
  
  private int curTest;
  
  private static final int NUMTESTS = 3;
  
  private Image magImage;
  
  public GeomAccuracyTest() {
    super("Geometry Accuracy Tests");
  }
  
  public void init(GameContainer container) throws SlickException {
    this.container = container;
    this.geomColor = Color.magenta;
    this.overlayColor = Color.white;
    this.magImage = new Image(21, 21);
  }
  
  public void render(GameContainer container, Graphics g) {
    String text = new String();
    switch (this.curTest) {
      case 0:
        text = "Rectangles";
        rectTest(g);
        break;
      case 1:
        text = "Ovals";
        ovalTest(g);
        break;
      case 2:
        text = "Arcs";
        arcTest(g);
        break;
    } 
    g.setColor(Color.white);
    g.drawString("Press T to toggle overlay", 200.0F, 55.0F);
    g.drawString("Press N to switch tests", 200.0F, 35.0F);
    g.drawString("Press C to cycle drawing colors", 200.0F, 15.0F);
    g.drawString("Current Test:", 400.0F, 35.0F);
    g.setColor(Color.blue);
    g.drawString(text, 485.0F, 35.0F);
    g.setColor(Color.white);
    g.drawString("Normal:", 10.0F, 150.0F);
    g.drawString("Filled:", 10.0F, 300.0F);
    g.drawString("Drawn with Graphics context", 125.0F, 400.0F);
    g.drawString("Drawn using Shapes", 450.0F, 400.0F);
    g.copyArea(this.magImage, container.getInput().getMouseX() - 10, container.getInput().getMouseY() - 10);
    this.magImage.draw(351.0F, 451.0F, 5.0F);
    g.drawString("Mag Area -", 250.0F, 475.0F);
    g.setColor(Color.darkGray);
    g.drawRect(350.0F, 450.0F, 106.0F, 106.0F);
    g.setColor(Color.white);
    g.drawString("NOTE:", 500.0F, 450.0F);
    g.drawString("lines should be flush with edges", 525.0F, 470.0F);
    g.drawString("corners should be symetric", 525.0F, 490.0F);
  }
  
  void arcTest(Graphics g) {
    if (!this.hideOverlay) {
      g.setColor(this.overlayColor);
      g.drawLine(198.0F, 100.0F, 198.0F, 198.0F);
      g.drawLine(100.0F, 198.0F, 198.0F, 198.0F);
    } 
    g.setColor(this.geomColor);
    g.drawArc(100.0F, 100.0F, 99.0F, 99.0F, 0.0F, 90.0F);
  }
  
  void ovalTest(Graphics g) {
    g.setColor(this.geomColor);
    g.drawOval(100.0F, 100.0F, 99.0F, 99.0F);
    g.fillOval(100.0F, 250.0F, 99.0F, 99.0F);
    Ellipse elip = new Ellipse(449.0F, 149.0F, 49.0F, 49.0F);
    g.draw((Shape)elip);
    elip = new Ellipse(449.0F, 299.0F, 49.0F, 49.0F);
    g.fill((Shape)elip);
    if (!this.hideOverlay) {
      g.setColor(this.overlayColor);
      g.drawLine(100.0F, 149.0F, 198.0F, 149.0F);
      g.drawLine(149.0F, 100.0F, 149.0F, 198.0F);
      g.drawLine(100.0F, 299.0F, 198.0F, 299.0F);
      g.drawLine(149.0F, 250.0F, 149.0F, 348.0F);
      g.drawLine(400.0F, 149.0F, 498.0F, 149.0F);
      g.drawLine(449.0F, 100.0F, 449.0F, 198.0F);
      g.drawLine(400.0F, 299.0F, 498.0F, 299.0F);
      g.drawLine(449.0F, 250.0F, 449.0F, 348.0F);
    } 
  }
  
  void rectTest(Graphics g) {
    g.setColor(this.geomColor);
    g.drawRect(100.0F, 100.0F, 99.0F, 99.0F);
    g.fillRect(100.0F, 250.0F, 99.0F, 99.0F);
    g.drawRoundRect(250.0F, 100.0F, 99.0F, 99.0F, 10);
    g.fillRoundRect(250.0F, 250.0F, 99.0F, 99.0F, 10);
    Rectangle rect = new Rectangle(400.0F, 100.0F, 99.0F, 99.0F);
    g.draw((Shape)rect);
    rect = new Rectangle(400.0F, 250.0F, 99.0F, 99.0F);
    g.fill((Shape)rect);
    RoundedRectangle rrect = new RoundedRectangle(550.0F, 100.0F, 99.0F, 99.0F, 10.0F);
    g.draw((Shape)rrect);
    rrect = new RoundedRectangle(550.0F, 250.0F, 99.0F, 99.0F, 10.0F);
    g.fill((Shape)rrect);
    if (!this.hideOverlay) {
      g.setColor(this.overlayColor);
      g.drawLine(100.0F, 149.0F, 198.0F, 149.0F);
      g.drawLine(149.0F, 100.0F, 149.0F, 198.0F);
      g.drawLine(250.0F, 149.0F, 348.0F, 149.0F);
      g.drawLine(299.0F, 100.0F, 299.0F, 198.0F);
      g.drawLine(400.0F, 149.0F, 498.0F, 149.0F);
      g.drawLine(449.0F, 100.0F, 449.0F, 198.0F);
      g.drawLine(550.0F, 149.0F, 648.0F, 149.0F);
      g.drawLine(599.0F, 100.0F, 599.0F, 198.0F);
      g.drawLine(100.0F, 299.0F, 198.0F, 299.0F);
      g.drawLine(149.0F, 250.0F, 149.0F, 348.0F);
      g.drawLine(250.0F, 299.0F, 348.0F, 299.0F);
      g.drawLine(299.0F, 250.0F, 299.0F, 348.0F);
      g.drawLine(400.0F, 299.0F, 498.0F, 299.0F);
      g.drawLine(449.0F, 250.0F, 449.0F, 348.0F);
      g.drawLine(550.0F, 299.0F, 648.0F, 299.0F);
      g.drawLine(599.0F, 250.0F, 599.0F, 348.0F);
    } 
  }
  
  public void update(GameContainer container, int delta) {}
  
  public void keyPressed(int key, char c) {
    if (key == 1)
      System.exit(0); 
    if (key == 49) {
      this.curTest++;
      this.curTest %= 3;
    } 
    if (key == 46) {
      this.colorIndex++;
      this.colorIndex %= 4;
      setColors();
    } 
    if (key == 20)
      this.hideOverlay = !this.hideOverlay; 
  }
  
  private void setColors() {
    switch (this.colorIndex) {
      case 0:
        this.overlayColor = Color.white;
        this.geomColor = Color.magenta;
        break;
      case 1:
        this.overlayColor = Color.magenta;
        this.geomColor = Color.white;
        break;
      case 2:
        this.overlayColor = Color.red;
        this.geomColor = Color.green;
        break;
      case 3:
        this.overlayColor = Color.red;
        this.geomColor = Color.white;
        break;
    } 
  }
  
  public static void main(String[] argv) {
    try {
      AppGameContainer container = new AppGameContainer((Game)new GeomAccuracyTest());
      container.setDisplayMode(800, 600, false);
      container.start();
    } catch (SlickException e) {
      e.printStackTrace();
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\tests\GeomAccuracyTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */