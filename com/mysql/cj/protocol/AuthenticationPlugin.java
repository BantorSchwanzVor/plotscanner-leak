package com.mysql.cj.protocol;

import java.util.List;

public interface AuthenticationPlugin<M extends Message> {
  default void init(Protocol<M> protocol) {}
  
  default void reset() {}
  
  default void destroy() {}
  
  String getProtocolPluginName();
  
  boolean requiresConfidentiality();
  
  boolean isReusable();
  
  void setAuthenticationParameters(String paramString1, String paramString2);
  
  boolean nextAuthenticationStep(M paramM, List<M> paramList);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\AuthenticationPlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */