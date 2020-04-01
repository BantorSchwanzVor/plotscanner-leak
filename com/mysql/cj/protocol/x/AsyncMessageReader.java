package com.mysql.cj.protocol.x;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Parser;
import com.mysql.cj.conf.PropertyKey;
import com.mysql.cj.conf.PropertySet;
import com.mysql.cj.conf.RuntimeProperty;
import com.mysql.cj.exceptions.AssertionFailedException;
import com.mysql.cj.exceptions.CJCommunicationsException;
import com.mysql.cj.exceptions.WrongArgumentException;
import com.mysql.cj.protocol.Message;
import com.mysql.cj.protocol.MessageHeader;
import com.mysql.cj.protocol.MessageListener;
import com.mysql.cj.protocol.MessageReader;
import com.mysql.cj.protocol.SocketConnection;
import com.mysql.cj.x.protobuf.Mysqlx;
import com.mysql.cj.x.protobuf.MysqlxNotice;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class AsyncMessageReader implements MessageReader<XMessageHeader, XMessage> {
  private static int READ_AHEAD_DEPTH = 10;
  
  CompletedRead currentReadResult;
  
  ByteBuffer messageBuf;
  
  private PropertySet propertySet;
  
  SocketConnection sc;
  
  CompletionHandler<Integer, Void> headerCompletionHandler = new HeaderCompletionHandler();
  
  CompletionHandler<Integer, Void> messageCompletionHandler = new MessageCompletionHandler();
  
  RuntimeProperty<Integer> asyncTimeout;
  
  MessageListener<XMessage> currentMessageListener;
  
  private BlockingQueue<MessageListener<XMessage>> messageListenerQueue = new LinkedBlockingQueue<>();
  
  BlockingQueue<CompletedRead> pendingCompletedReadQueue = new LinkedBlockingQueue<>(READ_AHEAD_DEPTH);
  
  CompletableFuture<XMessageHeader> pendingMsgHeader;
  
  Object pendingMsgMonitor = new Object();
  
  boolean stopAfterNextMessage = false;
  
  private static class CompletedRead {
    public XMessageHeader header = null;
    
    public GeneratedMessageV3 message = null;
  }
  
  public AsyncMessageReader(PropertySet propertySet, SocketConnection socketConnection) {
    this.propertySet = propertySet;
    this.sc = socketConnection;
    this.asyncTimeout = this.propertySet.getIntegerProperty(PropertyKey.xdevapiAsyncResponseTimeout);
  }
  
  public void start() {
    this.headerCompletionHandler.completed(Integer.valueOf(0), null);
  }
  
  public void stopAfterNextMessage() {
    this.stopAfterNextMessage = true;
  }
  
  private void checkClosed() {
    if (!this.sc.getAsynchronousSocketChannel().isOpen())
      throw new CJCommunicationsException("Socket closed"); 
  }
  
  public void pushMessageListener(MessageListener<XMessage> l) {
    checkClosed();
    this.messageListenerQueue.add(l);
  }
  
  MessageListener<XMessage> getMessageListener(boolean block) {
    try {
      if (this.currentMessageListener == null)
        this.currentMessageListener = block ? this.messageListenerQueue.take() : this.messageListenerQueue.poll(); 
      return this.currentMessageListener;
    } catch (InterruptedException ex) {
      throw new CJCommunicationsException(ex);
    } 
  }
  
  private class HeaderCompletionHandler implements CompletionHandler<Integer, Void> {
    public void completed(Integer bytesRead, Void attachment) {
      if (bytesRead.intValue() < 0) {
        AsyncMessageReader.this.onError((Throwable)new CJCommunicationsException("Socket closed"));
        return;
      } 
      try {
        if (AsyncMessageReader.this.currentReadResult == null) {
          AsyncMessageReader.this.currentReadResult = new AsyncMessageReader.CompletedRead();
          AsyncMessageReader.this.currentReadResult.header = new XMessageHeader();
        } 
        if (AsyncMessageReader.this.currentReadResult.header.getBuffer().position() < 5) {
          AsyncMessageReader.this.sc.getAsynchronousSocketChannel().read(AsyncMessageReader.this.currentReadResult.header.getBuffer(), (Object)null, this);
          return;
        } 
        AsyncMessageReader.this.messageBuf = ByteBuffer.allocate(AsyncMessageReader.this.currentReadResult.header.getMessageSize());
        if (AsyncMessageReader.this.getMessageListener(false) == null)
          synchronized (AsyncMessageReader.this.pendingMsgMonitor) {
            AsyncMessageReader.this.pendingMsgHeader = CompletableFuture.completedFuture(AsyncMessageReader.this.currentReadResult.header);
            AsyncMessageReader.this.pendingMsgMonitor.notify();
          }  
        AsyncMessageReader.this.messageCompletionHandler.completed(Integer.valueOf(0), null);
      } catch (Throwable t) {
        AsyncMessageReader.this.onError(t);
      } 
    }
    
    public void failed(Throwable exc, Void attachment) {
      if (AsyncMessageReader.this.getMessageListener(false) != null) {
        synchronized (AsyncMessageReader.this.pendingMsgMonitor) {
          AsyncMessageReader.this.pendingMsgMonitor.notify();
        } 
        if (AsynchronousCloseException.class.equals(exc.getClass())) {
          AsyncMessageReader.this.currentMessageListener.error((Throwable)new CJCommunicationsException("Socket closed", exc));
        } else {
          AsyncMessageReader.this.currentMessageListener.error(exc);
        } 
      } 
      AsyncMessageReader.this.currentMessageListener = null;
    }
  }
  
  private class MessageCompletionHandler implements CompletionHandler<Integer, Void> {
    public void completed(Integer bytesRead, Void attachment) {
      if (bytesRead.intValue() < 0) {
        AsyncMessageReader.this.onError((Throwable)new CJCommunicationsException("Socket closed"));
        return;
      } 
      try {
        if (AsyncMessageReader.this.messageBuf.position() < AsyncMessageReader.this.currentReadResult.header.getMessageSize()) {
          AsyncMessageReader.this.sc.getAsynchronousSocketChannel().read(AsyncMessageReader.this.messageBuf, (Object)null, this);
          return;
        } 
        ByteBuffer buf = AsyncMessageReader.this.messageBuf;
        AsyncMessageReader.this.messageBuf = null;
        Class<? extends GeneratedMessageV3> messageClass = MessageConstants.getMessageClassForType(AsyncMessageReader.this.currentReadResult.header.getMessageType());
        boolean localStopAfterNextMessage = AsyncMessageReader.this.stopAfterNextMessage;
        buf.flip();
        AsyncMessageReader.this.currentReadResult.message = parseMessage(messageClass, buf);
        AsyncMessageReader.this.pendingCompletedReadQueue.add(AsyncMessageReader.this.currentReadResult);
        AsyncMessageReader.this.currentReadResult = null;
        AsyncMessageReader.this.dispatchMessage();
        if (localStopAfterNextMessage && messageClass != MysqlxNotice.Frame.class) {
          AsyncMessageReader.this.stopAfterNextMessage = false;
          AsyncMessageReader.this.currentReadResult = null;
          return;
        } 
        AsyncMessageReader.this.headerCompletionHandler.completed(Integer.valueOf(0), null);
      } catch (Throwable t) {
        AsyncMessageReader.this.onError(t);
      } 
    }
    
    public void failed(Throwable exc, Void attachment) {
      if (AsyncMessageReader.this.getMessageListener(false) != null) {
        synchronized (AsyncMessageReader.this.pendingMsgMonitor) {
          AsyncMessageReader.this.pendingMsgMonitor.notify();
        } 
        if (AsynchronousCloseException.class.equals(exc.getClass())) {
          AsyncMessageReader.this.currentMessageListener.error((Throwable)new CJCommunicationsException("Socket closed", exc));
        } else {
          AsyncMessageReader.this.currentMessageListener.error(exc);
        } 
      } 
      AsyncMessageReader.this.currentMessageListener = null;
    }
    
    private GeneratedMessageV3 parseMessage(Class<? extends GeneratedMessageV3> messageClass, ByteBuffer buf) {
      try {
        Parser<? extends GeneratedMessageV3> parser = MessageConstants.MESSAGE_CLASS_TO_PARSER.get(messageClass);
        return (GeneratedMessageV3)parser.parseFrom(CodedInputStream.newInstance(buf));
      } catch (InvalidProtocolBufferException ex) {
        throw AssertionFailedException.shouldNotHappen(ex);
      } 
    }
  }
  
  void dispatchMessage() {
    if (this.pendingCompletedReadQueue.isEmpty())
      return; 
    if (getMessageListener(true) != null) {
      CompletedRead res;
      try {
        res = this.pendingCompletedReadQueue.take();
      } catch (InterruptedException e) {
        throw new CJCommunicationsException("Failed to peek pending message", e);
      } 
      GeneratedMessageV3 message = res.message;
      synchronized (this.pendingMsgMonitor) {
        if (this.currentMessageListener.processMessage(new XMessage((Message)message)))
          this.currentMessageListener = null; 
        this.pendingMsgHeader = null;
      } 
    } 
  }
  
  void onError(Throwable t) {
    try {
      this.sc.getAsynchronousSocketChannel().close();
    } catch (Exception exception) {}
    if (this.currentMessageListener != null) {
      try {
        this.currentMessageListener.error(t);
      } catch (Exception exception) {}
      this.currentMessageListener = null;
    } 
    this.messageListenerQueue.forEach(l -> {
          try {
            l.error(t);
          } catch (Exception exception) {}
        });
    synchronized (this.pendingMsgMonitor) {
      this.pendingMsgHeader = new CompletableFuture<>();
      this.pendingMsgHeader.completeExceptionally(t);
      this.pendingMsgMonitor.notify();
    } 
    this.messageListenerQueue.clear();
  }
  
  public XMessageHeader readHeader() throws IOException {
    XMessageHeader mh;
    synchronized (this.pendingMsgMonitor) {
      checkClosed();
      while (this.pendingMsgHeader == null) {
        try {
          this.pendingMsgMonitor.wait();
        } catch (InterruptedException ex) {
          throw new CJCommunicationsException(ex);
        } 
      } 
      try {
        mh = this.pendingMsgHeader.get();
      } catch (ExecutionException ex) {
        throw new CJCommunicationsException("Failed to peek pending message", ex.getCause());
      } catch (InterruptedException ex) {
        throw new CJCommunicationsException(ex);
      } 
    } 
    if (mh.getMessageType() == 1)
      readMessage((Optional<XMessage>)null, mh); 
    return mh;
  }
  
  public XMessage readMessage(Optional<XMessage> reuse, XMessageHeader hdr) throws IOException {
    return readMessage(reuse, hdr.getMessageType());
  }
  
  public XMessage readMessage(Optional<XMessage> reuse, int expectedType) throws IOException {
    Class<? extends GeneratedMessageV3> expectedClass = MessageConstants.getMessageClassForType(expectedType);
    CompletableFuture<XMessage> future = new CompletableFuture<>();
    SyncXMessageListener<? extends GeneratedMessageV3> r = new SyncXMessageListener<>(future, expectedClass);
    pushMessageListener(r);
    try {
      return future.get(((Integer)this.asyncTimeout.getValue()).intValue(), TimeUnit.SECONDS);
    } catch (ExecutionException ex) {
      if (XProtocolError.class.equals(ex.getCause().getClass()))
        throw new XProtocolError((XProtocolError)ex.getCause()); 
      throw new CJCommunicationsException(ex.getCause().getMessage(), ex.getCause());
    } catch (InterruptedException|java.util.concurrent.TimeoutException ex) {
      throw new CJCommunicationsException(ex);
    } 
  }
  
  private static final class SyncXMessageListener<T extends GeneratedMessageV3> implements MessageListener<XMessage> {
    private CompletableFuture<XMessage> future;
    
    private Class<T> expectedClass;
    
    List<Notice> notices = null;
    
    public SyncXMessageListener(CompletableFuture<XMessage> future, Class<T> expectedClass) {
      this.future = future;
      this.expectedClass = expectedClass;
    }
    
    public boolean processMessage(XMessage msg) {
      Class<? extends GeneratedMessageV3> msgClass = (Class)msg.getMessage().getClass();
      if (Mysqlx.Error.class.equals(msgClass)) {
        this.future.completeExceptionally((Throwable)new XProtocolError(Mysqlx.Error.class.cast(msg.getMessage())));
        return true;
      } 
      if (this.expectedClass.equals(msgClass)) {
        this.future.complete(msg.addNotices(this.notices));
        this.notices = null;
        return true;
      } 
      if (MysqlxNotice.Frame.class.equals(msgClass)) {
        if (this.notices == null)
          this.notices = new ArrayList<>(); 
        this.notices.add(Notice.getInstance(msg));
        return false;
      } 
      this.future.completeExceptionally((Throwable)new WrongArgumentException("Unhandled msg class (" + msgClass + ") + msg=" + msg.getMessage()));
      return true;
    }
    
    public void error(Throwable ex) {
      this.future.completeExceptionally(ex);
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\x\AsyncMessageReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */