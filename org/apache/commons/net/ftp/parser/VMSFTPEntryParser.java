package org.apache.commons.net.ftp.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.StringTokenizer;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileEntryParser;
import org.apache.commons.net.ftp.FTPListParseEngine;

public class VMSFTPEntryParser extends ConfigurableFTPFileEntryParserImpl {
  private static final String DEFAULT_DATE_FORMAT = "d-MMM-yyyy HH:mm:ss";
  
  private static final String REGEX = "(.*?;[0-9]+)\\s*(\\d+)/\\d+\\s*(\\S+)\\s+(\\S+)\\s+\\[(([0-9$A-Za-z_]+)|([0-9$A-Za-z_]+),([0-9$a-zA-Z_]+))\\]?\\s*\\([a-zA-Z]*,([a-zA-Z]*),([a-zA-Z]*),([a-zA-Z]*)\\)";
  
  public VMSFTPEntryParser() {
    this((FTPClientConfig)null);
  }
  
  public VMSFTPEntryParser(FTPClientConfig config) {
    super("(.*?;[0-9]+)\\s*(\\d+)/\\d+\\s*(\\S+)\\s+(\\S+)\\s+\\[(([0-9$A-Za-z_]+)|([0-9$A-Za-z_]+),([0-9$a-zA-Z_]+))\\]?\\s*\\([a-zA-Z]*,([a-zA-Z]*),([a-zA-Z]*),([a-zA-Z]*)\\)");
    configure(config);
  }
  
  public FTPFile parseFTPEntry(String entry) {
    long longBlock = 512L;
    if (matches(entry)) {
      String grp, user;
      FTPFile f = new FTPFile();
      f.setRawListing(entry);
      String name = group(1);
      String size = group(2);
      String datestr = String.valueOf(group(3)) + " " + group(4);
      String owner = group(5);
      String[] permissions = new String[3];
      permissions[0] = group(9);
      permissions[1] = group(10);
      permissions[2] = group(11);
      try {
        f.setTimestamp(parseTimestamp(datestr));
      } catch (ParseException parseException) {}
      StringTokenizer t = new StringTokenizer(owner, ",");
      switch (t.countTokens()) {
        case 1:
          grp = null;
          user = t.nextToken();
          break;
        case 2:
          grp = t.nextToken();
          user = t.nextToken();
          break;
        default:
          grp = null;
          user = null;
          break;
      } 
      if (name.lastIndexOf(".DIR") != -1) {
        f.setType(1);
      } else {
        f.setType(0);
      } 
      if (isVersioning()) {
        f.setName(name);
      } else {
        name = name.substring(0, name.lastIndexOf(';'));
        f.setName(name);
      } 
      long sizeInBytes = Long.parseLong(size) * longBlock;
      f.setSize(sizeInBytes);
      f.setGroup(grp);
      f.setUser(user);
      for (int access = 0; access < 3; access++) {
        String permission = permissions[access];
        f.setPermission(access, 0, (permission.indexOf('R') >= 0));
        f.setPermission(access, 1, (permission.indexOf('W') >= 0));
        f.setPermission(access, 2, (permission.indexOf('E') >= 0));
      } 
      return f;
    } 
    return null;
  }
  
  public String readNextEntry(BufferedReader reader) throws IOException {
    String line = reader.readLine();
    StringBuilder entry = new StringBuilder();
    while (line != null) {
      if (line.startsWith("Directory") || line.startsWith("Total")) {
        line = reader.readLine();
        continue;
      } 
      entry.append(line);
      if (line.trim().endsWith(")"))
        break; 
      line = reader.readLine();
    } 
    return (entry.length() == 0) ? null : entry.toString();
  }
  
  protected boolean isVersioning() {
    return false;
  }
  
  protected FTPClientConfig getDefaultConfiguration() {
    return new FTPClientConfig(
        "VMS", 
        "d-MMM-yyyy HH:mm:ss", 
        null);
  }
  
  @Deprecated
  public FTPFile[] parseFileList(InputStream listStream) throws IOException {
    FTPListParseEngine engine = new FTPListParseEngine((FTPFileEntryParser)this);
    engine.readServerList(listStream, null);
    return engine.getFiles();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\ftp\parser\VMSFTPEntryParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */