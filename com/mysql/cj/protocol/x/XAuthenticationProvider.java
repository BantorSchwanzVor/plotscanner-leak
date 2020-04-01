package com.mysql.cj.protocol.x;

import com.mysql.cj.conf.PropertyDefinitions;
import com.mysql.cj.conf.PropertyKey;
import com.mysql.cj.conf.PropertySet;
import com.mysql.cj.conf.RuntimeProperty;
import com.mysql.cj.exceptions.CJCommunicationsException;
import com.mysql.cj.exceptions.ExceptionInterceptor;
import com.mysql.cj.exceptions.WrongArgumentException;
import com.mysql.cj.protocol.AuthenticationProvider;
import com.mysql.cj.protocol.Protocol;
import com.mysql.cj.protocol.ServerSession;
import com.mysql.cj.util.StringUtils;
import com.mysql.cj.xdevapi.XDevAPIError;
import java.util.Arrays;
import java.util.List;

public class XAuthenticationProvider implements AuthenticationProvider<XMessage> {
  XProtocol protocol;
  
  private PropertyDefinitions.AuthMech authMech = null;
  
  private XMessageBuilder messageBuilder = new XMessageBuilder();
  
  public void init(Protocol<XMessage> prot, PropertySet propertySet, ExceptionInterceptor exceptionInterceptor) {
    this.protocol = (XProtocol)prot;
  }
  
  public void connect(ServerSession serverSession, String userName, String password, String database) {
    changeUser(serverSession, userName, password, database);
  }
  
  public void changeUser(ServerSession serverSession, String userName, String password, String database) {
    List<PropertyDefinitions.AuthMech> tryAuthMech;
    boolean overTLS = ((XServerCapabilities)this.protocol.getServerSession().getCapabilities()).getTls();
    RuntimeProperty<PropertyDefinitions.AuthMech> authMechProp = this.protocol.getPropertySet().getEnumProperty(PropertyKey.xdevapiAuth);
    if (overTLS || authMechProp.isExplicitlySet()) {
      tryAuthMech = Arrays.asList(new PropertyDefinitions.AuthMech[] { (PropertyDefinitions.AuthMech)authMechProp.getValue() });
    } else {
      tryAuthMech = Arrays.asList(new PropertyDefinitions.AuthMech[] { PropertyDefinitions.AuthMech.MYSQL41, PropertyDefinitions.AuthMech.SHA256_MEMORY });
    } 
    XProtocolError capturedAuthErr = null;
    for (PropertyDefinitions.AuthMech am : tryAuthMech) {
      this.authMech = am;
      try {
        byte[] nonce;
        byte[] salt;
        switch (this.authMech) {
          case SHA256_MEMORY:
            this.protocol.send(this.messageBuilder.buildSha256MemoryAuthStart(), 0);
            nonce = this.protocol.readAuthenticateContinue();
            this.protocol.send(this.messageBuilder.buildSha256MemoryAuthContinue(userName, password, nonce, database), 0);
            break;
          case MYSQL41:
            this.protocol.send(this.messageBuilder.buildMysql41AuthStart(), 0);
            salt = this.protocol.readAuthenticateContinue();
            this.protocol.send(this.messageBuilder.buildMysql41AuthContinue(userName, password, salt, database), 0);
            break;
          case PLAIN:
            if (overTLS) {
              this.protocol.send(this.messageBuilder.buildPlainAuthStart(userName, password, database), 0);
              break;
            } 
            throw new XProtocolError("PLAIN authentication is not allowed via unencrypted connection.");
          case EXTERNAL:
            this.protocol.send(this.messageBuilder.buildExternalAuthStart(database), 0);
            break;
          default:
            throw new WrongArgumentException("Unknown authentication mechanism '" + this.authMech + "'.");
        } 
      } catch (CJCommunicationsException e) {
        if (capturedAuthErr != null && e.getCause() instanceof java.nio.channels.ClosedChannelException)
          throw capturedAuthErr; 
        throw e;
      } 
      try {
        this.protocol.readAuthenticateOk();
        capturedAuthErr = null;
        break;
      } catch (XProtocolError e) {
        if (e.getErrorCode() != 1045)
          throw e; 
        capturedAuthErr = e;
      } 
    } 
    if (capturedAuthErr != null) {
      if (tryAuthMech.size() == 1)
        throw capturedAuthErr; 
      String errMsg = "Authentication failed using " + StringUtils.joinWithSerialComma(tryAuthMech) + ", check username and password or try a secure connection";
      XDevAPIError ex = new XDevAPIError(errMsg, (Throwable)capturedAuthErr);
      ex.setVendorCode(capturedAuthErr.getErrorCode());
      ex.setSQLState(capturedAuthErr.getSQLState());
      throw ex;
    } 
    this.protocol.afterHandshake();
  }
  
  public String getEncodingForHandshake() {
    return null;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\x\XAuthenticationProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */