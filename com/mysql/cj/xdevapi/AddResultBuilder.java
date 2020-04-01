package com.mysql.cj.xdevapi;

public class AddResultBuilder extends UpdateResultBuilder<AddResult> {
  public AddResult build() {
    return new AddResultImpl(this.statementExecuteOkBuilder.build());
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\xdevapi\AddResultBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */