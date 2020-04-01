package org.apache.commons.net.telnet;

import java.io.IOException;
import java.io.OutputStream;

final class TelnetOutputStream extends OutputStream {
  private final TelnetClient __client;
  
  private final boolean __convertCRtoCRLF = true;
  
  private boolean __lastWasCR = false;
  
  TelnetOutputStream(TelnetClient client) {
    this.__client = client;
  }
  
  public void write(int ch) throws IOException {
    synchronized (this.__client) {
      ch &= 0xFF;
      if (this.__client._requestedWont(0)) {
        if (this.__lastWasCR) {
          this.__client._sendByte(10);
          if (ch == 10) {
            this.__lastWasCR = false;
            return;
          } 
        } 
        switch (ch) {
          case 13:
            this.__client._sendByte(13);
            this.__lastWasCR = true;
            break;
          case 10:
            if (!this.__lastWasCR)
              this.__client._sendByte(13); 
            this.__client._sendByte(ch);
            this.__lastWasCR = false;
            break;
          case 255:
            this.__client._sendByte(255);
            this.__client._sendByte(255);
            this.__lastWasCR = false;
            break;
          default:
            this.__client._sendByte(ch);
            this.__lastWasCR = false;
            break;
        } 
      } else if (ch == 255) {
        this.__client._sendByte(ch);
        this.__client._sendByte(255);
      } else {
        this.__client._sendByte(ch);
      } 
    } 
  }
  
  public void write(byte[] buffer) throws IOException {
    write(buffer, 0, buffer.length);
  }
  
  public void write(byte[] buffer, int offset, int length) throws IOException {
    synchronized (this.__client) {
      while (length-- > 0)
        write(buffer[offset++]); 
    } 
  }
  
  public void flush() throws IOException {
    this.__client._flushOutputStream();
  }
  
  public void close() throws IOException {
    this.__client._closeOutputStream();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\telnet\TelnetOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */