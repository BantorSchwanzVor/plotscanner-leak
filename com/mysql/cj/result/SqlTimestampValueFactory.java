package com.mysql.cj.result;

import com.mysql.cj.Messages;
import com.mysql.cj.conf.PropertySet;
import com.mysql.cj.exceptions.DataReadException;
import com.mysql.cj.protocol.InternalDate;
import com.mysql.cj.protocol.InternalTime;
import com.mysql.cj.protocol.InternalTimestamp;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class SqlTimestampValueFactory extends AbstractDateTimeValueFactory<Timestamp> {
  private Calendar cal;
  
  public SqlTimestampValueFactory(PropertySet pset, Calendar calendar, TimeZone tz) {
    super(pset);
    if (calendar != null) {
      this.cal = (Calendar)calendar.clone();
    } else {
      this.cal = Calendar.getInstance(tz, Locale.US);
      this.cal.setLenient(false);
    } 
  }
  
  public Timestamp localCreateFromDate(InternalDate idate) {
    return createFromTimestamp(new InternalTimestamp(idate.getYear(), idate.getMonth(), idate.getDay(), 0, 0, 0, 0, 0));
  }
  
  public Timestamp localCreateFromTime(InternalTime it) {
    if (it.getHours() < 0 || it.getHours() >= 24)
      throw new DataReadException(
          Messages.getString("ResultSet.InvalidTimeValue", new Object[] { "" + it.getHours() + ":" + it.getMinutes() + ":" + it.getSeconds() })); 
    return createFromTimestamp(new InternalTimestamp(1970, 1, 1, it.getHours(), it.getMinutes(), it.getSeconds(), it.getNanos(), it.getScale()));
  }
  
  public Timestamp localCreateFromTimestamp(InternalTimestamp its) {
    if (its.getYear() == 0 && its.getMonth() == 0 && its.getDay() == 0)
      throw new DataReadException(Messages.getString("ResultSet.InvalidZeroDate")); 
    synchronized (this.cal) {
      this.cal.set(its.getYear(), its.getMonth() - 1, its.getDay(), its.getHours(), its.getMinutes(), its.getSeconds());
      Timestamp ts = new Timestamp(this.cal.getTimeInMillis());
      ts.setNanos(its.getNanos());
      return ts;
    } 
  }
  
  public String getTargetTypeName() {
    return Timestamp.class.getName();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\result\SqlTimestampValueFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */