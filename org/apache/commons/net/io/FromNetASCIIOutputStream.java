package org.apache.commons.net.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class FromNetASCIIOutputStream extends FilterOutputStream {
  private boolean __lastWasCR;
  
  public FromNetASCIIOutputStream(OutputStream output) {
    super(output);
    this.__lastWasCR = false;
  }
  
  private void __write(int ch) throws IOException {
    switch (ch) {
      case 13:
        this.__lastWasCR = true;
        return;
      case 10:
        if (this.__lastWasCR) {
          this.out.write(FromNetASCIIInputStream._lineSeparatorBytes);
          this.__lastWasCR = false;
        } else {
          this.__lastWasCR = false;
          this.out.write(10);
        } 
        return;
    } 
    if (this.__lastWasCR) {
      this.out.write(13);
      this.__lastWasCR = false;
    } 
    this.out.write(ch);
  }
  
  public synchronized void write(int ch) throws IOException {
    if (FromNetASCIIInputStream._noConversionRequired) {
      this.out.write(ch);
      return;
    } 
    __write(ch);
  }
  
  public synchronized void write(byte[] buffer) throws IOException {
    write(buffer, 0, buffer.length);
  }
  
  public synchronized void write(byte[] buffer, int offset, int length) throws IOException {
    if (FromNetASCIIInputStream._noConversionRequired) {
      this.out.write(buffer, offset, length);
      return;
    } 
    while (length-- > 0)
      __write(buffer[offset++]); 
  }
  
  public synchronized void close() throws IOException {
    if (FromNetASCIIInputStream._noConversionRequired) {
      super.close();
      return;
    } 
    if (this.__lastWasCR)
      this.out.write(13); 
    super.close();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\io\FromNetASCIIOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */