package org.apache.commons.net.examples.nntp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.io.Util;
import org.apache.commons.net.nntp.NNTPClient;
import org.apache.commons.net.nntp.NNTPReply;
import org.apache.commons.net.nntp.SimpleNNTPHeader;

public final class PostMessage {
  public static void main(String[] args) {
    FileReader fileReader = null;
    if (args.length < 1) {
      System.err.println("Usage: post newsserver");
      System.exit(1);
    } 
    String server = args[0];
    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
    try {
      System.out.print("From: ");
      System.out.flush();
      String from = stdin.readLine();
      System.out.print("Subject: ");
      System.out.flush();
      String subject = stdin.readLine();
      SimpleNNTPHeader header = new SimpleNNTPHeader(from, subject);
      System.out.print("Newsgroup: ");
      System.out.flush();
      String newsgroup = stdin.readLine();
      header.addNewsgroup(newsgroup);
      while (true) {
        System.out.print("Additional Newsgroup <Hit enter to end>: ");
        System.out.flush();
        newsgroup = stdin.readLine();
        if (newsgroup == null)
          break; 
        newsgroup = newsgroup.trim();
        if (newsgroup.length() == 0)
          break; 
        header.addNewsgroup(newsgroup);
      } 
      System.out.print("Organization: ");
      System.out.flush();
      String organization = stdin.readLine();
      System.out.print("References: ");
      System.out.flush();
      String references = stdin.readLine();
      if (organization != null && organization.length() > 0)
        header.addHeaderField("Organization", organization); 
      if (references != null && references.length() > 0)
        header.addHeaderField("References", references); 
      header.addHeaderField("X-Newsreader", "NetComponents");
      System.out.print("Filename: ");
      System.out.flush();
      String filename = stdin.readLine();
      try {
        fileReader = new FileReader(filename);
      } catch (FileNotFoundException e) {
        System.err.println("File not found. " + e.getMessage());
        System.exit(1);
      } 
      NNTPClient client = new NNTPClient();
      client.addProtocolCommandListener((ProtocolCommandListener)new PrintCommandListener(
            new PrintWriter(System.out), true));
      client.connect(server);
      if (!NNTPReply.isPositiveCompletion(client.getReplyCode())) {
        client.disconnect();
        System.err.println("NNTP server refused connection.");
        System.exit(1);
      } 
      if (client.isAllowedToPost()) {
        Writer writer = client.postArticle();
        if (writer != null) {
          writer.write(header.toString());
          Util.copyReader(fileReader, writer);
          writer.close();
          client.completePendingCommand();
        } 
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


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\examples\nntp\PostMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */