package com.mysql.cj.protocol.x;

import com.mysql.cj.conf.PropertySet;
import com.mysql.cj.exceptions.CJCommunicationsException;
import com.mysql.cj.exceptions.CJOperationNotSupportedException;
import com.mysql.cj.exceptions.ExceptionFactory;
import com.mysql.cj.exceptions.ExceptionInterceptor;
import com.mysql.cj.exceptions.FeatureNotAvailableException;
import com.mysql.cj.exceptions.SSLParamsException;
import com.mysql.cj.log.Log;
import com.mysql.cj.protocol.AbstractSocketConnection;
import com.mysql.cj.protocol.AsyncSocketFactory;
import com.mysql.cj.protocol.FullReadInputStream;
import com.mysql.cj.protocol.NetworkResources;
import com.mysql.cj.protocol.ServerSession;
import com.mysql.cj.protocol.SocketConnection;
import com.mysql.cj.protocol.SocketFactory;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.channels.AsynchronousSocketChannel;

public class XAsyncSocketConnection extends AbstractSocketConnection implements SocketConnection {
  AsynchronousSocketChannel channel;
  
  public void connect(String hostName, int portNumber, PropertySet propSet, ExceptionInterceptor excInterceptor, Log log, int loginTimeout) {
    this.port = portNumber;
    this.host = hostName;
    this.propertySet = propSet;
    this.socketFactory = (SocketFactory)new AsyncSocketFactory();
    try {
      this.channel = (AsynchronousSocketChannel)this.socketFactory.connect(hostName, portNumber, propSet, loginTimeout);
    } catch (CJCommunicationsException e) {
      throw e;
    } catch (IOException|RuntimeException ex) {
      throw new CJCommunicationsException(ex);
    } 
  }
  
  public void performTlsHandshake(ServerSession serverSession) throws SSLParamsException, FeatureNotAvailableException, IOException {
    this.channel = (AsynchronousSocketChannel)this.socketFactory.performTlsHandshake(this, serverSession);
  }
  
  public AsynchronousSocketChannel getAsynchronousSocketChannel() {
    return this.channel;
  }
  
  public final void forceClose() {
    try {
      if (this.channel != null && this.channel.isOpen())
        this.channel.close(); 
    } catch (IOException iOException) {
    
    } finally {
      this.channel = null;
    } 
  }
  
  public NetworkResources getNetworkResources() {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  public Socket getMysqlSocket() {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  public FullReadInputStream getMysqlInput() {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  public void setMysqlInput(FullReadInputStream mysqlInput) {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  public BufferedOutputStream getMysqlOutput() {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  public boolean isSSLEstablished() {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  public ExceptionInterceptor getExceptionInterceptor() {
    return null;
  }
  
  public boolean isSynchronous() {
    return false;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\x\XAsyncSocketConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */