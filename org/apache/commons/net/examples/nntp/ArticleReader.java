package org.apache.commons.net.examples.nntp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketException;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.nntp.NNTPClient;
import org.apache.commons.net.nntp.NewsgroupInfo;

public class ArticleReader {
  public static void main(String[] args) throws SocketException, IOException {
    BufferedReader brHdr, brBody;
    if (args.length != 2 && args.length != 3 && args.length != 5) {
      System.out.println("Usage: MessageThreading <hostname> <groupname> [<article specifier> [<user> <password>]]");
      return;
    } 
    String hostname = args[0];
    String newsgroup = args[1];
    String articleSpec = (args.length >= 3) ? args[2] : null;
    NNTPClient client = new NNTPClient();
    client.addProtocolCommandListener((ProtocolCommandListener)new PrintCommandListener(new PrintWriter(System.out), true));
    client.connect(hostname);
    if (args.length == 5) {
      String user = args[3];
      String password = args[4];
      if (!client.authenticate(user, password)) {
        System.out.println("Authentication failed for user " + user + "!");
        System.exit(1);
      } 
    } 
    NewsgroupInfo group = new NewsgroupInfo();
    client.selectNewsgroup(newsgroup, group);
    if (articleSpec != null) {
      brHdr = (BufferedReader)client.retrieveArticleHeader(articleSpec);
    } else {
      long articleNum = group.getLastArticleLong();
      brHdr = client.retrieveArticleHeader(articleNum);
    } 
    if (brHdr != null) {
      String line;
      while ((line = brHdr.readLine()) != null)
        System.out.println(line); 
      brHdr.close();
    } 
    if (articleSpec != null) {
      brBody = (BufferedReader)client.retrieveArticleBody(articleSpec);
    } else {
      long articleNum = group.getLastArticleLong();
      brBody = client.retrieveArticleBody(articleNum);
    } 
    if (brBody != null) {
      String line;
      while ((line = brBody.readLine()) != null)
        System.out.println(line); 
      brBody.close();
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\examples\nntp\ArticleReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */