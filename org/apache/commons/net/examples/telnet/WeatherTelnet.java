package org.apache.commons.net.examples.telnet;

import java.io.IOException;
import org.apache.commons.net.examples.util.IOUtil;
import org.apache.commons.net.telnet.TelnetClient;

public final class WeatherTelnet {
  public static final void main(String[] args) {
    TelnetClient telnet = new TelnetClient();
    try {
      telnet.connect("rainmaker.wunderground.com", 3000);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    } 
    IOUtil.readWrite(telnet.getInputStream(), telnet.getOutputStream(), 
        System.in, System.out);
    try {
      telnet.disconnect();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    } 
    System.exit(0);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\examples\telnet\WeatherTelnet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */