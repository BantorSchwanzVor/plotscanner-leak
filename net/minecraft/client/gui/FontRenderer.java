package net.minecraft.client.gui;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import optifine.CustomColors;
import optifine.FontUtils;
import optifine.GlBlendState;
import org.apache.commons.io.IOUtils;
import org.seltak.anubis.Anubis;
import org.seltak.anubis.module.Module;

public class FontRenderer implements IResourceManagerReloadListener {
  private static final ResourceLocation[] UNICODE_PAGE_LOCATIONS = new ResourceLocation[256];
  
  private final int[] charWidth = new int[256];
  
  public int FONT_HEIGHT = 9;
  
  public Random fontRandom = new Random();
  
  private final byte[] glyphWidth = new byte[65536];
  
  private final int[] colorCode = new int[32];
  
  private ResourceLocation locationFontTexture;
  
  private final TextureManager renderEngine;
  
  private float posX;
  
  private float posY;
  
  private boolean unicodeFlag;
  
  private boolean bidiFlag;
  
  private float red;
  
  private float blue;
  
  private float green;
  
  private float alpha;
  
  private int textColor;
  
  private boolean randomStyle;
  
  private boolean boldStyle;
  
  private boolean italicStyle;
  
  private boolean underlineStyle;
  
  private boolean strikethroughStyle;
  
  public GameSettings gameSettings;
  
  public ResourceLocation locationFontTextureBase;
  
  public boolean enabled = true;
  
  public float offsetBold = 1.0F;
  
  private float[] charWidthFloat = new float[256];
  
  private boolean blend = false;
  
  private GlBlendState oldBlendState = new GlBlendState();
  
  public FontRenderer(GameSettings gameSettingsIn, ResourceLocation location, TextureManager textureManagerIn, boolean unicode) {
    this.gameSettings = gameSettingsIn;
    this.locationFontTextureBase = location;
    this.locationFontTexture = location;
    this.renderEngine = textureManagerIn;
    this.unicodeFlag = unicode;
    this.locationFontTexture = FontUtils.getHdFontLocation(this.locationFontTextureBase);
    bindTexture(this.locationFontTexture);
    for (int i = 0; i < 32; i++) {
      int j = (i >> 3 & 0x1) * 85;
      int k = (i >> 2 & 0x1) * 170 + j;
      int l = (i >> 1 & 0x1) * 170 + j;
      int i1 = (i >> 0 & 0x1) * 170 + j;
      if (i == 6)
        k += 85; 
      if (gameSettingsIn.anaglyph) {
        int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
        int k1 = (k * 30 + l * 70) / 100;
        int l1 = (k * 30 + i1 * 70) / 100;
        k = j1;
        l = k1;
        i1 = l1;
      } 
      if (i >= 16) {
        k /= 4;
        l /= 4;
        i1 /= 4;
      } 
      this.colorCode[i] = (k & 0xFF) << 16 | (l & 0xFF) << 8 | i1 & 0xFF;
    } 
    readGlyphSizes();
  }
  
  public void onResourceManagerReload(IResourceManager resourceManager) {
    this.locationFontTexture = FontUtils.getHdFontLocation(this.locationFontTextureBase);
    for (int i = 0; i < UNICODE_PAGE_LOCATIONS.length; i++)
      UNICODE_PAGE_LOCATIONS[i] = null; 
    readFontTexture();
    readGlyphSizes();
  }
  
  private void readFontTexture() {
    BufferedImage bufferedimage;
    IResource iresource = null;
    try {
      iresource = getResource(this.locationFontTexture);
      bufferedimage = TextureUtil.readBufferedImage(iresource.getInputStream());
    } catch (IOException ioexception) {
      throw new RuntimeException(ioexception);
    } finally {
      IOUtils.closeQuietly((Closeable)iresource);
    } 
    Properties props = FontUtils.readFontProperties(this.locationFontTexture);
    this.blend = FontUtils.readBoolean(props, "blend", false);
    int imgWidth = bufferedimage.getWidth();
    int imgHeight = bufferedimage.getHeight();
    int charW = imgWidth / 16;
    int charH = imgHeight / 16;
    float kx = imgWidth / 128.0F;
    float boldScaleFactor = Config.limit(kx, 1.0F, 2.0F);
    this.offsetBold = 1.0F / boldScaleFactor;
    float offsetBoldConfig = FontUtils.readFloat(props, "offsetBold", -1.0F);
    if (offsetBoldConfig >= 0.0F)
      this.offsetBold = offsetBoldConfig; 
    int[] aint = new int[imgWidth * imgHeight];
    bufferedimage.getRGB(0, 0, imgWidth, imgHeight, aint, 0, imgWidth);
    for (int i1 = 0; i1 < 256; i1++) {
      int j1 = i1 % 16;
      int k1 = i1 / 16;
      int l1 = 0;
      for (l1 = charW - 1; l1 >= 0; l1--) {
        int i2 = j1 * charW + l1;
        boolean flag = true;
        for (int j2 = 0; j2 < charH && flag; j2++) {
          int k2 = (k1 * charH + j2) * imgWidth;
          int l2 = aint[i2 + k2];
          int i3 = l2 >> 24 & 0xFF;
          if (i3 > 16)
            flag = false; 
        } 
        if (!flag)
          break; 
      } 
      if (i1 == 65)
        i1 = i1; 
      if (i1 == 32)
        if (charW <= 8) {
          l1 = (int)(2.0F * kx);
        } else {
          l1 = (int)(1.5F * kx);
        }  
      this.charWidthFloat[i1] = (l1 + 1) / kx + 1.0F;
    } 
    FontUtils.readCustomCharWidths(props, this.charWidthFloat);
    for (int j3 = 0; j3 < this.charWidth.length; j3++)
      this.charWidth[j3] = Math.round(this.charWidthFloat[j3]); 
  }
  
  private void readGlyphSizes() {
    IResource iresource = null;
    try {
      iresource = getResource(new ResourceLocation("font/glyph_sizes.bin"));
      iresource.getInputStream().read(this.glyphWidth);
    } catch (IOException ioexception) {
      throw new RuntimeException(ioexception);
    } finally {
      IOUtils.closeQuietly((Closeable)iresource);
    } 
  }
  
  private float renderChar(char ch, boolean italic) {
    if (ch != ' ' && ch != ' ') {
      int i = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\000\000\000\000\000\000\000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\000".indexOf(ch);
      return (i != -1 && !this.unicodeFlag) ? renderDefaultChar(i, italic) : renderUnicodeChar(ch, italic);
    } 
    return !this.unicodeFlag ? this.charWidthFloat[ch] : 4.0F;
  }
  
  private float renderDefaultChar(int ch, boolean italic) {
    int i = ch % 16 * 8;
    int j = ch / 16 * 8;
    int k = italic ? 1 : 0;
    bindTexture(this.locationFontTexture);
    float f = this.charWidthFloat[ch];
    float f1 = 7.99F;
    GlStateManager.glBegin(5);
    GlStateManager.glTexCoord2f(i / 128.0F, j / 128.0F);
    GlStateManager.glVertex3f(this.posX + k, this.posY, 0.0F);
    GlStateManager.glTexCoord2f(i / 128.0F, (j + 7.99F) / 128.0F);
    GlStateManager.glVertex3f(this.posX - k, this.posY + 7.99F, 0.0F);
    GlStateManager.glTexCoord2f((i + f1 - 1.0F) / 128.0F, j / 128.0F);
    GlStateManager.glVertex3f(this.posX + f1 - 1.0F + k, this.posY, 0.0F);
    GlStateManager.glTexCoord2f((i + f1 - 1.0F) / 128.0F, (j + 7.99F) / 128.0F);
    GlStateManager.glVertex3f(this.posX + f1 - 1.0F - k, this.posY + 7.99F, 0.0F);
    GlStateManager.glEnd();
    return f;
  }
  
  private ResourceLocation getUnicodePageLocation(int page) {
    if (UNICODE_PAGE_LOCATIONS[page] == null) {
      UNICODE_PAGE_LOCATIONS[page] = new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", new Object[] { Integer.valueOf(page) }));
      UNICODE_PAGE_LOCATIONS[page] = FontUtils.getHdFontLocation(UNICODE_PAGE_LOCATIONS[page]);
    } 
    return UNICODE_PAGE_LOCATIONS[page];
  }
  
  private void loadGlyphTexture(int page) {
    bindTexture(getUnicodePageLocation(page));
  }
  
  private float renderUnicodeChar(char ch, boolean italic) {
    int i = this.glyphWidth[ch] & 0xFF;
    if (i == 0)
      return 0.0F; 
    int j = ch / 256;
    loadGlyphTexture(j);
    int k = i >>> 4;
    int l = i & 0xF;
    float f = k;
    float f1 = (l + 1);
    float f2 = (ch % 16 * 16) + f;
    float f3 = ((ch & 0xFF) / 16 * 16);
    float f4 = f1 - f - 0.02F;
    float f5 = italic ? 1.0F : 0.0F;
    GlStateManager.glBegin(5);
    GlStateManager.glTexCoord2f(f2 / 256.0F, f3 / 256.0F);
    GlStateManager.glVertex3f(this.posX + f5, this.posY, 0.0F);
    GlStateManager.glTexCoord2f(f2 / 256.0F, (f3 + 15.98F) / 256.0F);
    GlStateManager.glVertex3f(this.posX - f5, this.posY + 7.99F, 0.0F);
    GlStateManager.glTexCoord2f((f2 + f4) / 256.0F, f3 / 256.0F);
    GlStateManager.glVertex3f(this.posX + f4 / 2.0F + f5, this.posY, 0.0F);
    GlStateManager.glTexCoord2f((f2 + f4) / 256.0F, (f3 + 15.98F) / 256.0F);
    GlStateManager.glVertex3f(this.posX + f4 / 2.0F - f5, this.posY + 7.99F, 0.0F);
    GlStateManager.glEnd();
    return (f1 - f) / 2.0F + 1.0F;
  }
  
  public int drawStringWithShadow(String text, float x, float y, int color) {
    return drawString(text, x, y, color, true);
  }
  
  public int drawString(String text, int x, int y, int color) {
    return !this.enabled ? 0 : drawString(text, x, y, color, false);
  }
  
  public int drawString(String text, float x, float y, int color, boolean dropShadow) {
    int i;
    enableAlpha();
    if (this.blend) {
      GlStateManager.getBlendState(this.oldBlendState);
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    } 
    resetStyles();
    if (dropShadow) {
      i = renderString(text, x + 1.0F, y + 1.0F, color, true);
      i = Math.max(i, renderString(text, x, y, color, false));
    } else {
      i = renderString(text, x, y, color, false);
    } 
    if (this.blend)
      GlStateManager.setBlendState(this.oldBlendState); 
    return i;
  }
  
  private String bidiReorder(String text) {
    try {
      Bidi bidi = new Bidi((new ArabicShaping(8)).shape(text), 127);
      bidi.setReorderingMode(0);
      return bidi.writeReordered(2);
    } catch (ArabicShapingException var31) {
      return text;
    } 
  }
  
  private void resetStyles() {
    this.randomStyle = false;
    this.boldStyle = false;
    this.italicStyle = false;
    this.underlineStyle = false;
    this.strikethroughStyle = false;
  }
  
  private void renderStringAtPos(String text, boolean shadow) {
    for (int i = 0; i < text.length(); i++) {
      char c0 = text.charAt(i);
      if (c0 == '§' && i + 1 < text.length()) {
        int l = "0123456789abcdefklmnor".indexOf(String.valueOf(text.charAt(i + 1)).toLowerCase(Locale.ROOT).charAt(0));
        if (l < 16) {
          this.randomStyle = false;
          this.boldStyle = false;
          this.strikethroughStyle = false;
          this.underlineStyle = false;
          this.italicStyle = false;
          if (l < 0 || l > 15)
            l = 15; 
          if (shadow)
            l += 16; 
          int i1 = this.colorCode[l];
          if (Config.isCustomColors())
            i1 = CustomColors.getTextColor(l, i1); 
          this.textColor = i1;
          setColor((i1 >> 16) / 255.0F, (i1 >> 8 & 0xFF) / 255.0F, (i1 & 0xFF) / 255.0F, this.alpha);
        } else if (l == 16) {
          this.randomStyle = true;
        } else if (l == 17) {
          this.boldStyle = true;
        } else if (l == 18) {
          this.strikethroughStyle = true;
        } else if (l == 19) {
          this.underlineStyle = true;
        } else if (l == 20) {
          this.italicStyle = true;
        } else if (l == 21) {
          this.randomStyle = false;
          this.boldStyle = false;
          this.strikethroughStyle = false;
          this.underlineStyle = false;
          this.italicStyle = false;
          setColor(this.red, this.blue, this.green, this.alpha);
        } 
        i++;
      } else {
        int j = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\000\000\000\000\000\000\000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\000".indexOf(c0);
        if (this.randomStyle && j != -1) {
          char c1;
          int k = getCharWidth(c0);
          do {
            j = this.fontRandom.nextInt("ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\000\000\000\000\000\000\000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\000".length());
            c1 = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\000\000\000\000\000\000\000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\000".charAt(j);
          } while (k != getCharWidth(c1));
          c0 = c1;
        } 
        float f1 = (j != -1 && !this.unicodeFlag) ? this.offsetBold : 0.5F;
        boolean flag = ((c0 == '\000' || j == -1 || this.unicodeFlag) && shadow);
        if (flag) {
          this.posX -= f1;
          this.posY -= f1;
        } 
        float f = renderChar(c0, this.italicStyle);
        if (flag) {
          this.posX += f1;
          this.posY += f1;
        } 
        if (this.boldStyle) {
          this.posX += f1;
          if (flag) {
            this.posX -= f1;
            this.posY -= f1;
          } 
          renderChar(c0, this.italicStyle);
          this.posX -= f1;
          if (flag) {
            this.posX += f1;
            this.posY += f1;
          } 
          f += f1;
        } 
        doDraw(f);
      } 
    } 
  }
  
  protected void doDraw(float p_doDraw_1_) {
    if (this.strikethroughStyle) {
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferbuilder = tessellator.getBuffer();
      GlStateManager.disableTexture2D();
      bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
      bufferbuilder.pos(this.posX, (this.posY + (this.FONT_HEIGHT / 2)), 0.0D).endVertex();
      bufferbuilder.pos((this.posX + p_doDraw_1_), (this.posY + (this.FONT_HEIGHT / 2)), 0.0D).endVertex();
      bufferbuilder.pos((this.posX + p_doDraw_1_), (this.posY + (this.FONT_HEIGHT / 2) - 1.0F), 0.0D).endVertex();
      bufferbuilder.pos(this.posX, (this.posY + (this.FONT_HEIGHT / 2) - 1.0F), 0.0D).endVertex();
      tessellator.draw();
      GlStateManager.enableTexture2D();
    } 
    if (this.underlineStyle) {
      Tessellator tessellator1 = Tessellator.getInstance();
      BufferBuilder bufferbuilder1 = tessellator1.getBuffer();
      GlStateManager.disableTexture2D();
      bufferbuilder1.begin(7, DefaultVertexFormats.POSITION);
      int i = this.underlineStyle ? -1 : 0;
      bufferbuilder1.pos((this.posX + i), (this.posY + this.FONT_HEIGHT), 0.0D).endVertex();
      bufferbuilder1.pos((this.posX + p_doDraw_1_), (this.posY + this.FONT_HEIGHT), 0.0D).endVertex();
      bufferbuilder1.pos((this.posX + p_doDraw_1_), (this.posY + this.FONT_HEIGHT - 1.0F), 0.0D).endVertex();
      bufferbuilder1.pos((this.posX + i), (this.posY + this.FONT_HEIGHT - 1.0F), 0.0D).endVertex();
      tessellator1.draw();
      GlStateManager.enableTexture2D();
    } 
    this.posX += p_doDraw_1_;
  }
  
  private int renderStringAligned(String text, int x, int y, int width, int color, boolean dropShadow) {
    if (this.bidiFlag) {
      int i = getStringWidth(bidiReorder(text));
      x = x + width - i;
    } 
    return renderString(text, x, y, color, dropShadow);
  }
  
  private int renderString(String text, float x, float y, int color, boolean dropShadow) {
    try {
      for (Module module : Anubis.moduleManager.getEnabledModules()) {
        if (module.getName().equalsIgnoreCase("FakeName") && 
          text.contains((Minecraft.getMinecraft()).player.getName()))
          text = text.replace((Minecraft.getMinecraft()).player.getName(), "Anubis"); 
      } 
    } catch (NullPointerException nullPointerException) {}
    if (text == null)
      return 0; 
    if (this.bidiFlag)
      text = bidiReorder(text); 
    if ((color & 0xFC000000) == 0)
      color |= 0xFF000000; 
    if (dropShadow)
      color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000; 
    this.red = (color >> 16 & 0xFF) / 255.0F;
    this.blue = (color >> 8 & 0xFF) / 255.0F;
    this.green = (color & 0xFF) / 255.0F;
    this.alpha = (color >> 24 & 0xFF) / 255.0F;
    setColor(this.red, this.blue, this.green, this.alpha);
    this.posX = x;
    this.posY = y;
    renderStringAtPos(text, dropShadow);
    return (int)this.posX;
  }
  
  public int getStringWidth(String text) {
    if (text == null)
      return 0; 
    float f = 0.0F;
    boolean flag = false;
    for (int i = 0; i < text.length(); i++) {
      char c0 = text.charAt(i);
      float f1 = getCharWidthFloat(c0);
      if (f1 < 0.0F && i < text.length() - 1) {
        i++;
        c0 = text.charAt(i);
        if (c0 != 'l' && c0 != 'L') {
          if (c0 == 'r' || c0 == 'R')
            flag = false; 
        } else {
          flag = true;
        } 
        f1 = 0.0F;
      } 
      f += f1;
      if (flag && f1 > 0.0F)
        f += this.unicodeFlag ? 1.0F : this.offsetBold; 
    } 
    return Math.round(f);
  }
  
  public int getCharWidth(char character) {
    return Math.round(getCharWidthFloat(character));
  }
  
  private float getCharWidthFloat(char p_getCharWidthFloat_1_) {
    if (p_getCharWidthFloat_1_ == '§')
      return -1.0F; 
    if (p_getCharWidthFloat_1_ != ' ' && p_getCharWidthFloat_1_ != ' ') {
      int i = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\000\000\000\000\000\000\000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\000".indexOf(p_getCharWidthFloat_1_);
      if (p_getCharWidthFloat_1_ > '\000' && i != -1 && !this.unicodeFlag)
        return this.charWidthFloat[i]; 
      if (this.glyphWidth[p_getCharWidthFloat_1_] != 0) {
        int j = this.glyphWidth[p_getCharWidthFloat_1_] & 0xFF;
        int k = j >>> 4;
        int l = j & 0xF;
        l++;
        return ((l - k) / 2 + 1);
      } 
      return 0.0F;
    } 
    return this.charWidthFloat[32];
  }
  
  public String trimStringToWidth(String text, int width) {
    return trimStringToWidth(text, width, false);
  }
  
  public String trimStringToWidth(String text, int width, boolean reverse) {
    StringBuilder stringbuilder = new StringBuilder();
    float f = 0.0F;
    int i = reverse ? (text.length() - 1) : 0;
    int j = reverse ? -1 : 1;
    boolean flag = false;
    boolean flag1 = false;
    for (int k = i; k >= 0 && k < text.length() && f < width; k += j) {
      char c0 = text.charAt(k);
      float f1 = getCharWidthFloat(c0);
      if (flag) {
        flag = false;
        if (c0 != 'l' && c0 != 'L') {
          if (c0 == 'r' || c0 == 'R')
            flag1 = false; 
        } else {
          flag1 = true;
        } 
      } else if (f1 < 0.0F) {
        flag = true;
      } else {
        f += f1;
        if (flag1)
          f++; 
      } 
      if (f > width)
        break; 
      if (reverse) {
        stringbuilder.insert(0, c0);
      } else {
        stringbuilder.append(c0);
      } 
    } 
    return stringbuilder.toString();
  }
  
  private String trimStringNewline(String text) {
    while (text != null && text.endsWith("\n"))
      text = text.substring(0, text.length() - 1); 
    return text;
  }
  
  public void drawSplitString(String str, int x, int y, int wrapWidth, int textColor) {
    if (this.blend) {
      GlStateManager.getBlendState(this.oldBlendState);
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    } 
    resetStyles();
    this.textColor = textColor;
    str = trimStringNewline(str);
    renderSplitString(str, x, y, wrapWidth, false);
    if (this.blend)
      GlStateManager.setBlendState(this.oldBlendState); 
  }
  
  private void renderSplitString(String str, int x, int y, int wrapWidth, boolean addShadow) {
    for (String s : listFormattedStringToWidth(str, wrapWidth)) {
      renderStringAligned(s, x, y, wrapWidth, this.textColor, addShadow);
      y += this.FONT_HEIGHT;
    } 
  }
  
  public int splitStringWidth(String str, int maxLength) {
    return this.FONT_HEIGHT * listFormattedStringToWidth(str, maxLength).size();
  }
  
  public void setUnicodeFlag(boolean unicodeFlagIn) {
    this.unicodeFlag = unicodeFlagIn;
  }
  
  public boolean getUnicodeFlag() {
    return this.unicodeFlag;
  }
  
  public void setBidiFlag(boolean bidiFlagIn) {
    this.bidiFlag = bidiFlagIn;
  }
  
  public List<String> listFormattedStringToWidth(String str, int wrapWidth) {
    return Arrays.asList(wrapFormattedStringToWidth(str, wrapWidth).split("\n"));
  }
  
  String wrapFormattedStringToWidth(String str, int wrapWidth) {
    if (str.length() <= 1)
      return str; 
    int i = sizeStringToWidth(str, wrapWidth);
    if (str.length() <= i)
      return str; 
    String s = str.substring(0, i);
    char c0 = str.charAt(i);
    boolean flag = !(c0 != ' ' && c0 != '\n');
    String s1 = String.valueOf(getFormatFromString(s)) + str.substring(i + (flag ? 1 : 0));
    return String.valueOf(s) + "\n" + wrapFormattedStringToWidth(s1, wrapWidth);
  }
  
  private int sizeStringToWidth(String str, int wrapWidth) {
    int i = str.length();
    float f = 0.0F;
    int j = 0;
    int k = -1;
    for (boolean flag = false; j < i; j++) {
      char c0 = str.charAt(j);
      switch (c0) {
        case '\n':
          j--;
          break;
        case ' ':
          k = j;
        default:
          f += getCharWidthFloat(c0);
          if (flag)
            f++; 
          break;
        case '§':
          if (j < i - 1) {
            j++;
            char c1 = str.charAt(j);
            if (c1 != 'l' && c1 != 'L') {
              if (c1 == 'r' || c1 == 'R' || isFormatColor(c1))
                flag = false; 
              break;
            } 
            flag = true;
          } 
          break;
      } 
      if (c0 == '\n') {
        k = ++j;
        break;
      } 
      if (Math.round(f) > wrapWidth)
        break; 
    } 
    return (j != i && k != -1 && k < j) ? k : j;
  }
  
  private static boolean isFormatColor(char colorChar) {
    return !((colorChar < '0' || colorChar > '9') && (colorChar < 'a' || colorChar > 'f') && (colorChar < 'A' || colorChar > 'F'));
  }
  
  private static boolean isFormatSpecial(char formatChar) {
    return !((formatChar < 'k' || formatChar > 'o') && (formatChar < 'K' || formatChar > 'O') && formatChar != 'r' && formatChar != 'R');
  }
  
  public static String getFormatFromString(String text) {
    String s = "";
    int i = -1;
    int j = text.length();
    while ((i = text.indexOf('§', i + 1)) != -1) {
      if (i < j - 1) {
        char c0 = text.charAt(i + 1);
        if (isFormatColor(c0)) {
          s = "§" + c0;
          continue;
        } 
        if (isFormatSpecial(c0))
          s = String.valueOf(s) + "§" + c0; 
      } 
    } 
    return s;
  }
  
  public boolean getBidiFlag() {
    return this.bidiFlag;
  }
  
  public int getColorCode(char character) {
    int i = "0123456789abcdef".indexOf(character);
    if (i >= 0 && i < this.colorCode.length) {
      int j = this.colorCode[i];
      if (Config.isCustomColors())
        j = CustomColors.getTextColor(i, j); 
      return j;
    } 
    return 16777215;
  }
  
  protected void setColor(float p_setColor_1_, float p_setColor_2_, float p_setColor_3_, float p_setColor_4_) {
    GlStateManager.color(p_setColor_1_, p_setColor_2_, p_setColor_3_, p_setColor_4_);
  }
  
  protected void enableAlpha() {
    GlStateManager.enableAlpha();
  }
  
  protected void bindTexture(ResourceLocation p_bindTexture_1_) {
    this.renderEngine.bindTexture(p_bindTexture_1_);
  }
  
  protected IResource getResource(ResourceLocation p_getResource_1_) throws IOException {
    return Minecraft.getMinecraft().getResourceManager().getResource(p_getResource_1_);
  }
  
  public int drawString(String text, double x, double y, int color) {
    return !this.enabled ? 0 : drawString(text, (float)x, (float)y, color, false);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\gui\FontRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */