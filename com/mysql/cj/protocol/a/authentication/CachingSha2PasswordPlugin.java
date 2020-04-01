package com.mysql.cj.protocol.a.authentication;

import com.mysql.cj.Messages;
import com.mysql.cj.conf.PropertyKey;
import com.mysql.cj.exceptions.CJException;
import com.mysql.cj.exceptions.ExceptionFactory;
import com.mysql.cj.exceptions.UnableToConnectException;
import com.mysql.cj.protocol.Message;
import com.mysql.cj.protocol.Protocol;
import com.mysql.cj.protocol.Security;
import com.mysql.cj.protocol.a.NativeConstants;
import com.mysql.cj.protocol.a.NativePacketPayload;
import com.mysql.cj.util.StringUtils;
import java.util.List;

public class CachingSha2PasswordPlugin extends Sha256PasswordPlugin {
  public static String PLUGIN_NAME = "caching_sha2_password";
  
  public enum AuthStage {
    FAST_AUTH_SEND_SCRAMBLE, FAST_AUTH_READ_RESULT, FAST_AUTH_COMPLETE, FULL_AUTH;
  }
  
  private AuthStage stage = AuthStage.FAST_AUTH_SEND_SCRAMBLE;
  
  public void init(Protocol<NativePacketPayload> prot) {
    super.init(prot);
    this.stage = AuthStage.FAST_AUTH_SEND_SCRAMBLE;
  }
  
  public void reset() {
    this.stage = AuthStage.FAST_AUTH_SEND_SCRAMBLE;
  }
  
  public void destroy() {
    this.stage = AuthStage.FAST_AUTH_SEND_SCRAMBLE;
    super.destroy();
  }
  
  public String getProtocolPluginName() {
    return PLUGIN_NAME;
  }
  
  public boolean nextAuthenticationStep(NativePacketPayload fromServer, List<NativePacketPayload> toServer) {
    toServer.clear();
    if (this.password == null || this.password.length() == 0 || fromServer == null) {
      NativePacketPayload bresp = new NativePacketPayload(new byte[] { 0 });
      toServer.add(bresp);
    } else {
      try {
        if (this.stage == AuthStage.FAST_AUTH_SEND_SCRAMBLE) {
          this.seed = fromServer.readString(NativeConstants.StringSelfDataType.STRING_TERM, null);
          toServer.add(new NativePacketPayload(
                Security.scrambleCachingSha2(StringUtils.getBytes(this.password, this.protocol.getPasswordCharacterEncoding()), this.seed.getBytes())));
          this.stage = AuthStage.FAST_AUTH_READ_RESULT;
          return true;
        } 
        if (this.stage == AuthStage.FAST_AUTH_READ_RESULT) {
          int fastAuthResult = fromServer.readBytes(NativeConstants.StringLengthDataType.STRING_FIXED, 1)[0];
          switch (fastAuthResult) {
            case 3:
              this.stage = AuthStage.FAST_AUTH_COMPLETE;
              return true;
            case 4:
              this.stage = AuthStage.FULL_AUTH;
              break;
            default:
              throw ExceptionFactory.createException("Unknown server response after fast auth.", this.protocol.getExceptionInterceptor());
          } 
        } 
        if (this.protocol.getSocketConnection().isSSLEstablished()) {
          NativePacketPayload bresp = new NativePacketPayload(StringUtils.getBytes(this.password, this.protocol.getPasswordCharacterEncoding()));
          bresp.setPosition(bresp.getPayloadLength());
          bresp.writeInteger(NativeConstants.IntegerDataType.INT1, 0L);
          bresp.setPosition(0);
          toServer.add(bresp);
        } else if (this.serverRSAPublicKeyFile.getValue() != null) {
          NativePacketPayload bresp = new NativePacketPayload(encryptPassword());
          toServer.add(bresp);
        } else {
          if (!((Boolean)this.protocol.getPropertySet().getBooleanProperty(PropertyKey.allowPublicKeyRetrieval).getValue()).booleanValue())
            throw (UnableToConnectException)ExceptionFactory.createException(UnableToConnectException.class, Messages.getString("Sha256PasswordPlugin.2"), this.protocol
                .getExceptionInterceptor()); 
          if (this.publicKeyRequested && fromServer.getPayloadLength() > 20) {
            this.publicKeyString = fromServer.readString(NativeConstants.StringSelfDataType.STRING_TERM, null);
            NativePacketPayload bresp = new NativePacketPayload(encryptPassword());
            toServer.add(bresp);
            this.publicKeyRequested = false;
          } else {
            NativePacketPayload bresp = new NativePacketPayload(new byte[] { 2 });
            toServer.add(bresp);
            this.publicKeyRequested = true;
          } 
        } 
      } catch (CJException|java.security.DigestException e) {
        throw ExceptionFactory.createException(e.getMessage(), e, this.protocol.getExceptionInterceptor());
      } 
    } 
    return true;
  }
  
  protected byte[] encryptPassword() {
    if (this.protocol.versionMeetsMinimum(8, 0, 5))
      return super.encryptPassword(); 
    return encryptPassword("RSA/ECB/PKCS1Padding");
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\a\authentication\CachingSha2PasswordPlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */