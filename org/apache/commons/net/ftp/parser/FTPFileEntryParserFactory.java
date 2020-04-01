package org.apache.commons.net.ftp.parser;

import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFileEntryParser;

public interface FTPFileEntryParserFactory {
  FTPFileEntryParser createFileEntryParser(String paramString) throws ParserInitializationException;
  
  FTPFileEntryParser createFileEntryParser(FTPClientConfig paramFTPClientConfig) throws ParserInitializationException;
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\ftp\parser\FTPFileEntryParserFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */