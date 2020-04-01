package com.mysql.cj.xdevapi;

public class InsertResultBuilder extends UpdateResultBuilder<InsertResult> {
  public InsertResult build() {
    return new InsertResultImpl(this.statementExecuteOkBuilder.build());
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\xdevapi\InsertResultBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */