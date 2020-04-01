package net.minecraft.util;

import net.minecraft.crash.CrashReport;

public class ReportedException extends RuntimeException {
  private final CrashReport crashReport;
  
  public ReportedException(CrashReport report) {
    this.crashReport = report;
  }
  
  public CrashReport getCrashReport() {
    return this.crashReport;
  }
  
  public Throwable getCause() {
    return this.crashReport.getCrashCause();
  }
  
  public String getMessage() {
    return this.crashReport.getDescription();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\ReportedException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */