package com.mysql.cj.jdbc;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Driver extends NonRegisteringDriver implements Driver {
  static {
    try {
      DriverManager.registerDriver(new Driver());
    } catch (SQLException E) {
      throw new RuntimeException("Can't register driver!");
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\Driver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */