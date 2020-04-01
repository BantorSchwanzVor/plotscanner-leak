package com.mysql.cj.exceptions;

import com.mysql.cj.Messages;

public class AssertionFailedException extends CJException {
  private static final long serialVersionUID = 5832552608575043403L;
  
  public static AssertionFailedException shouldNotHappen(Exception ex) throws AssertionFailedException {
    throw new AssertionFailedException(ex);
  }
  
  public static AssertionFailedException shouldNotHappen(String assertion) throws AssertionFailedException {
    return new AssertionFailedException(assertion);
  }
  
  public AssertionFailedException(Exception ex) {
    super(Messages.getString("AssertionFailedException.0") + ex.toString() + Messages.getString("AssertionFailedException.1"), ex);
  }
  
  public AssertionFailedException(String assertion) {
    super(Messages.getString("AssertionFailedException.2", new Object[] { assertion }));
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\exceptions\AssertionFailedException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */