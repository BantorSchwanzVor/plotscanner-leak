package org.apache.commons.net.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public final class DotTerminatedMessageReader extends BufferedReader {
  private static final char LF = '\n';
  
  private static final char CR = '\r';
  
  private static final int DOT = 46;
  
  private boolean atBeginning;
  
  private boolean eof;
  
  private boolean seenCR;
  
  public DotTerminatedMessageReader(Reader reader) {
    super(reader);
    this.atBeginning = true;
    this.eof = false;
  }
  
  public int read() throws IOException {
    synchronized (this.lock) {
      if (this.eof)
        return -1; 
      int chint = super.read();
      if (chint == -1) {
        this.eof = true;
        return -1;
      } 
      if (this.atBeginning) {
        this.atBeginning = false;
        if (chint == 46) {
          mark(2);
          chint = super.read();
          if (chint == -1) {
            this.eof = true;
            return 46;
          } 
          if (chint == 46)
            return chint; 
          if (chint == 13) {
            chint = super.read();
            if (chint == -1) {
              reset();
              return 46;
            } 
            if (chint == 10) {
              this.atBeginning = true;
              this.eof = true;
              return -1;
            } 
          } 
          reset();
          return 46;
        } 
      } 
      if (this.seenCR) {
        this.seenCR = false;
        if (chint == 10)
          this.atBeginning = true; 
      } 
      if (chint == 13)
        this.seenCR = true; 
      return chint;
    } 
  }
  
  public int read(char[] buffer) throws IOException {
    return read(buffer, 0, buffer.length);
  }
  
  public int read(char[] buffer, int offset, int length) throws IOException {
    if (length < 1)
      return 0; 
    synchronized (this.lock) {
      int ch;
      if ((ch = read()) == -1)
        return -1; 
      int off = offset;
      do {
        buffer[offset++] = (char)ch;
      } while (--length > 0 && (ch = read()) != -1);
      return offset - off;
    } 
  }
  
  public void close() throws IOException {
    synchronized (this.lock) {
      if (!this.eof)
        do {
        
        } while (read() != -1); 
      this.eof = true;
      this.atBeginning = false;
    } 
  }
  
  public String readLine() throws IOException {
    StringBuilder sb = new StringBuilder();
    synchronized (this.lock) {
      int intch;
      while ((intch = read()) != -1) {
        if (intch == 10 && this.atBeginning)
          return sb.substring(0, sb.length() - 1); 
        sb.append((char)intch);
      } 
    } 
    String string = sb.toString();
    if (string.length() == 0)
      return null; 
    return string;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\io\DotTerminatedMessageReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */