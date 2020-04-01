package org.apache.commons.net;

import java.util.EventListener;

public interface ProtocolCommandListener extends EventListener {
  void protocolCommandSent(ProtocolCommandEvent paramProtocolCommandEvent);
  
  void protocolReplyReceived(ProtocolCommandEvent paramProtocolCommandEvent);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\ProtocolCommandListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */