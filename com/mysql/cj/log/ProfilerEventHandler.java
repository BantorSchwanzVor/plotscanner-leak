package com.mysql.cj.log;

import com.mysql.cj.Query;
import com.mysql.cj.Session;
import com.mysql.cj.protocol.Resultset;

public interface ProfilerEventHandler {
  void init(Log paramLog);
  
  void destroy();
  
  void consumeEvent(ProfilerEvent paramProfilerEvent);
  
  void processEvent(byte paramByte, Session paramSession, Query paramQuery, Resultset paramResultset, long paramLong, Throwable paramThrowable, String paramString);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\log\ProfilerEventHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */