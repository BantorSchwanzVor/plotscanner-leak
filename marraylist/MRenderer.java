package marraylist;

import org.lwjgl.util.Rectangle;

public interface MRenderer {
  void drawHorizontalLine(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  void drawRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
  
  void drawString(String paramString, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean);
  
  int getStringWidth(String paramString);
  
  int getStringHeight(String paramString);
  
  Rectangle getBounds();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\marraylist\MRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */