package com.mysql.cj.xdevapi;

import com.mysql.cj.Messages;
import com.mysql.cj.MysqlxSession;
import com.mysql.cj.protocol.Message;
import com.mysql.cj.protocol.x.XMessage;
import com.mysql.cj.protocol.x.XMessageBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ModifyStatementImpl extends FilterableStatement<ModifyStatement, Result> implements ModifyStatement {
  private List<UpdateSpec> updates = new ArrayList<>();
  
  ModifyStatementImpl(MysqlxSession mysqlxSession, String schema, String collection, String criteria) {
    super(new DocFilterParams(schema, collection, false));
    this.mysqlxSession = mysqlxSession;
    if (criteria == null || criteria.trim().length() == 0)
      throw new XDevAPIError(Messages.getString("ModifyStatement.0", new String[] { "criteria" })); 
    this.filterParams.setCriteria(criteria);
    if (!this.mysqlxSession.supportsPreparedStatements())
      this.preparedState = PreparableStatement.PreparedState.UNSUPPORTED; 
  }
  
  protected Result executeStatement() {
    return (Result)this.mysqlxSession.query((Message)getMessageBuilder().buildDocUpdate(this.filterParams, this.updates), new UpdateResultBuilder<>());
  }
  
  protected XMessage getPrepareStatementXMessage() {
    return getMessageBuilder().buildPrepareDocUpdate(this.preparedStatementId, this.filterParams, this.updates);
  }
  
  protected Result executePreparedStatement() {
    return (Result)this.mysqlxSession.query((Message)getMessageBuilder().buildPrepareExecute(this.preparedStatementId, this.filterParams), new UpdateResultBuilder<>());
  }
  
  public CompletableFuture<Result> executeAsync() {
    return this.mysqlxSession.queryAsync((Message)((XMessageBuilder)this.mysqlxSession
        .getMessageBuilder()).buildDocUpdate(this.filterParams, this.updates), new UpdateResultBuilder<>());
  }
  
  public ModifyStatement set(String docPath, Object value) {
    resetPrepareState();
    this.updates.add((new UpdateSpec(UpdateType.ITEM_SET, docPath)).setValue(value));
    return this;
  }
  
  public ModifyStatement change(String docPath, Object value) {
    resetPrepareState();
    this.updates.add((new UpdateSpec(UpdateType.ITEM_REPLACE, docPath)).setValue(value));
    return this;
  }
  
  public ModifyStatement unset(String... fields) {
    resetPrepareState();
    this.updates.addAll((Collection<? extends UpdateSpec>)Arrays.<String>stream(fields).map(docPath -> new UpdateSpec(UpdateType.ITEM_REMOVE, docPath)).collect(Collectors.toList()));
    return this;
  }
  
  public ModifyStatement patch(DbDoc document) {
    resetPrepareState();
    return patch(document.toString());
  }
  
  public ModifyStatement patch(String document) {
    resetPrepareState();
    this.updates.add((new UpdateSpec(UpdateType.MERGE_PATCH, "")).setValue(Expression.expr(document)));
    return this;
  }
  
  public ModifyStatement arrayInsert(String field, Object value) {
    resetPrepareState();
    this.updates.add((new UpdateSpec(UpdateType.ARRAY_INSERT, field)).setValue(value));
    return this;
  }
  
  public ModifyStatement arrayAppend(String docPath, Object value) {
    resetPrepareState();
    this.updates.add((new UpdateSpec(UpdateType.ARRAY_APPEND, docPath)).setValue(value));
    return this;
  }
  
  @Deprecated
  public ModifyStatement where(String searchCondition) {
    return super.where(searchCondition);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\xdevapi\ModifyStatementImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */