package org.apache.commons.net.ftp.parser;

import java.text.ParseException;
import java.util.List;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;

public class MVSFTPEntryParser extends ConfigurableFTPFileEntryParserImpl {
  static final int UNKNOWN_LIST_TYPE = -1;
  
  static final int FILE_LIST_TYPE = 0;
  
  static final int MEMBER_LIST_TYPE = 1;
  
  static final int UNIX_LIST_TYPE = 2;
  
  static final int JES_LEVEL_1_LIST_TYPE = 3;
  
  static final int JES_LEVEL_2_LIST_TYPE = 4;
  
  private int isType = -1;
  
  private UnixFTPEntryParser unixFTPEntryParser;
  
  static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd HH:mm";
  
  static final String FILE_LIST_REGEX = "\\S+\\s+\\S+\\s+\\S+\\s+\\S+\\s+(?:\\S+\\s+)?[FV]\\S*\\s+\\S+\\s+\\S+\\s+(PS|PO|PO-E)\\s+(\\S+)\\s*";
  
  static final String MEMBER_LIST_REGEX = "(\\S+)\\s+\\S+\\s+\\S+\\s+(\\S+)\\s+(\\S+)\\s+\\S+\\s+\\S+\\s+\\S+\\s+\\S+\\s*";
  
  static final String JES_LEVEL_1_LIST_REGEX = "(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s*";
  
  static final String JES_LEVEL_2_LIST_REGEX = "(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+).*";
  
  public MVSFTPEntryParser() {
    super("");
    configure((FTPClientConfig)null);
  }
  
  public FTPFile parseFTPEntry(String entry) {
    if (this.isType == 0)
      return parseFileList(entry); 
    if (this.isType == 1)
      return parseMemberList(entry); 
    if (this.isType == 2)
      return this.unixFTPEntryParser.parseFTPEntry(entry); 
    if (this.isType == 3)
      return parseJeslevel1List(entry); 
    if (this.isType == 4)
      return parseJeslevel2List(entry); 
    return null;
  }
  
  private FTPFile parseFileList(String entry) {
    if (matches(entry)) {
      FTPFile file = new FTPFile();
      file.setRawListing(entry);
      String name = group(2);
      String dsorg = group(1);
      file.setName(name);
      if ("PS".equals(dsorg)) {
        file.setType(0);
      } else if ("PO".equals(dsorg) || "PO-E".equals(dsorg)) {
        file.setType(1);
      } else {
        return null;
      } 
      return file;
    } 
    return null;
  }
  
  private FTPFile parseMemberList(String entry) {
    FTPFile file = new FTPFile();
    if (matches(entry)) {
      file.setRawListing(entry);
      String name = group(1);
      String datestr = String.valueOf(group(2)) + " " + group(3);
      file.setName(name);
      file.setType(0);
      try {
        file.setTimestamp(parseTimestamp(datestr));
      } catch (ParseException parseException) {}
      return file;
    } 
    if (entry != null && entry.trim().length() > 0) {
      file.setRawListing(entry);
      String name = entry.split(" ")[0];
      file.setName(name);
      file.setType(0);
      return file;
    } 
    return null;
  }
  
  private FTPFile parseJeslevel1List(String entry) {
    if (matches(entry)) {
      FTPFile file = new FTPFile();
      if (group(3).equalsIgnoreCase("OUTPUT")) {
        file.setRawListing(entry);
        String name = group(2);
        file.setName(name);
        file.setType(0);
        return file;
      } 
    } 
    return null;
  }
  
  private FTPFile parseJeslevel2List(String entry) {
    if (matches(entry)) {
      FTPFile file = new FTPFile();
      if (group(4).equalsIgnoreCase("OUTPUT")) {
        file.setRawListing(entry);
        String name = group(2);
        file.setName(name);
        file.setType(0);
        return file;
      } 
    } 
    return null;
  }
  
  public List<String> preParse(List<String> orig) {
    if (orig != null && orig.size() > 0) {
      String header = orig.get(0);
      if (header.indexOf("Volume") >= 0 && header.indexOf("Dsname") >= 0) {
        setType(0);
        setRegex("\\S+\\s+\\S+\\s+\\S+\\s+\\S+\\s+(?:\\S+\\s+)?[FV]\\S*\\s+\\S+\\s+\\S+\\s+(PS|PO|PO-E)\\s+(\\S+)\\s*");
      } else if (header.indexOf("Name") >= 0 && header.indexOf("Id") >= 0) {
        setType(1);
        setRegex("(\\S+)\\s+\\S+\\s+\\S+\\s+(\\S+)\\s+(\\S+)\\s+\\S+\\s+\\S+\\s+\\S+\\s+\\S+\\s*");
      } else if (header.indexOf("total") == 0) {
        setType(2);
        this.unixFTPEntryParser = new UnixFTPEntryParser();
      } else if (header.indexOf("Spool Files") >= 30) {
        setType(3);
        setRegex("(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s*");
      } else if (header.indexOf("JOBNAME") == 0 && 
        header.indexOf("JOBID") > 8) {
        setType(4);
        setRegex("(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+).*");
      } else {
        setType(-1);
      } 
      if (this.isType != 3)
        orig.remove(0); 
    } 
    return orig;
  }
  
  void setType(int type) {
    this.isType = type;
  }
  
  protected FTPClientConfig getDefaultConfiguration() {
    return new FTPClientConfig("MVS", 
        "yyyy/MM/dd HH:mm", null);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\ftp\parser\MVSFTPEntryParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */