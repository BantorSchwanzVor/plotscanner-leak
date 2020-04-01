package com.mysql.cj.protocol.a.authentication;

import com.mysql.cj.protocol.AuthenticationPlugin;
import com.mysql.cj.protocol.Message;
import com.mysql.cj.protocol.Protocol;
import com.mysql.cj.protocol.Security;
import com.mysql.cj.protocol.a.NativeConstants;
import com.mysql.cj.protocol.a.NativePacketPayload;
import java.util.List;

public class MysqlNativePasswordPlugin implements AuthenticationPlugin<NativePacketPayload> {
  private Protocol<NativePacketPayload> protocol;
  
  private String password = null;
  
  public void init(Protocol<NativePacketPayload> prot) {
    this.protocol = prot;
  }
  
  public void destroy() {
    this.password = null;
  }
  
  public String getProtocolPluginName() {
    return "mysql_native_password";
  }
  
  public boolean requiresConfidentiality() {
    return false;
  }
  
  public boolean isReusable() {
    return true;
  }
  
  public void setAuthenticationParameters(String user, String password) {
    this.password = password;
  }
  
  public boolean nextAuthenticationStep(NativePacketPayload fromServer, List<NativePacketPayload> toServer) {
    toServer.clear();
    NativePacketPayload bresp = null;
    String pwd = this.password;
    if (fromServer == null || pwd == null || pwd.length() == 0) {
      bresp = new NativePacketPayload(new byte[0]);
    } else {
      bresp = new NativePacketPayload(Security.scramble411(pwd, fromServer.readBytes(NativeConstants.StringSelfDataType.STRING_TERM), this.protocol.getPasswordCharacterEncoding()));
    } 
    toServer.add(bresp);
    return true;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\a\authentication\MysqlNativePasswordPlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */