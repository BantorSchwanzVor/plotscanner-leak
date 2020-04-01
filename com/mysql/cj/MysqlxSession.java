package com.mysql.cj;

import com.mysql.cj.conf.HostInfo;
import com.mysql.cj.conf.PropertySet;
import com.mysql.cj.exceptions.CJCommunicationsException;
import com.mysql.cj.protocol.ColumnDefinition;
import com.mysql.cj.protocol.Message;
import com.mysql.cj.protocol.Protocol;
import com.mysql.cj.protocol.ResultBuilder;
import com.mysql.cj.protocol.x.StatementExecuteOkBuilder;
import com.mysql.cj.protocol.x.XProtocol;
import com.mysql.cj.protocol.x.XProtocolError;
import com.mysql.cj.protocol.x.XProtocolRowInputStream;
import com.mysql.cj.result.Row;
import com.mysql.cj.xdevapi.PreparableStatement;
import java.io.IOException;
import java.util.Iterator;
import java.util.Spliterators;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class MysqlxSession extends CoreSession {
  public MysqlxSession(HostInfo hostInfo, PropertySet propSet) {
    super(hostInfo, propSet);
    this.protocol = (Protocol<? extends Message>)new XProtocol(hostInfo, propSet);
    this.messageBuilder = this.protocol.getMessageBuilder();
    this.protocol.connect(hostInfo.getUser(), hostInfo.getPassword(), hostInfo.getDatabase());
  }
  
  public MysqlxSession(XProtocol prot) {
    super(null, prot.getPropertySet());
    this.protocol = (Protocol<? extends Message>)prot;
    this.messageBuilder = this.protocol.getMessageBuilder();
  }
  
  public String getProcessHost() {
    return this.protocol.getSocketConnection().getHost();
  }
  
  public int getPort() {
    return this.protocol.getSocketConnection().getPort();
  }
  
  public XProtocol getProtocol() {
    return (XProtocol)this.protocol;
  }
  
  public void quit() {
    try {
      this.protocol.close();
    } catch (IOException ex) {
      throw new CJCommunicationsException(ex);
    } 
    super.quit();
  }
  
  public boolean isClosed() {
    return !((XProtocol)this.protocol).isOpen();
  }
  
  public boolean supportsPreparedStatements() {
    return ((XProtocol)this.protocol).supportsPreparedStatements();
  }
  
  public boolean readyForPreparingStatements() {
    return ((XProtocol)this.protocol).readyForPreparingStatements();
  }
  
  public int getNewPreparedStatementId(PreparableStatement<?> preparableStatement) {
    return ((XProtocol)this.protocol).getNewPreparedStatementId(preparableStatement);
  }
  
  public void freePreparedStatementId(int preparedStatementId) {
    ((XProtocol)this.protocol).freePreparedStatementId(preparedStatementId);
  }
  
  public boolean failedPreparingStatement(int preparedStatementId, XProtocolError e) {
    return ((XProtocol)this.protocol).failedPreparingStatement(preparedStatementId, e);
  }
  
  public <M extends Message, R, RES> RES query(M message, Predicate<Row> rowFilter, Function<Row, R> rowMapper, Collector<R, ?, RES> collector) {
    this.protocol.send((Message)message, 0);
    ColumnDefinition metadata = this.protocol.readMetadata();
    XProtocolRowInputStream xProtocolRowInputStream = new XProtocolRowInputStream(metadata, (XProtocol)this.protocol, null);
    Stream<Row> stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize((Iterator<? extends Row>)xProtocolRowInputStream, 0), false);
    if (rowFilter != null)
      stream = stream.filter(rowFilter); 
    RES result = stream.<R>map(rowMapper).collect(collector);
    this.protocol.readQueryResult((ResultBuilder)new StatementExecuteOkBuilder());
    return result;
  }
  
  public <M extends Message, R extends QueryResult> R query(M message, ResultBuilder<R> resultBuilder) {
    return (R)((XProtocol)this.protocol).query((Message)message, resultBuilder);
  }
  
  public <M extends Message, R extends QueryResult> CompletableFuture<R> queryAsync(M message, ResultBuilder<R> resultBuilder) {
    return ((XProtocol)this.protocol).queryAsync((Message)message, resultBuilder);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\MysqlxSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */