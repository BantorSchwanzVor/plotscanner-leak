package org.apache.commons.net.examples.mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.pop3.POP3Client;
import org.apache.commons.net.pop3.POP3MessageInfo;
import org.apache.commons.net.pop3.POP3SClient;

public final class POP3Mail {
  public static final void printMessageInfo(BufferedReader reader, int id) throws IOException {
    String from = "";
    String subject = "";
    String line;
    while ((line = reader.readLine()) != null) {
      String lower = line.toLowerCase(Locale.ENGLISH);
      if (lower.startsWith("from: ")) {
        from = line.substring(6).trim();
        continue;
      } 
      if (lower.startsWith("subject: "))
        subject = line.substring(9).trim(); 
    } 
    System.out.println(String.valueOf(Integer.toString(id)) + " From: " + from + "  Subject: " + subject);
  }
  
  public static void main(String[] args) {
    POP3Client pop3;
    int port;
    if (args.length < 3) {
      System.err.println(
          "Usage: POP3Mail <server[:port]> <username> <password|-|*|VARNAME> [TLS [true=implicit]]");
      System.exit(1);
    } 
    String[] arg0 = args[0].split(":");
    String server = arg0[0];
    String username = args[1];
    String password = args[2];
    try {
      password = Utils.getPassword(username, password);
    } catch (IOException e1) {
      System.err.println("Could not retrieve password: " + e1.getMessage());
      return;
    } 
    String proto = (args.length > 3) ? args[3] : null;
    boolean implicit = (args.length > 4) ? Boolean.parseBoolean(args[4]) : false;
    if (proto != null) {
      System.out.println("Using secure protocol: " + proto);
      POP3SClient pOP3SClient = new POP3SClient(proto, implicit);
    } else {
      pop3 = new POP3Client();
    } 
    if (arg0.length == 2) {
      port = Integer.parseInt(arg0[1]);
    } else {
      port = pop3.getDefaultPort();
    } 
    System.out.println("Connecting to server " + server + " on " + port);
    pop3.setDefaultTimeout(60000);
    pop3.addProtocolCommandListener((ProtocolCommandListener)new PrintCommandListener(new PrintWriter(System.out), true));
    try {
      pop3.connect(server);
    } catch (IOException e) {
      System.err.println("Could not connect to server.");
      e.printStackTrace();
      return;
    } 
    try {
      if (!pop3.login(username, password)) {
        System.err.println("Could not login to server.  Check password.");
        pop3.disconnect();
        return;
      } 
      POP3MessageInfo status = pop3.status();
      if (status == null) {
        System.err.println("Could not retrieve status.");
        pop3.logout();
        pop3.disconnect();
        return;
      } 
      System.out.println("Status: " + status);
      POP3MessageInfo[] messages = pop3.listMessages();
      if (messages == null) {
        System.err.println("Could not retrieve message list.");
        pop3.logout();
        pop3.disconnect();
        return;
      } 
      if (messages.length == 0) {
        System.out.println("No messages");
        pop3.logout();
        pop3.disconnect();
        return;
      } 
      System.out.println("Message count: " + messages.length);
      byte b;
      int i;
      POP3MessageInfo[] arrayOfPOP3MessageInfo1;
      for (i = (arrayOfPOP3MessageInfo1 = messages).length, b = 0; b < i; ) {
        POP3MessageInfo msginfo = arrayOfPOP3MessageInfo1[b];
        BufferedReader reader = (BufferedReader)pop3.retrieveMessageTop(msginfo.number, 0);
        if (reader == null) {
          System.err.println("Could not retrieve message header.");
          pop3.logout();
          pop3.disconnect();
          return;
        } 
        printMessageInfo(reader, msginfo.number);
        b++;
      } 
      pop3.logout();
      pop3.disconnect();
    } catch (IOException e) {
      e.printStackTrace();
      return;
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\examples\mail\POP3Mail.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */