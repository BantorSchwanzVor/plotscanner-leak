package com.mysql.cj.protocol;

import com.mysql.cj.result.Row;

public interface ResultsetRow extends Row, ProtocolEntity {
  default boolean isBinaryEncoded() {
    return false;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\ResultsetRow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */