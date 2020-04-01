package com.mysql.cj.jdbc;

import com.mysql.cj.conf.DefaultPropertySet;
import com.mysql.cj.conf.PropertyDefinition;
import com.mysql.cj.conf.PropertyDefinitions;
import com.mysql.cj.conf.PropertyKey;
import com.mysql.cj.conf.RuntimeProperty;
import com.mysql.cj.util.StringUtils;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcPropertySetImpl extends DefaultPropertySet implements JdbcPropertySet {
  private static final long serialVersionUID = -8223499903182568260L;
  
  public void postInitialization() {
    if (((Integer)getIntegerProperty(PropertyKey.maxRows).getValue()).intValue() == 0)
      getProperty(PropertyKey.maxRows).setValue(Integer.valueOf(-1), null); 
    String testEncoding = (String)getStringProperty(PropertyKey.characterEncoding).getValue();
    if (testEncoding != null) {
      String testString = "abc";
      StringUtils.getBytes(testString, testEncoding);
    } 
    if (((Boolean)getBooleanProperty(PropertyKey.useCursorFetch).getValue()).booleanValue())
      getProperty(PropertyKey.useServerPrepStmts).setValue(Boolean.valueOf(true)); 
  }
  
  public DriverPropertyInfo[] exposeAsDriverPropertyInfo(Properties info, int slotsToReserve) throws SQLException {
    initializeProperties(info);
    int numProperties = PropertyDefinitions.PROPERTY_KEY_TO_PROPERTY_DEFINITION.size();
    int listSize = numProperties + slotsToReserve;
    DriverPropertyInfo[] driverProperties = new DriverPropertyInfo[listSize];
    int i = slotsToReserve;
    for (PropertyKey propKey : PropertyDefinitions.PROPERTY_KEY_TO_PROPERTY_DEFINITION.keySet())
      driverProperties[i++] = getAsDriverPropertyInfo(getProperty(propKey)); 
    return driverProperties;
  }
  
  private DriverPropertyInfo getAsDriverPropertyInfo(RuntimeProperty<?> pr) {
    PropertyDefinition<?> pdef = pr.getPropertyDefinition();
    DriverPropertyInfo dpi = new DriverPropertyInfo(pdef.getName(), null);
    dpi.choices = pdef.getAllowableValues();
    dpi.value = (pr.getStringValue() != null) ? pr.getStringValue() : null;
    dpi.required = false;
    dpi.description = pdef.getDescription();
    return dpi;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\JdbcPropertySetImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */