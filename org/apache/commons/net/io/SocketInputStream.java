package org.apache.commons.net.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class SocketInputStream extends FilterInputStream {
  private final Socket __socket;
  
  public SocketInputStream(Socket socket, InputStream stream) {
    super(stream);
    this.__socket = socket;
  }
  
  public void close() throws IOException {
    super.close();
    this.__socket.close();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\io\SocketInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */