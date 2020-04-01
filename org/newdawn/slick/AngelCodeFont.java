package org.newdawn.slick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;

public class AngelCodeFont implements Font {
  private static SGL GL = Renderer.get();
  
  private static final int DISPLAY_LIST_CACHE_SIZE = 200;
  
  private static final int MAX_CHAR = 255;
  
  private boolean displayListCaching = true;
  
  private Image fontImage;
  
  private CharDef[] chars;
  
  private int lineHeight;
  
  private int baseDisplayListID = -1;
  
  private int eldestDisplayListID;
  
  private DisplayList eldestDisplayList;
  
  private final LinkedHashMap displayLists = new LinkedHashMap<Object, Object>(200, 1.0F, true) {
      protected boolean removeEldestEntry(Map.Entry eldest) {
        AngelCodeFont.this.eldestDisplayList = (AngelCodeFont.DisplayList)eldest.getValue();
        AngelCodeFont.this.eldestDisplayListID = AngelCodeFont.this.eldestDisplayList.id;
        return false;
      }
    };
  
  public AngelCodeFont(String fntFile, Image image) throws SlickException {
    this.fontImage = image;
    parseFnt(ResourceLoader.getResourceAsStream(fntFile));
  }
  
  public AngelCodeFont(String fntFile, String imgFile) throws SlickException {
    this.fontImage = new Image(imgFile);
    parseFnt(ResourceLoader.getResourceAsStream(fntFile));
  }
  
  public AngelCodeFont(String fntFile, Image image, boolean caching) throws SlickException {
    this.fontImage = image;
    this.displayListCaching = caching;
    parseFnt(ResourceLoader.getResourceAsStream(fntFile));
  }
  
  public AngelCodeFont(String fntFile, String imgFile, boolean caching) throws SlickException {
    this.fontImage = new Image(imgFile);
    this.displayListCaching = caching;
    parseFnt(ResourceLoader.getResourceAsStream(fntFile));
  }
  
  public AngelCodeFont(String name, InputStream fntFile, InputStream imgFile) throws SlickException {
    this.fontImage = new Image(imgFile, name, false);
    parseFnt(fntFile);
  }
  
  public AngelCodeFont(String name, InputStream fntFile, InputStream imgFile, boolean caching) throws SlickException {
    this.fontImage = new Image(imgFile, name, false);
    this.displayListCaching = caching;
    parseFnt(fntFile);
  }
  
  private void parseFnt(InputStream fntFile) throws SlickException {
    if (this.displayListCaching) {
      this.baseDisplayListID = GL.glGenLists(200);
      if (this.baseDisplayListID == 0)
        this.displayListCaching = false; 
    } 
    try {
      BufferedReader in = new BufferedReader(new InputStreamReader(
            fntFile));
      String info = in.readLine();
      String common = in.readLine();
      String page = in.readLine();
      Map<Object, Object> kerning = new HashMap<>(64);
      List<CharDef> charDefs = new ArrayList(255);
      int maxChar = 0;
      boolean done = false;
      while (!done) {
        String line = in.readLine();
        if (line == null) {
          done = true;
          continue;
        } 
        if (!line.startsWith("chars c"))
          if (line.startsWith("char")) {
            CharDef def = parseChar(line);
            if (def != null) {
              maxChar = Math.max(maxChar, def.id);
              charDefs.add(def);
            } 
          }  
        if (!line.startsWith("kernings c"))
          if (line.startsWith("kerning")) {
            StringTokenizer tokens = new StringTokenizer(line, " =");
            tokens.nextToken();
            tokens.nextToken();
            short first = Short.parseShort(tokens.nextToken());
            tokens.nextToken();
            int second = Integer.parseInt(tokens.nextToken());
            tokens.nextToken();
            int offset = Integer.parseInt(tokens.nextToken());
            List<Short> values = (List)kerning.get(new Short(first));
            if (values == null) {
              values = new ArrayList();
              kerning.put(new Short(first), values);
            } 
            values.add(new Short((short)(offset << 8 | second)));
          }  
      } 
      this.chars = new CharDef[maxChar + 1];
      for (Iterator<CharDef> iterator = charDefs.iterator(); iterator.hasNext(); ) {
        CharDef def = iterator.next();
        this.chars[def.id] = def;
      } 
      for (Iterator<Map.Entry> iter = kerning.entrySet().iterator(); iter.hasNext(); ) {
        Map.Entry entry = iter.next();
        short first = ((Short)entry.getKey()).shortValue();
        List valueList = (List)entry.getValue();
        short[] valueArray = new short[valueList.size()];
        int i = 0;
        for (Iterator valueIter = valueList.iterator(); valueIter.hasNext(); i++)
          valueArray[i] = ((Short)valueIter.next()).shortValue(); 
        (this.chars[first]).kerning = valueArray;
      } 
    } catch (IOException e) {
      Log.error(e);
      throw new SlickException("Failed to parse font file: " + fntFile);
    } 
  }
  
  private CharDef parseChar(String line) throws SlickException {
    CharDef def = new CharDef(null);
    StringTokenizer tokens = new StringTokenizer(line, " =");
    tokens.nextToken();
    tokens.nextToken();
    def.id = Short.parseShort(tokens.nextToken());
    if (def.id < 0)
      return null; 
    if (def.id > 255)
      throw new SlickException("Invalid character '" + def.id + 
          "': AngelCodeFont does not support characters above " + 'ÿ'); 
    tokens.nextToken();
    def.x = Short.parseShort(tokens.nextToken());
    tokens.nextToken();
    def.y = Short.parseShort(tokens.nextToken());
    tokens.nextToken();
    def.width = Short.parseShort(tokens.nextToken());
    tokens.nextToken();
    def.height = Short.parseShort(tokens.nextToken());
    tokens.nextToken();
    def.xoffset = Short.parseShort(tokens.nextToken());
    tokens.nextToken();
    def.yoffset = Short.parseShort(tokens.nextToken());
    tokens.nextToken();
    def.xadvance = Short.parseShort(tokens.nextToken());
    def.init();
    if (def.id != 32)
      this.lineHeight = Math.max(def.height + def.yoffset, this.lineHeight); 
    return def;
  }
  
  public void drawString(float x, float y, String text) {
    drawString(x, y, text, Color.white);
  }
  
  public void drawString(float x, float y, String text, Color col) {
    drawString(x, y, text, col, 0, text.length() - 1);
  }
  
  public void drawString(float x, float y, String text, Color col, int startIndex, int endIndex) {
    this.fontImage.bind();
    col.bind();
    GL.glTranslatef(x, y, 0.0F);
    if (this.displayListCaching && startIndex == 0 && endIndex == text.length() - 1) {
      DisplayList displayList = (DisplayList)this.displayLists.get(text);
      if (displayList != null) {
        GL.glCallList(displayList.id);
      } else {
        displayList = new DisplayList(null);
        displayList.text = text;
        int displayListCount = this.displayLists.size();
        if (displayListCount < 200) {
          displayList.id = this.baseDisplayListID + displayListCount;
        } else {
          displayList.id = this.eldestDisplayListID;
          this.displayLists.remove(this.eldestDisplayList.text);
        } 
        this.displayLists.put(text, displayList);
        GL.glNewList(displayList.id, 4865);
        render(text, startIndex, endIndex);
        GL.glEndList();
      } 
    } else {
      render(text, startIndex, endIndex);
    } 
    GL.glTranslatef(-x, -y, 0.0F);
  }
  
  private void render(String text, int start, int end) {
    GL.glBegin(7);
    int x = 0, y = 0;
    CharDef lastCharDef = null;
    char[] data = text.toCharArray();
    for (int i = 0; i < data.length; i++) {
      int id = data[i];
      if (id == 10) {
        x = 0;
        y += getLineHeight();
      } else if (id < this.chars.length) {
        CharDef charDef = this.chars[id];
        if (charDef != null) {
          if (lastCharDef != null)
            x += lastCharDef.getKerning(id); 
          lastCharDef = charDef;
          if (i >= start && i <= end)
            charDef.draw(x, y); 
          x += charDef.xadvance;
        } 
      } 
    } 
    GL.glEnd();
  }
  
  public int getYOffset(String text) {
    DisplayList displayList = null;
    if (this.displayListCaching) {
      displayList = (DisplayList)this.displayLists.get(text);
      if (displayList != null && displayList.yOffset != null)
        return displayList.yOffset.intValue(); 
    } 
    int stopIndex = text.indexOf('\n');
    if (stopIndex == -1)
      stopIndex = text.length(); 
    int minYOffset = 10000;
    for (int i = 0; i < stopIndex; i++) {
      int id = text.charAt(i);
      CharDef charDef = this.chars[id];
      if (charDef != null)
        minYOffset = Math.min(charDef.yoffset, minYOffset); 
    } 
    if (displayList != null)
      displayList.yOffset = new Short((short)minYOffset); 
    return minYOffset;
  }
  
  public int getHeight(String text) {
    DisplayList displayList = null;
    if (this.displayListCaching) {
      displayList = (DisplayList)this.displayLists.get(text);
      if (displayList != null && displayList.height != null)
        return displayList.height.intValue(); 
    } 
    int lines = 0;
    int maxHeight = 0;
    for (int i = 0; i < text.length(); i++) {
      int id = text.charAt(i);
      if (id == 10) {
        lines++;
        maxHeight = 0;
      } else if (id != 32) {
        CharDef charDef = this.chars[id];
        if (charDef != null)
          maxHeight = Math.max(charDef.height + charDef.yoffset, 
              maxHeight); 
      } 
    } 
    maxHeight += lines * getLineHeight();
    if (displayList != null)
      displayList.height = new Short((short)maxHeight); 
    return maxHeight;
  }
  
  public int getWidth(String text) {
    DisplayList displayList = null;
    if (this.displayListCaching) {
      displayList = (DisplayList)this.displayLists.get(text);
      if (displayList != null && displayList.width != null)
        return displayList.width.intValue(); 
    } 
    int maxWidth = 0;
    int width = 0;
    CharDef lastCharDef = null;
    for (int i = 0, n = text.length(); i < n; i++) {
      int id = text.charAt(i);
      if (id == 10) {
        width = 0;
      } else if (id < this.chars.length) {
        CharDef charDef = this.chars[id];
        if (charDef != null) {
          if (lastCharDef != null)
            width += lastCharDef.getKerning(id); 
          lastCharDef = charDef;
          if (i < n - 1) {
            width += charDef.xadvance;
          } else {
            width += charDef.width;
          } 
          maxWidth = Math.max(maxWidth, width);
        } 
      } 
    } 
    if (displayList != null)
      displayList.width = new Short((short)maxWidth); 
    return maxWidth;
  }
  
  private class CharDef {
    public short id;
    
    public short x;
    
    public short y;
    
    public short width;
    
    public short height;
    
    public short xoffset;
    
    public short yoffset;
    
    public short xadvance;
    
    public Image image;
    
    public short dlIndex;
    
    public short[] kerning;
    
    private CharDef() {}
    
    public void init() {
      this.image = AngelCodeFont.this.fontImage.getSubImage(this.x, this.y, this.width, this.height);
    }
    
    public String toString() {
      return "[CharDef id=" + this.id + " x=" + this.x + " y=" + this.y + "]";
    }
    
    public void draw(float x, float y) {
      this.image.drawEmbedded(x + this.xoffset, y + this.yoffset, this.width, this.height);
    }
    
    public int getKerning(int otherCodePoint) {
      if (this.kerning == null)
        return 0; 
      int low = 0;
      int high = this.kerning.length - 1;
      while (low <= high) {
        int midIndex = low + high >>> 1;
        int value = this.kerning[midIndex];
        int foundCodePoint = value & 0xFF;
        if (foundCodePoint < otherCodePoint) {
          low = midIndex + 1;
          continue;
        } 
        if (foundCodePoint > otherCodePoint) {
          high = midIndex - 1;
          continue;
        } 
        return value >> 8;
      } 
      return 0;
    }
  }
  
  public int getLineHeight() {
    return this.lineHeight;
  }
  
  private static class DisplayList {
    int id;
    
    Short yOffset;
    
    Short width;
    
    Short height;
    
    String text;
    
    private DisplayList() {}
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\AngelCodeFont.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */