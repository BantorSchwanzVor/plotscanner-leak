package org.apache.commons.net.examples.mail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.net.pop3.POP3Client;
import org.apache.commons.net.pop3.POP3MessageInfo;
import org.apache.commons.net.pop3.POP3SClient;

public final class POP3ExportMbox {
  private static final Pattern PATFROM = Pattern.compile(">*From ");
  
  public static void main(String[] args) {
    POP3Client pop3;
    int port;
    String file = null;
    int argIdx;
    for (argIdx = 0; argIdx < args.length && 
      args[argIdx].equals("-F"); argIdx++)
      file = args[++argIdx]; 
    int argCount = args.length - argIdx;
    if (argCount < 3) {
      System.err.println(
          "Usage: POP3Mail [-F file/directory] <server[:port]> <username> <password|-|*|VARNAME> [TLS [true=implicit]]");
      System.exit(1);
    } 
    String[] arg0 = args[argIdx++].split(":");
    String server = arg0[0];
    String username = args[argIdx++];
    String password = args[argIdx++];
    try {
      password = Utils.getPassword(username, password);
    } catch (IOException e1) {
      System.err.println("Could not retrieve password: " + e1.getMessage());
      return;
    } 
    String proto = (argCount > 3) ? args[argIdx++] : null;
    boolean implicit = (argCount > 4) ? Boolean.parseBoolean(args[argIdx++]) : false;
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
      int count = status.number;
      if (file != null) {
        System.out.println("Getting messages: " + count);
        File mbox = new File(file);
        if (mbox.isDirectory()) {
          System.out.println("Writing dir: " + mbox);
          for (int i = 1; i <= count; i++) {
            OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(new File(mbox, String.valueOf(i) + ".eml")), Charset.forName("iso-8859-1"));
            writeFile(pop3, fw, i);
            fw.close();
          } 
        } else {
          System.out.println("Writing file: " + mbox);
          OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(mbox), Charset.forName("iso-8859-1"));
          for (int i = 1; i <= count; i++)
            writeMbox(pop3, fw, i); 
          fw.close();
        } 
      } 
      pop3.logout();
      pop3.disconnect();
    } catch (IOException e) {
      e.printStackTrace();
      return;
    } 
  }
  
  private static void writeFile(POP3Client pop3, OutputStreamWriter fw, int i) throws IOException {
    BufferedReader r = (BufferedReader)pop3.retrieveMessage(i);
    String line;
    while ((line = r.readLine()) != null) {
      fw.write(line);
      fw.write("\n");
    } 
    r.close();
  }
  
  private static void writeMbox(POP3Client pop3, OutputStreamWriter fw, int i) throws IOException {
    SimpleDateFormat DATE_FORMAT = 
      new SimpleDateFormat("EEE MMM dd HH:mm:ss YYYY");
    String replyTo = "MAILER-DAEMON";
    Date received = new Date();
    BufferedReader r = (BufferedReader)pop3.retrieveMessage(i);
    fw.append("From ");
    fw.append(replyTo);
    fw.append(' ');
    fw.append(DATE_FORMAT.format(received));
    fw.append("\n");
    String line;
    while ((line = r.readLine()) != null) {
      if (startsWith(line, PATFROM))
        fw.write(">"); 
      fw.write(line);
      fw.write("\n");
    } 
    fw.write("\n");
    r.close();
  }
  
  private static boolean startsWith(String input, Pattern pat) {
    Matcher m = pat.matcher(input);
    return m.lookingAt();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\examples\mail\POP3ExportMbox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */