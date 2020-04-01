package com.mysql.cj.log;

import com.mysql.cj.exceptions.ExceptionFactory;
import com.mysql.cj.exceptions.WrongArgumentException;
import com.mysql.cj.util.Util;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class LogFactory {
  public static Log getLogger(String className, String instanceName) {
    if (className == null)
      throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, "Logger class can not be NULL"); 
    if (instanceName == null)
      throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, "Logger instance name can not be NULL"); 
    try {
      Class<?> loggerClass = null;
      try {
        loggerClass = Class.forName(className);
      } catch (ClassNotFoundException nfe) {
        loggerClass = Class.forName(Util.getPackageName(LogFactory.class) + "." + className);
      } 
      Constructor<?> constructor = loggerClass.getConstructor(new Class[] { String.class });
      return (Log)constructor.newInstance(new Object[] { instanceName });
    } catch (ClassNotFoundException cnfe) {
      throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, "Unable to load class for logger '" + className + "'", cnfe);
    } catch (NoSuchMethodException nsme) {
      throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, "Logger class does not have a single-arg constructor that takes an instance name", nsme);
    } catch (InstantiationException inse) {
      throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, "Unable to instantiate logger class '" + className + "', exception in constructor?", inse);
    } catch (InvocationTargetException ite) {
      throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, "Unable to instantiate logger class '" + className + "', exception in constructor?", ite);
    } catch (IllegalAccessException iae) {
      throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, "Unable to instantiate logger class '" + className + "', constructor not public", iae);
    } catch (ClassCastException cce) {
      throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, "Logger class '" + className + "' does not implement the '" + Log.class
          .getName() + "' interface", cce);
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\log\LogFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */