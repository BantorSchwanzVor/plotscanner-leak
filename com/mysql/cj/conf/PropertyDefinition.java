package com.mysql.cj.conf;

import com.mysql.cj.exceptions.ExceptionInterceptor;

public interface PropertyDefinition<T> {
  boolean hasValueConstraints();
  
  boolean isRangeBased();
  
  PropertyKey getPropertyKey();
  
  String getName();
  
  String getCcAlias();
  
  boolean hasCcAlias();
  
  T getDefaultValue();
  
  boolean isRuntimeModifiable();
  
  String getDescription();
  
  String getSinceVersion();
  
  String getCategory();
  
  int getOrder();
  
  String[] getAllowableValues();
  
  int getLowerBound();
  
  int getUpperBound();
  
  T parseObject(String paramString, ExceptionInterceptor paramExceptionInterceptor);
  
  RuntimeProperty<T> createRuntimeProperty();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\conf\PropertyDefinition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */