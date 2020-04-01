package org.apache.commons.net.nntp;

public interface Threadable {
  boolean isDummy();
  
  String messageThreadId();
  
  String[] messageThreadReferences();
  
  String simplifiedSubject();
  
  boolean subjectIsReply();
  
  void setChild(Threadable paramThreadable);
  
  void setNext(Threadable paramThreadable);
  
  Threadable makeDummy();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\nntp\Threadable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */