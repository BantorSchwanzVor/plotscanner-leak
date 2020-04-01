package org.apache.commons.net.ftp.parser;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileEntryParser;
import org.apache.commons.net.ftp.FTPFileEntryParserImpl;

public class CompositeFileEntryParser extends FTPFileEntryParserImpl {
  private final FTPFileEntryParser[] ftpFileEntryParsers;
  
  private FTPFileEntryParser cachedFtpFileEntryParser;
  
  public CompositeFileEntryParser(FTPFileEntryParser[] ftpFileEntryParsers) {
    this.cachedFtpFileEntryParser = null;
    this.ftpFileEntryParsers = ftpFileEntryParsers;
  }
  
  public FTPFile parseFTPEntry(String listEntry) {
    if (this.cachedFtpFileEntryParser != null) {
      FTPFile matched = this.cachedFtpFileEntryParser.parseFTPEntry(listEntry);
      if (matched != null)
        return matched; 
    } else {
      byte b;
      int i;
      FTPFileEntryParser[] arrayOfFTPFileEntryParser;
      for (i = (arrayOfFTPFileEntryParser = this.ftpFileEntryParsers).length, b = 0; b < i; ) {
        FTPFileEntryParser ftpFileEntryParser = arrayOfFTPFileEntryParser[b];
        FTPFile matched = ftpFileEntryParser.parseFTPEntry(listEntry);
        if (matched != null) {
          this.cachedFtpFileEntryParser = ftpFileEntryParser;
          return matched;
        } 
        b++;
      } 
    } 
    return null;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\ftp\parser\CompositeFileEntryParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */