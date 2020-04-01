package org.apache.commons.net.examples.mail;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.io.Util;
import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.smtp.SMTPReply;
import org.apache.commons.net.smtp.SimpleSMTPHeader;

public final class SMTPMail {
  public static void main(String[] args) {
    List<String> ccList = new ArrayList<>();
    FileReader fileReader = null;
    if (args.length < 1) {
      System.err.println("Usage: SMTPMail <smtpserver>");
      System.exit(1);
    } 
    String server = args[0];
    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
    try {
      System.out.print("From: ");
      System.out.flush();
      String sender = stdin.readLine();
      System.out.print("To: ");
      System.out.flush();
      String recipient = stdin.readLine();
      System.out.print("Subject: ");
      System.out.flush();
      String subject = stdin.readLine();
      SimpleSMTPHeader header = new SimpleSMTPHeader(sender, recipient, subject);
      while (true) {
        System.out.print("CC <enter one address per line, hit enter to end>: ");
        System.out.flush();
        String cc = stdin.readLine();
        if (cc == null || cc.length() == 0)
          break; 
        header.addCC(cc.trim());
        ccList.add(cc.trim());
      } 
      System.out.print("Filename: ");
      System.out.flush();
      String filename = stdin.readLine();
      try {
        fileReader = new FileReader(filename);
      } catch (FileNotFoundException e) {
        System.err.println("File not found. " + e.getMessage());
      } 
      SMTPClient client = new SMTPClient();
      client.addProtocolCommandListener((ProtocolCommandListener)new PrintCommandListener(
            new PrintWriter(System.out), true));
      client.connect(server);
      if (!SMTPReply.isPositiveCompletion(client.getReplyCode())) {
        client.disconnect();
        System.err.println("SMTP server refused connection.");
        System.exit(1);
      } 
      client.login();
      client.setSender(sender);
      client.addRecipient(recipient);
      for (String recpt : ccList)
        client.addRecipient(recpt); 
      Writer writer = client.sendMessageData();
      if (writer != null) {
        writer.write(header.toString());
        Util.copyReader(fileReader, writer);
        writer.close();
        client.completePendingCommand();
      } 
      if (fileReader != null)
        fileReader.close(); 
      client.logout();
      client.disconnect();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\examples\mail\SMTPMail.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */