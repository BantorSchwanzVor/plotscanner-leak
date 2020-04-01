package com.mysql.cj.protocol;

import com.mysql.cj.CharsetMapping;
import com.mysql.cj.Messages;
import com.mysql.cj.ServerVersion;
import com.mysql.cj.conf.PropertySet;
import com.mysql.cj.exceptions.ExceptionFactory;
import com.mysql.cj.exceptions.ExceptionInterceptor;

public interface AuthenticationProvider<M extends Message> {
  void init(Protocol<M> paramProtocol, PropertySet paramPropertySet, ExceptionInterceptor paramExceptionInterceptor);
  
  void connect(ServerSession paramServerSession, String paramString1, String paramString2, String paramString3);
  
  void changeUser(ServerSession paramServerSession, String paramString1, String paramString2, String paramString3);
  
  String getEncodingForHandshake();
  
  static byte getCharsetForHandshake(String enc, ServerVersion sv) {
    int charsetIndex = 0;
    if (enc != null)
      charsetIndex = CharsetMapping.getCollationIndexForJavaEncoding(enc, sv); 
    if (charsetIndex == 0)
      charsetIndex = 33; 
    if (charsetIndex > 255)
      throw ExceptionFactory.createException(Messages.getString("MysqlIO.113", new Object[] { enc })); 
    return (byte)charsetIndex;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\AuthenticationProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */