package org.apache.commons.net.telnet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TelnetClient extends Telnet {
  private static final int DEFAULT_MAX_SUBNEGOTIATION_LENGTH = 512;
  
  final int _maxSubnegotiationLength;
  
  private InputStream __input;
  
  private OutputStream __output;
  
  protected boolean readerThread = true;
  
  private TelnetInputListener inputListener;
  
  public TelnetClient() {
    this("VT100", 512);
  }
  
  public TelnetClient(String termtype) {
    this(termtype, 512);
  }
  
  public TelnetClient(int maxSubnegotiationLength) {
    this("VT100", maxSubnegotiationLength);
  }
  
  public TelnetClient(String termtype, int maxSubnegotiationLength) {
    super(termtype);
    this.__input = null;
    this.__output = null;
    this._maxSubnegotiationLength = maxSubnegotiationLength;
  }
  
  void _flushOutputStream() throws IOException {
    this._output_.flush();
  }
  
  void _closeOutputStream() throws IOException {
    try {
      this._output_.close();
    } finally {
      this._output_ = null;
    } 
  }
  
  protected void _connectAction_() throws IOException {
    super._connectAction_();
    TelnetInputStream tmp = new TelnetInputStream(this._input_, this, this.readerThread);
    if (this.readerThread)
      tmp._start(); 
    this.__input = new BufferedInputStream(tmp);
    this.__output = new TelnetOutputStream(this);
  }
  
  public void disconnect() throws IOException {
    try {
      if (this.__input != null)
        this.__input.close(); 
      if (this.__output != null)
        this.__output.close(); 
    } finally {
      this.__output = null;
      this.__input = null;
      super.disconnect();
    } 
  }
  
  public OutputStream getOutputStream() {
    return this.__output;
  }
  
  public InputStream getInputStream() {
    return this.__input;
  }
  
  public boolean getLocalOptionState(int option) {
    return (_stateIsWill(option) && _requestedWill(option));
  }
  
  public boolean getRemoteOptionState(int option) {
    return (_stateIsDo(option) && _requestedDo(option));
  }
  
  public boolean sendAYT(long timeout) throws IOException, IllegalArgumentException, InterruptedException {
    return _sendAYT(timeout);
  }
  
  public void sendSubnegotiation(int[] message) throws IOException, IllegalArgumentException {
    if (message.length < 1)
      throw new IllegalArgumentException("zero length message"); 
    _sendSubnegotiation(message);
  }
  
  public void sendCommand(byte command) throws IOException, IllegalArgumentException {
    _sendCommand(command);
  }
  
  public void addOptionHandler(TelnetOptionHandler opthand) throws InvalidTelnetOptionException, IOException {
    super.addOptionHandler(opthand);
  }
  
  public void deleteOptionHandler(int optcode) throws InvalidTelnetOptionException, IOException {
    super.deleteOptionHandler(optcode);
  }
  
  public void registerSpyStream(OutputStream spystream) {
    _registerSpyStream(spystream);
  }
  
  public void stopSpyStream() {
    _stopSpyStream();
  }
  
  public void registerNotifHandler(TelnetNotificationHandler notifhand) {
    super.registerNotifHandler(notifhand);
  }
  
  public void unregisterNotifHandler() {
    super.unregisterNotifHandler();
  }
  
  public void setReaderThread(boolean flag) {
    this.readerThread = flag;
  }
  
  public boolean getReaderThread() {
    return this.readerThread;
  }
  
  public synchronized void registerInputListener(TelnetInputListener listener) {
    this.inputListener = listener;
  }
  
  public synchronized void unregisterInputListener() {
    this.inputListener = null;
  }
  
  void notifyInputListener() {
    TelnetInputListener listener;
    synchronized (this) {
      listener = this.inputListener;
    } 
    if (listener != null)
      listener.telnetInputAvailable(); 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\telnet\TelnetClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */