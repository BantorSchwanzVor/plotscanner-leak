package org.seltak.anubis.alt.ui;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.seltak.anubis.Anubis;

public class GuiAltManager extends GuiScreen {
  private GuiButton login;
  
  private GuiButton remove;
  
  private GuiButton rename;
  
  private AltLoginThread loginThread;
  
  private int offset;
  
  public Alt selectedAlt = null;
  
  private String status = "§7" + "No alts selected";
  
  public void actionPerformed(GuiButton button) throws IOException {
    String user;
    AltManager altManager;
    String pass;
    switch (button.id) {
      case 0:
        if (this.loginThread == null) {
          this.mc.displayGuiScreen(null);
          break;
        } 
        if (!this.loginThread.getStatus().equals("§e" + "Attempting to log in") && !this.loginThread.getStatus().equals("§c" + "Do not hit back!" + "§e" + " Logging in...")) {
          this.mc.displayGuiScreen(null);
          break;
        } 
        this.loginThread.setStatus("§c" + "Failed to login! Please try again!" + "§e" + " Logging in...");
        break;
      case 1:
        user = this.selectedAlt.getUsername();
        pass = this.selectedAlt.getPassword();
        this.loginThread = new AltLoginThread(user, pass);
        this.loginThread.start();
        break;
      case 2:
        if (this.loginThread != null)
          this.loginThread = null; 
        altManager = Anubis.altManager;
        AltManager.registry.remove(this.selectedAlt);
        this.status = "§aRemoved.";
        AltUtils.saveAlts(AltManager.registry);
        this.selectedAlt = null;
        break;
      case 3:
        this.mc.displayGuiScreen(new GuiAddAlt(this));
        break;
      case 4:
        this.mc.displayGuiScreen(new GuiAltLogin(this));
        break;
      case 6:
        this.mc.displayGuiScreen(new GuiRenameAlt(this));
        break;
    } 
  }
  
  public void drawScreen(int par1, int par2, float par3) {
    if (Mouse.hasWheel()) {
      int wheel = Mouse.getDWheel();
      if (wheel < 0) {
        this.offset += 26;
        if (this.offset < 0)
          this.offset = 0; 
      } else if (wheel > 0) {
        this.offset -= 26;
        if (this.offset < 0)
          this.offset = 0; 
      } 
    } 
    drawDefaultBackground();
    drawString(this.fontRendererObj, "§aLogged in as: §e" + this.mc.session.getUsername(), 10, 10, -7829368);
    FontRenderer fontRendererObj = this.fontRendererObj;
    StringBuilder sb2 = new StringBuilder("Alt Manager - ");
    AltManager altManager = Anubis.altManager;
    drawCenteredString(fontRendererObj, sb2.append(AltManager.registry.size()).append(" alts").toString(), this.width / 2, 10, -1);
    drawCenteredString(this.fontRendererObj, (this.loginThread == null) ? this.status : this.loginThread.getStatus(), this.width / 2, 20, -1);
    GL11.glPushMatrix();
    prepareScissorBox(0.0F, 33.0F, this.width, (this.height - 50));
    GL11.glEnable(3089);
    int y2 = 38;
    FontRenderer fr = (Minecraft.getMinecraft()).fontRendererObj;
    ScaledResolution s1 = new ScaledResolution(this.mc);
    this.mc.getTextureManager().bindTexture(new ResourceLocation("seltak/bg.jpg"));
    Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0F, 0.0F, s1.getScaledWidth(), s1.getScaledHeight(), s1.getScaledWidth(), s1.getScaledHeight());
    Gui.drawRect(0, 0, this.width, this.height, 1073741824);
    AltManager altManager2 = Anubis.altManager;
    for (Alt alt2 : AltManager.registry) {
      if (!isAltInArea(y2))
        continue; 
      String name = alt2.getMask().equals("") ? alt2.getUsername() : alt2.getMask();
      String pass = alt2.getPassword().equals("") ? "§cCracked" : alt2.getPassword().replaceAll(".", "*");
      if (alt2 == this.selectedAlt) {
        if (isMouseOverAlt(par1, par2, y2 - this.offset) && Mouse.isButtonDown(0)) {
          RectangleUtils.drawBorderedRect(400.0F, (y2 - this.offset - 4), (this.width - 400), (y2 - this.offset + 20), 1.0F, -16777216, -2142943931);
        } else if (isMouseOverAlt(par1, par2, y2 - this.offset)) {
          RectangleUtils.drawBorderedRect(400.0F, (y2 - this.offset - 4), (this.width - 400), (y2 - this.offset + 20), 1.0F, -16777216, -2142088622);
        } else {
          RectangleUtils.drawBorderedRect(400.0F, (y2 - this.offset - 4), (this.width - 400), (y2 - this.offset + 20), 1.0F, -16777216, -2144259791);
        } 
      } else if (isMouseOverAlt(par1, par2, y2 - this.offset) && Mouse.isButtonDown(0)) {
        RectangleUtils.drawBorderedRect(400.0F, (y2 - this.offset - 4), (this.width - 400), (y2 - this.offset + 20), 1.0F, -16777216, -2146101995);
      } else if (isMouseOverAlt(par1, par2, y2 - this.offset)) {
        RectangleUtils.drawBorderedRect(400.0F, (y2 - this.offset - 4), (this.width - 400), (y2 - this.offset + 20), 1.0F, -16777216, -2145180893);
      } 
      drawCenteredString(this.fontRendererObj, name, this.width / 2, y2 - this.offset, -1);
      drawCenteredString(this.fontRendererObj, pass, this.width / 2, y2 - this.offset + 10, 5592405);
      y2 += 26;
    } 
    GL11.glDisable(3089);
    GL11.glPopMatrix();
    super.drawScreen(par1, par2, par3);
    if (this.selectedAlt == null) {
      this.login.enabled = false;
      this.remove.enabled = false;
      this.rename.enabled = false;
    } else {
      this.login.enabled = true;
      this.remove.enabled = true;
      this.rename.enabled = true;
    } 
    if (Keyboard.isKeyDown(200)) {
      this.offset -= 26;
      if (this.offset < 0)
        this.offset = 0; 
    } else if (Keyboard.isKeyDown(208)) {
      this.offset += 26;
      if (this.offset < 0)
        this.offset = 0; 
    } 
  }
  
  public void initGui() {
    this.buttonList.add(new GuiButton(0, this.width / 2 + 4 + 50, this.height - 24, 100, 20, "Back"));
    this.login = new GuiButton(1, this.width / 2 - 154, this.height - 48, 100, 20, "Login");
    this.buttonList.add(this.login);
    this.remove = new GuiButton(2, this.width / 2 - 154, this.height - 24, 100, 20, "Remove");
    this.buttonList.add(this.remove);
    this.buttonList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 48, 100, 20, "Add"));
    this.buttonList.add(new GuiButton(4, this.width / 2 - 50, this.height - 48, 100, 20, "Direct Login"));
    this.rename = new GuiButton(6, this.width / 2 - 50, this.height - 24, 100, 20, "Edit");
    this.buttonList.add(this.rename);
    this.login.enabled = false;
    this.remove.enabled = false;
    this.rename.enabled = false;
  }
  
  private boolean isAltInArea(int y2) {
    if (y2 - this.offset <= this.height - 50)
      return true; 
    return false;
  }
  
  private boolean isMouseOverAlt(int x2, int y2, int y1) {
    if (x2 >= 52 && y2 >= y1 - 4 && x2 <= this.width - 52 && y2 <= y1 + 20 && x2 >= 0 && y2 >= 33 && x2 <= this.width && y2 <= this.height - 50)
      return true; 
    return false;
  }
  
  protected void mouseClicked(int par1, int par2, int par3) throws IOException {
    if (this.offset < 0)
      this.offset = 0; 
    int y2 = 38 - this.offset;
    AltManager altManager = Anubis.altManager;
    for (Alt alt2 : AltManager.registry) {
      if (isMouseOverAlt(par1, par2, y2)) {
        if (alt2 == this.selectedAlt) {
          actionPerformed(this.buttonList.get(1));
          return;
        } 
        this.selectedAlt = alt2;
      } 
      y2 += 26;
    } 
    try {
      super.mouseClicked(par1, par2, par3);
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
  
  public void prepareScissorBox(float x2, float y2, float x22, float y22) {
    ScaledResolution scale = new ScaledResolution(this.mc);
    int factor = scale.getScaleFactor();
    GL11.glScissor((int)(x2 * factor), (int)((scale.getScaledHeight() - y22) * factor), (int)((x22 - x2) * factor), (int)((y22 - y2) * factor));
  }
  
  protected void overlayBackground(int startY, int endY, int startAlpha, int endAlpha) {
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder worldrenderer = tessellator.getBuffer();
    this.mc.getTextureManager().bindTexture(new ResourceLocation("scl/background.jpg"));
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    float f = 32.0F;
    worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
    worldrenderer.pos(0.0D, endY, 0.0D).tex(0.0D, (endY / 32.0F)).color(64, 64, 64, endAlpha).endVertex();
    worldrenderer.pos((0 + this.width), endY, 0.0D).tex((this.width / 32.0F), (endY / 32.0F)).color(64, 64, 64, endAlpha).endVertex();
    worldrenderer.pos((0 + this.width), startY, 0.0D).tex((this.width / 32.0F), (startY / 32.0F)).color(64, 64, 64, startAlpha).endVertex();
    worldrenderer.pos(0.0D, startY, 0.0D).tex(0.0D, (startY / 32.0F)).color(64, 64, 64, startAlpha).endVertex();
    tessellator.draw();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\al\\ui\GuiAltManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */