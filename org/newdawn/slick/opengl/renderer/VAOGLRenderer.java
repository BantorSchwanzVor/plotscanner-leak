package org.newdawn.slick.opengl.renderer;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class VAOGLRenderer extends ImmediateModeOGLRenderer {
  private static final int TOLERANCE = 20;
  
  public static final int NONE = -1;
  
  public static final int MAX_VERTS = 5000;
  
  private int currentType = -1;
  
  private float[] color = new float[] { 1.0F, 1.0F, 1.0F, 1.0F };
  
  private float[] tex = new float[] { 0.0F, 0.0F };
  
  private int vertIndex;
  
  private float[] verts = new float[15000];
  
  private float[] cols = new float[20000];
  
  private float[] texs = new float[15000];
  
  private FloatBuffer vertices = BufferUtils.createFloatBuffer(15000);
  
  private FloatBuffer colors = BufferUtils.createFloatBuffer(20000);
  
  private FloatBuffer textures = BufferUtils.createFloatBuffer(10000);
  
  private int listMode = 0;
  
  public void initDisplay(int width, int height) {
    super.initDisplay(width, height);
    startBuffer();
    GL11.glEnableClientState(32884);
    GL11.glEnableClientState(32888);
    GL11.glEnableClientState(32886);
  }
  
  private void startBuffer() {
    this.vertIndex = 0;
  }
  
  private void flushBuffer() {
    if (this.vertIndex == 0)
      return; 
    if (this.currentType == -1)
      return; 
    if (this.vertIndex < 20) {
      GL11.glBegin(this.currentType);
      for (int i = 0; i < this.vertIndex; i++) {
        GL11.glColor4f(this.cols[i * 4 + 0], this.cols[i * 4 + 1], this.cols[i * 4 + 2], this.cols[i * 4 + 3]);
        GL11.glTexCoord2f(this.texs[i * 2 + 0], this.texs[i * 2 + 1]);
        GL11.glVertex3f(this.verts[i * 3 + 0], this.verts[i * 3 + 1], this.verts[i * 3 + 2]);
      } 
      GL11.glEnd();
      this.currentType = -1;
      return;
    } 
    this.vertices.clear();
    this.colors.clear();
    this.textures.clear();
    this.vertices.put(this.verts, 0, this.vertIndex * 3);
    this.colors.put(this.cols, 0, this.vertIndex * 4);
    this.textures.put(this.texs, 0, this.vertIndex * 2);
    this.vertices.flip();
    this.colors.flip();
    this.textures.flip();
    GL11.glVertexPointer(3, 0, this.vertices);
    GL11.glColorPointer(4, 0, this.colors);
    GL11.glTexCoordPointer(2, 0, this.textures);
    GL11.glDrawArrays(this.currentType, 0, this.vertIndex);
    this.currentType = -1;
  }
  
  private void applyBuffer() {
    if (this.listMode > 0)
      return; 
    if (this.vertIndex != 0) {
      flushBuffer();
      startBuffer();
    } 
    super.glColor4f(this.color[0], this.color[1], this.color[2], this.color[3]);
  }
  
  public void flush() {
    super.flush();
    applyBuffer();
  }
  
  public void glBegin(int geomType) {
    if (this.listMode > 0) {
      super.glBegin(geomType);
      return;
    } 
    if (this.currentType != geomType) {
      applyBuffer();
      this.currentType = geomType;
    } 
  }
  
  public void glColor4f(float r, float g, float b, float a) {
    a *= this.alphaScale;
    this.color[0] = r;
    this.color[1] = g;
    this.color[2] = b;
    this.color[3] = a;
    if (this.listMode > 0) {
      super.glColor4f(r, g, b, a);
      return;
    } 
  }
  
  public void glEnd() {
    if (this.listMode > 0) {
      super.glEnd();
      return;
    } 
  }
  
  public void glTexCoord2f(float u, float v) {
    if (this.listMode > 0) {
      super.glTexCoord2f(u, v);
      return;
    } 
    this.tex[0] = u;
    this.tex[1] = v;
  }
  
  public void glVertex2f(float x, float y) {
    if (this.listMode > 0) {
      super.glVertex2f(x, y);
      return;
    } 
    glVertex3f(x, y, 0.0F);
  }
  
  public void glVertex3f(float x, float y, float z) {
    if (this.listMode > 0) {
      super.glVertex3f(x, y, z);
      return;
    } 
    this.verts[this.vertIndex * 3 + 0] = x;
    this.verts[this.vertIndex * 3 + 1] = y;
    this.verts[this.vertIndex * 3 + 2] = z;
    this.cols[this.vertIndex * 4 + 0] = this.color[0];
    this.cols[this.vertIndex * 4 + 1] = this.color[1];
    this.cols[this.vertIndex * 4 + 2] = this.color[2];
    this.cols[this.vertIndex * 4 + 3] = this.color[3];
    this.texs[this.vertIndex * 2 + 0] = this.tex[0];
    this.texs[this.vertIndex * 2 + 1] = this.tex[1];
    this.vertIndex++;
    if (this.vertIndex > 4950 && 
      isSplittable(this.vertIndex, this.currentType)) {
      int type = this.currentType;
      applyBuffer();
      this.currentType = type;
    } 
  }
  
  private boolean isSplittable(int count, int type) {
    switch (type) {
      case 7:
        return (count % 4 == 0);
      case 4:
        return (count % 3 == 0);
      case 6913:
        return (count % 2 == 0);
    } 
    return false;
  }
  
  public void glBindTexture(int target, int id) {
    applyBuffer();
    super.glBindTexture(target, id);
  }
  
  public void glBlendFunc(int src, int dest) {
    applyBuffer();
    super.glBlendFunc(src, dest);
  }
  
  public void glCallList(int id) {
    applyBuffer();
    super.glCallList(id);
  }
  
  public void glClear(int value) {
    applyBuffer();
    super.glClear(value);
  }
  
  public void glClipPlane(int plane, DoubleBuffer buffer) {
    applyBuffer();
    super.glClipPlane(plane, buffer);
  }
  
  public void glColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
    applyBuffer();
    super.glColorMask(red, green, blue, alpha);
  }
  
  public void glDisable(int item) {
    applyBuffer();
    super.glDisable(item);
  }
  
  public void glEnable(int item) {
    applyBuffer();
    super.glEnable(item);
  }
  
  public void glLineWidth(float width) {
    applyBuffer();
    super.glLineWidth(width);
  }
  
  public void glPointSize(float size) {
    applyBuffer();
    super.glPointSize(size);
  }
  
  public void glPopMatrix() {
    applyBuffer();
    super.glPopMatrix();
  }
  
  public void glPushMatrix() {
    applyBuffer();
    super.glPushMatrix();
  }
  
  public void glRotatef(float angle, float x, float y, float z) {
    applyBuffer();
    super.glRotatef(angle, x, y, z);
  }
  
  public void glScalef(float x, float y, float z) {
    applyBuffer();
    super.glScalef(x, y, z);
  }
  
  public void glScissor(int x, int y, int width, int height) {
    applyBuffer();
    super.glScissor(x, y, width, height);
  }
  
  public void glTexEnvi(int target, int mode, int value) {
    applyBuffer();
    super.glTexEnvi(target, mode, value);
  }
  
  public void glTranslatef(float x, float y, float z) {
    applyBuffer();
    super.glTranslatef(x, y, z);
  }
  
  public void glEndList() {
    this.listMode--;
    super.glEndList();
  }
  
  public void glNewList(int id, int option) {
    this.listMode++;
    super.glNewList(id, option);
  }
  
  public float[] getCurrentColor() {
    return this.color;
  }
  
  public void glLoadMatrix(FloatBuffer buffer) {
    flushBuffer();
    super.glLoadMatrix(buffer);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\opengl\renderer\VAOGLRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */