package net.minecraft.client.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSummary;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiListWorldSelectionEntry implements GuiListExtended.IGuiListEntry {
  private static final Logger LOGGER = LogManager.getLogger();
  
  private static final DateFormat DATE_FORMAT = new SimpleDateFormat();
  
  private static final ResourceLocation ICON_MISSING = new ResourceLocation("textures/misc/unknown_server.png");
  
  private static final ResourceLocation ICON_OVERLAY_LOCATION = new ResourceLocation("textures/gui/world_selection.png");
  
  private final Minecraft client;
  
  private final GuiWorldSelection worldSelScreen;
  
  private final WorldSummary worldSummary;
  
  private final ResourceLocation iconLocation;
  
  private final GuiListWorldSelection containingListSel;
  
  private File iconFile;
  
  private DynamicTexture icon;
  
  private long lastClickTime;
  
  public GuiListWorldSelectionEntry(GuiListWorldSelection listWorldSelIn, WorldSummary p_i46591_2_, ISaveFormat p_i46591_3_) {
    this.containingListSel = listWorldSelIn;
    this.worldSelScreen = listWorldSelIn.getGuiWorldSelection();
    this.worldSummary = p_i46591_2_;
    this.client = Minecraft.getMinecraft();
    this.iconLocation = new ResourceLocation("worlds/" + p_i46591_2_.getFileName() + "/icon");
    this.iconFile = p_i46591_3_.getFile(p_i46591_2_.getFileName(), "icon.png");
    if (!this.iconFile.isFile())
      this.iconFile = null; 
    loadServerIcon();
  }
  
  public void func_192634_a(int p_192634_1_, int p_192634_2_, int p_192634_3_, int p_192634_4_, int p_192634_5_, int p_192634_6_, int p_192634_7_, boolean p_192634_8_, float p_192634_9_) {
    String s = this.worldSummary.getDisplayName();
    String s1 = String.valueOf(this.worldSummary.getFileName()) + " (" + DATE_FORMAT.format(new Date(this.worldSummary.getLastTimePlayed())) + ")";
    String s2 = "";
    if (StringUtils.isEmpty(s))
      s = String.valueOf(I18n.format("selectWorld.world", new Object[0])) + " " + (p_192634_1_ + 1); 
    if (this.worldSummary.requiresConversion()) {
      s2 = String.valueOf(I18n.format("selectWorld.conversion", new Object[0])) + " " + s2;
    } else {
      s2 = I18n.format("gameMode." + this.worldSummary.getEnumGameType().getName(), new Object[0]);
      if (this.worldSummary.isHardcoreModeEnabled())
        s2 = TextFormatting.DARK_RED + I18n.format("gameMode.hardcore", new Object[0]) + TextFormatting.RESET; 
      if (this.worldSummary.getCheatsEnabled())
        s2 = String.valueOf(s2) + ", " + I18n.format("selectWorld.cheats", new Object[0]); 
      String s3 = this.worldSummary.getVersionName();
      if (this.worldSummary.markVersionInList()) {
        if (this.worldSummary.askToOpenWorld()) {
          s2 = String.valueOf(s2) + ", " + I18n.format("selectWorld.version", new Object[0]) + " " + TextFormatting.RED + s3 + TextFormatting.RESET;
        } else {
          s2 = String.valueOf(s2) + ", " + I18n.format("selectWorld.version", new Object[0]) + " " + TextFormatting.ITALIC + s3 + TextFormatting.RESET;
        } 
      } else {
        s2 = String.valueOf(s2) + ", " + I18n.format("selectWorld.version", new Object[0]) + " " + s3;
      } 
    } 
    this.client.fontRendererObj.drawString(s, p_192634_2_ + 32 + 3, p_192634_3_ + 1, 16777215);
    this.client.fontRendererObj.drawString(s1, p_192634_2_ + 32 + 3, p_192634_3_ + this.client.fontRendererObj.FONT_HEIGHT + 3, 8421504);
    this.client.fontRendererObj.drawString(s2, p_192634_2_ + 32 + 3, p_192634_3_ + this.client.fontRendererObj.FONT_HEIGHT + this.client.fontRendererObj.FONT_HEIGHT + 3, 8421504);
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.client.getTextureManager().bindTexture((this.icon != null) ? this.iconLocation : ICON_MISSING);
    GlStateManager.enableBlend();
    Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
    GlStateManager.disableBlend();
    if (this.client.gameSettings.touchscreen || p_192634_8_) {
      this.client.getTextureManager().bindTexture(ICON_OVERLAY_LOCATION);
      Gui.drawRect(p_192634_2_, p_192634_3_, p_192634_2_ + 32, p_192634_3_ + 32, -1601138544);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      int j = p_192634_6_ - p_192634_2_;
      int i = (j < 32) ? 32 : 0;
      if (this.worldSummary.markVersionInList()) {
        Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 32.0F, i, 32, 32, 256.0F, 256.0F);
        if (this.worldSummary.askToOpenWorld()) {
          Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 96.0F, i, 32, 32, 256.0F, 256.0F);
          if (j < 32)
            this.worldSelScreen.setVersionTooltip(TextFormatting.RED + I18n.format("selectWorld.tooltip.fromNewerVersion1", new Object[0]) + "\n" + TextFormatting.RED + I18n.format("selectWorld.tooltip.fromNewerVersion2", new Object[0])); 
        } else {
          Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 64.0F, i, 32, 32, 256.0F, 256.0F);
          if (j < 32)
            this.worldSelScreen.setVersionTooltip(TextFormatting.GOLD + I18n.format("selectWorld.tooltip.snapshot1", new Object[0]) + "\n" + TextFormatting.GOLD + I18n.format("selectWorld.tooltip.snapshot2", new Object[0])); 
        } 
      } else {
        Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 0.0F, i, 32, 32, 256.0F, 256.0F);
      } 
    } 
  }
  
  public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
    this.containingListSel.selectWorld(slotIndex);
    if (relativeX <= 32 && relativeX < 32) {
      joinWorld();
      return true;
    } 
    if (Minecraft.getSystemTime() - this.lastClickTime < 250L) {
      joinWorld();
      return true;
    } 
    this.lastClickTime = Minecraft.getSystemTime();
    return false;
  }
  
  public void joinWorld() {
    if (this.worldSummary.askToOpenWorld()) {
      this.client.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback() {
              public void confirmClicked(boolean result, int id) {
                if (result) {
                  GuiListWorldSelectionEntry.this.loadWorld();
                } else {
                  GuiListWorldSelectionEntry.this.client.displayGuiScreen(GuiListWorldSelectionEntry.this.worldSelScreen);
                } 
              }
            },  I18n.format("selectWorld.versionQuestion", new Object[0]), I18n.format("selectWorld.versionWarning", new Object[] { this.worldSummary.getVersionName() }), I18n.format("selectWorld.versionJoinButton", new Object[0]), I18n.format("gui.cancel", new Object[0]), 0));
    } else {
      loadWorld();
    } 
  }
  
  public void deleteWorld() {
    this.client.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback() {
            public void confirmClicked(boolean result, int id) {
              if (result) {
                GuiListWorldSelectionEntry.this.client.displayGuiScreen(new GuiScreenWorking());
                ISaveFormat isaveformat = GuiListWorldSelectionEntry.this.client.getSaveLoader();
                isaveformat.flushCache();
                isaveformat.deleteWorldDirectory(GuiListWorldSelectionEntry.this.worldSummary.getFileName());
                GuiListWorldSelectionEntry.this.containingListSel.refreshList();
              } 
              GuiListWorldSelectionEntry.this.client.displayGuiScreen(GuiListWorldSelectionEntry.this.worldSelScreen);
            }
          },  I18n.format("selectWorld.deleteQuestion", new Object[0]), "'" + this.worldSummary.getDisplayName() + "' " + I18n.format("selectWorld.deleteWarning", new Object[0]), I18n.format("selectWorld.deleteButton", new Object[0]), I18n.format("gui.cancel", new Object[0]), 0));
  }
  
  public void editWorld() {
    this.client.displayGuiScreen(new GuiWorldEdit(this.worldSelScreen, this.worldSummary.getFileName()));
  }
  
  public void recreateWorld() {
    this.client.displayGuiScreen(new GuiScreenWorking());
    GuiCreateWorld guicreateworld = new GuiCreateWorld(this.worldSelScreen);
    ISaveHandler isavehandler = this.client.getSaveLoader().getSaveLoader(this.worldSummary.getFileName(), false);
    WorldInfo worldinfo = isavehandler.loadWorldInfo();
    isavehandler.flush();
    if (worldinfo != null) {
      guicreateworld.recreateFromExistingWorld(worldinfo);
      this.client.displayGuiScreen(guicreateworld);
    } 
  }
  
  private void loadWorld() {
    this.client.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    if (this.client.getSaveLoader().canLoadWorld(this.worldSummary.getFileName()))
      this.client.launchIntegratedServer(this.worldSummary.getFileName(), this.worldSummary.getDisplayName(), null); 
  }
  
  private void loadServerIcon() {
    boolean flag = (this.iconFile != null && this.iconFile.isFile());
    if (flag) {
      BufferedImage bufferedimage;
      try {
        bufferedimage = ImageIO.read(this.iconFile);
        Validate.validState((bufferedimage.getWidth() == 64), "Must be 64 pixels wide", new Object[0]);
        Validate.validState((bufferedimage.getHeight() == 64), "Must be 64 pixels high", new Object[0]);
      } catch (Throwable throwable) {
        LOGGER.error("Invalid icon for world {}", this.worldSummary.getFileName(), throwable);
        this.iconFile = null;
        return;
      } 
      if (this.icon == null) {
        this.icon = new DynamicTexture(bufferedimage.getWidth(), bufferedimage.getHeight());
        this.client.getTextureManager().loadTexture(this.iconLocation, (ITextureObject)this.icon);
      } 
      bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), this.icon.getTextureData(), 0, bufferedimage.getWidth());
      this.icon.updateDynamicTexture();
    } else if (!flag) {
      this.client.getTextureManager().deleteTexture(this.iconLocation);
      this.icon = null;
    } 
  }
  
  public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {}
  
  public void func_192633_a(int p_192633_1_, int p_192633_2_, int p_192633_3_, float p_192633_4_) {}
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\gui\GuiListWorldSelectionEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */