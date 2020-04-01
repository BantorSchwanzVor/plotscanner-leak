package org.apache.commons.net.examples.mail;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

class Utils {
  static String getPassword(String username, String password) throws IOException {
    if ("-".equals(password)) {
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      password = in.readLine();
    } else if ("*".equals(password)) {
      Console con = System.console();
      if (con != null) {
        char[] pwd = con.readPassword("Password for " + username + ": ", new Object[0]);
        password = new String(pwd);
      } else {
        throw new IOException("Cannot access Console");
      } 
    } else if (password.equals(password.toUpperCase(Locale.ROOT))) {
      String tmp = System.getenv(password);
      if (tmp != null)
        password = tmp; 
    } 
    return password;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\examples\mail\Utils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */