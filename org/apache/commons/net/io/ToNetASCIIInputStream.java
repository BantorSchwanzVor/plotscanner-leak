package org.apache.commons.net.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ToNetASCIIInputStream extends FilterInputStream {
  private static final int __NOTHING_SPECIAL = 0;
  
  private static final int __LAST_WAS_CR = 1;
  
  private static final int __LAST_WAS_NL = 2;
  
  private int __status;
  
  public ToNetASCIIInputStream(InputStream input) {
    super(input);
    this.__status = 0;
  }
  
  public int read() throws IOException {
    if (this.__status == 2) {
      this.__status = 0;
      return 10;
    } 
    int ch = this.in.read();
    switch (ch) {
      case 13:
        this.__status = 1;
        return 13;
      case 10:
        if (this.__status != 1) {
          this.__status = 2;
          return 13;
        } 
        break;
    } 
    this.__status = 0;
    return ch;
  }
  
  public int read(byte[] buffer) throws IOException {
    return read(buffer, 0, buffer.length);
  }
  
  public int read(byte[] buffer, int offset, int length) throws IOException {
    if (length < 1)
      return 0; 
    int ch = available();
    if (length > ch)
      length = ch; 
    if (length < 1)
      length = 1; 
    if ((ch = read()) == -1)
      return -1; 
    int off = offset;
    do {
      buffer[offset++] = (byte)ch;
    } while (--length > 0 && (ch = read()) != -1);
    return offset - off;
  }
  
  public boolean markSupported() {
    return false;
  }
  
  public int available() throws IOException {
    int result = this.in.available();
    if (this.__status == 2)
      return result + 1; 
    return result;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\io\ToNetASCIIInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */