package com.mysql.cj;

import com.mysql.cj.util.StringUtils;
import java.util.LinkedList;

public class AppendingBatchVisitor implements BatchVisitor {
  LinkedList<byte[]> statementComponents = (LinkedList)new LinkedList<>();
  
  public BatchVisitor append(byte[] values) {
    this.statementComponents.addLast(values);
    return this;
  }
  
  public BatchVisitor increment() {
    return this;
  }
  
  public BatchVisitor decrement() {
    this.statementComponents.removeLast();
    return this;
  }
  
  public BatchVisitor merge(byte[] front, byte[] back) {
    int mergedLength = front.length + back.length;
    byte[] merged = new byte[mergedLength];
    System.arraycopy(front, 0, merged, 0, front.length);
    System.arraycopy(back, 0, merged, front.length, back.length);
    this.statementComponents.addLast(merged);
    return this;
  }
  
  public BatchVisitor mergeWithLast(byte[] values) {
    if (this.statementComponents.isEmpty())
      return append(values); 
    return merge(this.statementComponents.removeLast(), values);
  }
  
  public byte[][] getStaticSqlStrings() {
    byte[][] asBytes = new byte[this.statementComponents.size()][];
    this.statementComponents.toArray(asBytes);
    return asBytes;
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (byte[] comp : this.statementComponents)
      sb.append(StringUtils.toString(comp)); 
    return sb.toString();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\AppendingBatchVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */