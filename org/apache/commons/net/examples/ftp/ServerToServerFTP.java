package org.apache.commons.net.examples.ftp;

import java.io.IOException;
import java.io.PrintWriter;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public final class ServerToServerFTP {
  public static void main(String[] args) {
    int port1 = 0, port2 = 0;
    if (args.length < 8) {
      System.err.println(
          "Usage: ftp <host1> <user1> <pass1> <file1> <host2> <user2> <pass2> <file2>");
      System.exit(1);
    } 
    String server1 = args[0];
    String[] parts = server1.split(":");
    if (parts.length == 2) {
      server1 = parts[0];
      port1 = Integer.parseInt(parts[1]);
    } 
    String username1 = args[1];
    String password1 = args[2];
    String file1 = args[3];
    String server2 = args[4];
    parts = server2.split(":");
    if (parts.length == 2) {
      server2 = parts[0];
      port2 = Integer.parseInt(parts[1]);
    } 
    String username2 = args[5];
    String password2 = args[6];
    String file2 = args[7];
    PrintCommandListener printCommandListener = new PrintCommandListener(new PrintWriter(System.out), true);
    FTPClient ftp1 = new FTPClient();
    ftp1.addProtocolCommandListener((ProtocolCommandListener)printCommandListener);
    FTPClient ftp2 = new FTPClient();
    ftp2.addProtocolCommandListener((ProtocolCommandListener)printCommandListener);
    try {
      if (port1 > 0) {
        ftp1.connect(server1, port1);
      } else {
        ftp1.connect(server1);
      } 
      System.out.println("Connected to " + server1 + ".");
      int reply = ftp1.getReplyCode();
      if (!FTPReply.isPositiveCompletion(reply)) {
        ftp1.disconnect();
        System.err.println("FTP server1 refused connection.");
        System.exit(1);
      } 
    } catch (IOException e) {
      if (ftp1.isConnected())
        try {
          ftp1.disconnect();
        } catch (IOException iOException) {} 
      System.err.println("Could not connect to server1.");
      e.printStackTrace();
      System.exit(1);
    } 
    try {
      if (port2 > 0) {
        ftp2.connect(server2, port2);
      } else {
        ftp2.connect(server2);
      } 
      System.out.println("Connected to " + server2 + ".");
      int reply = ftp2.getReplyCode();
      if (!FTPReply.isPositiveCompletion(reply)) {
        ftp2.disconnect();
        System.err.println("FTP server2 refused connection.");
        System.exit(1);
      } 
    } catch (IOException e) {
      if (ftp2.isConnected())
        try {
          ftp2.disconnect();
        } catch (IOException iOException) {} 
      System.err.println("Could not connect to server2.");
      e.printStackTrace();
      System.exit(1);
    } 
    try {
    
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    } finally {
      try {
        if (ftp1.isConnected()) {
          ftp1.logout();
          ftp1.disconnect();
        } 
      } catch (IOException iOException) {}
      try {
        if (ftp2.isConnected()) {
          ftp2.logout();
          ftp2.disconnect();
        } 
      } catch (IOException iOException) {}
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\examples\ftp\ServerToServerFTP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */