package org.apache.commons.net.ftp;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public interface FTPFileEntryParser {
  FTPFile parseFTPEntry(String paramString);
  
  String readNextEntry(BufferedReader paramBufferedReader) throws IOException;
  
  List<String> preParse(List<String> paramList);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\ftp\FTPFileEntryParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */