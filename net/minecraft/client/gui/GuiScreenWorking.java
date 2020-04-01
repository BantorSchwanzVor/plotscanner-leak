package net.minecraft.client.gui;

import net.minecraft.util.IProgressUpdate;

public class GuiScreenWorking extends GuiScreen implements IProgressUpdate {
  private String title = "";
  
  private String stage = "";
  
  private int progress;
  
  private boolean doneWorking;
  
  public void displaySavingString(String message) {
    resetProgressAndMessage(message);
  }
  
  public void resetProgressAndMessage(String message) {
    this.title = message;
    displayLoadingString("Working...");
  }
  
  public void displayLoadingString(String message) {
    this.stage = message;
    setLoadingProgress(0);
  }
  
  public void setLoadingProgress(int progress) {
    this.progress = progress;
  }
  
  public void setDoneWorking() {
    this.doneWorking = true;
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    if (this.doneWorking) {
      if (!this.mc.isConnectedToRealms())
        this.mc.displayGuiScreen(null); 
    } else {
      drawDefaultBackground();
      drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 70, 16777215);
      drawCenteredString(this.fontRendererObj, String.valueOf(this.stage) + " " + this.progress + "%", this.width / 2, 90, 16777215);
      super.drawScreen(mouseX, mouseY, partialTicks);
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\gui\GuiScreenWorking.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */