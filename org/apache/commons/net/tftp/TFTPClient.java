package org.apache.commons.net.tftp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import org.apache.commons.net.io.FromNetASCIIOutputStream;
import org.apache.commons.net.io.ToNetASCIIInputStream;

public class TFTPClient extends TFTP {
  public static final int DEFAULT_MAX_TIMEOUTS = 5;
  
  private int __maxTimeouts;
  
  private long totalBytesReceived = 0L;
  
  private long totalBytesSent = 0L;
  
  public TFTPClient() {
    this.__maxTimeouts = 5;
  }
  
  public void setMaxTimeouts(int numTimeouts) {
    if (numTimeouts < 1) {
      this.__maxTimeouts = 1;
    } else {
      this.__maxTimeouts = numTimeouts;
    } 
  }
  
  public int getMaxTimeouts() {
    return this.__maxTimeouts;
  }
  
  public long getTotalBytesReceived() {
    return this.totalBytesReceived;
  }
  
  public long getTotalBytesSent() {
    return this.totalBytesSent;
  }
  
  public int receiveFile(String filename, int mode, OutputStream output, InetAddress host, int port) throws IOException {
    FromNetASCIIOutputStream fromNetASCIIOutputStream;
    int bytesRead = 0;
    int lastBlock = 0;
    int block = 1;
    int hostPort = 0;
    int dataLength = 0;
    this.totalBytesReceived = 0L;
    if (mode == 0)
      fromNetASCIIOutputStream = new FromNetASCIIOutputStream(output); 
    TFTPPacket sent = new TFTPReadRequestPacket(host, port, filename, mode);
    TFTPAckPacket ack = new TFTPAckPacket(host, port, 0);
    beginBufferedOps();
    boolean justStarted = true;
  }
  
  public int receiveFile(String filename, int mode, OutputStream output, String hostname, int port) throws UnknownHostException, IOException {
    return receiveFile(filename, mode, output, InetAddress.getByName(hostname), 
        port);
  }
  
  public int receiveFile(String filename, int mode, OutputStream output, InetAddress host) throws IOException {
    return receiveFile(filename, mode, output, host, 69);
  }
  
  public int receiveFile(String filename, int mode, OutputStream output, String hostname) throws UnknownHostException, IOException {
    return receiveFile(filename, mode, output, InetAddress.getByName(hostname), 
        69);
  }
  
  public void sendFile(String filename, int mode, InputStream input, InetAddress host, int port) throws IOException {
    ToNetASCIIInputStream toNetASCIIInputStream;
    int block = 0;
    int hostPort = 0;
    boolean justStarted = true;
    boolean lastAckWait = false;
    this.totalBytesSent = 0L;
    if (mode == 0)
      toNetASCIIInputStream = new ToNetASCIIInputStream(input); 
    TFTPPacket sent = new TFTPWriteRequestPacket(host, port, filename, mode);
    TFTPDataPacket data = new TFTPDataPacket(host, port, 0, this._sendBuffer, 4, 0);
    beginBufferedOps();
    try {
      while (true) {
        bufferedSend(sent);
        boolean wantReply = true;
        int timeouts = 0;
        do {
          try {
            TFTPPacket received = bufferedReceive();
            InetAddress recdAddress = received.getAddress();
            int recdPort = received.getPort();
            if (justStarted) {
              justStarted = false;
              if (recdPort == port) {
                TFTPErrorPacket error = new TFTPErrorPacket(recdAddress, 
                    recdPort, 5, 
                    "INCORRECT SOURCE PORT");
                bufferedSend(error);
                throw new IOException("Incorrect source port (" + recdPort + ") in request reply.");
              } 
              hostPort = recdPort;
              data.setPort(hostPort);
              if (!host.equals(recdAddress)) {
                host = recdAddress;
                data.setAddress(host);
                sent.setAddress(host);
              } 
            } 
            if (host.equals(recdAddress) && recdPort == hostPort) {
              TFTPErrorPacket error;
              int lastBlock;
              switch (received.getType()) {
                case 5:
                  error = (TFTPErrorPacket)received;
                  throw new IOException("Error code " + error.getError() + 
                      " received: " + error.getMessage());
                case 4:
                  lastBlock = ((TFTPAckPacket)received).getBlockNumber();
                  if (lastBlock == block) {
                    block++;
                    if (block > 65535)
                      block = 0; 
                    wantReply = false;
                    break;
                  } 
                  discardPackets();
                  break;
                default:
                  throw new IOException("Received unexpected packet type.");
              } 
            } else {
              TFTPErrorPacket error = new TFTPErrorPacket(recdAddress, 
                  recdPort, 
                  5, 
                  "Unexpected host or port.");
              bufferedSend(error);
            } 
          } catch (SocketException e) {
            if (++timeouts >= this.__maxTimeouts)
              throw new IOException("Connection timed out."); 
          } catch (InterruptedIOException e) {
            if (++timeouts >= this.__maxTimeouts)
              throw new IOException("Connection timed out."); 
          } catch (TFTPPacketException e) {
            throw new IOException("Bad packet: " + e.getMessage());
          } 
        } while (wantReply);
        if (lastAckWait)
          break; 
        int dataLength = 512;
        int offset = 4;
        int totalThisPacket = 0;
        int bytesRead = 0;
        while (dataLength > 0 && (
          bytesRead = toNetASCIIInputStream.read(this._sendBuffer, offset, dataLength)) > 0) {
          offset += bytesRead;
          dataLength -= bytesRead;
          totalThisPacket += bytesRead;
        } 
        if (totalThisPacket < 512)
          lastAckWait = true; 
        data.setBlockNumber(block);
        data.setData(this._sendBuffer, 4, totalThisPacket);
        sent = data;
        this.totalBytesSent += totalThisPacket;
      } 
    } finally {
      endBufferedOps();
    } 
  }
  
  public void sendFile(String filename, int mode, InputStream input, String hostname, int port) throws UnknownHostException, IOException {
    sendFile(filename, mode, input, InetAddress.getByName(hostname), port);
  }
  
  public void sendFile(String filename, int mode, InputStream input, InetAddress host) throws IOException {
    sendFile(filename, mode, input, host, 69);
  }
  
  public void sendFile(String filename, int mode, InputStream input, String hostname) throws UnknownHostException, IOException {
    sendFile(filename, mode, input, InetAddress.getByName(hostname), 
        69);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\tftp\TFTPClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */