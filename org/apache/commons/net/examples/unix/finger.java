package org.apache.commons.net.examples.unix;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.commons.net.finger.FingerClient;

public final class finger {
  public static void main(String[] args) {
    boolean longOutput = false;
    int arg = 0;
    InetAddress address = null;
    while (arg < args.length && args[arg].startsWith("-")) {
      if (args[arg].equals("-l")) {
        longOutput = true;
      } else {
        System.err.println("usage: finger [-l] [[[handle][@<server>]] ...]");
        System.exit(1);
      } 
      arg++;
    } 
    FingerClient fingerClient = new FingerClient();
    fingerClient.setDefaultTimeout(60000);
    if (arg >= args.length) {
      try {
        address = InetAddress.getLocalHost();
      } catch (UnknownHostException e) {
        System.err.println("Error unknown host: " + e.getMessage());
        System.exit(1);
      } 
      try {
        fingerClient.connect(address);
        System.out.print(fingerClient.query(longOutput));
        fingerClient.disconnect();
      } catch (IOException e) {
        System.err.println("Error I/O exception: " + e.getMessage());
        System.exit(1);
      } 
      return;
    } 
    while (arg < args.length) {
      String handle;
      int index = args[arg].lastIndexOf('@');
      if (index == -1) {
        handle = args[arg];
        try {
          address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
          System.err.println("Error unknown host: " + e.getMessage());
          System.exit(1);
        } 
      } else {
        handle = args[arg].substring(0, index);
        String host = args[arg].substring(index + 1);
        try {
          address = InetAddress.getByName(host);
          System.out.println("[" + address.getHostName() + "]");
        } catch (UnknownHostException e) {
          System.err.println("Error unknown host: " + e.getMessage());
          System.exit(1);
        } 
      } 
      try {
        fingerClient.connect(address);
        System.out.print(fingerClient.query(longOutput, handle));
        fingerClient.disconnect();
      } catch (IOException e) {
        System.err.println("Error I/O exception: " + e.getMessage());
        System.exit(1);
      } 
      arg++;
      if (arg != args.length)
        System.out.print("\n"); 
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\example\\unix\finger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */