package marraylist;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import org.lwjgl.util.Rectangle;

public class MArrayList {
  private MRenderer renderer;
  
  private Corner corner;
  
  private Sorting sorting;
  
  private EnumSet<Style> styles;
  
  private int color;
  
  private int state;
  
  public MArrayList(MRenderer renderer, Corner corner, Sorting sorting, EnumSet<Style> styles, int color) {
    this.renderer = renderer;
    this.corner = corner;
    this.sorting = sorting;
    this.styles = styles;
    this.color = color;
  }
  
  public MArrayList(MRenderer renderer) {
    this(renderer, Corner.UPPER_RIGHT, Sorting.WIDTH, EnumSet.noneOf(Style.class), -14003800);
  }
  
  public MArrayList() {
    this(new McRenderer());
  }
  
  public void drawArrayList(List<String> modules) {
    Rectangle bounds = this.renderer.getBounds();
    if (modules.size() != 0) {
      int y;
      modules = sort(modules);
      switch (this.corner) {
        case UPPER_LEFT:
        case UPPER_RIGHT:
          y = 1;
          break;
        case null:
        case LOWER_RIGHT:
          y = bounds.getHeight() - this.renderer.getStringHeight(modules.get(0));
          break;
        default:
          throw new NullPointerException("corner");
      } 
      this.renderer.drawRect(bounds.getWidth() - 4, bounds.getHeight() - 4, bounds.getWidth() - 2, bounds.getHeight() - 2, -65536);
      this.renderer.drawRect(bounds.getWidth() - 4, bounds.getHeight() - 2, bounds.getWidth() - 2, bounds.getHeight(), -16711936);
      this.renderer.drawRect(bounds.getWidth() - 2, bounds.getHeight() - 4, bounds.getWidth(), bounds.getHeight() - 2, -16776961);
      this.renderer.drawRect(bounds.getWidth() - 2, bounds.getHeight() - 2, bounds.getWidth(), bounds.getHeight(), -256);
      int dif = 10 / modules.size();
      this.state = (this.state + dif) % 255;
      int state1 = this.state;
      for (String module : modules) {
        int x, x1, color, width = this.renderer.getStringWidth(module);
        int height = this.renderer.getStringHeight(module);
        switch (this.corner) {
          case UPPER_LEFT:
          case null:
            x = x1 = 1;
            break;
          case UPPER_RIGHT:
          case LOWER_RIGHT:
            x1 = bounds.getWidth();
            x = x1 - width - 1;
            break;
          default:
            throw new NullPointerException("corner");
        } 
        if (this.styles.contains(Style.OUTER_LINE))
          switch (this.corner) {
            case UPPER_LEFT:
            case null:
              x += 3;
              break;
            case UPPER_RIGHT:
            case LOWER_RIGHT:
              x -= 3;
              break;
          }  
        if (this.styles.contains(Style.RAINBOW)) {
          color = rainbow(this.state);
        } else if (this.styles.contains(Style.RAINBOW_FLOW)) {
          state1 = (state1 + dif * 20) % 255;
          color = rainbow(state1);
        } else {
          color = this.color;
        } 
        this.renderer.drawString(module, x, y, color, this.styles.contains(Style.SHADOWED));
        if (this.styles.contains(Style.OUTER_LINE)) {
          int x2 = x1;
          switch (this.corner) {
            case UPPER_LEFT:
            case null:
              x2 += 3;
              break;
            case UPPER_RIGHT:
            case LOWER_RIGHT:
              x2 -= 3;
              break;
          } 
          this.renderer.drawRect(x2, y - 1, x1, y + height, color);
        } 
        if (this.styles.contains(Style.INNER_LINE)) {
          int x2 = x, x3 = x;
          switch (this.corner) {
            case UPPER_LEFT:
            case null:
              x2 += 4;
              x3++;
              break;
            case UPPER_RIGHT:
            case LOWER_RIGHT:
              x2 -= 4;
              x3--;
              break;
          } 
          this.renderer.drawRect(x3, y - 1, x2, y + height, color);
        } 
        if (this.styles.contains(Style.LINES)) {
          int y1 = y;
          switch (this.corner) {
            case UPPER_LEFT:
            case UPPER_RIGHT:
              y1 += height - 1;
              break;
            case null:
            case LOWER_RIGHT:
              y1 -= height - 1;
              break;
          } 
          this.renderer.drawHorizontalLine(x - 1, y1, x + width + 1, color);
        } 
        switch (this.corner) {
          case UPPER_LEFT:
          case UPPER_RIGHT:
            y += height + 1;
            continue;
          case null:
          case LOWER_RIGHT:
            y -= height + 1;
            continue;
        } 
        throw new NullPointerException("corner");
      } 
    } 
  }
  
  private List<String> sort(List<String> src) {
    List<String> dst = new ArrayList<>(src);
    switch (this.sorting) {
      case WIDTH:
        dst.sort(Comparator.comparingInt(s -> -this.renderer.getStringWidth(s)));
        break;
      case null:
        dst.sort(String.CASE_INSENSITIVE_ORDER);
        break;
      case RANDOM:
        Collections.shuffle(dst);
        break;
    } 
    return dst;
  }
  
  private int rainbow(int state) {
    return Color.HSBtoRGB(state / 255.0F, 1.0F, 1.0F);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\marraylist\MArrayList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */