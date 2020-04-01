package org.apache.commons.net.ftp.parser;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.apache.commons.net.ftp.FTPFileEntryParserImpl;

public abstract class RegexFTPFileEntryParserImpl extends FTPFileEntryParserImpl {
  private Pattern pattern = null;
  
  private MatchResult result = null;
  
  protected Matcher _matcher_ = null;
  
  public RegexFTPFileEntryParserImpl(String regex) {
    compileRegex(regex, 0);
  }
  
  public RegexFTPFileEntryParserImpl(String regex, int flags) {
    compileRegex(regex, flags);
  }
  
  public boolean matches(String s) {
    this.result = null;
    this._matcher_ = this.pattern.matcher(s);
    if (this._matcher_.matches())
      this.result = this._matcher_.toMatchResult(); 
    return (this.result != null);
  }
  
  public int getGroupCnt() {
    if (this.result == null)
      return 0; 
    return this.result.groupCount();
  }
  
  public String group(int matchnum) {
    if (this.result == null)
      return null; 
    return this.result.group(matchnum);
  }
  
  public String getGroupsAsString() {
    StringBuilder b = new StringBuilder();
    for (int i = 1; i <= this.result.groupCount(); i++)
      b.append(i).append(") ").append(this.result.group(i)).append(
          System.getProperty("line.separator")); 
    return b.toString();
  }
  
  public boolean setRegex(String regex) {
    compileRegex(regex, 0);
    return true;
  }
  
  public boolean setRegex(String regex, int flags) {
    compileRegex(regex, flags);
    return true;
  }
  
  private void compileRegex(String regex, int flags) {
    try {
      this.pattern = Pattern.compile(regex, flags);
    } catch (PatternSyntaxException pse) {
      throw new IllegalArgumentException("Unparseable regex supplied: " + regex);
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\ftp\parser\RegexFTPFileEntryParserImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */