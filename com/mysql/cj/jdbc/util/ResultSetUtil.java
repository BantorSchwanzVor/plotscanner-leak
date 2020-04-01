package com.mysql.cj.jdbc.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class ResultSetUtil {
  public static void resultSetToMap(Map<Object, Object> mappedValues, ResultSet rs) throws SQLException {
    while (rs.next())
      mappedValues.put(rs.getObject(1), rs.getObject(2)); 
  }
  
  public static void resultSetToMap(Map<Object, Object> mappedValues, ResultSet rs, int key, int value) throws SQLException {
    while (rs.next())
      mappedValues.put(rs.getObject(key), rs.getObject(value)); 
  }
  
  public static Object readObject(ResultSet resultSet, int index) throws IOException, SQLException, ClassNotFoundException {
    ObjectInputStream objIn = new ObjectInputStream(resultSet.getBinaryStream(index));
    Object obj = objIn.readObject();
    objIn.close();
    return obj;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdb\\util\ResultSetUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */