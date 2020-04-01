package org.apache.commons.net.ftp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import org.apache.commons.net.MalformedServerReplyException;
import org.apache.commons.net.ProtocolCommandSupport;
import org.apache.commons.net.SocketClient;
import org.apache.commons.net.io.CRLFLineReader;

public class FTP extends SocketClient {
  public static final int DEFAULT_DATA_PORT = 20;
  
  public static final int DEFAULT_PORT = 21;
  
  public static final int ASCII_FILE_TYPE = 0;
  
  public static final int EBCDIC_FILE_TYPE = 1;
  
  public static final int BINARY_FILE_TYPE = 2;
  
  public static final int LOCAL_FILE_TYPE = 3;
  
  public static final int NON_PRINT_TEXT_FORMAT = 4;
  
  public static final int TELNET_TEXT_FORMAT = 5;
  
  public static final int CARRIAGE_CONTROL_TEXT_FORMAT = 6;
  
  public static final int FILE_STRUCTURE = 7;
  
  public static final int RECORD_STRUCTURE = 8;
  
  public static final int PAGE_STRUCTURE = 9;
  
  public static final int STREAM_TRANSFER_MODE = 10;
  
  public static final int BLOCK_TRANSFER_MODE = 11;
  
  public static final int COMPRESSED_TRANSFER_MODE = 12;
  
  public static final String DEFAULT_CONTROL_ENCODING = "ISO-8859-1";
  
  public static final int REPLY_CODE_LEN = 3;
  
  private static final String __modes = "AEILNTCFRPSBC";
  
  protected int _replyCode;
  
  protected ArrayList<String> _replyLines;
  
  protected boolean _newReplyString;
  
  protected String _replyString;
  
  protected String _controlEncoding;
  
  protected ProtocolCommandSupport _commandSupport_;
  
  protected boolean strictMultilineParsing = false;
  
  private boolean strictReplyParsing = true;
  
  protected BufferedReader _controlInput_;
  
  protected BufferedWriter _controlOutput_;
  
  public FTP() {
    setDefaultPort(21);
    this._replyLines = new ArrayList<>();
    this._newReplyString = false;
    this._replyString = null;
    this._controlEncoding = "ISO-8859-1";
    this._commandSupport_ = new ProtocolCommandSupport(this);
  }
  
  private boolean __strictCheck(String line, String code) {
    return !(line.startsWith(code) && line.charAt(3) == ' ');
  }
  
  private boolean __lenientCheck(String line) {
    return !(line.length() > 3 && line.charAt(3) != '-' && 
      Character.isDigit(line.charAt(0)));
  }
  
  private void __getReply() throws IOException {
    __getReply(true);
  }
  
  protected void __getReplyNoReport() throws IOException {
    __getReply(false);
  }
  
  private void __getReply(boolean reportReply) throws IOException {
    this._newReplyString = true;
    this._replyLines.clear();
    String line = this._controlInput_.readLine();
    if (line == null)
      throw new FTPConnectionClosedException(
          "Connection closed without indication."); 
    int length = line.length();
    if (length < 3)
      throw new MalformedServerReplyException(
          "Truncated server reply: " + line); 
    String code = null;
    try {
      code = line.substring(0, 3);
      this._replyCode = Integer.parseInt(code);
    } catch (NumberFormatException e) {
      throw new MalformedServerReplyException(
          "Could not parse response code.\nServer Reply: " + line);
    } 
    this._replyLines.add(line);
    if (length > 3) {
      char sep = line.charAt(3);
      if (sep == '-') {
        do {
          line = this._controlInput_.readLine();
          if (line == null)
            throw new FTPConnectionClosedException(
                "Connection closed without indication."); 
          this._replyLines.add(line);
        } while (isStrictMultilineParsing() ? !__strictCheck(line, code) : !__lenientCheck(line));
      } else if (isStrictReplyParsing()) {
        if (length == 4)
          throw new MalformedServerReplyException("Truncated server reply: '" + line + "'"); 
        if (sep != ' ')
          throw new MalformedServerReplyException("Invalid server reply: '" + line + "'"); 
      } 
    } else if (isStrictReplyParsing()) {
      throw new MalformedServerReplyException("Truncated server reply: '" + line + "'");
    } 
    if (reportReply)
      fireReplyReceived(this._replyCode, getReplyString()); 
    if (this._replyCode == 421)
      throw new FTPConnectionClosedException("FTP response 421 received.  Server closed connection."); 
  }
  
  protected void _connectAction_() throws IOException {
    _connectAction_((Reader)null);
  }
  
  protected void _connectAction_(Reader socketIsReader) throws IOException {
    super._connectAction_();
    if (socketIsReader == null) {
      this._controlInput_ = 
        (BufferedReader)new CRLFLineReader(new InputStreamReader(this._input_, getControlEncoding()));
    } else {
      this._controlInput_ = (BufferedReader)new CRLFLineReader(socketIsReader);
    } 
    this._controlOutput_ = 
      new BufferedWriter(new OutputStreamWriter(this._output_, getControlEncoding()));
    if (this.connectTimeout > 0) {
      int original = this._socket_.getSoTimeout();
      this._socket_.setSoTimeout(this.connectTimeout);
      try {
        __getReply();
        if (FTPReply.isPositivePreliminary(this._replyCode))
          __getReply(); 
      } catch (SocketTimeoutException e) {
        IOException ioe = new IOException("Timed out waiting for initial connect reply");
        ioe.initCause(e);
        throw ioe;
      } finally {
        this._socket_.setSoTimeout(original);
      } 
    } else {
      __getReply();
      if (FTPReply.isPositivePreliminary(this._replyCode))
        __getReply(); 
    } 
  }
  
  public void setControlEncoding(String encoding) {
    this._controlEncoding = encoding;
  }
  
  public String getControlEncoding() {
    return this._controlEncoding;
  }
  
  public void disconnect() throws IOException {
    super.disconnect();
    this._controlInput_ = null;
    this._controlOutput_ = null;
    this._newReplyString = false;
    this._replyString = null;
  }
  
  public int sendCommand(String command, String args) throws IOException {
    if (this._controlOutput_ == null)
      throw new IOException("Connection is not open"); 
    String message = __buildMessage(command, args);
    __send(message);
    fireCommandSent(command, message);
    __getReply();
    return this._replyCode;
  }
  
  private String __buildMessage(String command, String args) {
    StringBuilder __commandBuffer = new StringBuilder();
    __commandBuffer.append(command);
    if (args != null) {
      __commandBuffer.append(' ');
      __commandBuffer.append(args);
    } 
    __commandBuffer.append("\r\n");
    return __commandBuffer.toString();
  }
  
  private void __send(String message) throws IOException, FTPConnectionClosedException, SocketException {
    try {
      this._controlOutput_.write(message);
      this._controlOutput_.flush();
    } catch (SocketException e) {
      if (!isConnected())
        throw new FTPConnectionClosedException("Connection unexpectedly closed."); 
      throw e;
    } 
  }
  
  protected void __noop() throws IOException {
    String msg = __buildMessage(FTPCmd.NOOP.getCommand(), (String)null);
    __send(msg);
    __getReplyNoReport();
  }
  
  @Deprecated
  public int sendCommand(int command, String args) throws IOException {
    return sendCommand(FTPCommand.getCommand(command), args);
  }
  
  public int sendCommand(FTPCmd command) throws IOException {
    return sendCommand(command, (String)null);
  }
  
  public int sendCommand(FTPCmd command, String args) throws IOException {
    return sendCommand(command.getCommand(), args);
  }
  
  public int sendCommand(String command) throws IOException {
    return sendCommand(command, (String)null);
  }
  
  public int sendCommand(int command) throws IOException {
    return sendCommand(command, (String)null);
  }
  
  public int getReplyCode() {
    return this._replyCode;
  }
  
  public int getReply() throws IOException {
    __getReply();
    return this._replyCode;
  }
  
  public String[] getReplyStrings() {
    return this._replyLines.<String>toArray(new String[this._replyLines.size()]);
  }
  
  public String getReplyString() {
    if (!this._newReplyString)
      return this._replyString; 
    StringBuilder buffer = new StringBuilder(256);
    for (String line : this._replyLines) {
      buffer.append(line);
      buffer.append("\r\n");
    } 
    this._newReplyString = false;
    return this._replyString = buffer.toString();
  }
  
  public int user(String username) throws IOException {
    return sendCommand(FTPCmd.USER, username);
  }
  
  public int pass(String password) throws IOException {
    return sendCommand(FTPCmd.PASS, password);
  }
  
  public int acct(String account) throws IOException {
    return sendCommand(FTPCmd.ACCT, account);
  }
  
  public int abor() throws IOException {
    return sendCommand(FTPCmd.ABOR);
  }
  
  public int cwd(String directory) throws IOException {
    return sendCommand(FTPCmd.CWD, directory);
  }
  
  public int cdup() throws IOException {
    return sendCommand(FTPCmd.CDUP);
  }
  
  public int quit() throws IOException {
    return sendCommand(FTPCmd.QUIT);
  }
  
  public int rein() throws IOException {
    return sendCommand(FTPCmd.REIN);
  }
  
  public int smnt(String dir) throws IOException {
    return sendCommand(FTPCmd.SMNT, dir);
  }
  
  public int port(InetAddress host, int port) throws IOException {
    StringBuilder info = new StringBuilder(24);
    info.append(host.getHostAddress().replace('.', ','));
    int num = port >>> 8;
    info.append(',');
    info.append(num);
    info.append(',');
    num = port & 0xFF;
    info.append(num);
    return sendCommand(FTPCmd.PORT, info.toString());
  }
  
  public int eprt(InetAddress host, int port) throws IOException {
    StringBuilder info = new StringBuilder();
    String h = host.getHostAddress();
    int num = h.indexOf('%');
    if (num > 0)
      h = h.substring(0, num); 
    info.append("|");
    if (host instanceof java.net.Inet4Address) {
      info.append("1");
    } else if (host instanceof java.net.Inet6Address) {
      info.append("2");
    } 
    info.append("|");
    info.append(h);
    info.append("|");
    info.append(port);
    info.append("|");
    return sendCommand(FTPCmd.EPRT, info.toString());
  }
  
  public int pasv() throws IOException {
    return sendCommand(FTPCmd.PASV);
  }
  
  public int epsv() throws IOException {
    return sendCommand(FTPCmd.EPSV);
  }
  
  public int type(int fileType, int formatOrByteSize) throws IOException {
    StringBuilder arg = new StringBuilder();
    arg.append("AEILNTCFRPSBC".charAt(fileType));
    arg.append(' ');
    if (fileType == 3) {
      arg.append(formatOrByteSize);
    } else {
      arg.append("AEILNTCFRPSBC".charAt(formatOrByteSize));
    } 
    return sendCommand(FTPCmd.TYPE, arg.toString());
  }
  
  public int type(int fileType) throws IOException {
    return sendCommand(FTPCmd.TYPE, 
        "AEILNTCFRPSBC".substring(fileType, fileType + 1));
  }
  
  public int stru(int structure) throws IOException {
    return sendCommand(FTPCmd.STRU, 
        "AEILNTCFRPSBC".substring(structure, structure + 1));
  }
  
  public int mode(int mode) throws IOException {
    return sendCommand(FTPCmd.MODE, 
        "AEILNTCFRPSBC".substring(mode, mode + 1));
  }
  
  public int retr(String pathname) throws IOException {
    return sendCommand(FTPCmd.RETR, pathname);
  }
  
  public int stor(String pathname) throws IOException {
    return sendCommand(FTPCmd.STOR, pathname);
  }
  
  public int stou() throws IOException {
    return sendCommand(FTPCmd.STOU);
  }
  
  public int stou(String pathname) throws IOException {
    return sendCommand(FTPCmd.STOU, pathname);
  }
  
  public int appe(String pathname) throws IOException {
    return sendCommand(FTPCmd.APPE, pathname);
  }
  
  public int allo(int bytes) throws IOException {
    return sendCommand(FTPCmd.ALLO, Integer.toString(bytes));
  }
  
  public int feat() throws IOException {
    return sendCommand(FTPCmd.FEAT);
  }
  
  public int allo(int bytes, int recordSize) throws IOException {
    return sendCommand(FTPCmd.ALLO, String.valueOf(Integer.toString(bytes)) + " R " + 
        Integer.toString(recordSize));
  }
  
  public int rest(String marker) throws IOException {
    return sendCommand(FTPCmd.REST, marker);
  }
  
  public int mdtm(String file) throws IOException {
    return sendCommand(FTPCmd.MDTM, file);
  }
  
  public int mfmt(String pathname, String timeval) throws IOException {
    return sendCommand(FTPCmd.MFMT, String.valueOf(timeval) + " " + pathname);
  }
  
  public int rnfr(String pathname) throws IOException {
    return sendCommand(FTPCmd.RNFR, pathname);
  }
  
  public int rnto(String pathname) throws IOException {
    return sendCommand(FTPCmd.RNTO, pathname);
  }
  
  public int dele(String pathname) throws IOException {
    return sendCommand(FTPCmd.DELE, pathname);
  }
  
  public int rmd(String pathname) throws IOException {
    return sendCommand(FTPCmd.RMD, pathname);
  }
  
  public int mkd(String pathname) throws IOException {
    return sendCommand(FTPCmd.MKD, pathname);
  }
  
  public int pwd() throws IOException {
    return sendCommand(FTPCmd.PWD);
  }
  
  public int list() throws IOException {
    return sendCommand(FTPCmd.LIST);
  }
  
  public int list(String pathname) throws IOException {
    return sendCommand(FTPCmd.LIST, pathname);
  }
  
  public int mlsd() throws IOException {
    return sendCommand(FTPCmd.MLSD);
  }
  
  public int mlsd(String path) throws IOException {
    return sendCommand(FTPCmd.MLSD, path);
  }
  
  public int mlst() throws IOException {
    return sendCommand(FTPCmd.MLST);
  }
  
  public int mlst(String path) throws IOException {
    return sendCommand(FTPCmd.MLST, path);
  }
  
  public int nlst() throws IOException {
    return sendCommand(FTPCmd.NLST);
  }
  
  public int nlst(String pathname) throws IOException {
    return sendCommand(FTPCmd.NLST, pathname);
  }
  
  public int site(String parameters) throws IOException {
    return sendCommand(FTPCmd.SITE, parameters);
  }
  
  public int size(String parameters) throws IOException {
    return sendCommand(FTPCmd.SIZE, parameters);
  }
  
  public int syst() throws IOException {
    return sendCommand(FTPCmd.SYST);
  }
  
  public int stat() throws IOException {
    return sendCommand(FTPCmd.STAT);
  }
  
  public int stat(String pathname) throws IOException {
    return sendCommand(FTPCmd.STAT, pathname);
  }
  
  public int help() throws IOException {
    return sendCommand(FTPCmd.HELP);
  }
  
  public int help(String command) throws IOException {
    return sendCommand(FTPCmd.HELP, command);
  }
  
  public int noop() throws IOException {
    return sendCommand(FTPCmd.NOOP);
  }
  
  public boolean isStrictMultilineParsing() {
    return this.strictMultilineParsing;
  }
  
  public void setStrictMultilineParsing(boolean strictMultilineParsing) {
    this.strictMultilineParsing = strictMultilineParsing;
  }
  
  public boolean isStrictReplyParsing() {
    return this.strictReplyParsing;
  }
  
  public void setStrictReplyParsing(boolean strictReplyParsing) {
    this.strictReplyParsing = strictReplyParsing;
  }
  
  protected ProtocolCommandSupport getCommandSupport() {
    return this._commandSupport_;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\ftp\FTP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */