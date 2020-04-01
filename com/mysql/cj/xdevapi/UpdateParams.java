package com.mysql.cj.xdevapi;

import com.mysql.cj.x.protobuf.MysqlxExpr;
import java.util.HashMap;
import java.util.Map;

public class UpdateParams {
  private Map<MysqlxExpr.ColumnIdentifier, MysqlxExpr.Expr> updateOps = new HashMap<>();
  
  public void setUpdates(Map<String, Object> updates) {
    updates.entrySet().forEach(e -> addUpdate((String)e.getKey(), e.getValue()));
  }
  
  public void addUpdate(String path, Object value) {
    this.updateOps.put((new ExprParser(path, true)).parseTableUpdateField(), ExprUtil.argObjectToExpr(value, true));
  }
  
  public Object getUpdates() {
    return this.updateOps;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\xdevapi\UpdateParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */