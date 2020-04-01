package org.apache.commons.net.ftp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.net.util.Base64;

public class FTPHTTPClient extends FTPClient {
  private final String proxyHost;
  
  private final int proxyPort;
  
  private final String proxyUsername;
  
  private final String proxyPassword;
  
  private final Charset charset;
  
  private static final byte[] CRLF = new byte[] { 13, 10 };
  
  private final Base64 base64 = new Base64();
  
  private String tunnelHost;
  
  public FTPHTTPClient(String proxyHost, int proxyPort, String proxyUser, String proxyPass, Charset encoding) {
    this.proxyHost = proxyHost;
    this.proxyPort = proxyPort;
    this.proxyUsername = proxyUser;
    this.proxyPassword = proxyPass;
    this.tunnelHost = null;
    this.charset = encoding;
  }
  
  public FTPHTTPClient(String proxyHost, int proxyPort, String proxyUser, String proxyPass) {
    this(proxyHost, proxyPort, proxyUser, proxyPass, Charset.forName("UTF-8"));
  }
  
  public FTPHTTPClient(String proxyHost, int proxyPort) {
    this(proxyHost, proxyPort, (String)null, (String)null);
  }
  
  public FTPHTTPClient(String proxyHost, int proxyPort, Charset encoding) {
    this(proxyHost, proxyPort, (String)null, (String)null, encoding);
  }
  
  @Deprecated
  protected Socket _openDataConnection_(int command, String arg) throws IOException {
    return super._openDataConnection_(command, arg);
  }
  
  protected Socket _openDataConnection_(String command, String arg) throws IOException {
    if (getDataConnectionMode() != 2)
      throw new IllegalStateException("Only passive connection mode supported"); 
    boolean isInet6Address = getRemoteAddress() instanceof java.net.Inet6Address;
    String passiveHost = null;
    boolean attemptEPSV = !(!isUseEPSVwithIPv4() && !isInet6Address);
    if (attemptEPSV && epsv() == 229) {
      _parseExtendedPassiveModeReply(this._replyLines.get(0));
      passiveHost = this.tunnelHost;
    } else {
      if (isInet6Address)
        return null; 
      if (pasv() != 227)
        return null; 
      _parsePassiveModeReply(this._replyLines.get(0));
      passiveHost = getPassiveHost();
    } 
    Socket socket = this._socketFactory_.createSocket(this.proxyHost, this.proxyPort);
    InputStream is = socket.getInputStream();
    OutputStream os = socket.getOutputStream();
    tunnelHandshake(passiveHost, getPassivePort(), is, os);
    if (getRestartOffset() > 0L && !restart(getRestartOffset())) {
      socket.close();
      return null;
    } 
    if (!FTPReply.isPositivePreliminary(sendCommand(command, arg))) {
      socket.close();
      return null;
    } 
    return socket;
  }
  
  public void connect(String host, int port) throws SocketException, IOException {
    Reader socketIsReader;
    this._socket_ = this._socketFactory_.createSocket(this.proxyHost, this.proxyPort);
    this._input_ = this._socket_.getInputStream();
    this._output_ = this._socket_.getOutputStream();
    try {
      socketIsReader = tunnelHandshake(host, port, this._input_, this._output_);
    } catch (Exception e) {
      IOException ioe = new IOException("Could not connect to " + host + " using port " + port);
      ioe.initCause(e);
      throw ioe;
    } 
    _connectAction_(socketIsReader);
  }
  
  private BufferedReader tunnelHandshake(String host, int port, InputStream input, OutputStream output) throws IOException, UnsupportedEncodingException {
    String connectString = "CONNECT " + host + ":" + port + " HTTP/1.1";
    String hostString = "Host: " + host + ":" + port;
    this.tunnelHost = host;
    output.write(connectString.getBytes(this.charset));
    output.write(CRLF);
    output.write(hostString.getBytes(this.charset));
    output.write(CRLF);
    if (this.proxyUsername != null && this.proxyPassword != null) {
      String auth = String.valueOf(this.proxyUsername) + ":" + this.proxyPassword;
      String header = "Proxy-Authorization: Basic " + 
        this.base64.encodeToString(auth.getBytes(this.charset));
      output.write(header.getBytes(this.charset));
    } 
    output.write(CRLF);
    List<String> response = new ArrayList<>();
    BufferedReader reader = new BufferedReader(
        new InputStreamReader(input, getCharset()));
    for (String line = reader.readLine(); line != null && 
      line.length() > 0; line = reader.readLine())
      response.add(line); 
    int size = response.size();
    if (size == 0)
      throw new IOException("No response from proxy"); 
    String code = null;
    String resp = response.get(0);
    if (resp.startsWith("HTTP/") && resp.length() >= 12) {
      code = resp.substring(9, 12);
    } else {
      throw new IOException("Invalid response from proxy: " + resp);
    } 
    if (!"200".equals(code)) {
      StringBuilder msg = new StringBuilder();
      msg.append("HTTPTunnelConnector: connection failed\r\n");
      msg.append("Response received from the proxy:\r\n");
      for (String str : response) {
        msg.append(str);
        msg.append("\r\n");
      } 
      throw new IOException(msg.toString());
    } 
    return reader;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\ftp\FTPHTTPClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */