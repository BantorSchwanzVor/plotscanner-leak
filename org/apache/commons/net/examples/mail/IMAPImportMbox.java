package org.apache.commons.net.examples.mail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.net.imap.IMAPClient;

public final class IMAPImportMbox {
  private static final String CRLF = "\r\n";
  
  private static final Pattern PATFROM = Pattern.compile(">+From ");
  
  public static void main(String[] args) throws IOException {
    if (args.length < 2) {
      System.err.println("Usage: IMAPImportMbox imap[s]://user:password@host[:port]/folder/path <mboxfile> [selectors]");
      System.err.println("\tWhere: a selector is a list of numbers/number ranges - 1,2,3-10 - or a list of strings to match in the initial From line");
      System.exit(1);
    } 
    URI uri = URI.create(args[0]);
    String file = args[1];
    File mbox = new File(file);
    if (!mbox.isFile() || !mbox.canRead())
      throw new IOException("Cannot read mailbox file: " + mbox); 
    String path = uri.getPath();
    if (path == null || path.length() < 1)
      throw new IllegalArgumentException("Invalid folderPath: '" + path + "'"); 
    String folder = path.substring(1);
    List<String> contains = new ArrayList<>();
    BitSet msgNums = new BitSet();
    for (int i = 2; i < args.length; i++) {
      String arg = args[i];
      if (arg.matches("\\d+(-\\d+)?(,\\d+(-\\d+)?)*")) {
        byte b;
        int j;
        String[] arrayOfString;
        for (j = (arrayOfString = arg.split(",")).length, b = 0; b < j; ) {
          String entry = arrayOfString[b];
          String[] parts = entry.split("-");
          if (parts.length == 2) {
            int low = Integer.parseInt(parts[0]);
            int high = Integer.parseInt(parts[1]);
            for (int k = low; k <= high; k++)
              msgNums.set(k); 
          } else {
            msgNums.set(Integer.parseInt(entry));
          } 
          b++;
        } 
      } else {
        contains.add(arg);
      } 
    } 
    IMAPClient imap = IMAPUtils.imapLogin(uri, 10000, null);
    int total = 0;
    int loaded = 0;
    try {
      imap.setSoTimeout(6000);
      BufferedReader br = new BufferedReader(new FileReader(file));
      StringBuilder sb = new StringBuilder();
      boolean wanted = false;
      String line;
      while ((line = br.readLine()) != null) {
        if (line.startsWith("From ")) {
          if (process(sb, imap, folder, total))
            loaded++; 
          sb.setLength(0);
          total++;
          wanted = wanted(total, line, msgNums, contains);
        } else if (startsWith(line, PATFROM)) {
          line = line.substring(1);
        } 
        if (wanted) {
          sb.append(line);
          sb.append("\r\n");
        } 
      } 
      br.close();
      if (wanted && process(sb, imap, folder, total))
        loaded++; 
    } catch (IOException e) {
      System.out.println(imap.getReplyString());
      e.printStackTrace();
      System.exit(10);
      return;
    } finally {
      imap.logout();
      imap.disconnect();
    } 
    System.out.println("Processed " + total + " messages, loaded " + loaded);
  }
  
  private static boolean startsWith(String input, Pattern pat) {
    Matcher m = pat.matcher(input);
    return m.lookingAt();
  }
  
  private static boolean process(StringBuilder sb, IMAPClient imap, String folder, int msgNum) throws IOException {
    int length = sb.length();
    boolean haveMessage = (length > 2);
    if (haveMessage) {
      System.out.println("MsgNum: " + msgNum + " Length " + length);
      sb.setLength(length - 2);
      String msg = sb.toString();
      if (!imap.append(folder, null, null, msg))
        throw new IOException("Failed to import message: " + msgNum + " " + imap.getReplyString()); 
    } 
    return haveMessage;
  }
  
  private static boolean wanted(int msgNum, String line, BitSet msgNums, List<String> contains) {
    return !((!msgNums.isEmpty() || !contains.isEmpty()) && 
      !msgNums.get(msgNum) && 
      !listContains(contains, line));
  }
  
  private static boolean listContains(List<String> contains, String string) {
    for (String entry : contains) {
      if (string.contains(entry))
        return true; 
    } 
    return false;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\examples\mail\IMAPImportMbox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */