package org.newdawn.slick.opengl.renderer;

public class DefaultLineStripRenderer implements LineStripRenderer {
  private SGL GL = Renderer.get();
  
  public void end() {
    this.GL.glEnd();
  }
  
  public void setAntiAlias(boolean antialias) {
    if (antialias) {
      this.GL.glEnable(2848);
    } else {
      this.GL.glDisable(2848);
    } 
  }
  
  public void setWidth(float width) {
    this.GL.glLineWidth(width);
  }
  
  public void start() {
    this.GL.glBegin(3);
  }
  
  public void vertex(float x, float y) {
    this.GL.glVertex2f(x, y);
  }
  
  public void color(float r, float g, float b, float a) {
    this.GL.glColor4f(r, g, b, a);
  }
  
  public void setLineCaps(boolean caps) {}
  
  public boolean applyGLLineFixes() {
    return true;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\opengl\renderer\DefaultLineStripRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */