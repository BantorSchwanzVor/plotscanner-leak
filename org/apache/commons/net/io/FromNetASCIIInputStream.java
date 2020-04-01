package org.apache.commons.net.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.io.UnsupportedEncodingException;

public final class FromNetASCIIInputStream extends PushbackInputStream {
  static final boolean _noConversionRequired;
  
  static final String _lineSeparator = System.getProperty("line.separator");
  
  static final byte[] _lineSeparatorBytes;
  
  static {
    _noConversionRequired = _lineSeparator.equals("\r\n");
    try {
      _lineSeparatorBytes = _lineSeparator.getBytes("US-ASCII");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("Broken JVM - cannot find US-ASCII charset!", e);
    } 
  }
  
  private int __length = 0;
  
  public static final boolean isConversionRequired() {
    return !_noConversionRequired;
  }
  
  public FromNetASCIIInputStream(InputStream input) {
    super(input, _lineSeparatorBytes.length + 1);
  }
  
  private int __read() throws IOException {
    int ch = super.read();
    if (ch == 13) {
      ch = super.read();
      if (ch == 10) {
        unread(_lineSeparatorBytes);
        ch = super.read();
        this.__length--;
      } else {
        if (ch != -1)
          unread(ch); 
        return 13;
      } 
    } 
    return ch;
  }
  
  public int read() throws IOException {
    if (_noConversionRequired)
      return super.read(); 
    return __read();
  }
  
  public int read(byte[] buffer) throws IOException {
    return read(buffer, 0, buffer.length);
  }
  
  public int read(byte[] buffer, int offset, int length) throws IOException {
    if (_noConversionRequired)
      return super.read(buffer, offset, length); 
    if (length < 1)
      return 0; 
    int ch = available();
    this.__length = (length > ch) ? ch : length;
    if (this.__length < 1)
      this.__length = 1; 
    if ((ch = __read()) == -1)
      return -1; 
    int off = offset;
    do {
      buffer[offset++] = (byte)ch;
    } while (--this.__length > 0 && (ch = __read()) != -1);
    return offset - off;
  }
  
  public int available() throws IOException {
    if (this.in == null)
      throw new IOException("Stream closed"); 
    return this.buf.length - this.pos + this.in.available();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\io\FromNetASCIIInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */