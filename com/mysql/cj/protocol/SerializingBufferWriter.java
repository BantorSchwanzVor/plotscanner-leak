package com.mysql.cj.protocol;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.ReadPendingException;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class SerializingBufferWriter implements CompletionHandler<Long, Void> {
  private static int WRITES_AT_ONCE = 200;
  
  protected AsynchronousSocketChannel channel;
  
  private Queue<ByteBufferWrapper> pendingWrites = new LinkedList<>();
  
  private static class ByteBufferWrapper {
    private ByteBuffer buffer;
    
    private CompletionHandler<Long, Void> handler = null;
    
    ByteBufferWrapper(ByteBuffer buffer, CompletionHandler<Long, Void> completionHandler) {
      this.buffer = buffer;
      this.handler = completionHandler;
    }
    
    public ByteBuffer getBuffer() {
      return this.buffer;
    }
    
    public CompletionHandler<Long, Void> getHandler() {
      return this.handler;
    }
  }
  
  public SerializingBufferWriter(AsynchronousSocketChannel channel) {
    this.channel = channel;
  }
  
  private void initiateWrite() {
    try {
      ByteBuffer[] bufs = (ByteBuffer[])this.pendingWrites.stream().limit(WRITES_AT_ONCE).map(ByteBufferWrapper::getBuffer).toArray(size -> new ByteBuffer[size]);
      this.channel.write(bufs, 0, bufs.length, 0L, TimeUnit.MILLISECONDS, (Object)null, this);
    } catch (ReadPendingException|java.nio.channels.WritePendingException t) {
      return;
    } catch (Throwable t) {
      failed(t, (Void)null);
    } 
  }
  
  public void queueBuffer(ByteBuffer buf, CompletionHandler<Long, Void> callback) {
    synchronized (this.pendingWrites) {
      this.pendingWrites.add(new ByteBufferWrapper(buf, callback));
      if (this.pendingWrites.size() == 1)
        initiateWrite(); 
    } 
  }
  
  public void completed(Long bytesWritten, Void v) {
    LinkedList<CompletionHandler<Long, Void>> completedWrites = new LinkedList<>();
    synchronized (this.pendingWrites) {
      while (this.pendingWrites.peek() != null && !((ByteBufferWrapper)this.pendingWrites.peek()).getBuffer().hasRemaining() && completedWrites.size() < WRITES_AT_ONCE)
        completedWrites.add(((ByteBufferWrapper)this.pendingWrites.remove()).getHandler()); 
      completedWrites.stream().filter(Objects::nonNull).forEach(l -> {
            try {
              l.completed(Long.valueOf(0L), null);
            } catch (Throwable ex) {
              try {
                l.failed(ex, null);
              } catch (Throwable ex2) {
                ex2.printStackTrace();
              } 
            } 
          });
      if (this.pendingWrites.size() > 0)
        initiateWrite(); 
    } 
  }
  
  public void failed(Throwable t, Void v) {
    try {
      this.channel.close();
    } catch (Exception exception) {}
    LinkedList<CompletionHandler<Long, Void>> failedWrites = new LinkedList<>();
    synchronized (this.pendingWrites) {
      while (this.pendingWrites.peek() != null) {
        ByteBufferWrapper bw = this.pendingWrites.remove();
        if (bw.getHandler() != null)
          failedWrites.add(bw.getHandler()); 
      } 
    } 
    failedWrites.forEach(l -> {
          try {
            l.failed(t, null);
          } catch (Exception exception) {}
        });
    failedWrites.clear();
  }
  
  public void setChannel(AsynchronousSocketChannel channel) {
    this.channel = channel;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\SerializingBufferWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */