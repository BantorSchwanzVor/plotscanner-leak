package com.mysql.cj.result;

import com.mysql.cj.Messages;
import com.mysql.cj.WarningListener;
import com.mysql.cj.conf.PropertySet;
import com.mysql.cj.exceptions.DataReadException;
import com.mysql.cj.protocol.InternalDate;
import com.mysql.cj.protocol.InternalTime;
import com.mysql.cj.protocol.InternalTimestamp;
import java.sql.Time;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class SqlTimeValueFactory extends AbstractDateTimeValueFactory<Time> {
  private WarningListener warningListener;
  
  private Calendar cal;
  
  public SqlTimeValueFactory(PropertySet pset, Calendar calendar, TimeZone tz) {
    super(pset);
    if (calendar != null) {
      this.cal = (Calendar)calendar.clone();
    } else {
      this.cal = Calendar.getInstance(tz, Locale.US);
      this.cal.setLenient(false);
    } 
  }
  
  public SqlTimeValueFactory(PropertySet pset, Calendar calendar, TimeZone tz, WarningListener warningListener) {
    this(pset, calendar, tz);
    this.warningListener = warningListener;
  }
  
  Time localCreateFromDate(InternalDate idate) {
    return unsupported("DATE");
  }
  
  public Time localCreateFromTime(InternalTime it) {
    if (it.getHours() < 0 || it.getHours() >= 24)
      throw new DataReadException(
          Messages.getString("ResultSet.InvalidTimeValue", new Object[] { "" + it.getHours() + ":" + it.getMinutes() + ":" + it.getSeconds() })); 
    synchronized (this.cal) {
      this.cal.set(1970, 0, 1, it.getHours(), it.getMinutes(), it.getSeconds());
      this.cal.set(14, 0);
      long ms = (it.getNanos() / 1000000) + this.cal.getTimeInMillis();
      return new Time(ms);
    } 
  }
  
  public Time localCreateFromTimestamp(InternalTimestamp its) {
    if (this.warningListener != null)
      this.warningListener.warningEncountered(Messages.getString("ResultSet.PrecisionLostWarning", new Object[] { "java.sql.Time" })); 
    return createFromTime(new InternalTime(its.getHours(), its.getMinutes(), its.getSeconds(), its.getNanos(), its.getScale()));
  }
  
  public Time createFromYear(long year) {
    return unsupported("YEAR");
  }
  
  public String getTargetTypeName() {
    return Time.class.getName();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\result\SqlTimeValueFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */