package com.mysql.cj.protocol.x;

import com.mysql.cj.protocol.ColumnDefinition;
import com.mysql.cj.result.Row;
import com.mysql.cj.result.RowList;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class XProtocolRowInputStream implements RowList {
  private ColumnDefinition metadata;
  
  private XProtocol protocol;
  
  private boolean isDone = false;
  
  private int position = -1;
  
  private Row next;
  
  private Consumer<Notice> noticeConsumer;
  
  public XProtocolRowInputStream(ColumnDefinition metadata, XProtocol protocol, Consumer<Notice> noticeConsumer) {
    this.metadata = metadata;
    this.protocol = protocol;
    this.noticeConsumer = noticeConsumer;
  }
  
  public XProtocolRowInputStream(ColumnDefinition metadata, Row row, XProtocol protocol, Consumer<Notice> noticeConsumer) {
    this.metadata = metadata;
    this.protocol = protocol;
    this.next = row;
    this.next.setMetadata(metadata);
    this.noticeConsumer = noticeConsumer;
  }
  
  public Row readRow() {
    if (!hasNext()) {
      this.isDone = true;
      return null;
    } 
    this.position++;
    Row r = this.next;
    this.next = null;
    return r;
  }
  
  public Row next() {
    if (!hasNext())
      throw new NoSuchElementException(); 
    return readRow();
  }
  
  public boolean hasNext() {
    if (this.isDone)
      return false; 
    if (this.next == null)
      this.next = this.protocol.readRowOrNull(this.metadata, this.noticeConsumer); 
    return (this.next != null);
  }
  
  public int getPosition() {
    return this.position;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\x\XProtocolRowInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */