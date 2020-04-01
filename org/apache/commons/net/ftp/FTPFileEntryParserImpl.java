package org.apache.commons.net.ftp;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public abstract class FTPFileEntryParserImpl implements FTPFileEntryParser {
  public String readNextEntry(BufferedReader reader) throws IOException {
    return reader.readLine();
  }
  
  public List<String> preParse(List<String> original) {
    return original;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\ftp\FTPFileEntryParserImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */