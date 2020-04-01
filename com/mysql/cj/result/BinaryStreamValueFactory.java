package com.mysql.cj.result;

import com.mysql.cj.conf.PropertySet;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class BinaryStreamValueFactory extends DefaultValueFactory<InputStream> {
  public BinaryStreamValueFactory(PropertySet pset) {
    super(pset);
  }
  
  public InputStream createFromBytes(byte[] bytes, int offset, int length, Field f) {
    return new ByteArrayInputStream(bytes, offset, length);
  }
  
  public String getTargetTypeName() {
    return InputStream.class.getName();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\result\BinaryStreamValueFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */