package org.apache.commons.net.bsd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.commons.net.SocketClient;
import org.apache.commons.net.io.SocketInputStream;

public class RExecClient extends SocketClient {
  protected static final char NULL_CHAR = '\000';
  
  public static final int DEFAULT_PORT = 512;
  
  private boolean __remoteVerificationEnabled;
  
  protected InputStream _errorStream_;
  
  InputStream _createErrorStream() throws IOException {
    ServerSocket server = this._serverSocketFactory_.createServerSocket(0, 1, getLocalAddress());
    this._output_.write(Integer.toString(server.getLocalPort()).getBytes("UTF-8"));
    this._output_.write(0);
    this._output_.flush();
    Socket socket = server.accept();
    server.close();
    if (this.__remoteVerificationEnabled && !verifyRemote(socket)) {
      socket.close();
      throw new IOException(
          "Security violation: unexpected connection attempt by " + 
          socket.getInetAddress().getHostAddress());
    } 
    return (InputStream)new SocketInputStream(socket, socket.getInputStream());
  }
  
  public RExecClient() {
    this._errorStream_ = null;
    setDefaultPort(512);
  }
  
  public InputStream getInputStream() {
    return this._input_;
  }
  
  public OutputStream getOutputStream() {
    return this._output_;
  }
  
  public InputStream getErrorStream() {
    return this._errorStream_;
  }
  
  public void rexec(String username, String password, String command, boolean separateErrorStream) throws IOException {
    if (separateErrorStream) {
      this._errorStream_ = _createErrorStream();
    } else {
      this._output_.write(0);
    } 
    this._output_.write(username.getBytes(getCharset()));
    this._output_.write(0);
    this._output_.write(password.getBytes(getCharset()));
    this._output_.write(0);
    this._output_.write(command.getBytes(getCharset()));
    this._output_.write(0);
    this._output_.flush();
    int ch = this._input_.read();
    if (ch > 0) {
      StringBuilder buffer = new StringBuilder();
      while ((ch = this._input_.read()) != -1 && ch != 10)
        buffer.append((char)ch); 
      throw new IOException(buffer.toString());
    } 
    if (ch < 0)
      throw new IOException("Server closed connection."); 
  }
  
  public void rexec(String username, String password, String command) throws IOException {
    rexec(username, password, command, false);
  }
  
  public void disconnect() throws IOException {
    if (this._errorStream_ != null)
      this._errorStream_.close(); 
    this._errorStream_ = null;
    super.disconnect();
  }
  
  public final void setRemoteVerificationEnabled(boolean enable) {
    this.__remoteVerificationEnabled = enable;
  }
  
  public final boolean isRemoteVerificationEnabled() {
    return this.__remoteVerificationEnabled;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\bsd\RExecClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */