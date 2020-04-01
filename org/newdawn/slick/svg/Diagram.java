package org.newdawn.slick.svg;

import java.util.ArrayList;
import java.util.HashMap;

public class Diagram {
  private ArrayList figures = new ArrayList();
  
  private HashMap patterns = new HashMap<>();
  
  private HashMap gradients = new HashMap<>();
  
  private HashMap figureMap = new HashMap<>();
  
  private float width;
  
  private float height;
  
  public Diagram(float width, float height) {
    this.width = width;
    this.height = height;
  }
  
  public float getWidth() {
    return this.width;
  }
  
  public float getHeight() {
    return this.height;
  }
  
  public void addPatternDef(String name, String href) {
    this.patterns.put(name, href);
  }
  
  public void addGradient(String name, Gradient gradient) {
    this.gradients.put(name, gradient);
  }
  
  public String getPatternDef(String name) {
    return (String)this.patterns.get(name);
  }
  
  public Gradient getGradient(String name) {
    return (Gradient)this.gradients.get(name);
  }
  
  public String[] getPatternDefNames() {
    return (String[])this.patterns.keySet().toArray((Object[])new String[0]);
  }
  
  public Figure getFigureByID(String id) {
    return (Figure)this.figureMap.get(id);
  }
  
  public void addFigure(Figure figure) {
    this.figures.add(figure);
    this.figureMap.put(figure.getData().getAttribute("id"), figure);
    String fillRef = figure.getData().getAsReference("fill");
    Gradient gradient = getGradient(fillRef);
    if (gradient != null && 
      gradient.isRadial())
      for (int i = 0; i < InkscapeLoader.RADIAL_TRIANGULATION_LEVEL; i++)
        figure.getShape().increaseTriangulation();  
  }
  
  public int getFigureCount() {
    return this.figures.size();
  }
  
  public Figure getFigure(int index) {
    return this.figures.get(index);
  }
  
  public void removeFigure(Figure figure) {
    this.figures.remove(figure);
    this.figureMap.remove(figure.getData().getAttribute("id"));
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\svg\Diagram.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */