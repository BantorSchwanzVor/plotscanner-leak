package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.GameType;

public class GuiShareToLan extends GuiScreen {
  private final GuiScreen lastScreen;
  
  private GuiButton allowCheatsButton;
  
  private GuiButton gameModeButton;
  
  private String gameMode = "survival";
  
  private boolean allowCheats;
  
  public GuiShareToLan(GuiScreen p_i1055_1_) {
    this.lastScreen = p_i1055_1_;
  }
  
  public void initGui() {
    this.buttonList.clear();
    this.buttonList.add(new GuiButton(101, this.width / 2 - 155, this.height - 28, 150, 20, I18n.format("lanServer.start", new Object[0])));
    this.buttonList.add(new GuiButton(102, this.width / 2 + 5, this.height - 28, 150, 20, I18n.format("gui.cancel", new Object[0])));
    this.gameModeButton = addButton(new GuiButton(104, this.width / 2 - 155, 100, 150, 20, I18n.format("selectWorld.gameMode", new Object[0])));
    this.allowCheatsButton = addButton(new GuiButton(103, this.width / 2 + 5, 100, 150, 20, I18n.format("selectWorld.allowCommands", new Object[0])));
    updateDisplayNames();
  }
  
  private void updateDisplayNames() {
    this.gameModeButton.displayString = String.valueOf(I18n.format("selectWorld.gameMode", new Object[0])) + ": " + I18n.format("selectWorld.gameMode." + this.gameMode, new Object[0]);
    this.allowCheatsButton.displayString = String.valueOf(I18n.format("selectWorld.allowCommands", new Object[0])) + " ";
    if (this.allowCheats) {
      this.allowCheatsButton.displayString = String.valueOf(this.allowCheatsButton.displayString) + I18n.format("options.on", new Object[0]);
    } else {
      this.allowCheatsButton.displayString = String.valueOf(this.allowCheatsButton.displayString) + I18n.format("options.off", new Object[0]);
    } 
  }
  
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button.id == 102) {
      this.mc.displayGuiScreen(this.lastScreen);
    } else if (button.id == 104) {
      if ("spectator".equals(this.gameMode)) {
        this.gameMode = "creative";
      } else if ("creative".equals(this.gameMode)) {
        this.gameMode = "adventure";
      } else if ("adventure".equals(this.gameMode)) {
        this.gameMode = "survival";
      } else {
        this.gameMode = "spectator";
      } 
      updateDisplayNames();
    } else if (button.id == 103) {
      this.allowCheats = !this.allowCheats;
      updateDisplayNames();
    } else if (button.id == 101) {
      TextComponentString textComponentString;
      this.mc.displayGuiScreen(null);
      String s = this.mc.getIntegratedServer().shareToLAN(GameType.getByName(this.gameMode), this.allowCheats);
      if (s != null) {
        TextComponentTranslation textComponentTranslation = new TextComponentTranslation("commands.publish.started", new Object[] { s });
      } else {
        textComponentString = new TextComponentString("commands.publish.failed");
      } 
      this.mc.ingameGUI.getChatGUI().printChatMessage((ITextComponent)textComponentString);
    } 
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();
    drawCenteredString(this.fontRendererObj, I18n.format("lanServer.title", new Object[0]), this.width / 2, 50, 16777215);
    drawCenteredString(this.fontRendererObj, I18n.format("lanServer.otherPlayers", new Object[0]), this.width / 2, 82, 16777215);
    super.drawScreen(mouseX, mouseY, partialTicks);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\gui\GuiShareToLan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */