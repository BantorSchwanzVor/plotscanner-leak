package net.minecraft.client.gui;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.ITabCompleter;
import net.minecraft.util.TabCompleter;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;

public class GuiCommandBlock extends GuiScreen implements ITabCompleter {
  private GuiTextField commandTextField;
  
  private GuiTextField previousOutputTextField;
  
  private final TileEntityCommandBlock commandBlock;
  
  private GuiButton doneBtn;
  
  private GuiButton cancelBtn;
  
  private GuiButton outputBtn;
  
  private GuiButton modeBtn;
  
  private GuiButton conditionalBtn;
  
  private GuiButton autoExecBtn;
  
  private boolean trackOutput;
  
  private TileEntityCommandBlock.Mode commandBlockMode = TileEntityCommandBlock.Mode.REDSTONE;
  
  private TabCompleter tabCompleter;
  
  private boolean conditional;
  
  private boolean automatic;
  
  public GuiCommandBlock(TileEntityCommandBlock commandBlockIn) {
    this.commandBlock = commandBlockIn;
  }
  
  public void updateScreen() {
    this.commandTextField.updateCursorCounter();
  }
  
  public void initGui() {
    final CommandBlockBaseLogic commandblockbaselogic = this.commandBlock.getCommandBlockLogic();
    Keyboard.enableRepeatEvents(true);
    this.buttonList.clear();
    this.doneBtn = addButton(new GuiButton(0, this.width / 2 - 4 - 150, this.height / 4 + 120 + 12, 150, 20, I18n.format("gui.done", new Object[0])));
    this.cancelBtn = addButton(new GuiButton(1, this.width / 2 + 4, this.height / 4 + 120 + 12, 150, 20, I18n.format("gui.cancel", new Object[0])));
    this.outputBtn = addButton(new GuiButton(4, this.width / 2 + 150 - 20, 135, 20, 20, "O"));
    this.modeBtn = addButton(new GuiButton(5, this.width / 2 - 50 - 100 - 4, 165, 100, 20, I18n.format("advMode.mode.sequence", new Object[0])));
    this.conditionalBtn = addButton(new GuiButton(6, this.width / 2 - 50, 165, 100, 20, I18n.format("advMode.mode.unconditional", new Object[0])));
    this.autoExecBtn = addButton(new GuiButton(7, this.width / 2 + 50 + 4, 165, 100, 20, I18n.format("advMode.mode.redstoneTriggered", new Object[0])));
    this.commandTextField = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 150, 50, 300, 20);
    this.commandTextField.setMaxStringLength(32500);
    this.commandTextField.setFocused(true);
    this.previousOutputTextField = new GuiTextField(3, this.fontRendererObj, this.width / 2 - 150, 135, 276, 20);
    this.previousOutputTextField.setMaxStringLength(32500);
    this.previousOutputTextField.setEnabled(false);
    this.previousOutputTextField.setText("-");
    this.doneBtn.enabled = false;
    this.outputBtn.enabled = false;
    this.modeBtn.enabled = false;
    this.conditionalBtn.enabled = false;
    this.autoExecBtn.enabled = false;
    this.tabCompleter = new TabCompleter(this.commandTextField, true) {
        @Nullable
        public BlockPos getTargetBlockPos() {
          return commandblockbaselogic.getPosition();
        }
      };
  }
  
  public void updateGui() {
    CommandBlockBaseLogic commandblockbaselogic = this.commandBlock.getCommandBlockLogic();
    this.commandTextField.setText(commandblockbaselogic.getCommand());
    this.trackOutput = commandblockbaselogic.shouldTrackOutput();
    this.commandBlockMode = this.commandBlock.getMode();
    this.conditional = this.commandBlock.isConditional();
    this.automatic = this.commandBlock.isAuto();
    updateCmdOutput();
    updateMode();
    updateConditional();
    updateAutoExec();
    this.doneBtn.enabled = true;
    this.outputBtn.enabled = true;
    this.modeBtn.enabled = true;
    this.conditionalBtn.enabled = true;
    this.autoExecBtn.enabled = true;
  }
  
  public void onGuiClosed() {
    Keyboard.enableRepeatEvents(false);
  }
  
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button.enabled) {
      CommandBlockBaseLogic commandblockbaselogic = this.commandBlock.getCommandBlockLogic();
      if (button.id == 1) {
        commandblockbaselogic.setTrackOutput(this.trackOutput);
        this.mc.displayGuiScreen(null);
      } else if (button.id == 0) {
        PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
        commandblockbaselogic.fillInInfo((ByteBuf)packetbuffer);
        packetbuffer.writeString(this.commandTextField.getText());
        packetbuffer.writeBoolean(commandblockbaselogic.shouldTrackOutput());
        packetbuffer.writeString(this.commandBlockMode.name());
        packetbuffer.writeBoolean(this.conditional);
        packetbuffer.writeBoolean(this.automatic);
        this.mc.getConnection().sendPacket((Packet)new CPacketCustomPayload("MC|AutoCmd", packetbuffer));
        if (!commandblockbaselogic.shouldTrackOutput())
          commandblockbaselogic.setLastOutput(null); 
        this.mc.displayGuiScreen(null);
      } else if (button.id == 4) {
        commandblockbaselogic.setTrackOutput(!commandblockbaselogic.shouldTrackOutput());
        updateCmdOutput();
      } else if (button.id == 5) {
        nextMode();
        updateMode();
      } else if (button.id == 6) {
        this.conditional = !this.conditional;
        updateConditional();
      } else if (button.id == 7) {
        this.automatic = !this.automatic;
        updateAutoExec();
      } 
    } 
  }
  
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    this.tabCompleter.resetRequested();
    if (keyCode == 15) {
      this.tabCompleter.complete();
    } else {
      this.tabCompleter.resetDidComplete();
    } 
    this.commandTextField.textboxKeyTyped(typedChar, keyCode);
    this.previousOutputTextField.textboxKeyTyped(typedChar, keyCode);
    if (keyCode != 28 && keyCode != 156) {
      if (keyCode == 1)
        actionPerformed(this.cancelBtn); 
    } else {
      actionPerformed(this.doneBtn);
    } 
  }
  
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    super.mouseClicked(mouseX, mouseY, mouseButton);
    this.commandTextField.mouseClicked(mouseX, mouseY, mouseButton);
    this.previousOutputTextField.mouseClicked(mouseX, mouseY, mouseButton);
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();
    drawCenteredString(this.fontRendererObj, I18n.format("advMode.setCommand", new Object[0]), this.width / 2, 20, 16777215);
    drawString(this.fontRendererObj, I18n.format("advMode.command", new Object[0]), this.width / 2 - 150, 40, 10526880);
    this.commandTextField.drawTextBox();
    int i = 75;
    int j = 0;
    drawString(this.fontRendererObj, I18n.format("advMode.nearestPlayer", new Object[0]), this.width / 2 - 140, i + j++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
    drawString(this.fontRendererObj, I18n.format("advMode.randomPlayer", new Object[0]), this.width / 2 - 140, i + j++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
    drawString(this.fontRendererObj, I18n.format("advMode.allPlayers", new Object[0]), this.width / 2 - 140, i + j++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
    drawString(this.fontRendererObj, I18n.format("advMode.allEntities", new Object[0]), this.width / 2 - 140, i + j++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
    drawString(this.fontRendererObj, I18n.format("advMode.self", new Object[0]), this.width / 2 - 140, i + j++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
    if (!this.previousOutputTextField.getText().isEmpty()) {
      i = i + j * this.fontRendererObj.FONT_HEIGHT + 1;
      drawString(this.fontRendererObj, I18n.format("advMode.previousOutput", new Object[0]), this.width / 2 - 150, i + 4, 10526880);
      this.previousOutputTextField.drawTextBox();
    } 
    super.drawScreen(mouseX, mouseY, partialTicks);
  }
  
  private void updateCmdOutput() {
    CommandBlockBaseLogic commandblockbaselogic = this.commandBlock.getCommandBlockLogic();
    if (commandblockbaselogic.shouldTrackOutput()) {
      this.outputBtn.displayString = "O";
      if (commandblockbaselogic.getLastOutput() != null)
        this.previousOutputTextField.setText(commandblockbaselogic.getLastOutput().getUnformattedText()); 
    } else {
      this.outputBtn.displayString = "X";
      this.previousOutputTextField.setText("-");
    } 
  }
  
  private void updateMode() {
    switch (this.commandBlockMode) {
      case SEQUENCE:
        this.modeBtn.displayString = I18n.format("advMode.mode.sequence", new Object[0]);
        break;
      case null:
        this.modeBtn.displayString = I18n.format("advMode.mode.auto", new Object[0]);
        break;
      case REDSTONE:
        this.modeBtn.displayString = I18n.format("advMode.mode.redstone", new Object[0]);
        break;
    } 
  }
  
  private void nextMode() {
    switch (this.commandBlockMode) {
      case SEQUENCE:
        this.commandBlockMode = TileEntityCommandBlock.Mode.AUTO;
        break;
      case null:
        this.commandBlockMode = TileEntityCommandBlock.Mode.REDSTONE;
        break;
      case REDSTONE:
        this.commandBlockMode = TileEntityCommandBlock.Mode.SEQUENCE;
        break;
    } 
  }
  
  private void updateConditional() {
    if (this.conditional) {
      this.conditionalBtn.displayString = I18n.format("advMode.mode.conditional", new Object[0]);
    } else {
      this.conditionalBtn.displayString = I18n.format("advMode.mode.unconditional", new Object[0]);
    } 
  }
  
  private void updateAutoExec() {
    if (this.automatic) {
      this.autoExecBtn.displayString = I18n.format("advMode.mode.autoexec.bat", new Object[0]);
    } else {
      this.autoExecBtn.displayString = I18n.format("advMode.mode.redstoneTriggered", new Object[0]);
    } 
  }
  
  public void setCompletions(String... newCompletions) {
    this.tabCompleter.setCompletions(newCompletions);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\gui\GuiCommandBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */