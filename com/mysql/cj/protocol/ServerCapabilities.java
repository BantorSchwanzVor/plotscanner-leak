package com.mysql.cj.protocol;

import com.mysql.cj.ServerVersion;

public interface ServerCapabilities {
  int getCapabilityFlags();
  
  void setCapabilityFlags(int paramInt);
  
  ServerVersion getServerVersion();
  
  void setServerVersion(ServerVersion paramServerVersion);
  
  boolean serverSupportsFracSecs();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\ServerCapabilities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */