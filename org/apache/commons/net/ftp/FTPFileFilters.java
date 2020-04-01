package org.apache.commons.net.ftp;

public class FTPFileFilters {
  public static final FTPFileFilter ALL = new FTPFileFilter() {
      public boolean accept(FTPFile file) {
        return true;
      }
    };
  
  public static final FTPFileFilter NON_NULL = new FTPFileFilter() {
      public boolean accept(FTPFile file) {
        return (file != null);
      }
    };
  
  public static final FTPFileFilter DIRECTORIES = new FTPFileFilter() {
      public boolean accept(FTPFile file) {
        return (file != null && file.isDirectory());
      }
    };
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\ftp\FTPFileFilters.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */