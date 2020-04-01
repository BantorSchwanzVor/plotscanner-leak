package org.apache.commons.net.examples.mail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.imap.IMAP;
import org.apache.commons.net.imap.IMAPClient;

public final class IMAPExportMbox {
  private static final String CRLF = "\r\n";
  
  private static final String LF = "\n";
  
  private static final String EOL_DEFAULT = System.getProperty("line.separator");
  
  private static final Pattern PATFROM = Pattern.compile(">*From ");
  
  private static final Pattern PATID = Pattern.compile(".*INTERNALDATE \"(\\d\\d-\\w{3}-\\d{4} \\d\\d:\\d\\d:\\d\\d [+-]\\d+)\"");
  
  private static final int PATID_DATE_GROUP = 1;
  
  private static final Pattern PATSEQ = Pattern.compile("\\* (\\d+) ");
  
  private static final int PATSEQ_SEQUENCE_GROUP = 1;
  
  private static final Pattern PATEXISTS = Pattern.compile("\\* (\\d+) EXISTS");
  
  private static final Pattern PATTEMPFAIL = Pattern.compile("[A-Z]{4} NO \\[TEMPFAIL\\] FETCH .*");
  
  private static final int CONNECT_TIMEOUT = 10;
  
  private static final int READ_TIMEOUT = 10;
  
  public static void main(String[] args) throws IOException, URISyntaxException {
    URI uri;
    String itemNames;
    MboxListener chunkListener;
    int connect_timeout = 10;
    int read_timeout = 10;
    int argIdx = 0;
    String eol = EOL_DEFAULT;
    boolean printHash = false;
    boolean printMarker = false;
    int retryWaitSecs = 0;
    for (argIdx = 0; argIdx < args.length; argIdx++) {
      if (args[argIdx].equals("-c")) {
        connect_timeout = Integer.parseInt(args[++argIdx]);
      } else if (args[argIdx].equals("-r")) {
        read_timeout = Integer.parseInt(args[++argIdx]);
      } else if (args[argIdx].equals("-R")) {
        retryWaitSecs = Integer.parseInt(args[++argIdx]);
      } else if (args[argIdx].equals("-LF")) {
        eol = "\n";
      } else if (args[argIdx].equals("-CRLF")) {
        eol = "\r\n";
      } else if (args[argIdx].equals("-.")) {
        printHash = true;
      } else if (args[argIdx].equals("-X")) {
        printMarker = true;
      } else {
        break;
      } 
    } 
    int argCount = args.length - argIdx;
    if (argCount < 2) {
      System.err.println("Usage: IMAPExportMbox [-LF|-CRLF] [-c n] [-r n] [-R n] [-.] [-X] imap[s]://user:password@host[:port]/folder/path [+|-]<mboxfile> [sequence-set] [itemnames]");
      System.err.println("\t-LF | -CRLF set end-of-line to LF or CRLF (default is the line.separator system property)");
      System.err.println("\t-c connect timeout in seconds (default 10)");
      System.err.println("\t-r read timeout in seconds (default 10)");
      System.err.println("\t-R temporary failure retry wait in seconds (default 0; i.e. disabled)");
      System.err.println("\t-. print a . for each complete message received");
      System.err.println("\t-X print the X-IMAP line for each complete message received");
      System.err.println("\tthe mboxfile is where the messages are stored; use '-' to write to standard output.");
      System.err.println("\tPrefix filename with '+' to append to the file. Prefix with '-' to allow overwrite.");
      System.err.println("\ta sequence-set is a list of numbers/number ranges e.g. 1,2,3-10,20:* - default 1:*");
      System.err.println("\titemnames are the message data item name(s) e.g. BODY.PEEK[HEADER.FIELDS (SUBJECT)] or a macro e.g. ALL - default (INTERNALDATE BODY.PEEK[])");
      System.exit(1);
    } 
    String uriString = args[argIdx++];
    try {
      uri = URI.create(uriString);
    } catch (IllegalArgumentException e) {
      Matcher m = Pattern.compile("(imaps?://[^/]+)(/.*)").matcher(uriString);
      if (m.matches()) {
        uri = URI.create(m.group(1));
        uri = new URI(uri.getScheme(), uri.getAuthority(), m.group(2), null, null);
      } else {
        throw e;
      } 
    } 
    String file = args[argIdx++];
    String sequenceSet = (argCount > 2) ? args[argIdx++] : "1:*";
    if (argCount > 3) {
      if (argCount > 4) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 4; i <= argCount; i++) {
          if (i > 4)
            sb.append(" "); 
          sb.append(args[argIdx++]);
        } 
        sb.append(")");
        itemNames = sb.toString();
      } else {
        itemNames = args[argIdx++];
      } 
    } else {
      itemNames = "(INTERNALDATE BODY.PEEK[])";
    } 
    boolean checkSequence = sequenceSet.matches("\\d+:(\\d+|\\*)");
    if (file.equals("-")) {
      chunkListener = null;
    } else if (file.startsWith("+")) {
      File mbox = new File(file.substring(1));
      System.out.println("Appending to file " + mbox);
      chunkListener = new MboxListener(
          new BufferedWriter(new FileWriter(mbox, true)), eol, printHash, printMarker, checkSequence);
    } else if (file.startsWith("-")) {
      File mbox = new File(file.substring(1));
      System.out.println("Writing to file " + mbox);
      chunkListener = new MboxListener(
          new BufferedWriter(new FileWriter(mbox, false)), eol, printHash, printMarker, checkSequence);
    } else {
      File mbox = new File(file);
      if (mbox.exists() && mbox.length() > 0L)
        throw new IOException("mailbox file: " + mbox + " already exists and is non-empty!"); 
      System.out.println("Creating file " + mbox);
      chunkListener = new MboxListener(new BufferedWriter(new FileWriter(mbox)), eol, printHash, printMarker, checkSequence);
    } 
    String path = uri.getPath();
    if (path == null || path.length() < 1)
      throw new IllegalArgumentException("Invalid folderPath: '" + path + "'"); 
    String folder = path.substring(1);
    PrintCommandListener listener = new PrintCommandListener(System.out, true) {
        public void protocolReplyReceived(ProtocolCommandEvent event) {
          if (event.getReplyCode() != 3)
            super.protocolReplyReceived(event); 
        }
      };
    IMAPClient imap = IMAPUtils.imapLogin(uri, connect_timeout * 1000, (ProtocolCommandListener)listener);
    String maxIndexInFolder = null;
    try {
      imap.setSoTimeout(read_timeout * 1000);
      if (!imap.select(folder))
        throw new IOException("Could not select folder: " + folder); 
      byte b;
      int i;
      String[] arrayOfString;
      for (i = (arrayOfString = imap.getReplyStrings()).length, b = 0; b < i; ) {
        String line = arrayOfString[b];
        maxIndexInFolder = matches(line, PATEXISTS, 1);
        if (maxIndexInFolder != null)
          break; 
        b++;
      } 
      if (chunkListener != null)
        imap.setChunkListener(chunkListener); 
      while (true) {
        boolean ok = imap.fetch(sequenceSet, itemNames);
        if (!ok && retryWaitSecs > 0 && chunkListener != null && checkSequence) {
          String replyString = imap.getReplyString();
          if (startsWith(replyString, PATTEMPFAIL)) {
            System.err.println("Temporary error detected, will retry in " + retryWaitSecs + "seconds");
            sequenceSet = String.valueOf(chunkListener.lastSeq + 1L) + ":*";
            try {
              Thread.sleep((retryWaitSecs * 1000));
            } catch (InterruptedException interruptedException) {}
            continue;
          } 
          throw new IOException("FETCH " + sequenceSet + " " + itemNames + " failed with " + replyString);
        } 
        break;
      } 
    } catch (IOException ioe) {
      String count = (chunkListener == null) ? "?" : Integer.toString(chunkListener.total);
      System.err.println(
          "FETCH " + sequenceSet + " " + itemNames + " failed after processing " + count + " complete messages ");
      if (chunkListener != null)
        System.err.println("Last complete response seen: " + chunkListener.lastFetched); 
      throw ioe;
    } finally {
      if (printHash)
        System.err.println(); 
      if (chunkListener != null) {
        chunkListener.close();
        Iterator<String> missingIds = chunkListener.missingIds.iterator();
        if (missingIds.hasNext()) {
          StringBuilder sb = new StringBuilder();
          while (true) {
            sb.append(missingIds.next());
            if (!missingIds.hasNext())
              break; 
            sb.append(",");
          } 
          System.err.println("*** Missing ids: " + sb.toString());
        } 
      } 
      imap.logout();
      imap.disconnect();
    } 
    if (chunkListener != null)
      System.out.println("Processed " + chunkListener.total + " messages."); 
    if (maxIndexInFolder != null)
      System.out.println("Folder contained " + maxIndexInFolder + " messages."); 
  }
  
  private static boolean startsWith(String input, Pattern pat) {
    Matcher m = pat.matcher(input);
    return m.lookingAt();
  }
  
  private static String matches(String input, Pattern pat, int index) {
    Matcher m = pat.matcher(input);
    if (m.lookingAt())
      return m.group(index); 
    return null;
  }
  
  private static class MboxListener implements IMAP.IMAPChunkListener {
    private final BufferedWriter bw;
    
    volatile int total = 0;
    
    volatile String lastFetched;
    
    volatile List<String> missingIds = new ArrayList<>();
    
    volatile long lastSeq = -1L;
    
    private final String eol;
    
    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE MMM dd HH:mm:ss YYYY");
    
    private final SimpleDateFormat IDPARSE = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss Z");
    
    private final boolean printHash;
    
    private final boolean printMarker;
    
    private final boolean checkSequence;
    
    MboxListener(BufferedWriter bw, String eol, boolean printHash, boolean printMarker, boolean checkSequence) throws IOException {
      this.eol = eol;
      this.printHash = printHash;
      this.printMarker = printMarker;
      this.DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
      this.bw = bw;
      this.checkSequence = checkSequence;
    }
    
    public boolean chunkReceived(IMAP imap) {
      String[] replyStrings = imap.getReplyStrings();
      Date received = new Date();
      String firstLine = replyStrings[0];
      Matcher m = IMAPExportMbox.PATID.matcher(firstLine);
      if (m.lookingAt()) {
        String date = m.group(1);
        try {
          received = this.IDPARSE.parse(date);
        } catch (ParseException e) {
          System.err.println(e);
        } 
      } else {
        System.err.println("No timestamp found in: " + firstLine + "  - using current time");
      } 
      String replyTo = "MAILER-DAEMON";
      int i;
      for (i = 1; i < replyStrings.length - 1; i++) {
        String line = replyStrings[i];
        if (line.startsWith("Return-Path: ")) {
          String[] parts = line.split(" ", 2);
          replyTo = parts[1];
          if (replyTo.startsWith("<")) {
            replyTo = replyTo.substring(1, replyTo.length() - 1);
            break;
          } 
          System.err.println("Unexpected Return-path:" + line + " in " + firstLine);
          break;
        } 
      } 
      try {
        this.bw.append("From ");
        this.bw.append(replyTo);
        this.bw.append(' ');
        this.bw.append(this.DATE_FORMAT.format(received));
        this.bw.append(this.eol);
        this.bw.append("X-IMAP-Response: ").append(firstLine).append(this.eol);
        if (this.printMarker)
          System.err.println("[" + this.total + "] " + firstLine); 
        for (i = 1; i < replyStrings.length - 1; i++) {
          String line = replyStrings[i];
          if (IMAPExportMbox.startsWith(line, IMAPExportMbox.PATFROM))
            this.bw.append('>'); 
          this.bw.append(line);
          this.bw.append(this.eol);
        } 
        String lastLine = replyStrings[replyStrings.length - 1];
        int lastLength = lastLine.length();
        if (lastLength > 1) {
          this.bw.append(lastLine, 0, lastLength - 1);
          this.bw.append(this.eol);
        } 
        this.bw.append(this.eol);
      } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      } 
      this.lastFetched = firstLine;
      this.total++;
      if (this.checkSequence) {
        m = IMAPExportMbox.PATSEQ.matcher(firstLine);
        if (m.lookingAt()) {
          long msgSeq = Long.parseLong(m.group(1));
          if (this.lastSeq != -1L) {
            long missing = msgSeq - this.lastSeq - 1L;
            if (missing != 0L) {
              for (long j = this.lastSeq + 1L; j < msgSeq; j++)
                this.missingIds.add(String.valueOf(j)); 
              System.err.println(
                  "*** Sequence error: current=" + msgSeq + " previous=" + this.lastSeq + " Missing=" + missing);
            } 
          } 
          this.lastSeq = msgSeq;
        } 
      } 
      if (this.printHash)
        System.err.print("."); 
      return true;
    }
    
    public void close() throws IOException {
      if (this.bw != null)
        this.bw.close(); 
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\examples\mail\IMAPExportMbox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */