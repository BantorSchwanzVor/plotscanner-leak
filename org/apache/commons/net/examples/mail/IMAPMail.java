package org.apache.commons.net.examples.mail;

import java.io.IOException;
import java.net.URI;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.imap.IMAPClient;

public final class IMAPMail {
  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      System.err.println(
          "Usage: IMAPMail imap[s]://username:password@server/");
      System.err.println("Connects to server; lists capabilities and shows Inbox status");
      System.exit(1);
    } 
    URI uri = URI.create(args[0]);
    IMAPClient imap = IMAPUtils.imapLogin(uri, 10000, null);
    imap.addProtocolCommandListener((ProtocolCommandListener)new PrintCommandListener(System.out, true));
    try {
      imap.setSoTimeout(6000);
      imap.capability();
      imap.select("inbox");
      imap.examine("inbox");
      imap.status("inbox", new String[] { "MESSAGES" });
    } catch (IOException e) {
      System.out.println(imap.getReplyString());
      e.printStackTrace();
      System.exit(10);
      return;
    } finally {
      imap.logout();
      imap.disconnect();
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\examples\mail\IMAPMail.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */