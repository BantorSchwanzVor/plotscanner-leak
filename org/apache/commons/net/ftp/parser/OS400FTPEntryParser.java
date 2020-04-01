package org.apache.commons.net.ftp.parser;

import java.io.File;
import java.text.ParseException;
import java.util.Locale;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;

public class OS400FTPEntryParser extends ConfigurableFTPFileEntryParserImpl {
  private static final String DEFAULT_DATE_FORMAT = "yy/MM/dd HH:mm:ss";
  
  private static final String REGEX = "(\\S+)\\s+(?:(\\d+)\\s+)?(?:(\\S+)\\s+(\\S+)\\s+)?(\\*STMF|\\*DIR|\\*FILE|\\*MEM)\\s+(?:(\\S+)\\s*)?";
  
  public OS400FTPEntryParser() {
    this((FTPClientConfig)null);
  }
  
  public OS400FTPEntryParser(FTPClientConfig config) {
    super("(\\S+)\\s+(?:(\\d+)\\s+)?(?:(\\S+)\\s+(\\S+)\\s+)?(\\*STMF|\\*DIR|\\*FILE|\\*MEM)\\s+(?:(\\S+)\\s*)?");
    configure(config);
  }
  
  public FTPFile parseFTPEntry(String entry) {
    FTPFile file = new FTPFile();
    file.setRawListing(entry);
    if (matches(entry)) {
      int type;
      String usr = group(1);
      String filesize = group(2);
      String datestr = "";
      if (!isNullOrEmpty(group(3)) || !isNullOrEmpty(group(4)))
        datestr = String.valueOf(group(3)) + " " + group(4); 
      String typeStr = group(5);
      String name = group(6);
      boolean mustScanForPathSeparator = true;
      try {
        file.setTimestamp(parseTimestamp(datestr));
      } catch (ParseException parseException) {}
      if (typeStr.equalsIgnoreCase("*STMF")) {
        type = 0;
        if (isNullOrEmpty(filesize) || isNullOrEmpty(name))
          return null; 
      } else if (typeStr.equalsIgnoreCase("*DIR")) {
        type = 1;
        if (isNullOrEmpty(filesize) || isNullOrEmpty(name))
          return null; 
      } else if (typeStr.equalsIgnoreCase("*FILE")) {
        if (name != null && name.toUpperCase(Locale.ROOT).endsWith(".SAVF")) {
          mustScanForPathSeparator = false;
          type = 0;
        } else {
          return null;
        } 
      } else if (typeStr.equalsIgnoreCase("*MEM")) {
        mustScanForPathSeparator = false;
        type = 0;
        if (isNullOrEmpty(name))
          return null; 
        if (!isNullOrEmpty(filesize) || !isNullOrEmpty(datestr))
          return null; 
        name = name.replace('/', File.separatorChar);
      } else {
        type = 3;
      } 
      file.setType(type);
      file.setUser(usr);
      try {
        file.setSize(Long.parseLong(filesize));
      } catch (NumberFormatException numberFormatException) {}
      if (name.endsWith("/"))
        name = name.substring(0, name.length() - 1); 
      if (mustScanForPathSeparator) {
        int pos = name.lastIndexOf('/');
        if (pos > -1)
          name = name.substring(pos + 1); 
      } 
      file.setName(name);
      return file;
    } 
    return null;
  }
  
  private boolean isNullOrEmpty(String string) {
    if (string == null || string.length() == 0)
      return true; 
    return false;
  }
  
  protected FTPClientConfig getDefaultConfiguration() {
    return new FTPClientConfig(
        "OS/400", 
        "yy/MM/dd HH:mm:ss", 
        null);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\ftp\parser\OS400FTPEntryParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */