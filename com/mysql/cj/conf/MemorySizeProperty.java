package com.mysql.cj.conf;

import com.mysql.cj.exceptions.ExceptionInterceptor;
import java.util.Properties;
import javax.naming.Reference;

public class MemorySizeProperty extends IntegerProperty {
  private static final long serialVersionUID = 4200558564320133284L;
  
  private String initialValueAsString;
  
  protected String valueAsString;
  
  protected MemorySizeProperty(PropertyDefinition<Integer> propertyDefinition) {
    super(propertyDefinition);
    this.valueAsString = ((Integer)propertyDefinition.getDefaultValue()).toString();
  }
  
  public void initializeFrom(Properties extractFrom, ExceptionInterceptor exceptionInterceptor) {
    super.initializeFrom(extractFrom, exceptionInterceptor);
    this.initialValueAsString = this.valueAsString;
  }
  
  public void initializeFrom(Reference ref, ExceptionInterceptor exceptionInterceptor) {
    super.initializeFrom(ref, exceptionInterceptor);
    this.initialValueAsString = this.valueAsString;
  }
  
  public String getStringValue() {
    return this.valueAsString;
  }
  
  public void setValueInternal(Integer value, String valueAsString, ExceptionInterceptor exceptionInterceptor) {
    super.setValueInternal(value, valueAsString, exceptionInterceptor);
    this.valueAsString = (valueAsString == null) ? String.valueOf(value.intValue()) : valueAsString;
  }
  
  public void resetValue() {
    this.value = this.initialValue;
    this.valueAsString = this.initialValueAsString;
    invokeListeners();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\conf\MemorySizeProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */