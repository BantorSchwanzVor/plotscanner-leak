package marraylist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;

public class McRenderer implements MRenderer {
  public void drawHorizontalLine(int xFrom, int y, int xTo, int color) {
    Gui.drawRect(xFrom, y, xTo, y + 1, color);
  }
  
  public void drawRect(int xFrom, int yFrom, int xTo, int yTo, int color) {
    Gui.drawRect(xFrom, yFrom, xTo, yTo, color);
  }
  
  public void drawString(String text, int x, int y, int color, boolean shadow) {
    (Minecraft.getMinecraft()).fontRendererObj.drawString(text, x, y, color, shadow);
  }
  
  public int getStringWidth(String text) {
    return (Minecraft.getMinecraft()).fontRendererObj.getStringWidth(text);
  }
  
  public int getStringHeight(String text) {
    return (Minecraft.getMinecraft()).fontRendererObj.FONT_HEIGHT;
  }
  
  public Rectangle getBounds() {
    ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
    return new Rectangle(0, 0, sr.getScaledWidth(), sr.getScaledHeight());
  }
  
  private void setColor(int color) {
    GL11.glColor4f((
        color >> 24 & 0xFF) / 255.0F, (
        color >> 16 & 0xFF) / 255.0F, (
        color >> 8 & 0xFF) / 255.0F, (
        color & 0xFF) / 255.0F);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\marraylist\McRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */