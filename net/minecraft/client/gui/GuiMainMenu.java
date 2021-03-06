package net.minecraft.client.gui;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Runnables;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServerDemo;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import optifine.CustomPanorama;
import optifine.CustomPanoramaProperties;
import optifine.Reflector;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GLContext;
import org.seltak.anubis.alt.ui.GuiAltManager;
import org.seltak.anubis.utils.GuiResourceButton;

public class GuiMainMenu extends GuiScreen {
  private static final Logger LOGGER = LogManager.getLogger();
  
  private static final Random RANDOM = new Random();
  
  private final float updateCounter;
  
  private String splashText;
  
  private GuiButton buttonResetDemo;
  
  private float panoramaTimer;
  
  private DynamicTexture viewportTexture;
  
  private final Object threadLock = new Object();
  
  public static final String MORE_INFO_TEXT = "Please click " + TextFormatting.UNDERLINE + "here" + TextFormatting.RESET + " for more information.";
  
  private int openGLWarning2Width;
  
  private int openGLWarning1Width;
  
  private int openGLWarningX1;
  
  private int openGLWarningY1;
  
  private int openGLWarningX2;
  
  private int openGLWarningY2;
  
  private String openGLWarning1;
  
  private String openGLWarning2;
  
  private String openGLWarningLink;
  
  private static final ResourceLocation SPLASH_TEXTS = new ResourceLocation("texts/splashes.txt");
  
  private static final ResourceLocation MINECRAFT_TITLE_TEXTURES = new ResourceLocation("textures/gui/title/minecraft.png");
  
  private static final ResourceLocation field_194400_H = new ResourceLocation("textures/gui/title/edition.png");
  
  private static final ResourceLocation GG = new ResourceLocation("seltak/minecraft.png");
  
  private static final ResourceLocation[] TITLE_PANORAMA_PATHS = new ResourceLocation[] { new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png") };
  
  private ResourceLocation backgroundTexture;
  
  private GuiButton realmsButton;
  
  private boolean hasCheckedForRealmsNotification;
  
  private GuiScreen realmsNotification;
  
  private int field_193978_M;
  
  private int field_193979_N;
  
  private GuiButton modButton;
  
  private GuiScreen modUpdateNotification;
  
  public GuiMainMenu() {
    this.openGLWarning2 = MORE_INFO_TEXT;
    this.splashText = "missingno";
    IResource iresource = null;
    try {
      List<String> list = Lists.newArrayList();
      iresource = Minecraft.getMinecraft().getResourceManager().getResource(SPLASH_TEXTS);
      BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(iresource.getInputStream(), StandardCharsets.UTF_8));
      String s;
      while ((s = bufferedreader.readLine()) != null) {
        s = s.trim();
        if (!s.isEmpty())
          list.add(s); 
      } 
      if (!list.isEmpty())
        do {
          this.splashText = list.get(RANDOM.nextInt(list.size()));
        } while (this.splashText.hashCode() == 125780783); 
    } catch (IOException iOException) {
    
    } finally {
      IOUtils.closeQuietly((Closeable)iresource);
    } 
    this.updateCounter = RANDOM.nextFloat();
    this.openGLWarning1 = "";
    if (!(GLContext.getCapabilities()).OpenGL20 && !OpenGlHelper.areShadersSupported()) {
      this.openGLWarning1 = I18n.format("title.oldgl1", new Object[0]);
      this.openGLWarning2 = I18n.format("title.oldgl2", new Object[0]);
      this.openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
    } 
  }
  
  private boolean areRealmsNotificationsEnabled() {
    return ((Minecraft.getMinecraft()).gameSettings.getOptionOrdinalValue(GameSettings.Options.REALMS_NOTIFICATIONS) && this.realmsNotification != null);
  }
  
  public void updateScreen() {
    if (areRealmsNotificationsEnabled())
      this.realmsNotification.updateScreen(); 
  }
  
  public boolean doesGuiPauseGame() {
    return false;
  }
  
  protected void keyTyped(char typedChar, int keyCode) throws IOException {}
  
  public void initGui() {
    this.viewportTexture = new DynamicTexture(256, 256);
    this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
    this.field_193978_M = this.fontRendererObj.getStringWidth("Copyright Mojang AB.");
    this.field_193979_N = this.width - this.field_193978_M - 2;
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    if (calendar.get(2) + 1 == 12 && calendar.get(5) == 24) {
      this.splashText = "Merry X-mas!";
    } else if (calendar.get(2) + 1 == 1 && calendar.get(5) == 1) {
      this.splashText = "Happy new year!";
    } else if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31) {
      this.splashText = "OOoooOOOoooo! Spooky!";
    } 
    int i = 24;
    int j = this.height / 4 + 48;
    if (this.mc.isDemo()) {
      addDemoButtons(j, 24);
    } else {
      addSingleplayerMultiplayerButtons(j, 24);
    } 
    synchronized (this.threadLock) {
      this.openGLWarning1Width = this.fontRendererObj.getStringWidth(this.openGLWarning1);
      this.openGLWarning2Width = this.fontRendererObj.getStringWidth(this.openGLWarning2);
      int k = Math.max(this.openGLWarning1Width, this.openGLWarning2Width);
      this.openGLWarningX1 = (this.width - k) / 2;
      this.openGLWarningY1 = ((GuiButton)this.buttonList.get(0)).yPosition - 24;
      this.openGLWarningX2 = this.openGLWarningX1 + k;
      this.openGLWarningY2 = this.openGLWarningY1 + 24;
    } 
    this.mc.setConnectedToRealms(false);
    if ((Minecraft.getMinecraft()).gameSettings.getOptionOrdinalValue(GameSettings.Options.REALMS_NOTIFICATIONS) && !this.hasCheckedForRealmsNotification) {
      RealmsBridge realmsbridge = new RealmsBridge();
      this.realmsNotification = realmsbridge.getNotificationScreen(this);
      this.hasCheckedForRealmsNotification = true;
    } 
    if (areRealmsNotificationsEnabled()) {
      this.realmsNotification.setGuiSize(this.width, this.height);
      this.realmsNotification.initGui();
    } 
    if (Reflector.NotificationModUpdateScreen_init.exists())
      this.modUpdateNotification = (GuiScreen)Reflector.call(Reflector.NotificationModUpdateScreen_init, new Object[] { this, this.modButton }); 
  }
  
  private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
    int var3 = this.height / 4 + 48;
    this.buttonListRes.add(new GuiResourceButton(1, this.width / 2 - 70, this.height - 40, 30, 30, "Singleplayer", new ResourceLocation("seltak/icons2/user.png")));
    this.buttonListRes.add(new GuiResourceButton(2, this.width / 2 - 35, this.height - 40, 30, 30, "Multiplayer", new ResourceLocation("seltak/icons2/users.png")));
    this.buttonListRes.add(new GuiResourceButton(111, this.width / 2, this.height - 40, 30, 30, "Account Manager", new ResourceLocation("seltak/icons2/list.png")));
    this.buttonListRes.add(new GuiResourceButton(0, this.width / 2 + 35, this.height - 40, 30, 30, "Options", new ResourceLocation("seltak/icons2/settings-3.png")));
    this.buttonListRes.add(new GuiResourceButton(4, this.width / 2 + 70, this.height - 40, 30, 30, "Exit", new ResourceLocation("seltak/icons2/error.png")));
    if (Reflector.GuiModList_Constructor.exists()) {
      this.realmsButton = addButton(new GuiButton(14, 1000000, p_73969_1_ + p_73969_2_ * 2, 98, 20, I18n.format("menu.online", new Object[0]).replace("Minecraft", "").trim()));
    } else {
      this.realmsButton = addButton(new GuiButton(14, 100000, p_73969_1_ + p_73969_2_ * 2, I18n.format("menu.online", new Object[0])));
    } 
  }
  
  private void addDemoButtons(int p_73972_1_, int p_73972_2_) {
    this.buttonList.add(new GuiButton(11, this.width / 2 - 100, p_73972_1_, I18n.format("menu.playdemo", new Object[0])));
    this.buttonResetDemo = addButton(new GuiButton(12, this.width / 2 - 100, p_73972_1_ + p_73972_2_ * 1, I18n.format("menu.resetdemo", new Object[0])));
    ISaveFormat isaveformat = this.mc.getSaveLoader();
    WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");
    if (worldinfo == null)
      this.buttonResetDemo.enabled = false; 
  }
  
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button.id == 0)
      this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings)); 
    if (button.id == 5)
      this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager())); 
    if (button.id == 111)
      this.mc.displayGuiScreen((GuiScreen)new GuiAltManager()); 
    if (button.id == 1)
      this.mc.displayGuiScreen(new GuiWorldSelection(this)); 
    if (button.id == 2)
      this.mc.displayGuiScreen(new GuiMultiplayer(this)); 
    if (button.id == 14 && this.realmsButton.visible)
      switchToRealms(); 
    if (button.id == 4)
      this.mc.shutdown(); 
    if (button.id == 6 && Reflector.GuiModList_Constructor.exists())
      this.mc.displayGuiScreen((GuiScreen)Reflector.newInstance(Reflector.GuiModList_Constructor, new Object[] { this })); 
    if (button.id == 11)
      this.mc.launchIntegratedServer("Demo_World", "Demo_World", WorldServerDemo.DEMO_WORLD_SETTINGS); 
    if (button.id == 12) {
      ISaveFormat isaveformat = this.mc.getSaveLoader();
      WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");
      if (worldinfo != null)
        this.mc.displayGuiScreen(new GuiYesNo(this, I18n.format("selectWorld.deleteQuestion", new Object[0]), "'" + worldinfo.getWorldName() + "' " + I18n.format("selectWorld.deleteWarning", new Object[0]), I18n.format("selectWorld.deleteButton", new Object[0]), I18n.format("gui.cancel", new Object[0]), 12)); 
    } 
  }
  
  private void switchToRealms() {
    RealmsBridge realmsbridge = new RealmsBridge();
    realmsbridge.switchToRealms(this);
  }
  
  public void confirmClicked(boolean result, int id) {
    if (result && id == 12) {
      ISaveFormat isaveformat = this.mc.getSaveLoader();
      isaveformat.flushCache();
      isaveformat.deleteWorldDirectory("Demo_World");
      this.mc.displayGuiScreen(this);
    } else if (id == 12) {
      this.mc.displayGuiScreen(this);
    } else if (id == 13) {
      if (result)
        try {
          Class<?> oclass = Class.forName("java.awt.Desktop");
          Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
          oclass.getMethod("browse", new Class[] { URI.class }).invoke(object, new Object[] { new URI(this.openGLWarningLink) });
        } catch (Throwable throwable1) {
          LOGGER.error("Couldn't open link", throwable1);
        }  
      this.mc.displayGuiScreen(this);
    } 
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    this.panoramaTimer += partialTicks;
    GlStateManager.disableAlpha();
    GlStateManager.enableAlpha();
    int i = 274;
    int j = this.width / 2 - 137;
    int k = 30;
    int l = -2130706433;
    int i1 = 16777215;
    int j1 = 0;
    int k1 = Integer.MIN_VALUE;
    CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();
    ScaledResolution s5 = new ScaledResolution(this.mc);
    this.mc.getTextureManager().bindTexture(new ResourceLocation("seltak/bg.jpg"));
    Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0F, 0.0F, s5.getScaledWidth(), s5.getScaledHeight(), s5.getScaledWidth(), s5.getScaledHeight());
    Gui.drawRect(0, 0, this.width, this.height, 16777215);
    if (custompanoramaproperties != null) {
      l = custompanoramaproperties.getOverlay1Top();
      i1 = custompanoramaproperties.getOverlay1Bottom();
      j1 = custompanoramaproperties.getOverlay2Top();
      k1 = custompanoramaproperties.getOverlay2Bottom();
    } 
    this.mc.getTextureManager().bindTexture(field_194400_H);
    drawModalRectWithCustomSizedTexture(j + 88, 67, 0.0F, 0.0F, 98, 14, 128.0F, 16.0F);
    if (Reflector.ForgeHooksClient_renderMainMenu.exists())
      this.splashText = Reflector.callString(Reflector.ForgeHooksClient_renderMainMenu, new Object[] { this, this.fontRendererObj, Integer.valueOf(this.width), Integer.valueOf(this.height), this.splashText }); 
    GlStateManager.pushMatrix();
    GlStateManager.translate((this.width / 2 + 90), 70.0F, 0.0F);
    GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
    float f = 1.8F - MathHelper.abs(MathHelper.sin((float)(Minecraft.getSystemTime() % 1000L) / 1000.0F * 6.2831855F) * 0.1F);
    f = f * 100.0F / (this.fontRendererObj.getStringWidth(this.splashText) + 32);
    GlStateManager.scale(f, f, f);
    GlStateManager.popMatrix();
    String s = "Anubis 1.12.2";
    if (this.mc.isDemo()) {
      s = String.valueOf(s) + " Demo";
    } else {
      s = String.valueOf(s) + ("release".equalsIgnoreCase(this.mc.getVersionType()) ? "" : ("/" + this.mc.getVersionType()));
    } 
    if (Reflector.FMLCommonHandler_getBrandings.exists()) {
      Object object = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
      List<String> list = Lists.reverse((List)Reflector.call(object, Reflector.FMLCommonHandler_getBrandings, new Object[] { Boolean.valueOf(true) }));
      for (int l1 = 0; l1 < list.size(); l1++) {
        String s1 = list.get(l1);
        if (!Strings.isNullOrEmpty(s1))
          drawString(this.fontRendererObj, s1, 2, this.height - 10 + l1 * (this.fontRendererObj.FONT_HEIGHT + 1), 16777215); 
      } 
    } else {
      drawString(this.fontRendererObj, s, 2, this.height - 10, -1);
    } 
    drawString(this.fontRendererObj, "Copyright Mojang AB.", this.field_193979_N, this.height - 10, -1);
    if (mouseX > this.field_193979_N && mouseX < this.field_193979_N + this.field_193978_M && mouseY > this.height - 10 && mouseY < this.height && Mouse.isInsideWindow())
      drawRect(this.field_193979_N, this.height - 1, this.field_193979_N + this.field_193978_M, this.height, -1); 
    if (this.openGLWarning1 != null && !this.openGLWarning1.isEmpty()) {
      drawRect(this.openGLWarningX1 - 2, this.openGLWarningY1 - 2, this.openGLWarningX2 + 2, this.openGLWarningY2 - 1, 1428160512);
      drawString(this.fontRendererObj, this.openGLWarning1, this.openGLWarningX1, this.openGLWarningY1, -1);
      drawString(this.fontRendererObj, this.openGLWarning2, (this.width - this.openGLWarning2Width) / 2, ((GuiButton)this.buttonList.get(0)).yPosition - 12, -1);
    } 
    this.mc.getTextureManager().bindTexture(new ResourceLocation("seltak/test.png"));
    Gui.drawModalRectWithCustomSizedTexture(this.field_193979_N + 65, this.height - 50, 0.0F, 0.0F, 30, 30, 30.0F, 30.0F);
    drawScreenRes(mouseX, mouseY);
    super.drawScreen(mouseX, mouseY, partialTicks);
    if (areRealmsNotificationsEnabled())
      this.realmsNotification.drawScreen(mouseX, mouseY, partialTicks); 
    if (this.modUpdateNotification != null)
      this.modUpdateNotification.drawScreen(mouseX, mouseY, partialTicks); 
  }
  
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    super.mouseClicked(mouseX, mouseY, mouseButton);
    synchronized (this.threadLock) {
      if (!this.openGLWarning1.isEmpty() && !StringUtils.isNullOrEmpty(this.openGLWarningLink) && mouseX >= this.openGLWarningX1 && mouseX <= this.openGLWarningX2 && mouseY >= this.openGLWarningY1 && mouseY <= this.openGLWarningY2) {
        GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(this, this.openGLWarningLink, 13, true);
        guiconfirmopenlink.disableSecurityWarning();
        this.mc.displayGuiScreen(guiconfirmopenlink);
      } 
    } 
    if (areRealmsNotificationsEnabled())
      this.realmsNotification.mouseClicked(mouseX, mouseY, mouseButton); 
    if (mouseX > this.field_193979_N && mouseX < this.field_193979_N + this.field_193978_M && mouseY > this.height - 10 && mouseY < this.height)
      this.mc.displayGuiScreen(new GuiWinGame(false, Runnables.doNothing())); 
  }
  
  public void onGuiClosed() {
    if (this.realmsNotification != null)
      this.realmsNotification.onGuiClosed(); 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\gui\GuiMainMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */