package com.mysql.cj.xdevapi;

public interface FindStatement extends Statement<FindStatement, DocResult> {
  FindStatement fields(String... paramVarArgs);
  
  FindStatement fields(Expression paramExpression);
  
  FindStatement groupBy(String... paramVarArgs);
  
  FindStatement having(String paramString);
  
  FindStatement orderBy(String... paramVarArgs);
  
  FindStatement sort(String... paramVarArgs);
  
  @Deprecated
  default FindStatement skip(long limitOffset) {
    return offset(limitOffset);
  }
  
  FindStatement offset(long paramLong);
  
  FindStatement limit(long paramLong);
  
  FindStatement lockShared();
  
  FindStatement lockShared(Statement.LockContention paramLockContention);
  
  FindStatement lockExclusive();
  
  FindStatement lockExclusive(Statement.LockContention paramLockContention);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\xdevapi\FindStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */