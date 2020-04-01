package org.apache.commons.net.ftp.parser;

import java.text.ParseException;
import org.apache.commons.net.ftp.Configurable;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;

public class NTFTPEntryParser extends ConfigurableFTPFileEntryParserImpl {
  private static final String DEFAULT_DATE_FORMAT = "MM-dd-yy hh:mma";
  
  private static final String DEFAULT_DATE_FORMAT2 = "MM-dd-yy kk:mm";
  
  private final FTPTimestampParser timestampParser;
  
  private static final String REGEX = "(\\S+)\\s+(\\S+)\\s+(?:(<DIR>)|([0-9]+))\\s+(\\S.*)";
  
  public NTFTPEntryParser() {
    this((FTPClientConfig)null);
  }
  
  public NTFTPEntryParser(FTPClientConfig config) {
    super("(\\S+)\\s+(\\S+)\\s+(?:(<DIR>)|([0-9]+))\\s+(\\S.*)", 32);
    configure(config);
    FTPClientConfig config2 = new FTPClientConfig(
        "WINDOWS", 
        "MM-dd-yy kk:mm", 
        null);
    config2.setDefaultDateFormatStr("MM-dd-yy kk:mm");
    this.timestampParser = new FTPTimestampParserImpl();
    ((Configurable)this.timestampParser).configure(config2);
  }
  
  public FTPFile parseFTPEntry(String entry) {
    FTPFile f = new FTPFile();
    f.setRawListing(entry);
    if (matches(entry)) {
      String datestr = String.valueOf(group(1)) + " " + group(2);
      String dirString = group(3);
      String size = group(4);
      String name = group(5);
      try {
        f.setTimestamp(parseTimestamp(datestr));
      } catch (ParseException e) {
        try {
          f.setTimestamp(this.timestampParser.parseTimestamp(datestr));
        } catch (ParseException parseException) {}
      } 
      if (name == null || name.equals(".") || name.equals(".."))
        return null; 
      f.setName(name);
      if ("<DIR>".equals(dirString)) {
        f.setType(1);
        f.setSize(0L);
      } else {
        f.setType(0);
        if (size != null)
          f.setSize(Long.parseLong(size)); 
      } 
      return f;
    } 
    return null;
  }
  
  public FTPClientConfig getDefaultConfiguration() {
    return new FTPClientConfig(
        "WINDOWS", 
        "MM-dd-yy hh:mma", 
        null);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\ftp\parser\NTFTPEntryParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */