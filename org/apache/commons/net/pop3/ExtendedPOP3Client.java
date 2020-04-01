package org.apache.commons.net.pop3;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.net.util.Base64;

public class ExtendedPOP3Client extends POP3SClient {
  public boolean auth(AUTH_METHOD method, String username, String password) throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
    byte[] serverChallenge;
    Mac hmac_md5;
    byte[] hmacResult;
    byte[] usernameBytes;
    byte[] toEncode;
    if (sendCommand(13, method.getAuthName()) != 
      2)
      return false; 
    switch (method) {
      case PLAIN:
        return (sendCommand(
            new String(
              Base64.encodeBase64(("\000" + username + "\000" + password).getBytes(getCharset())), 
              getCharset())) == 0);
      case null:
        serverChallenge = Base64.decodeBase64(getReplyString().substring(2).trim());
        hmac_md5 = Mac.getInstance("HmacMD5");
        hmac_md5.init(new SecretKeySpec(password.getBytes(getCharset()), "HmacMD5"));
        hmacResult = _convertToHexString(hmac_md5.doFinal(serverChallenge)).getBytes(getCharset());
        usernameBytes = username.getBytes(getCharset());
        toEncode = new byte[usernameBytes.length + 1 + hmacResult.length];
        System.arraycopy(usernameBytes, 0, toEncode, 0, usernameBytes.length);
        toEncode[usernameBytes.length] = 32;
        System.arraycopy(hmacResult, 0, toEncode, usernameBytes.length + 1, hmacResult.length);
        return (sendCommand(Base64.encodeBase64StringUnChunked(toEncode)) == 0);
    } 
    return false;
  }
  
  private String _convertToHexString(byte[] a) {
    StringBuilder result = new StringBuilder(a.length * 2);
    byte b;
    int i;
    byte[] arrayOfByte;
    for (i = (arrayOfByte = a).length, b = 0; b < i; ) {
      byte element = arrayOfByte[b];
      if ((element & 0xFF) <= 15)
        result.append("0"); 
      result.append(Integer.toHexString(element & 0xFF));
      b++;
    } 
    return result.toString();
  }
  
  public enum AUTH_METHOD {
    PLAIN(
      "PLAIN"),
    CRAM_MD5(
      "CRAM-MD5");
    
    private final String methodName;
    
    AUTH_METHOD(String methodName) {
      this.methodName = methodName;
    }
    
    public final String getAuthName() {
      return this.methodName;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\pop3\ExtendedPOP3Client.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */