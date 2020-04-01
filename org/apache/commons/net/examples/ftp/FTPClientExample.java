package org.apache.commons.net.examples.ftp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPHTTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;
import org.apache.commons.net.util.TrustManagerUtils;

public final class FTPClientExample {
  public static final String USAGE = "Expected Parameters: [options] <hostname> <username> <password> [<remote file> [<local file>]]\n\nDefault behavior is to download a file and use ASCII transfer mode.\n\t-a - use local active mode (default is local passive)\n\t-A - anonymous login (omit username and password parameters)\n\t-b - use binary transfer mode\n\t-c cmd - issue arbitrary command (remote is used as a parameter if provided) \n\t-d - list directory details using MLSD (remote is used as the pathname if provided)\n\t-e - use EPSV with IPv4 (default false)\n\t-E - encoding to use for control channel\n\t-f - issue FEAT command (remote and local files are ignored)\n\t-h - list hidden files (applies to -l and -n only)\n\t-i - issue SIZE command for a file\n\t-k secs - use keep-alive timer (setControlKeepAliveTimeout)\n\t-l - list files using LIST (remote is used as the pathname if provided)\n\t     Files are listed twice: first in raw mode, then as the formatted parsed data.\n\t     N.B. if the wrong server-type is used, output may be lost. Use -U or -S as necessary.\n\t-L - use lenient future dates (server dates may be up to 1 day into future)\n\t-m - list file details using MDTM (remote is used as the pathname if provided)\n\t-n - list file names using NLST (remote is used as the pathname if provided)\n\t-p true|false|protocol[,true|false] - use FTPSClient with the specified protocol and/or isImplicit setting\n\t-s - store file on server (upload)\n\t-S - systemType set server system type (e.g. UNIX VMS WINDOWS)\n\t-t - list file details using MLST (remote is used as the pathname if provided)\n\t-U - save unparseable responses\n\t-w msec - wait time for keep-alive reply (setControlKeepAliveReplyTimeout)\n\t-T  all|valid|none - use one of the built-in TrustManager implementations (none = JVM default)\n\t-y format - set default date format string\n\t-Y format - set recent date format string\n\t-Z timezone - set the server timezone for parsing LIST responses\n\t-z timezone - set the timezone for displaying MDTM, LIST, MLSD, MLST responses\n\t-PrH server[:port] - HTTP Proxy host and optional port[80] \n\t-PrU user - HTTP Proxy server username\n\t-PrP password - HTTP Proxy server password\n\t-# - add hash display during transfers\n";
  
  public static void main(String[] args) throws UnknownHostException {
    FTPSClient fTPSClient;
    FTPClientConfig config;
    boolean storeFile = false, binaryTransfer = false, error = false, listFiles = false, listNames = false, hidden = false;
    boolean localActive = false, useEpsvWithIPv4 = false, feat = false, printHash = false;
    boolean mlst = false, mlsd = false, mdtm = false, saveUnparseable = false;
    boolean size = false;
    boolean lenient = false;
    long keepAliveTimeout = -1L;
    int controlKeepAliveReplyTimeout = -1;
    int minParams = 5;
    String protocol = null;
    String doCommand = null;
    String trustmgr = null;
    String proxyHost = null;
    int proxyPort = 80;
    String proxyUser = null;
    String proxyPassword = null;
    String username = null;
    String password = null;
    String encoding = null;
    String serverTimeZoneId = null;
    String displayTimeZoneId = null;
    String serverType = null;
    String defaultDateFormat = null;
    String recentDateFormat = null;
    int base = 0;
    for (base = 0; base < args.length; base++) {
      if (args[base].equals("-s")) {
        storeFile = true;
      } else if (args[base].equals("-a")) {
        localActive = true;
      } else if (args[base].equals("-A")) {
        username = "anonymous";
        password = String.valueOf(System.getProperty("user.name")) + "@" + InetAddress.getLocalHost().getHostName();
      } else if (args[base].equals("-b")) {
        binaryTransfer = true;
      } else if (args[base].equals("-c")) {
        doCommand = args[++base];
        minParams = 3;
      } else if (args[base].equals("-d")) {
        mlsd = true;
        minParams = 3;
      } else if (args[base].equals("-e")) {
        useEpsvWithIPv4 = true;
      } else if (args[base].equals("-E")) {
        encoding = args[++base];
      } else if (args[base].equals("-f")) {
        feat = true;
        minParams = 3;
      } else if (args[base].equals("-h")) {
        hidden = true;
      } else if (args[base].equals("-i")) {
        size = true;
        minParams = 3;
      } else if (args[base].equals("-k")) {
        keepAliveTimeout = Long.parseLong(args[++base]);
      } else if (args[base].equals("-l")) {
        listFiles = true;
        minParams = 3;
      } else if (args[base].equals("-m")) {
        mdtm = true;
        minParams = 3;
      } else if (args[base].equals("-L")) {
        lenient = true;
      } else if (args[base].equals("-n")) {
        listNames = true;
        minParams = 3;
      } else if (args[base].equals("-p")) {
        protocol = args[++base];
      } else if (args[base].equals("-S")) {
        serverType = args[++base];
      } else if (args[base].equals("-t")) {
        mlst = true;
        minParams = 3;
      } else if (args[base].equals("-U")) {
        saveUnparseable = true;
      } else if (args[base].equals("-w")) {
        controlKeepAliveReplyTimeout = Integer.parseInt(args[++base]);
      } else if (args[base].equals("-T")) {
        trustmgr = args[++base];
      } else if (args[base].equals("-y")) {
        defaultDateFormat = args[++base];
      } else if (args[base].equals("-Y")) {
        recentDateFormat = args[++base];
      } else if (args[base].equals("-Z")) {
        serverTimeZoneId = args[++base];
      } else if (args[base].equals("-z")) {
        displayTimeZoneId = args[++base];
      } else if (args[base].equals("-PrH")) {
        proxyHost = args[++base];
        String[] arrayOfString = proxyHost.split(":");
        if (arrayOfString.length == 2) {
          proxyHost = arrayOfString[0];
          proxyPort = Integer.parseInt(arrayOfString[1]);
        } 
      } else if (args[base].equals("-PrU")) {
        proxyUser = args[++base];
      } else if (args[base].equals("-PrP")) {
        proxyPassword = args[++base];
      } else if (args[base].equals("-#")) {
        printHash = true;
      } else {
        break;
      } 
    } 
    int remain = args.length - base;
    if (username != null)
      minParams -= 2; 
    if (remain < minParams) {
      if (args.length > 0)
        System.err.println("Actual Parameters: " + Arrays.toString((Object[])args)); 
      System.err.println("Expected Parameters: [options] <hostname> <username> <password> [<remote file> [<local file>]]\n\nDefault behavior is to download a file and use ASCII transfer mode.\n\t-a - use local active mode (default is local passive)\n\t-A - anonymous login (omit username and password parameters)\n\t-b - use binary transfer mode\n\t-c cmd - issue arbitrary command (remote is used as a parameter if provided) \n\t-d - list directory details using MLSD (remote is used as the pathname if provided)\n\t-e - use EPSV with IPv4 (default false)\n\t-E - encoding to use for control channel\n\t-f - issue FEAT command (remote and local files are ignored)\n\t-h - list hidden files (applies to -l and -n only)\n\t-i - issue SIZE command for a file\n\t-k secs - use keep-alive timer (setControlKeepAliveTimeout)\n\t-l - list files using LIST (remote is used as the pathname if provided)\n\t     Files are listed twice: first in raw mode, then as the formatted parsed data.\n\t     N.B. if the wrong server-type is used, output may be lost. Use -U or -S as necessary.\n\t-L - use lenient future dates (server dates may be up to 1 day into future)\n\t-m - list file details using MDTM (remote is used as the pathname if provided)\n\t-n - list file names using NLST (remote is used as the pathname if provided)\n\t-p true|false|protocol[,true|false] - use FTPSClient with the specified protocol and/or isImplicit setting\n\t-s - store file on server (upload)\n\t-S - systemType set server system type (e.g. UNIX VMS WINDOWS)\n\t-t - list file details using MLST (remote is used as the pathname if provided)\n\t-U - save unparseable responses\n\t-w msec - wait time for keep-alive reply (setControlKeepAliveReplyTimeout)\n\t-T  all|valid|none - use one of the built-in TrustManager implementations (none = JVM default)\n\t-y format - set default date format string\n\t-Y format - set recent date format string\n\t-Z timezone - set the server timezone for parsing LIST responses\n\t-z timezone - set the timezone for displaying MDTM, LIST, MLSD, MLST responses\n\t-PrH server[:port] - HTTP Proxy host and optional port[80] \n\t-PrU user - HTTP Proxy server username\n\t-PrP password - HTTP Proxy server password\n\t-# - add hash display during transfers\n");
      System.exit(1);
    } 
    String server = args[base++];
    int port = 0;
    String[] parts = server.split(":");
    if (parts.length == 2) {
      server = parts[0];
      port = Integer.parseInt(parts[1]);
    } 
    if (username == null) {
      username = args[base++];
      password = args[base++];
    } 
    String remote = null;
    if (args.length - base > 0)
      remote = args[base++]; 
    String local = null;
    if (args.length - base > 0)
      local = args[base++]; 
    if (protocol == null) {
      if (proxyHost != null) {
        System.out.println("Using HTTP proxy server: " + proxyHost);
        FTPHTTPClient fTPHTTPClient = new FTPHTTPClient(proxyHost, proxyPort, proxyUser, proxyPassword);
      } else {
        FTPClient ftp = new FTPClient();
      } 
    } else {
      FTPSClient ftps;
      if (protocol.equals("true")) {
        ftps = new FTPSClient(true);
      } else if (protocol.equals("false")) {
        ftps = new FTPSClient(false);
      } else {
        String[] prot = protocol.split(",");
        if (prot.length == 1) {
          ftps = new FTPSClient(protocol);
        } else {
          ftps = new FTPSClient(prot[0], Boolean.parseBoolean(prot[1]));
        } 
      } 
      fTPSClient = ftps;
      if ("all".equals(trustmgr)) {
        ftps.setTrustManager(TrustManagerUtils.getAcceptAllTrustManager());
      } else if ("valid".equals(trustmgr)) {
        ftps.setTrustManager(TrustManagerUtils.getValidateServerCertificateTrustManager());
      } else if ("none".equals(trustmgr)) {
        ftps.setTrustManager(null);
      } 
    } 
    if (printHash)
      fTPSClient.setCopyStreamListener(createListener()); 
    if (keepAliveTimeout >= 0L)
      fTPSClient.setControlKeepAliveTimeout(keepAliveTimeout); 
    if (controlKeepAliveReplyTimeout >= 0)
      fTPSClient.setControlKeepAliveReplyTimeout(controlKeepAliveReplyTimeout); 
    if (encoding != null)
      fTPSClient.setControlEncoding(encoding); 
    fTPSClient.setListHiddenFiles(hidden);
    fTPSClient.addProtocolCommandListener((ProtocolCommandListener)new PrintCommandListener(new PrintWriter(System.out), true));
    if (serverType != null) {
      config = new FTPClientConfig(serverType);
    } else {
      config = new FTPClientConfig();
    } 
    config.setUnparseableEntries(saveUnparseable);
    if (defaultDateFormat != null)
      config.setDefaultDateFormatStr(defaultDateFormat); 
    if (recentDateFormat != null)
      config.setRecentDateFormatStr(recentDateFormat); 
    fTPSClient.configure(config);
    try {
      if (port > 0) {
        fTPSClient.connect(server, port);
      } else {
        fTPSClient.connect(server);
      } 
      System.out.println("Connected to " + server + " on " + ((port > 0) ? port : fTPSClient.getDefaultPort()));
      int reply = fTPSClient.getReplyCode();
      if (!FTPReply.isPositiveCompletion(reply)) {
        fTPSClient.disconnect();
        System.err.println("FTP server refused connection.");
        System.exit(1);
      } 
    } catch (IOException e) {
      if (fTPSClient.isConnected())
        try {
          fTPSClient.disconnect();
        } catch (IOException iOException) {} 
      System.err.println("Could not connect to server.");
      e.printStackTrace();
      System.exit(1);
    } 
    try {
    
    } catch (FTPConnectionClosedException e) {
      error = true;
      System.err.println("Server closed connection.");
      e.printStackTrace();
    } catch (IOException e) {
      error = true;
      e.printStackTrace();
    } finally {
      if (fTPSClient.isConnected())
        try {
          fTPSClient.disconnect();
        } catch (IOException iOException) {} 
    } 
    System.exit(error ? 1 : 0);
  }
  
  private static void showCslStats(FTPClient ftp) {
    int[] stats = ftp.getCslDebug();
    System.out.println("CslDebug=" + Arrays.toString(stats));
  }
  
  private static CopyStreamListener createListener() {
    return new CopyStreamListener() {
        private long megsTotal = 0L;
        
        public void bytesTransferred(CopyStreamEvent event) {
          bytesTransferred(event.getTotalBytesTransferred(), event.getBytesTransferred(), event.getStreamSize());
        }
        
        public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {
          long megs = totalBytesTransferred / 1000000L;
          for (long l = this.megsTotal; l < megs; l++)
            System.err.print("#"); 
          this.megsTotal = megs;
        }
      };
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\examples\ftp\FTPClientExample.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */