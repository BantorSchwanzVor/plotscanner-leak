package net.minecraft.util;

public interface IProgressUpdate {
  void displaySavingString(String paramString);
  
  void resetProgressAndMessage(String paramString);
  
  void displayLoadingString(String paramString);
  
  void setLoadingProgress(int paramInt);
  
  void setDoneWorking();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\IProgressUpdate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */