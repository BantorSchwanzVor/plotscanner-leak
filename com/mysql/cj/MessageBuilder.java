package com.mysql.cj;

import java.util.List;

public interface MessageBuilder<M extends com.mysql.cj.protocol.Message> {
  M buildSqlStatement(String paramString);
  
  M buildSqlStatement(String paramString, List<Object> paramList);
  
  M buildClose();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\MessageBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */