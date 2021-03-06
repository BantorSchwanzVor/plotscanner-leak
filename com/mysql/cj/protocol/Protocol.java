package com.mysql.cj.protocol;

import com.mysql.cj.MessageBuilder;
import com.mysql.cj.Session;
import com.mysql.cj.TransactionEventHandler;
import com.mysql.cj.conf.PropertySet;
import com.mysql.cj.exceptions.ExceptionInterceptor;
import java.io.IOException;
import java.io.InputStream;

public interface Protocol<M extends Message> {
  void init(Session paramSession, SocketConnection paramSocketConnection, PropertySet paramPropertySet, TransactionEventHandler paramTransactionEventHandler);
  
  PropertySet getPropertySet();
  
  void setPropertySet(PropertySet paramPropertySet);
  
  MessageBuilder<M> getMessageBuilder();
  
  ServerCapabilities readServerCapabilities();
  
  ServerSession getServerSession();
  
  SocketConnection getSocketConnection();
  
  AuthenticationProvider<M> getAuthenticationProvider();
  
  ExceptionInterceptor getExceptionInterceptor();
  
  PacketSentTimeHolder getPacketSentTimeHolder();
  
  void setPacketSentTimeHolder(PacketSentTimeHolder paramPacketSentTimeHolder);
  
  PacketReceivedTimeHolder getPacketReceivedTimeHolder();
  
  void setPacketReceivedTimeHolder(PacketReceivedTimeHolder paramPacketReceivedTimeHolder);
  
  void connect(String paramString1, String paramString2, String paramString3);
  
  void negotiateSSLConnection(int paramInt);
  
  void beforeHandshake();
  
  void afterHandshake();
  
  void changeDatabase(String paramString);
  
  void changeUser(String paramString1, String paramString2, String paramString3);
  
  String getPasswordCharacterEncoding();
  
  boolean versionMeetsMinimum(int paramInt1, int paramInt2, int paramInt3);
  
  M readMessage(M paramM);
  
  M checkErrorMessage();
  
  void send(Message paramMessage, int paramInt);
  
  ColumnDefinition readMetadata();
  
  M sendCommand(Message paramMessage, boolean paramBoolean, int paramInt);
  
  <T extends ProtocolEntity> T read(Class<T> paramClass, ProtocolEntityFactory<T, M> paramProtocolEntityFactory) throws IOException;
  
  <T extends ProtocolEntity> T read(Class<Resultset> paramClass, int paramInt, boolean paramBoolean1, M paramM, boolean paramBoolean2, ColumnDefinition paramColumnDefinition, ProtocolEntityFactory<T, M> paramProtocolEntityFactory) throws IOException;
  
  void setLocalInfileInputStream(InputStream paramInputStream);
  
  InputStream getLocalInfileInputStream();
  
  String getQueryComment();
  
  void setQueryComment(String paramString);
  
  <T extends com.mysql.cj.QueryResult> T readQueryResult(ResultBuilder<T> paramResultBuilder);
  
  void close() throws IOException;
  
  void configureTimezone();
  
  void initServerSession();
  
  void reset();
  
  String getQueryTimingUnits();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\Protocol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */