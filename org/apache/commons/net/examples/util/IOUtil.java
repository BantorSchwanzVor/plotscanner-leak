package org.apache.commons.net.examples.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.net.io.Util;

public final class IOUtil {
  public static final void readWrite(final InputStream remoteInput, final OutputStream remoteOutput, final InputStream localInput, final OutputStream localOutput) {
    Thread reader = new Thread() {
        public void run() {
          try {
            int ch;
            while (!interrupted() && (ch = localInput.read()) != -1) {
              remoteOutput.write(ch);
              remoteOutput.flush();
            } 
          } catch (IOException iOException) {}
        }
      };
    Thread writer = new Thread() {
        public void run() {
          try {
            Util.copyStream(remoteInput, localOutput);
          } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
          } 
        }
      };
    writer.setPriority(Thread.currentThread().getPriority() + 1);
    writer.start();
    reader.setDaemon(true);
    reader.start();
    try {
      writer.join();
      reader.interrupt();
    } catch (InterruptedException interruptedException) {}
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\example\\util\IOUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */