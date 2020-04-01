package com.mysql.cj.jdbc.exceptions;

import com.mysql.cj.Messages;
import java.sql.SQLException;

public class NotUpdatable extends SQLException {
  private static final long serialVersionUID = 6004153665887216929L;
  
  public NotUpdatable(String reason) {
    super(reason + Messages.getString("NotUpdatable.1"), "S1000");
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\exceptions\NotUpdatable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */