package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

public class GuiSnooper extends GuiScreen {
  private final GuiScreen lastScreen;
  
  private final GameSettings game_settings_2;
  
  private final java.util.List<String> keys = Lists.newArrayList();
  
  private final java.util.List<String> values = Lists.newArrayList();
  
  private String title;
  
  private String[] desc;
  
  private List list;
  
  private GuiButton toggleButton;
  
  public GuiSnooper(GuiScreen p_i1061_1_, GameSettings p_i1061_2_) {
    this.lastScreen = p_i1061_1_;
    this.game_settings_2 = p_i1061_2_;
  }
  
  public void initGui() {
    this.title = I18n.format("options.snooper.title", new Object[0]);
    String s = I18n.format("options.snooper.desc", new Object[0]);
    java.util.List<String> list = Lists.newArrayList();
    for (String s1 : this.fontRendererObj.listFormattedStringToWidth(s, this.width - 30))
      list.add(s1); 
    this.desc = list.<String>toArray(new String[list.size()]);
    this.keys.clear();
    this.values.clear();
    this.toggleButton = addButton(new GuiButton(1, this.width / 2 - 152, this.height - 30, 150, 20, this.game_settings_2.getKeyBinding(GameSettings.Options.SNOOPER_ENABLED)));
    this.buttonList.add(new GuiButton(2, this.width / 2 + 2, this.height - 30, 150, 20, I18n.format("gui.done", new Object[0])));
    boolean flag = (this.mc.getIntegratedServer() != null && this.mc.getIntegratedServer().getPlayerUsageSnooper() != null);
    for (Map.Entry<String, String> entry : (new TreeMap<>(this.mc.getPlayerUsageSnooper().getCurrentStats())).entrySet()) {
      this.keys.add(String.valueOf(flag ? "C " : "") + (String)entry.getKey());
      this.values.add(this.fontRendererObj.trimStringToWidth(entry.getValue(), this.width - 220));
    } 
    if (flag)
      for (Map.Entry<String, String> entry1 : (new TreeMap<>(this.mc.getIntegratedServer().getPlayerUsageSnooper().getCurrentStats())).entrySet()) {
        this.keys.add("S " + (String)entry1.getKey());
        this.values.add(this.fontRendererObj.trimStringToWidth(entry1.getValue(), this.width - 220));
      }  
    this.list = new List();
  }
  
  public void handleMouseInput() throws IOException {
    super.handleMouseInput();
    this.list.handleMouseInput();
  }
  
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button.enabled) {
      if (button.id == 2) {
        this.game_settings_2.saveOptions();
        this.game_settings_2.saveOptions();
        this.mc.displayGuiScreen(this.lastScreen);
      } 
      if (button.id == 1) {
        this.game_settings_2.setOptionValue(GameSettings.Options.SNOOPER_ENABLED, 1);
        this.toggleButton.displayString = this.game_settings_2.getKeyBinding(GameSettings.Options.SNOOPER_ENABLED);
      } 
    } 
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();
    this.list.drawScreen(mouseX, mouseY, partialTicks);
    drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 8, 16777215);
    int i = 22;
    byte b;
    int j;
    String[] arrayOfString;
    for (j = (arrayOfString = this.desc).length, b = 0; b < j; ) {
      String s = arrayOfString[b];
      drawCenteredString(this.fontRendererObj, s, this.width / 2, i, 8421504);
      i += this.fontRendererObj.FONT_HEIGHT;
      b++;
    } 
    super.drawScreen(mouseX, mouseY, partialTicks);
  }
  
  class List extends GuiSlot {
    public List() {
      super(GuiSnooper.this.mc, GuiSnooper.this.width, GuiSnooper.this.height, 80, GuiSnooper.this.height - 40, GuiSnooper.this.fontRendererObj.FONT_HEIGHT + 1);
    }
    
    protected int getSize() {
      return GuiSnooper.this.keys.size();
    }
    
    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {}
    
    protected boolean isSelected(int slotIndex) {
      return false;
    }
    
    protected void drawBackground() {}
    
    protected void func_192637_a(int p_192637_1_, int p_192637_2_, int p_192637_3_, int p_192637_4_, int p_192637_5_, int p_192637_6_, float p_192637_7_) {
      GuiSnooper.this.fontRendererObj.drawString(GuiSnooper.this.keys.get(p_192637_1_), 10, p_192637_3_, 16777215);
      GuiSnooper.this.fontRendererObj.drawString(GuiSnooper.this.values.get(p_192637_1_), 230, p_192637_3_, 16777215);
    }
    
    protected int getScrollBarX() {
      return this.width - 10;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\gui\GuiSnooper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */