package com.mysql.cj.protocol;

import com.mysql.cj.exceptions.AssertionFailedException;
import com.mysql.cj.exceptions.CJCommunicationsException;
import java.io.IOException;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.NetworkChannel;
import java.nio.channels.spi.AsynchronousChannelProvider;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;

public class TlsAsynchronousSocketChannel extends AsynchronousSocketChannel implements CompletionHandler<Integer, Void> {
  private static final ByteBuffer emptyBuffer = ByteBuffer.allocate(0);
  
  private AsynchronousSocketChannel channel;
  
  private SSLEngine sslEngine;
  
  private ByteBuffer cipherTextBuffer;
  
  private ByteBuffer clearTextBuffer;
  
  private CompletionHandler<Integer, ?> handler;
  
  private ByteBuffer dst;
  
  private SerializingBufferWriter bufferWriter;
  
  private LinkedBlockingQueue<ByteBuffer> cipherTextBuffers = new LinkedBlockingQueue<>();
  
  public TlsAsynchronousSocketChannel(AsynchronousSocketChannel in, SSLEngine sslEngine) {
    super((AsynchronousChannelProvider)null);
    this.sslEngine = sslEngine;
    this.channel = in;
    this.sslEngine = sslEngine;
    this.cipherTextBuffer = ByteBuffer.allocate(sslEngine.getSession().getPacketBufferSize());
    this.cipherTextBuffer.flip();
    this.clearTextBuffer = ByteBuffer.allocate(sslEngine.getSession().getApplicationBufferSize());
    this.clearTextBuffer.flip();
    this.bufferWriter = new SerializingBufferWriter(this.channel);
  }
  
  public void completed(Integer result, Void attachment) {
    if (result.intValue() < 0) {
      CompletionHandler<Integer, ?> h = this.handler;
      this.handler = null;
      h.completed(result, null);
      return;
    } 
    this.cipherTextBuffer.flip();
    decryptAndDispatch();
  }
  
  public void failed(Throwable exc, Void attachment) {
    CompletionHandler<Integer, ?> h = this.handler;
    this.handler = null;
    h.failed(exc, null);
  }
  
  private synchronized void decryptAndDispatch() {
    try {
      int newPeerNetDataSize;
      this.clearTextBuffer.clear();
      SSLEngineResult res = this.sslEngine.unwrap(this.cipherTextBuffer, this.clearTextBuffer);
      switch (res.getStatus()) {
        case BUFFER_UNDERFLOW:
          newPeerNetDataSize = this.sslEngine.getSession().getPacketBufferSize();
          if (newPeerNetDataSize > this.cipherTextBuffer.capacity()) {
            ByteBuffer newPeerNetData = ByteBuffer.allocate(newPeerNetDataSize);
            newPeerNetData.put(this.cipherTextBuffer);
            newPeerNetData.flip();
            this.cipherTextBuffer = newPeerNetData;
          } else {
            this.cipherTextBuffer.compact();
          } 
          this.channel.read(this.cipherTextBuffer, (Object)null, this);
          return;
        case BUFFER_OVERFLOW:
          throw new BufferOverflowException();
        case OK:
          this.clearTextBuffer.flip();
          dispatchData();
          break;
        case CLOSED:
          this.handler.completed(Integer.valueOf(-1), null);
          break;
      } 
    } catch (Throwable ex) {
      failed(ex, (Void)null);
    } 
  }
  
  public <A> void read(ByteBuffer dest, long timeout, TimeUnit unit, A attachment, CompletionHandler<Integer, ? super A> hdlr) {
    try {
      if (this.handler != null)
        hdlr.completed(Integer.valueOf(0), null); 
      this.handler = hdlr;
      this.dst = dest;
      if (this.clearTextBuffer.hasRemaining()) {
        dispatchData();
      } else if (this.cipherTextBuffer.hasRemaining()) {
        decryptAndDispatch();
      } else {
        this.cipherTextBuffer.clear();
        this.channel.read(this.cipherTextBuffer, (Object)null, this);
      } 
    } catch (Throwable ex) {
      hdlr.failed(ex, null);
    } 
  }
  
  public <A> void read(ByteBuffer[] dsts, int offset, int length, long timeout, TimeUnit unit, A attachment, CompletionHandler<Long, ? super A> hdlr) {
    hdlr.failed(new UnsupportedOperationException(), null);
  }
  
  private synchronized void dispatchData() {
    final int transferred = Math.min(this.dst.remaining(), this.clearTextBuffer.remaining());
    if (this.clearTextBuffer.remaining() > this.dst.remaining()) {
      int newLimit = this.clearTextBuffer.position() + transferred;
      ByteBuffer src = this.clearTextBuffer.duplicate();
      src.limit(newLimit);
      this.dst.put(src);
      this.clearTextBuffer.position(this.clearTextBuffer.position() + transferred);
    } else {
      this.dst.put(this.clearTextBuffer);
    } 
    final CompletionHandler<Integer, ?> h = this.handler;
    this.handler = null;
    if (this.channel.isOpen()) {
      this.channel.read(emptyBuffer, (Object)null, new CompletionHandler<Integer, Void>() {
            public void completed(Integer result, Void attachment) {
              h.completed(Integer.valueOf(transferred), null);
            }
            
            public void failed(Throwable t, Void attachment) {
              t.printStackTrace();
              h.failed((Throwable)AssertionFailedException.shouldNotHappen(new Exception(t)), null);
            }
          });
    } else {
      h.completed(Integer.valueOf(transferred), null);
    } 
  }
  
  public void close() throws IOException {
    this.channel.close();
  }
  
  public boolean isOpen() {
    return this.channel.isOpen();
  }
  
  public Future<Integer> read(ByteBuffer dest) {
    throw new UnsupportedOperationException("This channel does not support direct reads");
  }
  
  public Future<Integer> write(ByteBuffer src) {
    throw new UnsupportedOperationException("This channel does not support writes");
  }
  
  private static class ErrorPropagatingCompletionHandler<V> implements CompletionHandler<V, Void> {
    private CompletionHandler<Long, ?> target;
    
    private Runnable success;
    
    public ErrorPropagatingCompletionHandler(CompletionHandler<Long, ?> target, Runnable success) {
      this.target = target;
      this.success = success;
    }
    
    public void completed(V result, Void attachment) {
      this.success.run();
    }
    
    public void failed(Throwable ex, Void attachment) {
      this.target.failed(ex, null);
    }
  }
  
  private boolean isDrained(ByteBuffer[] buffers) {
    for (ByteBuffer b : buffers) {
      if (b.hasRemaining())
        return false; 
    } 
    return true;
  }
  
  public <A> void write(ByteBuffer[] srcs, int offset, int length, long timeout, TimeUnit unit, A attachment, CompletionHandler<Long, ? super A> hdlr) {
    try {
      long totalWriteSize = 0L;
      while (true) {
        ByteBuffer cipherText = getCipherTextBuffer();
        SSLEngineResult res = this.sslEngine.wrap(srcs, offset, length, cipherText);
        if (res.getStatus() != SSLEngineResult.Status.OK)
          hdlr.failed((Throwable)new CJCommunicationsException("Unacceptable SSLEngine result: " + res), null); 
        totalWriteSize += res.bytesConsumed();
        cipherText.flip();
        if (isDrained(srcs)) {
          long finalTotal = totalWriteSize;
          Runnable successHandler = () -> {
              hdlr.completed(Long.valueOf(finalTotal), null);
              putCipherTextBuffer(cipherText);
            };
          this.bufferWriter.queueBuffer(cipherText, new ErrorPropagatingCompletionHandler<>(hdlr, successHandler));
          break;
        } 
        this.bufferWriter.queueBuffer(cipherText, new ErrorPropagatingCompletionHandler<>(hdlr, () -> putCipherTextBuffer(cipherText)));
      } 
    } catch (SSLException ex) {
      hdlr.failed((Throwable)new CJCommunicationsException(ex), null);
    } catch (Throwable ex) {
      hdlr.failed(ex, null);
    } 
  }
  
  public <A> void write(ByteBuffer src, long timeout, TimeUnit unit, A attachment, CompletionHandler<Integer, ? super A> hdlr) {
    hdlr.failed(new UnsupportedOperationException(), null);
  }
  
  private ByteBuffer getCipherTextBuffer() {
    ByteBuffer buf = this.cipherTextBuffers.poll();
    if (buf == null)
      return ByteBuffer.allocate(this.sslEngine.getSession().getPacketBufferSize()); 
    buf.clear();
    return buf;
  }
  
  private void putCipherTextBuffer(ByteBuffer buf) {
    if (this.cipherTextBuffers.size() < 10)
      this.cipherTextBuffers.offer(buf); 
  }
  
  public <T> T getOption(SocketOption<T> name) throws IOException {
    throw new UnsupportedOperationException();
  }
  
  public Set<SocketOption<?>> supportedOptions() {
    throw new UnsupportedOperationException();
  }
  
  public AsynchronousSocketChannel bind(SocketAddress local) throws IOException {
    throw new UnsupportedOperationException();
  }
  
  public <T> AsynchronousSocketChannel setOption(SocketOption<T> name, T value) throws IOException {
    throw new UnsupportedOperationException();
  }
  
  public AsynchronousSocketChannel shutdownInput() throws IOException {
    return this.channel.shutdownInput();
  }
  
  public AsynchronousSocketChannel shutdownOutput() throws IOException {
    return this.channel.shutdownOutput();
  }
  
  public SocketAddress getRemoteAddress() throws IOException {
    return this.channel.getRemoteAddress();
  }
  
  public <A> void connect(SocketAddress remote, A attachment, CompletionHandler<Void, ? super A> hdlr) {
    hdlr.failed(new UnsupportedOperationException(), null);
  }
  
  public Future<Void> connect(SocketAddress remote) {
    throw new UnsupportedOperationException();
  }
  
  public SocketAddress getLocalAddress() throws IOException {
    return this.channel.getLocalAddress();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\TlsAsynchronousSocketChannel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */