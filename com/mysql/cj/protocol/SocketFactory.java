package com.mysql.cj.protocol;

import com.mysql.cj.conf.PropertySet;
import java.io.IOException;

public interface SocketFactory extends SocketMetadata {
  <T extends java.io.Closeable> T connect(String paramString, int paramInt1, PropertySet paramPropertySet, int paramInt2) throws IOException;
  
  default void beforeHandshake() throws IOException {}
  
  <T extends java.io.Closeable> T performTlsHandshake(SocketConnection paramSocketConnection, ServerSession paramServerSession) throws IOException;
  
  default void afterHandshake() throws IOException {}
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\SocketFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */