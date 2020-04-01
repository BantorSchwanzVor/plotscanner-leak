package com.mysql.cj.result;

import com.mysql.cj.Messages;
import com.mysql.cj.WarningListener;
import com.mysql.cj.conf.PropertySet;
import com.mysql.cj.exceptions.DataReadException;
import com.mysql.cj.protocol.InternalDate;
import com.mysql.cj.protocol.InternalTime;
import com.mysql.cj.protocol.InternalTimestamp;
import java.time.LocalTime;

public class LocalTimeValueFactory extends AbstractDateTimeValueFactory<LocalTime> {
  private WarningListener warningListener;
  
  public LocalTimeValueFactory(PropertySet pset) {
    super(pset);
  }
  
  public LocalTimeValueFactory(PropertySet pset, WarningListener warningListener) {
    this(pset);
    this.warningListener = warningListener;
  }
  
  LocalTime localCreateFromDate(InternalDate idate) {
    return unsupported("DATE");
  }
  
  public LocalTime localCreateFromTime(InternalTime it) {
    if (it.getHours() < 0 || it.getHours() >= 24)
      throw new DataReadException(
          Messages.getString("ResultSet.InvalidTimeValue", new Object[] { "" + it.getHours() + ":" + it.getMinutes() + ":" + it.getSeconds() })); 
    return LocalTime.of(it.getHours(), it.getMinutes(), it.getSeconds(), it.getNanos());
  }
  
  public LocalTime localCreateFromTimestamp(InternalTimestamp its) {
    if (this.warningListener != null)
      this.warningListener.warningEncountered(Messages.getString("ResultSet.PrecisionLostWarning", new Object[] { getTargetTypeName() })); 
    return createFromTime(new InternalTime(its.getHours(), its.getMinutes(), its.getSeconds(), its.getNanos(), its.getScale()));
  }
  
  public LocalTime createFromYear(long year) {
    return unsupported("YEAR");
  }
  
  public String getTargetTypeName() {
    return LocalTime.class.getName();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\result\LocalTimeValueFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */