package com.mysql.cj.protocol.x;

import com.mysql.cj.protocol.Message;
import com.mysql.cj.protocol.ProtocolEntityFactory;

public class FetchDoneEntityFactory implements ProtocolEntityFactory<FetchDoneEntity, XMessage> {
  public FetchDoneEntity createFromMessage(XMessage message) {
    return new FetchDoneEntity();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\x\FetchDoneEntityFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */