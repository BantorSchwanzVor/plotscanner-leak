package org.apache.commons.net.imap;

public enum IMAPCommand {
  CAPABILITY(
    
    0),
  NOOP(0),
  LOGOUT(0),
  STARTTLS(
    0),
  AUTHENTICATE(1),
  LOGIN(2),
  XOAUTH(1),
  SELECT(
    1),
  EXAMINE(1),
  CREATE(1),
  DELETE(1),
  RENAME(2),
  SUBSCRIBE(1),
  UNSUBSCRIBE(1),
  LIST(2),
  LSUB(2),
  STATUS(2),
  APPEND(2, 4),
  CHECK(0),
  CLOSE(0),
  EXPUNGE(0),
  SEARCH(1, 2147483647),
  FETCH(2),
  STORE(3),
  COPY(2),
  UID(2, 2147483647);
  
  private final String imapCommand;
  
  private final int minParamCount;
  
  private final int maxParamCount;
  
  IMAPCommand(String name, int minCount, int maxCount) {
    this.imapCommand = name;
    this.minParamCount = minCount;
    this.maxParamCount = maxCount;
  }
  
  public static final String getCommand(IMAPCommand command) {
    return command.getIMAPCommand();
  }
  
  public String getIMAPCommand() {
    return (this.imapCommand != null) ? this.imapCommand : name();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\imap\IMAPCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */