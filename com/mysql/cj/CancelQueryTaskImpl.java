package com.mysql.cj;

import com.mysql.cj.conf.HostInfo;
import com.mysql.cj.conf.PropertyKey;
import com.mysql.cj.exceptions.OperationCancelledException;
import com.mysql.cj.protocol.a.NativeMessageBuilder;
import com.mysql.cj.util.StringUtils;
import java.util.TimerTask;

public class CancelQueryTaskImpl extends TimerTask implements CancelQueryTask {
  Query queryToCancel;
  
  Throwable caughtWhileCancelling = null;
  
  boolean queryTimeoutKillsConnection = false;
  
  public CancelQueryTaskImpl(Query cancellee) {
    this.queryToCancel = cancellee;
    NativeSession session = (NativeSession)cancellee.getSession();
    this.queryTimeoutKillsConnection = ((Boolean)session.getPropertySet().getBooleanProperty(PropertyKey.queryTimeoutKillsConnection).getValue()).booleanValue();
  }
  
  public boolean cancel() {
    boolean res = super.cancel();
    this.queryToCancel = null;
    return res;
  }
  
  public void run() {
    Thread cancelThread = new Thread() {
        public void run() {
          Query localQueryToCancel = CancelQueryTaskImpl.this.queryToCancel;
          if (localQueryToCancel == null)
            return; 
          NativeSession session = (NativeSession)localQueryToCancel.getSession();
          if (session == null)
            return; 
          try {
            if (CancelQueryTaskImpl.this.queryTimeoutKillsConnection) {
              localQueryToCancel.setCancelStatus(Query.CancelStatus.CANCELED_BY_TIMEOUT);
              session.invokeCleanupListeners((Throwable)new OperationCancelledException(Messages.getString("Statement.ConnectionKilledDueToTimeout")));
            } else {
              synchronized (localQueryToCancel.getCancelTimeoutMutex()) {
                long origConnId = session.getThreadId();
                HostInfo hostInfo = session.getHostInfo();
                String database = hostInfo.getDatabase();
                String user = StringUtils.isNullOrEmpty(hostInfo.getUser()) ? "" : hostInfo.getUser();
                String password = StringUtils.isNullOrEmpty(hostInfo.getPassword()) ? "" : hostInfo.getPassword();
                NativeSession newSession = new NativeSession(hostInfo, session.getPropertySet());
                newSession.connect(hostInfo, user, password, database, 30000, new TransactionEventHandler() {
                      public void transactionCompleted() {}
                      
                      public void transactionBegun() {}
                    });
                newSession.sendCommand((new NativeMessageBuilder()).buildComQuery(newSession.getSharedSendPacket(), "KILL QUERY " + origConnId), false, 0);
                localQueryToCancel.setCancelStatus(Query.CancelStatus.CANCELED_BY_TIMEOUT);
              } 
            } 
          } catch (Throwable t) {
            CancelQueryTaskImpl.this.caughtWhileCancelling = t;
          } finally {
            CancelQueryTaskImpl.this.setQueryToCancel(null);
          } 
        }
      };
    cancelThread.start();
  }
  
  public Throwable getCaughtWhileCancelling() {
    return this.caughtWhileCancelling;
  }
  
  public void setCaughtWhileCancelling(Throwable caughtWhileCancelling) {
    this.caughtWhileCancelling = caughtWhileCancelling;
  }
  
  public Query getQueryToCancel() {
    return this.queryToCancel;
  }
  
  public void setQueryToCancel(Query queryToCancel) {
    this.queryToCancel = queryToCancel;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\CancelQueryTaskImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */