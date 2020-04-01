package net.minecraft.client.gui;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.seltak.anubis.utils.GuiResourceButton;

public abstract class GuiScreen extends Gui implements GuiYesNoCallback {
  private static final Logger LOGGER = LogManager.getLogger();
  
  private static final Set<String> PROTOCOLS = Sets.newHashSet((Object[])new String[] { "http", "https" });
  
  private static final Splitter NEWLINE_SPLITTER = Splitter.on('\n');
  
  protected Minecraft mc;
  
  protected RenderItem itemRender;
  
  public int width;
  
  public int height;
  
  protected List<GuiButton> buttonList = Lists.newArrayList();
  
  protected List<GuiResourceButton> buttonListRes = Lists.newArrayList();
  
  protected List<GuiLabel> labelList = Lists.newArrayList();
  
  public boolean allowUserInput;
  
  protected FontRenderer fontRendererObj;
  
  protected GuiButton selectedButton;
  
  protected GuiResourceButton selectedButtonRes;
  
  protected int eventButton;
  
  private long lastMouseEvent;
  
  private int touchValue;
  
  private URI clickedLinkURI;
  
  private boolean field_193977_u;
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    for (int i = 0; i < this.buttonList.size(); i++)
      ((GuiButton)this.buttonList.get(i)).func_191745_a(this.mc, mouseX, mouseY, partialTicks); 
    for (int j = 0; j < this.labelList.size(); j++)
      ((GuiLabel)this.labelList.get(j)).drawLabel(this.mc, mouseX, mouseY); 
  }
  
  public void drawScreenRes(int mouseX, int mouseY) {
    for (int i = 0; i < this.buttonListRes.size(); i++)
      ((GuiResourceButton)this.buttonListRes.get(i)).drawButton2(this.mc, mouseX, mouseY); 
    for (int j = 0; j < this.labelList.size(); j++)
      ((GuiLabel)this.labelList.get(j)).drawLabel(this.mc, mouseX, mouseY); 
  }
  
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    if (keyCode == 1) {
      this.mc.displayGuiScreen(null);
      if (this.mc.currentScreen == null)
        this.mc.setIngameFocus(); 
    } 
  }
  
  protected <T extends GuiResourceButton> T addButtonRes(T buttonIn2) {
    this.buttonListRes.add((GuiResourceButton)buttonIn2);
    return buttonIn2;
  }
  
  protected <T extends GuiButton> T addButton(T p_189646_1_) {
    this.buttonList.add((GuiButton)p_189646_1_);
    return p_189646_1_;
  }
  
  public static String getClipboardString() {
    try {
      Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
      if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor))
        return (String)transferable.getTransferData(DataFlavor.stringFlavor); 
    } catch (Exception exception) {}
    return "";
  }
  
  public static void setClipboardString(String copyText) {
    if (!StringUtils.isEmpty(copyText))
      try {
        StringSelection stringselection = new StringSelection(copyText);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringselection, null);
      } catch (Exception exception) {} 
  }
  
  protected void renderToolTip(ItemStack stack, int x, int y) {
    drawHoveringText(func_191927_a(stack), x, y);
  }
  
  public List<String> func_191927_a(ItemStack p_191927_1_) {
    List<String> list = p_191927_1_.getTooltip((EntityPlayer)this.mc.player, this.mc.gameSettings.advancedItemTooltips ? (ITooltipFlag)ITooltipFlag.TooltipFlags.ADVANCED : (ITooltipFlag)ITooltipFlag.TooltipFlags.NORMAL);
    for (int i = 0; i < list.size(); i++) {
      if (i == 0) {
        list.set(i, (p_191927_1_.getRarity()).rarityColor + (String)list.get(i));
      } else {
        list.set(i, TextFormatting.GRAY + (String)list.get(i));
      } 
    } 
    return list;
  }
  
  public void drawCreativeTabHoveringText(String tabName, int mouseX, int mouseY) {
    drawHoveringText(Arrays.asList(new String[] { tabName }, ), mouseX, mouseY);
  }
  
  public void func_193975_a(boolean p_193975_1_) {
    this.field_193977_u = p_193975_1_;
  }
  
  public boolean func_193976_p() {
    return this.field_193977_u;
  }
  
  public void drawHoveringText(List<String> textLines, int x, int y) {
    if (!textLines.isEmpty()) {
      GlStateManager.disableRescaleNormal();
      RenderHelper.disableStandardItemLighting();
      GlStateManager.disableLighting();
      GlStateManager.disableDepth();
      int i = 0;
      for (String s : textLines) {
        int j = this.fontRendererObj.getStringWidth(s);
        if (j > i)
          i = j; 
      } 
      int l1 = x + 12;
      int i2 = y - 12;
      int k = 8;
      if (textLines.size() > 1)
        k += 2 + (textLines.size() - 1) * 10; 
      if (l1 + i > this.width)
        l1 -= 28 + i; 
      if (i2 + k + 6 > this.height)
        i2 = this.height - k - 6; 
      this.zLevel = 300.0F;
      this.itemRender.zLevel = 300.0F;
      int l = -267386864;
      drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, -267386864, -267386864);
      drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, -267386864, -267386864);
      drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, -267386864, -267386864);
      drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, -267386864, -267386864);
      drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, -267386864, -267386864);
      int i1 = 1347420415;
      int j1 = 1344798847;
      drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, 1347420415, 1344798847);
      drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, 1347420415, 1344798847);
      drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, 1347420415, 1347420415);
      drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, 1344798847, 1344798847);
      for (int k1 = 0; k1 < textLines.size(); k1++) {
        String s1 = textLines.get(k1);
        this.fontRendererObj.drawStringWithShadow(s1, l1, i2, -1);
        if (k1 == 0)
          i2 += 2; 
        i2 += 10;
      } 
      this.zLevel = 0.0F;
      this.itemRender.zLevel = 0.0F;
      GlStateManager.enableLighting();
      GlStateManager.enableDepth();
      RenderHelper.enableStandardItemLighting();
      GlStateManager.enableRescaleNormal();
    } 
  }
  
  protected void handleComponentHover(ITextComponent component, int x, int y) {
    if (component != null && component.getStyle().getHoverEvent() != null) {
      HoverEvent hoverevent = component.getStyle().getHoverEvent();
      if (hoverevent.getAction() == HoverEvent.Action.SHOW_ITEM) {
        ItemStack itemstack = ItemStack.field_190927_a;
        try {
          NBTTagCompound nBTTagCompound = JsonToNBT.getTagFromJson(hoverevent.getValue().getUnformattedText());
          if (nBTTagCompound instanceof NBTTagCompound)
            itemstack = new ItemStack(nBTTagCompound); 
        } catch (NBTException nBTException) {}
        if (itemstack.func_190926_b()) {
          drawCreativeTabHoveringText(TextFormatting.RED + "Invalid Item!", x, y);
        } else {
          renderToolTip(itemstack, x, y);
        } 
      } else if (hoverevent.getAction() == HoverEvent.Action.SHOW_ENTITY) {
        if (this.mc.gameSettings.advancedItemTooltips)
          try {
            NBTTagCompound nbttagcompound = JsonToNBT.getTagFromJson(hoverevent.getValue().getUnformattedText());
            List<String> list = Lists.newArrayList();
            list.add(nbttagcompound.getString("name"));
            if (nbttagcompound.hasKey("type", 8)) {
              String s = nbttagcompound.getString("type");
              list.add("Type: " + s);
            } 
            list.add(nbttagcompound.getString("id"));
            drawHoveringText(list, x, y);
          } catch (NBTException var8) {
            drawCreativeTabHoveringText(TextFormatting.RED + "Invalid Entity!", x, y);
          }  
      } else if (hoverevent.getAction() == HoverEvent.Action.SHOW_TEXT) {
        drawHoveringText(this.mc.fontRendererObj.listFormattedStringToWidth(hoverevent.getValue().getFormattedText(), Math.max(this.width / 2, 200)), x, y);
      } 
      GlStateManager.disableLighting();
    } 
  }
  
  protected void setText(String newChatText, boolean shouldOverwrite) {}
  
  public boolean handleComponentClick(ITextComponent component) {
    if (component == null)
      return false; 
    ClickEvent clickevent = component.getStyle().getClickEvent();
    if (isShiftKeyDown()) {
      if (component.getStyle().getInsertion() != null)
        setText(component.getStyle().getInsertion(), false); 
    } else if (clickevent != null) {
      if (clickevent.getAction() == ClickEvent.Action.OPEN_URL) {
        if (!this.mc.gameSettings.chatLinks)
          return false; 
        try {
          URI uri = new URI(clickevent.getValue());
          String s = uri.getScheme();
          if (s == null)
            throw new URISyntaxException(clickevent.getValue(), "Missing protocol"); 
          if (!PROTOCOLS.contains(s.toLowerCase(Locale.ROOT)))
            throw new URISyntaxException(clickevent.getValue(), "Unsupported protocol: " + s.toLowerCase(Locale.ROOT)); 
          if (this.mc.gameSettings.chatLinksPrompt) {
            this.clickedLinkURI = uri;
            this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, clickevent.getValue(), 31102009, false));
          } else {
            openWebLink(uri);
          } 
        } catch (URISyntaxException urisyntaxexception) {
          LOGGER.error("Can't open url for {}", clickevent, urisyntaxexception);
        } 
      } else if (clickevent.getAction() == ClickEvent.Action.OPEN_FILE) {
        URI uri1 = (new File(clickevent.getValue())).toURI();
        openWebLink(uri1);
      } else if (clickevent.getAction() == ClickEvent.Action.SUGGEST_COMMAND) {
        setText(clickevent.getValue(), true);
      } else if (clickevent.getAction() == ClickEvent.Action.RUN_COMMAND) {
        sendChatMessage(clickevent.getValue(), false);
      } else {
        LOGGER.error("Don't know how to handle {}", clickevent);
      } 
      return true;
    } 
    return false;
  }
  
  public void sendChatMessage(String msg) {
    sendChatMessage(msg, true);
  }
  
  public void sendChatMessage(String msg, boolean addToChat) {
    if (addToChat)
      this.mc.ingameGUI.getChatGUI().addToSentMessages(msg); 
    this.mc.player.sendChatMessage(msg);
  }
  
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    mouseClickedRes(mouseX, mouseY, mouseButton);
    if (mouseButton == 0)
      for (int i = 0; i < this.buttonList.size(); i++) {
        GuiButton guibutton = this.buttonList.get(i);
        if (guibutton.mousePressed(this.mc, mouseX, mouseY)) {
          this.selectedButton = guibutton;
          guibutton.playPressSound(this.mc.getSoundHandler());
          actionPerformed(guibutton);
        } 
      }  
  }
  
  protected void mouseClickedRes(int mouseX, int mouseY, int mouseButton) throws IOException {
    if (mouseButton == 0)
      for (int i = 0; i < this.buttonListRes.size(); i++) {
        GuiResourceButton guibutton = this.buttonListRes.get(i);
        if (guibutton.mousePressed(this.mc, mouseX, mouseY)) {
          this.selectedButtonRes = guibutton;
          guibutton.playPressSound(this.mc.getSoundHandler());
          actionPerformed((GuiButton)guibutton);
        } 
      }  
  }
  
  protected void mouseReleased(int mouseX, int mouseY, int state) {
    mouseReleasedRes(mouseX, mouseY, state);
    if (this.selectedButton != null && state == 0) {
      this.selectedButton.mouseReleased(mouseX, mouseY);
      this.selectedButton = null;
    } 
  }
  
  protected void mouseReleasedRes(int mouseX, int mouseY, int state) {
    if (this.selectedButtonRes != null && state == 0) {
      this.selectedButtonRes.mouseReleased(mouseX, mouseY);
      this.selectedButtonRes = null;
    } 
  }
  
  protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {}
  
  protected void actionPerformed(GuiButton button) throws IOException {}
  
  public void setWorldAndResolution(Minecraft mc, int width, int height) {
    this.mc = mc;
    this.itemRender = mc.getRenderItem();
    this.fontRendererObj = mc.fontRendererObj;
    this.width = width;
    this.height = height;
    this.buttonList.clear();
    this.buttonListRes.clear();
    initGui();
  }
  
  public void setGuiSize(int w, int h) {
    this.width = w;
    this.height = h;
  }
  
  public void initGui() {}
  
  public void handleInput() throws IOException {
    if (Mouse.isCreated())
      while (Mouse.next())
        handleMouseInput();  
    if (Keyboard.isCreated())
      while (Keyboard.next())
        handleKeyboardInput();  
  }
  
  public void handleMouseInput() throws IOException {
    int i = Mouse.getEventX() * this.width / this.mc.displayWidth;
    int j = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
    int k = Mouse.getEventButton();
    if (Mouse.getEventButtonState()) {
      if (this.mc.gameSettings.touchscreen && this.touchValue++ > 0)
        return; 
      this.eventButton = k;
      this.lastMouseEvent = Minecraft.getSystemTime();
      mouseClicked(i, j, this.eventButton);
    } else if (k != -1) {
      if (this.mc.gameSettings.touchscreen && --this.touchValue > 0)
        return; 
      this.eventButton = -1;
      mouseReleased(i, j, k);
    } else if (this.eventButton != -1 && this.lastMouseEvent > 0L) {
      long l = Minecraft.getSystemTime() - this.lastMouseEvent;
      mouseClickMove(i, j, this.eventButton, l);
    } 
  }
  
  public void handleKeyboardInput() throws IOException {
    char c0 = Keyboard.getEventCharacter();
    if ((Keyboard.getEventKey() == 0 && c0 >= ' ') || Keyboard.getEventKeyState())
      keyTyped(c0, Keyboard.getEventKey()); 
    this.mc.dispatchKeypresses();
  }
  
  public void updateScreen() {}
  
  public void onGuiClosed() {}
  
  public void drawDefaultBackground() {
    drawWorldBackground(0);
  }
  
  public void drawWorldBackground(int tint) {
    if (this.mc.world != null) {
      drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
    } else {
      drawBackground(tint);
    } 
  }
  
  public void drawBackground(int tint) {
    GlStateManager.disableLighting();
    GlStateManager.disableFog();
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferbuilder = tessellator.getBuffer();
    this.mc.getTextureManager().bindTexture(OPTIONS_BACKGROUND);
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    float f = 32.0F;
    bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
    bufferbuilder.pos(0.0D, this.height, 0.0D).tex(0.0D, (this.height / 32.0F + tint)).color(64, 64, 64, 255).endVertex();
    bufferbuilder.pos(this.width, this.height, 0.0D).tex((this.width / 32.0F), (this.height / 32.0F + tint)).color(64, 64, 64, 255).endVertex();
    bufferbuilder.pos(this.width, 0.0D, 0.0D).tex((this.width / 32.0F), tint).color(64, 64, 64, 255).endVertex();
    bufferbuilder.pos(0.0D, 0.0D, 0.0D).tex(0.0D, tint).color(64, 64, 64, 255).endVertex();
    tessellator.draw();
  }
  
  public boolean doesGuiPauseGame() {
    return true;
  }
  
  public void confirmClicked(boolean result, int id) {
    if (id == 31102009) {
      if (result)
        openWebLink(this.clickedLinkURI); 
      this.clickedLinkURI = null;
      this.mc.displayGuiScreen(this);
    } 
  }
  
  private void openWebLink(URI url) {
    try {
      Class<?> oclass = Class.forName("java.awt.Desktop");
      Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
      oclass.getMethod("browse", new Class[] { URI.class }).invoke(object, new Object[] { url });
    } catch (Throwable throwable1) {
      Throwable throwable = throwable1.getCause();
      LOGGER.error("Couldn't open link: {}", (throwable == null) ? "<UNKNOWN>" : throwable.getMessage());
    } 
  }
  
  public static boolean isCtrlKeyDown() {
    if (Minecraft.IS_RUNNING_ON_MAC)
      return !(!Keyboard.isKeyDown(219) && !Keyboard.isKeyDown(220)); 
    return !(!Keyboard.isKeyDown(29) && !Keyboard.isKeyDown(157));
  }
  
  public static boolean isShiftKeyDown() {
    return !(!Keyboard.isKeyDown(42) && !Keyboard.isKeyDown(54));
  }
  
  public static boolean isAltKeyDown() {
    return !(!Keyboard.isKeyDown(56) && !Keyboard.isKeyDown(184));
  }
  
  public static boolean isKeyComboCtrlX(int keyID) {
    return (keyID == 45 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown());
  }
  
  public static boolean isKeyComboCtrlV(int keyID) {
    return (keyID == 47 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown());
  }
  
  public static boolean isKeyComboCtrlC(int keyID) {
    return (keyID == 46 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown());
  }
  
  public static boolean isKeyComboCtrlA(int keyID) {
    return (keyID == 30 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown());
  }
  
  public void onResize(Minecraft mcIn, int w, int h) {
    setWorldAndResolution(mcIn, w, h);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\gui\GuiScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */