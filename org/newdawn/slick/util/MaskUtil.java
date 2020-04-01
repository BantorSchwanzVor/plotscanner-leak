package org.newdawn.slick.util;

import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;

public class MaskUtil {
  protected static SGL GL = Renderer.get();
  
  public static void defineMask() {
    GL.glDepthMask(true);
    GL.glClearDepth(1.0F);
    GL.glClear(256);
    GL.glDepthFunc(519);
    GL.glEnable(2929);
    GL.glDepthMask(true);
    GL.glColorMask(false, false, false, false);
  }
  
  public static void finishDefineMask() {
    GL.glDepthMask(false);
    GL.glColorMask(true, true, true, true);
  }
  
  public static void drawOnMask() {
    GL.glDepthFunc(514);
  }
  
  public static void drawOffMask() {
    GL.glDepthFunc(517);
  }
  
  public static void resetMask() {
    GL.glDepthMask(true);
    GL.glClearDepth(0.0F);
    GL.glClear(256);
    GL.glDepthMask(false);
    GL.glDisable(2929);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slic\\util\MaskUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */