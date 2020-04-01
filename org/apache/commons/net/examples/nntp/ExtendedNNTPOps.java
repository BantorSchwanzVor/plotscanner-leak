package org.apache.commons.net.examples.nntp;

import java.io.IOException;
import java.io.PrintWriter;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.nntp.Article;
import org.apache.commons.net.nntp.NNTPClient;
import org.apache.commons.net.nntp.NewsgroupInfo;

public class ExtendedNNTPOps {
  NNTPClient client;
  
  public ExtendedNNTPOps() {
    this.client = new NNTPClient();
    this.client.addProtocolCommandListener((ProtocolCommandListener)new PrintCommandListener(new PrintWriter(System.out), true));
  }
  
  private void demo(String host, String user, String password) {
    try {
      this.client.connect(host);
      if (user != null && password != null) {
        boolean success = this.client.authenticate(user, password);
        if (success) {
          System.out.println("Authentication succeeded");
        } else {
          System.out.println("Authentication failed, error =" + this.client.getReplyString());
        } 
      } 
      NewsgroupInfo testGroup = new NewsgroupInfo();
      this.client.selectNewsgroup("alt.test", testGroup);
      long lowArticleNumber = testGroup.getFirstArticleLong();
      long highArticleNumber = lowArticleNumber + 100L;
      Iterable<Article> articles = this.client.iterateArticleInfo(lowArticleNumber, highArticleNumber);
      for (Article article : articles) {
        if (article.isDummy()) {
          System.out.println("Could not parse: " + article.getSubject());
          continue;
        } 
        System.out.println(article.getSubject());
      } 
      NewsgroupInfo[] fanGroups = this.client.listNewsgroups("alt.fan.*");
      byte b;
      int i;
      NewsgroupInfo[] arrayOfNewsgroupInfo1;
      for (i = (arrayOfNewsgroupInfo1 = fanGroups).length, b = 0; b < i; ) {
        NewsgroupInfo fanGroup = arrayOfNewsgroupInfo1[b];
        System.out.println(fanGroup.getNewsgroup());
        b++;
      } 
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
  
  public static void main(String[] args) {
    int argc = args.length;
    if (argc < 1) {
      System.err.println("usage: ExtendedNNTPOps nntpserver [username password]");
      System.exit(1);
    } 
    ExtendedNNTPOps ops = new ExtendedNNTPOps();
    ops.demo(args[0], (argc >= 3) ? args[1] : null, (argc >= 3) ? args[2] : null);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\examples\nntp\ExtendedNNTPOps.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */