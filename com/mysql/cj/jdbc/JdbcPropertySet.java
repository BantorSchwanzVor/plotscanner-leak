package com.mysql.cj.jdbc;

import com.mysql.cj.conf.PropertySet;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

public interface JdbcPropertySet extends PropertySet {
  DriverPropertyInfo[] exposeAsDriverPropertyInfo(Properties paramProperties, int paramInt) throws SQLException;
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\JdbcPropertySet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */