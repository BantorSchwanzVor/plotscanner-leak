package optifine;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class PlayerConfigurationReceiver implements IFileDownloadListener {
  private String player = null;
  
  public PlayerConfigurationReceiver(String p_i71_1_) {
    this.player = p_i71_1_;
  }
  
  public void fileDownloadFinished(String p_fileDownloadFinished_1_, byte[] p_fileDownloadFinished_2_, Throwable p_fileDownloadFinished_3_) {
    if (p_fileDownloadFinished_2_ != null)
      try {
        String s = new String(p_fileDownloadFinished_2_, "ASCII");
        JsonParser jsonparser = new JsonParser();
        JsonElement jsonelement = jsonparser.parse(s);
        PlayerConfigurationParser playerconfigurationparser = new PlayerConfigurationParser(this.player);
        PlayerConfiguration playerconfiguration = playerconfigurationparser.parsePlayerConfiguration(jsonelement);
        if (playerconfiguration != null) {
          playerconfiguration.setInitialized(true);
          PlayerConfigurations.setPlayerConfiguration(this.player, playerconfiguration);
        } 
      } catch (Exception exception) {
        Config.dbg("Error parsing configuration: " + p_fileDownloadFinished_1_ + ", " + exception.getClass().getName() + ": " + exception.getMessage());
      }  
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\optifine\PlayerConfigurationReceiver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */