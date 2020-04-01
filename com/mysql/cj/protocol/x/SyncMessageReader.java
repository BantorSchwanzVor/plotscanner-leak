package com.mysql.cj.protocol.x;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Parser;
import com.mysql.cj.exceptions.CJCommunicationsException;
import com.mysql.cj.exceptions.WrongArgumentException;
import com.mysql.cj.protocol.FullReadInputStream;
import com.mysql.cj.protocol.Message;
import com.mysql.cj.protocol.MessageHeader;
import com.mysql.cj.protocol.MessageListener;
import com.mysql.cj.protocol.MessageReader;
import com.mysql.cj.x.protobuf.Mysqlx;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class SyncMessageReader implements MessageReader<XMessageHeader, XMessage> {
  private FullReadInputStream inputStream;
  
  private XMessageHeader header;
  
  BlockingQueue<MessageListener<XMessage>> messageListenerQueue = new LinkedBlockingQueue<>();
  
  Object dispatchingThreadMonitor = new Object();
  
  Object waitingSyncOperationMonitor = new Object();
  
  Thread dispatchingThread = null;
  
  public SyncMessageReader(FullReadInputStream inputStream) {
    this.inputStream = inputStream;
  }
  
  public XMessageHeader readHeader() throws IOException {
    synchronized (this.waitingSyncOperationMonitor) {
      if (this.header == null)
        this.header = readHeaderLocal(); 
      if (this.header.getMessageType() == 1)
        throw new XProtocolError((Mysqlx.Error)readMessageLocal(Mysqlx.Error.class)); 
      return this.header;
    } 
  }
  
  private XMessageHeader readHeaderLocal() throws IOException {
    try {
      byte[] len = new byte[5];
      this.inputStream.readFully(len);
      this.header = new XMessageHeader(len);
    } catch (IOException ex) {
      throw new CJCommunicationsException("Cannot read packet header", ex);
    } 
    return this.header;
  }
  
  private <T extends GeneratedMessageV3> T readMessageLocal(Class<T> messageClass) {
    Parser<T> parser = (Parser<T>)MessageConstants.MESSAGE_CLASS_TO_PARSER.get(messageClass);
    byte[] packet = new byte[this.header.getMessageSize()];
    try {
      this.inputStream.readFully(packet);
    } catch (IOException ex) {
      throw new CJCommunicationsException("Cannot read packet payload", ex);
    } 
    try {
      return (T)parser.parseFrom(packet);
    } catch (InvalidProtocolBufferException ex) {
      throw new WrongArgumentException(ex);
    } finally {
      this.header = null;
    } 
  }
  
  public XMessage readMessage(Optional<XMessage> reuse, XMessageHeader hdr) throws IOException {
    return readMessage(reuse, hdr.getMessageType());
  }
  
  public XMessage readMessage(Optional<XMessage> reuse, int expectedType) throws IOException {
    synchronized (this.waitingSyncOperationMonitor) {
      Class<? extends GeneratedMessageV3> expectedClass = MessageConstants.getMessageClassForType(expectedType);
      List<Notice> notices = null;
      XMessageHeader hdr;
      while ((hdr = readHeader()).getMessageType() == 11 && expectedType != 11) {
        if (notices == null)
          notices = new ArrayList<>(); 
        notices.add(Notice.getInstance(new XMessage((Message)readMessageLocal(MessageConstants.getMessageClassForType(11)))));
      } 
      Class<? extends GeneratedMessageV3> messageClass = MessageConstants.getMessageClassForType(hdr.getMessageType());
      if (expectedClass != messageClass)
        throw new WrongArgumentException("Unexpected message class. Expected '" + expectedClass.getSimpleName() + "' but actually received '" + messageClass
            .getSimpleName() + "'"); 
      return (new XMessage((Message)readMessageLocal(messageClass))).addNotices(notices);
    } 
  }
  
  public void pushMessageListener(MessageListener<XMessage> listener) {
    try {
      this.messageListenerQueue.put(listener);
    } catch (InterruptedException e) {
      throw new CJCommunicationsException("Cannot queue message listener.", e);
    } 
    synchronized (this.dispatchingThreadMonitor) {
      if (this.dispatchingThread == null) {
        ListenersDispatcher ld = new ListenersDispatcher();
        this.dispatchingThread = new Thread(ld, "Message listeners dispatching thread");
        this.dispatchingThread.start();
        int millis = 5000;
        while (!ld.started) {
          try {
            Thread.sleep(10L);
            millis -= 10;
          } catch (InterruptedException e) {
            throw new XProtocolError(e.getMessage(), e);
          } 
          if (millis <= 0)
            throw new XProtocolError("Timeout for starting ListenersDispatcher exceeded."); 
        } 
      } 
    } 
  }
  
  private class ListenersDispatcher implements Runnable {
    private static final long POLL_TIMEOUT = 100L;
    
    boolean started = false;
    
    public void run() {
      synchronized (SyncMessageReader.this.waitingSyncOperationMonitor) {
        this.started = true;
        try {
          label32: while (true) {
            MessageListener<XMessage> l;
            while (true) {
              if ((l = SyncMessageReader.this.messageListenerQueue.poll(100L, TimeUnit.MILLISECONDS)) == null) {
                synchronized (SyncMessageReader.this.dispatchingThreadMonitor) {
                  if (SyncMessageReader.this.messageListenerQueue.peek() == null) {
                    SyncMessageReader.this.dispatchingThread = null;
                  } else {
                    continue;
                  } 
                } 
              } else {
                break;
              } 
            } 
            try {
              XMessage msg = null;
              while (true) {
                XMessageHeader hdr = SyncMessageReader.this.readHeader();
                msg = SyncMessageReader.this.readMessage((Optional<XMessage>)null, hdr);
                if (l.processMessage(msg))
                  continue label32; 
              } 
            } catch (Throwable t) {
              l.error(t);
            } 
          } 
        } catch (InterruptedException e) {
          throw new CJCommunicationsException("Read operation interrupted.", e);
        } 
      } 
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\x\SyncMessageReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */