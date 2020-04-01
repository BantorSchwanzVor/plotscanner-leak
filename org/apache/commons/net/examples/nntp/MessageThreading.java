package org.apache.commons.net.examples.nntp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketException;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.nntp.Article;
import org.apache.commons.net.nntp.NNTPClient;
import org.apache.commons.net.nntp.NewsgroupInfo;
import org.apache.commons.net.nntp.Threader;

public class MessageThreading {
  public static void main(String[] args) throws SocketException, IOException {
    if (args.length != 2 && args.length != 4) {
      System.out.println("Usage: MessageThreading <hostname> <groupname> [<user> <password>]");
      return;
    } 
    String hostname = args[0];
    String newsgroup = args[1];
    NNTPClient client = new NNTPClient();
    client.addProtocolCommandListener((ProtocolCommandListener)new PrintCommandListener(new PrintWriter(System.out), true));
    client.connect(hostname);
    if (args.length == 4) {
      String user = args[2];
      String password = args[3];
      if (!client.authenticate(user, password)) {
        System.out.println("Authentication failed for user " + user + "!");
        System.exit(1);
      } 
    } 
    String[] fmt = client.listOverviewFmt();
    if (fmt != null) {
      System.out.println("LIST OVERVIEW.FMT:");
      byte b;
      int i;
      String[] arrayOfString;
      for (i = (arrayOfString = fmt).length, b = 0; b < i; ) {
        String s = arrayOfString[b];
        System.out.println(s);
        b++;
      } 
    } else {
      System.out.println("Failed to get OVERVIEW.FMT");
    } 
    NewsgroupInfo group = new NewsgroupInfo();
    client.selectNewsgroup(newsgroup, group);
    long lowArticleNumber = group.getFirstArticleLong();
    long highArticleNumber = lowArticleNumber + 5000L;
    System.out.println("Retrieving articles between [" + lowArticleNumber + "] and [" + highArticleNumber + "]");
    Iterable<Article> articles = client.iterateArticleInfo(lowArticleNumber, highArticleNumber);
    System.out.println("Building message thread tree...");
    Threader threader = new Threader();
    Article root = (Article)threader.thread(articles);
    Article.printThread(root, 0);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\examples\nntp\MessageThreading.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */