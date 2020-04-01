package com.mysql.cj.protocol.a;

import com.mysql.cj.log.Log;
import com.mysql.cj.protocol.MessageSender;
import com.mysql.cj.util.StringUtils;
import java.io.IOException;

public class TracingPacketSender implements MessageSender<NativePacketPayload> {
  private MessageSender<NativePacketPayload> packetSender;
  
  private String host;
  
  private long serverThreadId;
  
  private Log log;
  
  public TracingPacketSender(MessageSender<NativePacketPayload> packetSender, Log log, String host, long serverThreadId) {
    this.packetSender = packetSender;
    this.host = host;
    this.serverThreadId = serverThreadId;
    this.log = log;
  }
  
  public void setServerThreadId(long serverThreadId) {
    this.serverThreadId = serverThreadId;
  }
  
  private void logPacket(byte[] packet, int packetLen, byte packetSequence) {
    StringBuilder traceMessageBuf = new StringBuilder();
    traceMessageBuf.append("send packet payload:\n");
    traceMessageBuf.append("host: '");
    traceMessageBuf.append(this.host);
    traceMessageBuf.append("' serverThreadId: '");
    traceMessageBuf.append(this.serverThreadId);
    traceMessageBuf.append("' packetLen: '");
    traceMessageBuf.append(packetLen);
    traceMessageBuf.append("' packetSequence: '");
    traceMessageBuf.append(packetSequence);
    traceMessageBuf.append("'\n");
    traceMessageBuf.append(StringUtils.dumpAsHex(packet, packetLen));
    this.log.logTrace(traceMessageBuf.toString());
  }
  
  public void send(byte[] packet, int packetLen, byte packetSequence) throws IOException {
    logPacket(packet, packetLen, packetSequence);
    this.packetSender.send(packet, packetLen, packetSequence);
  }
  
  public MessageSender<NativePacketPayload> undecorateAll() {
    return this.packetSender.undecorateAll();
  }
  
  public MessageSender<NativePacketPayload> undecorate() {
    return this.packetSender;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\a\TracingPacketSender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */