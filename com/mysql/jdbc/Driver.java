package com.mysql.jdbc;

import com.mysql.cj.jdbc.Driver;

public class Driver extends Driver {
  static {
    System.err.println("Loading class `com.mysql.jdbc.Driver'. This is deprecated. The new driver class is `com.mysql.cj.jdbc.Driver'. The driver is automatically registered via the SPI and manual loading of the driver class is generally unnecessary.");
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\jdbc\Driver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */