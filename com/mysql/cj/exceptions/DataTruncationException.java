package com.mysql.cj.exceptions;

public class DataTruncationException extends CJException {
  private static final long serialVersionUID = -5209088385943506720L;
  
  private int index;
  
  private boolean parameter;
  
  private boolean read;
  
  private int dataSize;
  
  private int transferSize;
  
  public DataTruncationException() {}
  
  public DataTruncationException(String message) {
    super(message);
  }
  
  public DataTruncationException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public DataTruncationException(Throwable cause) {
    super(cause);
  }
  
  protected DataTruncationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
  
  public DataTruncationException(String message, int index, boolean parameter, boolean read, int dataSize, int transferSize, int vendorErrorCode) {
    super(message);
    setIndex(index);
    setParameter(parameter);
    setRead(read);
    setDataSize(dataSize);
    setTransferSize(transferSize);
    setVendorCode(vendorErrorCode);
  }
  
  public int getIndex() {
    return this.index;
  }
  
  public void setIndex(int index) {
    this.index = index;
  }
  
  public boolean isParameter() {
    return this.parameter;
  }
  
  public void setParameter(boolean parameter) {
    this.parameter = parameter;
  }
  
  public boolean isRead() {
    return this.read;
  }
  
  public void setRead(boolean read) {
    this.read = read;
  }
  
  public int getDataSize() {
    return this.dataSize;
  }
  
  public void setDataSize(int dataSize) {
    this.dataSize = dataSize;
  }
  
  public int getTransferSize() {
    return this.transferSize;
  }
  
  public void setTransferSize(int transferSize) {
    this.transferSize = transferSize;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\exceptions\DataTruncationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */