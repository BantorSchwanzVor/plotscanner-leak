package shadersmod.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.math.MathHelper;
import optifine.Config;
import optifine.GuiScreenOF;
import optifine.Lang;
import optifine.StrUtils;

public class GuiShaderOptions extends GuiScreenOF {
  private GuiScreen prevScreen;
  
  protected String title;
  
  private GameSettings settings;
  
  private int lastMouseX;
  
  private int lastMouseY;
  
  private long mouseStillTime;
  
  private String screenName;
  
  private String screenText;
  
  private boolean changed;
  
  public static final String OPTION_PROFILE = "<profile>";
  
  public static final String OPTION_EMPTY = "<empty>";
  
  public static final String OPTION_REST = "*";
  
  public GuiShaderOptions(GuiScreen guiscreen, GameSettings gamesettings) {
    this.lastMouseX = 0;
    this.lastMouseY = 0;
    this.mouseStillTime = 0L;
    this.screenName = null;
    this.screenText = null;
    this.changed = false;
    this.title = "Shader Options";
    this.prevScreen = guiscreen;
    this.settings = gamesettings;
  }
  
  public GuiShaderOptions(GuiScreen guiscreen, GameSettings gamesettings, String screenName) {
    this(guiscreen, gamesettings);
    this.screenName = screenName;
    if (screenName != null)
      this.screenText = Shaders.translate("screen." + screenName, screenName); 
  }
  
  public void initGui() {
    this.title = I18n.format("of.options.shaderOptionsTitle", new Object[0]);
    int i = 100;
    int j = 0;
    int k = 30;
    int l = 20;
    int i1 = this.width - 130;
    int j1 = 120;
    int k1 = 20;
    int l1 = Shaders.getShaderPackColumns(this.screenName, 2);
    ShaderOption[] ashaderoption = Shaders.getShaderPackOptions(this.screenName);
    if (ashaderoption != null) {
      int i2 = MathHelper.ceil(ashaderoption.length / 9.0D);
      if (l1 < i2)
        l1 = i2; 
      for (int j2 = 0; j2 < ashaderoption.length; j2++) {
        ShaderOption shaderoption = ashaderoption[j2];
        if (shaderoption != null && shaderoption.isVisible()) {
          GuiButtonShaderOption guibuttonshaderoption;
          int k2 = j2 % l1;
          int l2 = j2 / l1;
          int i3 = Math.min(this.width / l1, 200);
          j = (this.width - i3 * l1) / 2;
          int j3 = k2 * i3 + 5 + j;
          int k3 = k + l2 * l;
          int l3 = i3 - 10;
          String s = getButtonText(shaderoption, l3);
          if (Shaders.isShaderPackOptionSlider(shaderoption.getName())) {
            guibuttonshaderoption = new GuiSliderShaderOption(i + j2, j3, k3, l3, k1, shaderoption, s);
          } else {
            guibuttonshaderoption = new GuiButtonShaderOption(i + j2, j3, k3, l3, k1, shaderoption, s);
          } 
          guibuttonshaderoption.enabled = shaderoption.isEnabled();
          this.buttonList.add(guibuttonshaderoption);
        } 
      } 
    } 
    this.buttonList.add(new GuiButton(201, this.width / 2 - j1 - 20, this.height / 6 + 168 + 11, j1, k1, I18n.format("controls.reset", new Object[0])));
    this.buttonList.add(new GuiButton(200, this.width / 2 + 20, this.height / 6 + 168 + 11, j1, k1, I18n.format("gui.done", new Object[0])));
  }
  
  public static String getButtonText(ShaderOption so, int btnWidth) {
    String s = so.getNameText();
    if (so instanceof ShaderOptionScreen) {
      ShaderOptionScreen shaderoptionscreen = (ShaderOptionScreen)so;
      return String.valueOf(s) + "...";
    } 
    FontRenderer fontrenderer = (Config.getMinecraft()).fontRendererObj;
    for (int i = fontrenderer.getStringWidth(": " + Lang.getOff()) + 5; fontrenderer.getStringWidth(s) + i >= btnWidth && s.length() > 0; s = s.substring(0, s.length() - 1));
    String s1 = so.isChanged() ? so.getValueColor(so.getValue()) : "";
    String s2 = so.getValueText(so.getValue());
    return String.valueOf(s) + ": " + s1 + s2;
  }
  
  protected void actionPerformed(GuiButton guibutton) {
    if (guibutton.enabled) {
      if (guibutton.id < 200 && guibutton instanceof GuiButtonShaderOption) {
        GuiButtonShaderOption guibuttonshaderoption = (GuiButtonShaderOption)guibutton;
        ShaderOption shaderoption = guibuttonshaderoption.getShaderOption();
        if (shaderoption instanceof ShaderOptionScreen) {
          String s = shaderoption.getName();
          GuiShaderOptions guishaderoptions = new GuiShaderOptions((GuiScreen)this, this.settings, s);
          this.mc.displayGuiScreen((GuiScreen)guishaderoptions);
          return;
        } 
        if (isShiftKeyDown()) {
          shaderoption.resetValue();
        } else {
          shaderoption.nextValue();
        } 
        updateAllButtons();
        this.changed = true;
      } 
      if (guibutton.id == 201) {
        ShaderOption[] ashaderoption = Shaders.getChangedOptions(Shaders.getShaderPackOptions());
        for (int i = 0; i < ashaderoption.length; i++) {
          ShaderOption shaderoption1 = ashaderoption[i];
          shaderoption1.resetValue();
          this.changed = true;
        } 
        updateAllButtons();
      } 
      if (guibutton.id == 200) {
        if (this.changed) {
          Shaders.saveShaderPackOptions();
          this.changed = false;
          Shaders.uninit();
        } 
        this.mc.displayGuiScreen(this.prevScreen);
      } 
    } 
  }
  
  protected void actionPerformedRightClick(GuiButton btn) {
    if (btn instanceof GuiButtonShaderOption) {
      GuiButtonShaderOption guibuttonshaderoption = (GuiButtonShaderOption)btn;
      ShaderOption shaderoption = guibuttonshaderoption.getShaderOption();
      if (isShiftKeyDown()) {
        shaderoption.resetValue();
      } else {
        shaderoption.prevValue();
      } 
      updateAllButtons();
      this.changed = true;
    } 
  }
  
  public void onGuiClosed() {
    super.onGuiClosed();
    if (this.changed) {
      Shaders.saveShaderPackOptions();
      this.changed = false;
      Shaders.uninit();
    } 
  }
  
  private void updateAllButtons() {
    for (GuiButton guibutton : this.buttonList) {
      if (guibutton instanceof GuiButtonShaderOption) {
        GuiButtonShaderOption guibuttonshaderoption = (GuiButtonShaderOption)guibutton;
        ShaderOption shaderoption = guibuttonshaderoption.getShaderOption();
        if (shaderoption instanceof ShaderOptionProfile) {
          ShaderOptionProfile shaderoptionprofile = (ShaderOptionProfile)shaderoption;
          shaderoptionprofile.updateProfile();
        } 
        guibuttonshaderoption.displayString = getButtonText(shaderoption, guibuttonshaderoption.getButtonWidth());
        guibuttonshaderoption.valueChanged();
      } 
    } 
  }
  
  public void drawScreen(int x, int y, float f) {
    drawDefaultBackground();
    if (this.screenText != null) {
      drawCenteredString(this.fontRendererObj, this.screenText, this.width / 2, 15, 16777215);
    } else {
      drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 15, 16777215);
    } 
    super.drawScreen(x, y, f);
    if (Math.abs(x - this.lastMouseX) <= 5 && Math.abs(y - this.lastMouseY) <= 5) {
      drawTooltips(x, y, this.buttonList);
    } else {
      this.lastMouseX = x;
      this.lastMouseY = y;
      this.mouseStillTime = System.currentTimeMillis();
    } 
  }
  
  private void drawTooltips(int x, int y, List buttons) {
    int i = 700;
    if (System.currentTimeMillis() >= this.mouseStillTime + i) {
      int j = this.width / 2 - 150;
      int k = this.height / 6 - 7;
      if (y <= k + 98)
        k += 105; 
      int l = j + 150 + 150;
      int i1 = k + 84 + 10;
      GuiButton guibutton = getSelectedButton(buttons, x, y);
      if (guibutton instanceof GuiButtonShaderOption) {
        GuiButtonShaderOption guibuttonshaderoption = (GuiButtonShaderOption)guibutton;
        ShaderOption shaderoption = guibuttonshaderoption.getShaderOption();
        String[] astring = makeTooltipLines(shaderoption, l - j);
        if (astring == null)
          return; 
        drawGradientRect(j, k, l, i1, -536870912, -536870912);
        for (int j1 = 0; j1 < astring.length; j1++) {
          String s = astring[j1];
          int k1 = 14540253;
          if (s.endsWith("!"))
            k1 = 16719904; 
          this.fontRendererObj.drawStringWithShadow(s, (j + 5), (k + 5 + j1 * 11), k1);
        } 
      } 
    } 
  }
  
  private String[] makeTooltipLines(ShaderOption so, int width) {
    if (so instanceof ShaderOptionProfile)
      return null; 
    String s = so.getNameText();
    String s1 = Config.normalize(so.getDescriptionText()).trim();
    String[] astring = splitDescription(s1);
    String s2 = null;
    if (!s.equals(so.getName()) && this.settings.advancedItemTooltips)
      s2 = "§8" + Lang.get("of.general.id") + ": " + so.getName(); 
    String s3 = null;
    if (so.getPaths() != null && this.settings.advancedItemTooltips)
      s3 = "§8" + Lang.get("of.general.from") + ": " + Config.arrayToString((Object[])so.getPaths()); 
    String s4 = null;
    if (so.getValueDefault() != null && this.settings.advancedItemTooltips) {
      String s5 = so.isEnabled() ? so.getValueText(so.getValueDefault()) : Lang.get("of.general.ambiguous");
      s4 = "§8" + Lang.getDefault() + ": " + s5;
    } 
    List<String> list = new ArrayList<>();
    list.add(s);
    list.addAll(Arrays.asList(astring));
    if (s2 != null)
      list.add(s2); 
    if (s3 != null)
      list.add(s3); 
    if (s4 != null)
      list.add(s4); 
    String[] astring1 = makeTooltipLines(width, list);
    return astring1;
  }
  
  private String[] splitDescription(String desc) {
    if (desc.length() <= 0)
      return new String[0]; 
    desc = StrUtils.removePrefix(desc, "//");
    String[] astring = desc.split("\\. ");
    for (int i = 0; i < astring.length; i++) {
      astring[i] = "- " + astring[i].trim();
      astring[i] = StrUtils.removeSuffix(astring[i], ".");
    } 
    return astring;
  }
  
  private String[] makeTooltipLines(int width, List<String> args) {
    FontRenderer fontrenderer = (Config.getMinecraft()).fontRendererObj;
    List<String> list = new ArrayList<>();
    for (int i = 0; i < args.size(); i++) {
      String s = args.get(i);
      if (s != null && s.length() > 0)
        for (String s1 : fontrenderer.listFormattedStringToWidth(s, width))
          list.add(s1);  
    } 
    String[] astring = list.<String>toArray(new String[list.size()]);
    return astring;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\shadersmod\client\GuiShaderOptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */