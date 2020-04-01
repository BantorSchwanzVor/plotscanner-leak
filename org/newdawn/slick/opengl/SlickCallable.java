package org.newdawn.slick.opengl;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.renderer.Renderer;

public abstract class SlickCallable {
  private static Texture lastUsed;
  
  private static boolean inSafe = false;
  
  public static void enterSafeBlock() {
    if (inSafe)
      return; 
    Renderer.get().flush();
    lastUsed = TextureImpl.getLastBind();
    TextureImpl.bindNone();
    GL11.glPushAttrib(1048575);
    GL11.glPushClientAttrib(-1);
    GL11.glMatrixMode(5888);
    GL11.glPushMatrix();
    GL11.glMatrixMode(5889);
    GL11.glPushMatrix();
    GL11.glMatrixMode(5888);
    inSafe = true;
  }
  
  public static void leaveSafeBlock() {
    if (!inSafe)
      return; 
    GL11.glMatrixMode(5889);
    GL11.glPopMatrix();
    GL11.glMatrixMode(5888);
    GL11.glPopMatrix();
    GL11.glPopClientAttrib();
    GL11.glPopAttrib();
    if (lastUsed != null) {
      lastUsed.bind();
    } else {
      TextureImpl.bindNone();
    } 
    inSafe = false;
  }
  
  public final void call() throws SlickException {
    enterSafeBlock();
    performGLOperations();
    leaveSafeBlock();
  }
  
  protected abstract void performGLOperations() throws SlickException;
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\opengl\SlickCallable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */