package com.mysql.cj.protocol;

public class InternalDate {
  protected int year = 0;
  
  protected int month = 0;
  
  protected int day = 0;
  
  public InternalDate() {}
  
  public InternalDate(int year, int month, int day) {
    this.year = year;
    this.month = month;
    this.day = day;
  }
  
  public int getYear() {
    return this.year;
  }
  
  public void setYear(int year) {
    this.year = year;
  }
  
  public int getMonth() {
    return this.month;
  }
  
  public void setMonth(int month) {
    this.month = month;
  }
  
  public int getDay() {
    return this.day;
  }
  
  public void setDay(int day) {
    this.day = day;
  }
  
  public boolean isZero() {
    return (this.year == 0 && this.month == 0 && this.day == 0);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\InternalDate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */