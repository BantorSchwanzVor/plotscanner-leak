package com.mysql.cj.protocol.a.authentication;

import com.mysql.cj.protocol.AuthenticationPlugin;
import com.mysql.cj.protocol.Message;
import com.mysql.cj.protocol.Protocol;
import com.mysql.cj.protocol.a.NativeConstants;
import com.mysql.cj.protocol.a.NativePacketPayload;
import com.mysql.cj.util.StringUtils;
import java.util.List;

public class MysqlClearPasswordPlugin implements AuthenticationPlugin<NativePacketPayload> {
  private Protocol<NativePacketPayload> protocol;
  
  private String password = null;
  
  public void init(Protocol<NativePacketPayload> prot) {
    this.protocol = prot;
  }
  
  public void destroy() {
    this.password = null;
  }
  
  public String getProtocolPluginName() {
    return "mysql_clear_password";
  }
  
  public boolean requiresConfidentiality() {
    return true;
  }
  
  public boolean isReusable() {
    return true;
  }
  
  public void setAuthenticationParameters(String user, String password) {
    this.password = password;
  }
  
  public boolean nextAuthenticationStep(NativePacketPayload fromServer, List<NativePacketPayload> toServer) {
    toServer.clear();
    String encoding = this.protocol.versionMeetsMinimum(5, 7, 6) ? this.protocol.getPasswordCharacterEncoding() : "UTF-8";
    NativePacketPayload bresp = new NativePacketPayload(StringUtils.getBytes((this.password != null) ? this.password : "", encoding));
    bresp.setPosition(bresp.getPayloadLength());
    bresp.writeInteger(NativeConstants.IntegerDataType.INT1, 0L);
    bresp.setPosition(0);
    toServer.add(bresp);
    return true;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\a\authentication\MysqlClearPasswordPlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */