package com.mysql.cj.xdevapi;

import com.mysql.cj.conf.PropertySet;
import com.mysql.cj.exceptions.AssertionFailedException;
import com.mysql.cj.result.DefaultValueFactory;
import com.mysql.cj.result.Field;
import com.mysql.cj.util.StringUtils;
import java.io.IOException;
import java.io.StringReader;

public class DbDocValueFactory extends DefaultValueFactory<DbDoc> {
  public DbDocValueFactory(PropertySet pset) {
    super(pset);
  }
  
  public DbDoc createFromBytes(byte[] bytes, int offset, int length, Field f) {
    try {
      return JsonParser.parseDoc(new StringReader(StringUtils.toString(bytes, offset, length, f.getEncoding())));
    } catch (IOException ex) {
      throw AssertionFailedException.shouldNotHappen(ex);
    } 
  }
  
  public DbDoc createFromNull() {
    return null;
  }
  
  public String getTargetTypeName() {
    return DbDoc.class.getName();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\xdevapi\DbDocValueFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */