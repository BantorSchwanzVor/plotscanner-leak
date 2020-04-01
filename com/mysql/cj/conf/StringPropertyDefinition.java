package com.mysql.cj.conf;

import com.mysql.cj.exceptions.ExceptionInterceptor;

public class StringPropertyDefinition extends AbstractPropertyDefinition<String> {
  private static final long serialVersionUID = 8228934389127796555L;
  
  public StringPropertyDefinition(String name, String alias, String defaultValue, boolean isRuntimeModifiable, String description, String sinceVersion, String category, int orderInCategory) {
    super(name, alias, defaultValue, isRuntimeModifiable, description, sinceVersion, category, orderInCategory);
  }
  
  public StringPropertyDefinition(PropertyKey key, String defaultValue, boolean isRuntimeModifiable, String description, String sinceVersion, String category, int orderInCategory) {
    super(key, defaultValue, isRuntimeModifiable, description, sinceVersion, category, orderInCategory);
  }
  
  public String parseObject(String value, ExceptionInterceptor exceptionInterceptor) {
    return value;
  }
  
  public RuntimeProperty<String> createRuntimeProperty() {
    return new StringProperty(this);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\conf\StringPropertyDefinition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */