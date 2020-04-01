package org.apache.commons.net.ftp.parser;

import java.text.ParseException;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;

public class NetwareFTPEntryParser extends ConfigurableFTPFileEntryParserImpl {
  private static final String DEFAULT_DATE_FORMAT = "MMM dd yyyy";
  
  private static final String DEFAULT_RECENT_DATE_FORMAT = "MMM dd HH:mm";
  
  private static final String REGEX = "(d|-){1}\\s+\\[([-A-Z]+)\\]\\s+(\\S+)\\s+(\\d+)\\s+(\\S+\\s+\\S+\\s+((\\d+:\\d+)|(\\d{4})))\\s+(.*)";
  
  public NetwareFTPEntryParser() {
    this((FTPClientConfig)null);
  }
  
  public NetwareFTPEntryParser(FTPClientConfig config) {
    super("(d|-){1}\\s+\\[([-A-Z]+)\\]\\s+(\\S+)\\s+(\\d+)\\s+(\\S+\\s+\\S+\\s+((\\d+:\\d+)|(\\d{4})))\\s+(.*)");
    configure(config);
  }
  
  public FTPFile parseFTPEntry(String entry) {
    FTPFile f = new FTPFile();
    if (matches(entry)) {
      String dirString = group(1);
      String attrib = group(2);
      String user = group(3);
      String size = group(4);
      String datestr = group(5);
      String name = group(9);
      try {
        f.setTimestamp(parseTimestamp(datestr));
      } catch (ParseException parseException) {}
      if (dirString.trim().equals("d")) {
        f.setType(1);
      } else {
        f.setType(0);
      } 
      f.setUser(user);
      f.setName(name.trim());
      f.setSize(Long.parseLong(size.trim()));
      if (attrib.indexOf('R') != -1)
        f.setPermission(0, 0, 
            true); 
      if (attrib.indexOf('W') != -1)
        f.setPermission(0, 1, 
            true); 
      return f;
    } 
    return null;
  }
  
  protected FTPClientConfig getDefaultConfiguration() {
    return new FTPClientConfig("NETWARE", 
        "MMM dd yyyy", "MMM dd HH:mm");
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\ftp\parser\NetwareFTPEntryParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */