package com.mysql.cj.jdbc;

import com.mysql.cj.MysqlConnection;
import com.mysql.cj.protocol.NetworkResources;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AbandonedConnectionCleanupThread implements Runnable {
  private static final Set<ConnectionFinalizerPhantomReference> connectionFinalizerPhantomRefs = ConcurrentHashMap.newKeySet();
  
  private static final ReferenceQueue<MysqlConnection> referenceQueue = new ReferenceQueue<>();
  
  private static final ExecutorService cleanupThreadExcecutorService;
  
  private static Thread threadRef = null;
  
  private static Lock threadRefLock = new ReentrantLock();
  
  static {
    cleanupThreadExcecutorService = Executors.newSingleThreadExecutor(r -> {
          Thread t = new Thread(r, "mysql-cj-abandoned-connection-cleanup");
          t.setDaemon(true);
          ClassLoader classLoader = AbandonedConnectionCleanupThread.class.getClassLoader();
          if (classLoader == null)
            classLoader = ClassLoader.getSystemClassLoader(); 
          t.setContextClassLoader(classLoader);
          return threadRef = t;
        });
    cleanupThreadExcecutorService.execute(new AbandonedConnectionCleanupThread());
  }
  
  public void run() {
    while (true) {
      try {
        checkThreadContextClassLoader();
        Reference<? extends MysqlConnection> reference = referenceQueue.remove(5000L);
        if (reference != null)
          finalizeResource((ConnectionFinalizerPhantomReference)reference); 
      } catch (InterruptedException e) {
        threadRefLock.lock();
        try {
          threadRef = null;
          Reference<? extends MysqlConnection> reference;
          while ((reference = referenceQueue.poll()) != null)
            finalizeResource((ConnectionFinalizerPhantomReference)reference); 
          connectionFinalizerPhantomRefs.clear();
        } finally {
          threadRefLock.unlock();
        } 
        return;
      } catch (Exception exception) {}
    } 
  }
  
  private void checkThreadContextClassLoader() {
    try {
      threadRef.getContextClassLoader().getResource("");
    } catch (Throwable e) {
      uncheckedShutdown();
    } 
  }
  
  private static boolean consistentClassLoaders() {
    threadRefLock.lock();
    try {
      if (threadRef == null)
        return false; 
      ClassLoader callerCtxClassLoader = Thread.currentThread().getContextClassLoader();
      ClassLoader threadCtxClassLoader = threadRef.getContextClassLoader();
      return (callerCtxClassLoader != null && threadCtxClassLoader != null && callerCtxClassLoader == threadCtxClassLoader);
    } finally {
      threadRefLock.unlock();
    } 
  }
  
  private static void shutdown(boolean checked) {
    if (checked && !consistentClassLoaders())
      return; 
    cleanupThreadExcecutorService.shutdownNow();
  }
  
  public static void checkedShutdown() {
    shutdown(true);
  }
  
  public static void uncheckedShutdown() {
    shutdown(false);
  }
  
  public static boolean isAlive() {
    threadRefLock.lock();
    try {
      return (threadRef != null && threadRef.isAlive());
    } finally {
      threadRefLock.unlock();
    } 
  }
  
  protected static void trackConnection(MysqlConnection conn, NetworkResources io) {
    threadRefLock.lock();
    try {
      if (isAlive()) {
        ConnectionFinalizerPhantomReference reference = new ConnectionFinalizerPhantomReference(conn, io, referenceQueue);
        connectionFinalizerPhantomRefs.add(reference);
      } 
    } finally {
      threadRefLock.unlock();
    } 
  }
  
  private static void finalizeResource(ConnectionFinalizerPhantomReference reference) {
    try {
      reference.finalizeResources();
      reference.clear();
    } finally {
      connectionFinalizerPhantomRefs.remove(reference);
    } 
  }
  
  private static class ConnectionFinalizerPhantomReference extends PhantomReference<MysqlConnection> {
    private NetworkResources networkResources;
    
    ConnectionFinalizerPhantomReference(MysqlConnection conn, NetworkResources networkResources, ReferenceQueue<? super MysqlConnection> refQueue) {
      super(conn, refQueue);
      this.networkResources = networkResources;
    }
    
    void finalizeResources() {
      if (this.networkResources != null)
        try {
          this.networkResources.forceClose();
        } finally {
          this.networkResources = null;
        }  
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\AbandonedConnectionCleanupThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */