package org.apache.commons.net.nntp;

class ThreadContainer {
  Threadable threadable;
  
  ThreadContainer parent;
  
  ThreadContainer next;
  
  ThreadContainer child;
  
  boolean findChild(ThreadContainer target) {
    if (this.child == null)
      return false; 
    if (this.child == target)
      return true; 
    return this.child.findChild(target);
  }
  
  void flush() {
    if (this.parent != null && this.threadable == null)
      throw new RuntimeException("no threadable in " + toString()); 
    this.parent = null;
    if (this.threadable != null)
      this.threadable.setChild((this.child == null) ? null : this.child.threadable); 
    if (this.child != null) {
      this.child.flush();
      this.child = null;
    } 
    if (this.threadable != null)
      this.threadable.setNext((this.next == null) ? null : this.next.threadable); 
    if (this.next != null) {
      this.next.flush();
      this.next = null;
    } 
    this.threadable = null;
  }
  
  void reverseChildren() {
    if (this.child != null) {
      ThreadContainer prev = null, kid = this.child, rest = kid.next;
      for (; kid != null; 
        prev = kid, 
        kid = rest, 
        rest = (rest == null) ? null : rest.next)
        kid.next = prev; 
      this.child = prev;
      for (kid = this.child; kid != null; kid = kid.next)
        kid.reverseChildren(); 
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\nntp\ThreadContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */