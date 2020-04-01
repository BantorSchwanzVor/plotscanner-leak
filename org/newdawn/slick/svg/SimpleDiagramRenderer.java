package org.newdawn.slick.svg;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.ShapeRenderer;
import org.newdawn.slick.geom.TexCoordGenerator;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;

public class SimpleDiagramRenderer {
  protected static SGL GL = Renderer.get();
  
  public Diagram diagram;
  
  public int list = -1;
  
  public SimpleDiagramRenderer(Diagram diagram) {
    this.diagram = diagram;
  }
  
  public void render(Graphics g) {
    if (this.list == -1) {
      this.list = GL.glGenLists(1);
      GL.glNewList(this.list, 4864);
      render(g, this.diagram);
      GL.glEndList();
    } 
    GL.glCallList(this.list);
    TextureImpl.bindNone();
  }
  
  public static void render(Graphics g, Diagram diagram) {
    for (int i = 0; i < diagram.getFigureCount(); i++) {
      Figure figure = diagram.getFigure(i);
      if (figure.getData().isFilled()) {
        if (figure.getData().isColor("fill")) {
          g.setColor(figure.getData().getAsColor("fill"));
          g.fill(diagram.getFigure(i).getShape());
          g.setAntiAlias(true);
          g.draw(diagram.getFigure(i).getShape());
          g.setAntiAlias(false);
        } 
        String fill = figure.getData().getAsReference("fill");
        if (diagram.getPatternDef(fill) != null)
          System.out.println("PATTERN"); 
        if (diagram.getGradient(fill) != null) {
          Gradient gradient = diagram.getGradient(fill);
          Shape shape = diagram.getFigure(i).getShape();
          TexCoordGenerator fg = null;
          if (gradient.isRadial()) {
            fg = new RadialGradientFill(shape, diagram.getFigure(i).getTransform(), gradient);
          } else {
            fg = new LinearGradientFill(shape, diagram.getFigure(i).getTransform(), gradient);
          } 
          Color.white.bind();
          ShapeRenderer.texture(shape, gradient.getImage(), fg);
        } 
      } 
      if (figure.getData().isStroked() && 
        figure.getData().isColor("stroke")) {
        g.setColor(figure.getData().getAsColor("stroke"));
        g.setLineWidth(figure.getData().getAsFloat("stroke-width"));
        g.setAntiAlias(true);
        g.draw(diagram.getFigure(i).getShape());
        g.setAntiAlias(false);
        g.resetLineWidth();
      } 
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\svg\SimpleDiagramRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */