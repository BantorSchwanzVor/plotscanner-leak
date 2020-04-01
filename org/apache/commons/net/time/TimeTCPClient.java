package org.apache.commons.net.time;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Date;
import org.apache.commons.net.SocketClient;

public final class TimeTCPClient extends SocketClient {
  public static final int DEFAULT_PORT = 37;
  
  public static final long SECONDS_1900_TO_1970 = 2208988800L;
  
  public TimeTCPClient() {
    setDefaultPort(37);
  }
  
  public long getTime() throws IOException {
    DataInputStream input = new DataInputStream(this._input_);
    return input.readInt() & 0xFFFFFFFFL;
  }
  
  public Date getDate() throws IOException {
    return new Date((getTime() - 2208988800L) * 1000L);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\time\TimeTCPClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */