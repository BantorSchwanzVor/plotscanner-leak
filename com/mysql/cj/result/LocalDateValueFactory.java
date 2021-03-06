package com.mysql.cj.result;

import com.mysql.cj.Messages;
import com.mysql.cj.WarningListener;
import com.mysql.cj.conf.PropertySet;
import com.mysql.cj.exceptions.DataReadException;
import com.mysql.cj.protocol.InternalDate;
import com.mysql.cj.protocol.InternalTime;
import com.mysql.cj.protocol.InternalTimestamp;
import java.time.LocalDate;

public class LocalDateValueFactory extends AbstractDateTimeValueFactory<LocalDate> {
  private WarningListener warningListener;
  
  public LocalDateValueFactory(PropertySet pset) {
    super(pset);
  }
  
  public LocalDateValueFactory(PropertySet pset, WarningListener warningListener) {
    this(pset);
    this.warningListener = warningListener;
  }
  
  public LocalDate localCreateFromDate(InternalDate idate) {
    if (idate.getYear() == 0 && idate.getMonth() == 0 && idate.getDay() == 0)
      throw new DataReadException(Messages.getString("ResultSet.InvalidZeroDate")); 
    return LocalDate.of(idate.getYear(), idate.getMonth(), idate.getDay());
  }
  
  public LocalDate localCreateFromTimestamp(InternalTimestamp its) {
    if (this.warningListener != null)
      this.warningListener.warningEncountered(Messages.getString("ResultSet.PrecisionLostWarning", new Object[] { getTargetTypeName() })); 
    return createFromDate((InternalDate)its);
  }
  
  LocalDate localCreateFromTime(InternalTime it) {
    return unsupported("TIME");
  }
  
  public String getTargetTypeName() {
    return LocalDate.class.getName();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\result\LocalDateValueFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */