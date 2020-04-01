package org.apache.commons.net.imap;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import org.apache.commons.net.util.Base64;

public class AuthenticatingIMAPClient extends IMAPSClient {
  public AuthenticatingIMAPClient() {
    this("TLS", false);
  }
  
  public AuthenticatingIMAPClient(boolean implicit) {
    this("TLS", implicit);
  }
  
  public AuthenticatingIMAPClient(String proto) {
    this(proto, false);
  }
  
  public AuthenticatingIMAPClient(String proto, boolean implicit) {
    this(proto, implicit, (SSLContext)null);
  }
  
  public AuthenticatingIMAPClient(String proto, boolean implicit, SSLContext ctx) {
    super(proto, implicit, ctx);
  }
  
  public AuthenticatingIMAPClient(boolean implicit, SSLContext ctx) {
    this("TLS", implicit, ctx);
  }
  
  public AuthenticatingIMAPClient(SSLContext context) {
    this(false, context);
  }
  
  public boolean authenticate(AUTH_METHOD method, String username, String password) throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
    return auth(method, username, password);
  }
  
  public boolean auth(AUTH_METHOD method, String username, String password) throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
    int i;
    byte[] serverChallenge;
    int result;
    Mac hmac_md5;
    byte[] hmacResult;
    byte[] usernameBytes;
    byte[] toEncode;
    int j;
    if (!IMAPReply.isContinuation(sendCommand(IMAPCommand.AUTHENTICATE, method.getAuthName())))
      return false; 
    switch (method) {
      case PLAIN:
        i = sendData(
            Base64.encodeBase64StringUnChunked(("\000" + username + "\000" + password)
              .getBytes(getCharset())));
        if (i == 0)
          setState(IMAP.IMAPState.AUTH_STATE); 
        return (i == 0);
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
        j = sendData(Base64.encodeBase64StringUnChunked(toEncode));
        if (j == 0)
          setState(IMAP.IMAPState.AUTH_STATE); 
        return (j == 0);
      case LOGIN:
        if (sendData(Base64.encodeBase64StringUnChunked(username.getBytes(getCharset()))) != 3)
          return false; 
        result = sendData(Base64.encodeBase64StringUnChunked(password.getBytes(getCharset())));
        if (result == 0)
          setState(IMAP.IMAPState.AUTH_STATE); 
        return (result == 0);
      case XOAUTH:
      case XOAUTH2:
        result = sendData(username);
        if (result == 0)
          setState(IMAP.IMAPState.AUTH_STATE); 
        return (result == 0);
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
      "CRAM-MD5"),
    LOGIN(
      "LOGIN"),
    XOAUTH(
      "XOAUTH"),
    XOAUTH2(
      "XOAUTH2");
    
    private final String authName;
    
    AUTH_METHOD(String name) {
      this.authName = name;
    }
    
    public final String getAuthName() {
      return this.authName;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\imap\AuthenticatingIMAPClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */