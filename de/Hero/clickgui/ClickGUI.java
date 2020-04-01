package de.Hero.clickgui;

import de.Hero.clickgui.elements.Element;
import de.Hero.clickgui.elements.ModuleButton;
import de.Hero.clickgui.elements.menu.ElementSlider;
import de.Hero.clickgui.util.ColorUtil;
import de.Hero.clickgui.util.FontUtil;
import de.Hero.settings.SettingsManager;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.seltak.anubis.Anubis;
import org.seltak.anubis.module.Category;
import org.seltak.anubis.module.Module;

public class ClickGUI extends GuiScreen {
  public static ArrayList<Panel> panels;
  
  public static ArrayList<Panel> rpanels;
  
  private ModuleButton mb = null;
  
  public SettingsManager setmgr;
  
  public ClickGUI() {
    this.setmgr = Anubis.setmgr;
    FontUtil.setupFontUtils();
    panels = new ArrayList<>();
    double pwidth = 80.0D;
    double pheight = 15.0D;
    double px = 10.0D;
    double py = 10.0D;
    double pxplus = pwidth + 10.0D;
    byte b;
    int i;
    Category[] arrayOfCategory;
    for (i = (arrayOfCategory = Category.values()).length, b = 0; b < i; ) {
      final Category c = arrayOfCategory[b];
      String title = String.valueOf(Character.toUpperCase(c.name().toLowerCase().charAt(0))) + c.name().toLowerCase().substring(1);
      panels.add(new Panel(title, px, py, pwidth, pheight, false, this) {
            public void setup() {
              for (Module m : Anubis.moduleManager.getModules()) {
                if (!m.getCategory().equals(c))
                  continue; 
                this.Elements.add(new ModuleButton(m, this));
              } 
            }
          });
      px += pxplus;
      b++;
    } 
    rpanels = new ArrayList<>();
    for (Panel p : panels)
      rpanels.add(p); 
    Collections.reverse(rpanels);
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    for (Panel p : panels)
      p.drawScreen(mouseX, mouseY, partialTicks); 
    this.mb = null;
    label57: for (Panel p : panels) {
      if (p != null && p.visible && p.extended && p.Elements != null && 
        p.Elements.size() > 0)
        for (ModuleButton e : p.Elements) {
          if (e.listening) {
            this.mb = e;
            break label57;
          } 
        }  
    } 
    for (Panel panel : panels) {
      if (panel.extended && panel.visible && panel.Elements != null)
        for (ModuleButton b : panel.Elements) {
          if (b.extended && b.menuelements != null && !b.menuelements.isEmpty()) {
            double off = 0.0D;
            Color temp = ColorUtil.getClickGUIColor().darker();
            int outlineColor = (new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 170)).getRGB();
            for (Element e : b.menuelements) {
              e.offset = off;
              e.update();
              if (Anubis.setmgr.getSettingByName("Design").getValString().equalsIgnoreCase("New"))
                Gui.drawRect(e.x, e.y, e.x + e.width + 2.0D, e.y + e.height, outlineColor); 
              e.drawScreen(mouseX, mouseY, partialTicks);
              off += e.height;
            } 
          } 
        }  
    } 
    ScaledResolution s = new ScaledResolution(this.mc);
    if (this.mb != null) {
      drawRect(0, 0, this.width, this.height, -2012213232);
      GL11.glPushMatrix();
      GL11.glTranslatef((s.getScaledWidth() / 2), (s.getScaledHeight() / 2), 0.0F);
      GL11.glScalef(4.0F, 4.0F, 0.0F);
      FontUtil.drawTotalCenteredStringWithShadow("Listening...", 0.0D, -10.0D, -1);
      GL11.glScalef(0.5F, 0.5F, 0.0F);
      FontUtil.drawTotalCenteredStringWithShadow("Press 'ESCAPE' to unbind " + this.mb.mod.getName() + ((this.mb.mod.getKeybind() > -1) ? (" (" + Keyboard.getKeyName(this.mb.mod.getKeybind()) + ")") : ""), 0.0D, 0.0D, -1);
      GL11.glScalef(0.25F, 0.25F, 0.0F);
      FontUtil.drawTotalCenteredStringWithShadow("by HeroCode", 0.0D, 20.0D, -1);
      GL11.glPopMatrix();
    } 
    super.drawScreen(mouseX, mouseY, partialTicks);
  }
  
  public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    if (this.mb != null)
      return; 
    for (Panel panel : rpanels) {
      if (panel.extended && panel.visible && panel.Elements != null)
        for (ModuleButton b : panel.Elements) {
          if (b.extended)
            for (Element e : b.menuelements) {
              if (e.mouseClicked(mouseX, mouseY, mouseButton))
                return; 
            }  
        }  
    } 
    for (Panel p : rpanels) {
      if (p.mouseClicked(mouseX, mouseY, mouseButton))
        return; 
    } 
    try {
      super.mouseClicked(mouseX, mouseY, mouseButton);
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
  
  public void mouseReleased(int mouseX, int mouseY, int state) {
    if (this.mb != null)
      return; 
    for (Panel panel : rpanels) {
      if (panel.extended && panel.visible && panel.Elements != null)
        for (ModuleButton b : panel.Elements) {
          if (b.extended)
            for (Element e : b.menuelements)
              e.mouseReleased(mouseX, mouseY, state);  
        }  
    } 
    for (Panel p : rpanels)
      p.mouseReleased(mouseX, mouseY, state); 
    super.mouseReleased(mouseX, mouseY, state);
  }
  
  protected void keyTyped(char typedChar, int keyCode) {
    for (Panel p : rpanels) {
      if (p != null && p.visible && p.extended && p.Elements != null && p.Elements.size() > 0)
        for (ModuleButton e : p.Elements) {
          try {
            if (e.keyTyped(typedChar, keyCode))
              return; 
          } catch (IOException e1) {
            e1.printStackTrace();
          } 
        }  
    } 
    try {
      super.keyTyped(typedChar, keyCode);
    } catch (IOException e2) {
      e2.printStackTrace();
    } 
  }
  
  public void initGui() {
    if (OpenGlHelper.shadersSupported && this.mc.getRenderViewEntity() instanceof net.minecraft.entity.player.EntityPlayer) {
      if (this.mc.entityRenderer.theShaderGroup != null)
        this.mc.entityRenderer.theShaderGroup.deleteShaderGroup(); 
      this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
    } 
  }
  
  public void onGuiClosed() {
    Anubis.saveFile();
    if (this.mc.entityRenderer.theShaderGroup != null) {
      this.mc.entityRenderer.theShaderGroup.deleteShaderGroup();
      this.mc.entityRenderer.theShaderGroup = null;
    } 
    for (Panel panel : rpanels) {
      if (panel.extended && panel.visible && panel.Elements != null)
        for (ModuleButton b : panel.Elements) {
          if (b.extended)
            for (Element e : b.menuelements) {
              if (e instanceof ElementSlider)
                ((ElementSlider)e).dragging = false; 
            }  
        }  
    } 
  }
  
  public void closeAllSettings() {
    for (Panel p : rpanels) {
      if (p != null && p.visible && p.extended && p.Elements != null && 
        p.Elements.size() > 0)
        for (ModuleButton moduleButton : p.Elements); 
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\de\Hero\clickgui\ClickGUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */