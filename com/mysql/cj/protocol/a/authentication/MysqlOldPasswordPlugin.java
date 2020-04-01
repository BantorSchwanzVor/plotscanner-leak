package com.mysql.cj.protocol.a.authentication;

import com.mysql.cj.protocol.AuthenticationPlugin;
import com.mysql.cj.protocol.Message;
import com.mysql.cj.protocol.Protocol;
import com.mysql.cj.protocol.a.NativeConstants;
import com.mysql.cj.protocol.a.NativePacketPayload;
import com.mysql.cj.util.StringUtils;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class MysqlOldPasswordPlugin implements AuthenticationPlugin<NativePacketPayload> {
  private Protocol<NativePacketPayload> protocol;
  
  private String password = null;
  
  public void init(Protocol<NativePacketPayload> prot) {
    this.protocol = prot;
  }
  
  public void destroy() {
    this.password = null;
  }
  
  public String getProtocolPluginName() {
    return "mysql_old_password";
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
      bresp = new NativePacketPayload(StringUtils.getBytes(
            newCrypt(pwd, fromServer.readString(NativeConstants.StringSelfDataType.STRING_TERM, null).substring(0, 8), this.protocol.getPasswordCharacterEncoding())));
      bresp.setPosition(bresp.getPayloadLength());
      bresp.writeInteger(NativeConstants.IntegerDataType.INT1, 0L);
      bresp.setPosition(0);
    } 
    toServer.add(bresp);
    return true;
  }
  
  private static String newCrypt(String password, String seed, String encoding) {
    if (password == null || password.length() == 0)
      return password; 
    long[] pw = newHash(seed.getBytes());
    long[] msg = hashPre41Password(password, encoding);
    long max = 1073741823L;
    long seed1 = (pw[0] ^ msg[0]) % max;
    long seed2 = (pw[1] ^ msg[1]) % max;
    char[] chars = new char[seed.length()];
    int i;
    for (i = 0; i < seed.length(); i++) {
      seed1 = (seed1 * 3L + seed2) % max;
      seed2 = (seed1 + seed2 + 33L) % max;
      double d1 = seed1 / max;
      byte b1 = (byte)(int)Math.floor(d1 * 31.0D + 64.0D);
      chars[i] = (char)b1;
    } 
    seed1 = (seed1 * 3L + seed2) % max;
    seed2 = (seed1 + seed2 + 33L) % max;
    double d = seed1 / max;
    byte b = (byte)(int)Math.floor(d * 31.0D);
    for (i = 0; i < seed.length(); i++)
      chars[i] = (char)(chars[i] ^ (char)b); 
    return new String(chars);
  }
  
  private static long[] hashPre41Password(String password, String encoding) {
    try {
      return newHash(password.replaceAll("\\s", "").getBytes(encoding));
    } catch (UnsupportedEncodingException e) {
      return new long[0];
    } 
  }
  
  private static long[] newHash(byte[] password) {
    long nr = 1345345333L;
    long add = 7L;
    long nr2 = 305419889L;
    for (byte b : password) {
      long tmp = (0xFF & b);
      nr ^= ((nr & 0x3FL) + add) * tmp + (nr << 8L);
      nr2 += nr2 << 8L ^ nr;
      add += tmp;
    } 
    long[] result = new long[2];
    result[0] = nr & 0x7FFFFFFFL;
    result[1] = nr2 & 0x7FFFFFFFL;
    return result;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\a\authentication\MysqlOldPasswordPlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */