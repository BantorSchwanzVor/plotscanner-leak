package org.apache.commons.net.examples.nntp;

import java.io.IOException;
import org.apache.commons.net.nntp.NNTPClient;
import org.apache.commons.net.nntp.NewsgroupInfo;

public final class ListNewsgroups {
  public static void main(String[] args) {
    if (args.length < 1) {
      System.err.println("Usage: newsgroups newsserver [pattern]");
      return;
    } 
    NNTPClient client = new NNTPClient();
    String pattern = (args.length >= 2) ? args[1] : "";
    try {
      client.connect(args[0]);
      int j = 0;
      try {
        for (String s : client.iterateNewsgroupListing(pattern)) {
          j++;
          System.out.println(s);
        } 
      } catch (IOException e1) {
        e1.printStackTrace();
      } 
      System.out.println(j);
      j = 0;
      for (NewsgroupInfo n : client.iterateNewsgroups(pattern)) {
        j++;
        System.out.println(n.getNewsgroup());
      } 
      System.out.println(j);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (client.isConnected())
          client.disconnect(); 
      } catch (IOException e) {
        System.err.println("Error disconnecting from server.");
        e.printStackTrace();
        System.exit(1);
      } 
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\examples\nntp\ListNewsgroups.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */