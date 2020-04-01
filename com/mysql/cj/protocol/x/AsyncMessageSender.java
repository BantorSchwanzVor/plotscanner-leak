package com.mysql.cj.protocol.x;

import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Message;
import com.mysql.cj.Messages;
import com.mysql.cj.exceptions.CJCommunicationsException;
import com.mysql.cj.exceptions.CJPacketTooBigException;
import com.mysql.cj.protocol.Message;
import com.mysql.cj.protocol.MessageSender;
import com.mysql.cj.protocol.SerializingBufferWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AsyncMessageSender implements MessageSender<XMessage> {
  private static final int HEADER_LEN = 5;
  
  private int maxAllowedPacket = -1;
  
  private SerializingBufferWriter bufferWriter;
  
  public AsyncMessageSender(AsynchronousSocketChannel channel) {
    this.bufferWriter = new SerializingBufferWriter(channel);
  }
  
  public void send(XMessage message) {
    CompletableFuture<Void> f = new CompletableFuture<>();
    send(message, f, () -> f.complete(null));
    try {
      f.get();
    } catch (ExecutionException ex) {
      throw new CJCommunicationsException("Failed to write message", ex.getCause());
    } catch (InterruptedException ex) {
      throw new CJCommunicationsException("Failed to write message", ex);
    } 
  }
  
  public CompletableFuture<?> send(XMessage message, CompletableFuture<?> future, Runnable callback) {
    Message message1 = message.getMessage();
    int type = MessageConstants.getTypeForMessageClass((Class)message1.getClass());
    int size = message1.getSerializedSize();
    int payloadSize = size + 1;
    if (this.maxAllowedPacket > 0 && payloadSize > this.maxAllowedPacket)
      throw new CJPacketTooBigException(Messages.getString("PacketTooBigException.1", new Object[] { Integer.valueOf(size), Integer.valueOf(this.maxAllowedPacket) })); 
    ByteBuffer messageBuf = ByteBuffer.allocate(5 + size).order(ByteOrder.LITTLE_ENDIAN).putInt(payloadSize);
    messageBuf.put((byte)type);
    try {
      message1.writeTo(CodedOutputStream.newInstance(messageBuf.array(), 5, size));
      messageBuf.position(messageBuf.limit());
    } catch (IOException ex) {
      throw new CJCommunicationsException("Unable to write message", ex);
    } 
    messageBuf.flip();
    this.bufferWriter.queueBuffer(messageBuf, new ErrorToFutureCompletionHandler(future, callback));
    return future;
  }
  
  public void setMaxAllowedPacket(int maxAllowedPacket) {
    this.maxAllowedPacket = maxAllowedPacket;
  }
  
  public void setChannel(AsynchronousSocketChannel channel) {
    this.bufferWriter.setChannel(channel);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\x\AsyncMessageSender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */