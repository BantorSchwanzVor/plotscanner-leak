package com.mysql.cj.protocol.a;

import com.mysql.cj.MessageBuilder;
import com.mysql.cj.exceptions.CJOperationNotSupportedException;
import com.mysql.cj.exceptions.ExceptionFactory;
import com.mysql.cj.protocol.Message;
import com.mysql.cj.util.StringUtils;
import java.util.List;

public class NativeMessageBuilder implements MessageBuilder<NativePacketPayload> {
  public NativePacketPayload buildSqlStatement(String statement) {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  public NativePacketPayload buildSqlStatement(String statement, List<Object> args) {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  public NativePacketPayload buildClose() {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  public NativePacketPayload buildComQuery(NativePacketPayload sharedPacket, byte[] query) {
    NativePacketPayload packet = (sharedPacket != null) ? sharedPacket : new NativePacketPayload(query.length + 1);
    packet.writeInteger(NativeConstants.IntegerDataType.INT1, 3L);
    packet.writeBytes(NativeConstants.StringLengthDataType.STRING_FIXED, query);
    return packet;
  }
  
  public NativePacketPayload buildComQuery(NativePacketPayload sharedPacket, String query) {
    return buildComQuery(sharedPacket, StringUtils.getBytes(query));
  }
  
  public NativePacketPayload buildComQuery(NativePacketPayload sharedPacket, String query, String encoding) {
    return buildComQuery(sharedPacket, StringUtils.getBytes(query, encoding));
  }
  
  public NativePacketPayload buildComInitDb(NativePacketPayload sharedPacket, byte[] dbName) {
    NativePacketPayload packet = (sharedPacket != null) ? sharedPacket : new NativePacketPayload(dbName.length + 1);
    packet.writeInteger(NativeConstants.IntegerDataType.INT1, 2L);
    packet.writeBytes(NativeConstants.StringLengthDataType.STRING_FIXED, dbName);
    return packet;
  }
  
  public NativePacketPayload buildComInitDb(NativePacketPayload sharedPacket, String dbName) {
    return buildComInitDb(sharedPacket, StringUtils.getBytes(dbName));
  }
  
  public NativePacketPayload buildComShutdown(NativePacketPayload sharedPacket) {
    NativePacketPayload packet = (sharedPacket != null) ? sharedPacket : new NativePacketPayload(1);
    packet.writeInteger(NativeConstants.IntegerDataType.INT1, 8L);
    return packet;
  }
  
  public NativePacketPayload buildComSetOption(NativePacketPayload sharedPacket, int val) {
    NativePacketPayload packet = (sharedPacket != null) ? sharedPacket : new NativePacketPayload(3);
    packet.writeInteger(NativeConstants.IntegerDataType.INT1, 27L);
    packet.writeInteger(NativeConstants.IntegerDataType.INT2, val);
    return packet;
  }
  
  public NativePacketPayload buildComPing(NativePacketPayload sharedPacket) {
    NativePacketPayload packet = (sharedPacket != null) ? sharedPacket : new NativePacketPayload(1);
    packet.writeInteger(NativeConstants.IntegerDataType.INT1, 14L);
    return packet;
  }
  
  public NativePacketPayload buildComQuit(NativePacketPayload sharedPacket) {
    NativePacketPayload packet = (sharedPacket != null) ? sharedPacket : new NativePacketPayload(1);
    packet.writeInteger(NativeConstants.IntegerDataType.INT1, 1L);
    return packet;
  }
  
  public NativePacketPayload buildComStmtPrepare(NativePacketPayload sharedPacket, byte[] query) {
    NativePacketPayload packet = (sharedPacket != null) ? sharedPacket : new NativePacketPayload(query.length + 1);
    packet.writeInteger(NativeConstants.IntegerDataType.INT1, 22L);
    packet.writeBytes(NativeConstants.StringLengthDataType.STRING_FIXED, query);
    return packet;
  }
  
  public NativePacketPayload buildComStmtPrepare(NativePacketPayload sharedPacket, String queryString, String characterEncoding) {
    return buildComStmtPrepare(sharedPacket, StringUtils.getBytes(queryString, characterEncoding));
  }
  
  public NativePacketPayload buildComStmtClose(NativePacketPayload sharedPacket, long serverStatementId) {
    NativePacketPayload packet = (sharedPacket != null) ? sharedPacket : new NativePacketPayload(5);
    packet.writeInteger(NativeConstants.IntegerDataType.INT1, 25L);
    packet.writeInteger(NativeConstants.IntegerDataType.INT4, serverStatementId);
    return packet;
  }
  
  public NativePacketPayload buildComStmtReset(NativePacketPayload sharedPacket, long serverStatementId) {
    NativePacketPayload packet = (sharedPacket != null) ? sharedPacket : new NativePacketPayload(5);
    packet.writeInteger(NativeConstants.IntegerDataType.INT1, 26L);
    packet.writeInteger(NativeConstants.IntegerDataType.INT4, serverStatementId);
    return packet;
  }
  
  public NativePacketPayload buildComStmtFetch(NativePacketPayload sharedPacket, long serverStatementId, long numRowsToFetch) {
    NativePacketPayload packet = (sharedPacket != null) ? sharedPacket : new NativePacketPayload(9);
    packet.writeInteger(NativeConstants.IntegerDataType.INT1, 28L);
    packet.writeInteger(NativeConstants.IntegerDataType.INT4, serverStatementId);
    packet.writeInteger(NativeConstants.IntegerDataType.INT4, numRowsToFetch);
    return packet;
  }
  
  public NativePacketPayload buildComStmtSendLongData(NativePacketPayload sharedPacket, long serverStatementId, int parameterIndex, byte[] longData) {
    NativePacketPayload packet = (sharedPacket != null) ? sharedPacket : new NativePacketPayload(9);
    packet.writeInteger(NativeConstants.IntegerDataType.INT1, 24L);
    packet.writeInteger(NativeConstants.IntegerDataType.INT4, serverStatementId);
    packet.writeInteger(NativeConstants.IntegerDataType.INT2, parameterIndex);
    packet.writeBytes(NativeConstants.StringLengthDataType.STRING_FIXED, longData);
    return packet;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\a\NativeMessageBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */