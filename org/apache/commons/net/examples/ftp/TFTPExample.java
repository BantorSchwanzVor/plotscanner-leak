package org.apache.commons.net.examples.ftp;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import org.apache.commons.net.tftp.TFTPClient;
import org.apache.commons.net.tftp.TFTPPacket;

public final class TFTPExample {
  static final String USAGE = "Usage: tftp [options] hostname localfile remotefile\n\nhostname   - The name of the remote host [:port]\nlocalfile  - The name of the local file to send or the name to use for\n\tthe received file\nremotefile - The name of the remote file to receive or the name for\n\tthe remote server to use to name the local file being sent.\n\noptions: (The default is to assume -r -b)\n\t-t timeout in seconds (default 60s)\n\t-s Send a local file\n\t-r Receive a remote file\n\t-a Use ASCII transfer mode\n\t-b Use binary transfer mode\n\t-v Verbose (trace packets)\n";
  
  public static void main(String[] args) {
    TFTPClient tftp;
    boolean receiveFile = true;
    int transferMode = 1;
    int timeout = 60000;
    boolean verbose = false;
    int argc;
    for (argc = 0; argc < args.length; ) {
      String arg = args[argc];
      if (arg.startsWith("-")) {
        if (arg.equals("-r")) {
          receiveFile = true;
        } else if (arg.equals("-s")) {
          receiveFile = false;
        } else if (arg.equals("-a")) {
          transferMode = 0;
        } else if (arg.equals("-b")) {
          transferMode = 1;
        } else if (arg.equals("-t")) {
          timeout = 1000 * Integer.parseInt(args[++argc]);
        } else if (arg.equals("-v")) {
          verbose = true;
        } else {
          System.err.println("\007Error: unrecognized option.");
          System.err.print("Usage: tftp [options] hostname localfile remotefile\n\nhostname   - The name of the remote host [:port]\nlocalfile  - The name of the local file to send or the name to use for\n\tthe received file\nremotefile - The name of the remote file to receive or the name for\n\tthe remote server to use to name the local file being sent.\n\noptions: (The default is to assume -r -b)\n\t-t timeout in seconds (default 60s)\n\t-s Send a local file\n\t-r Receive a remote file\n\t-a Use ASCII transfer mode\n\t-b Use binary transfer mode\n\t-v Verbose (trace packets)\n");
          System.exit(1);
        } 
        argc++;
      } 
      break;
    } 
    if (args.length - argc != 3) {
      System.err.println("\007Error: invalid number of arguments.");
      System.err.print("Usage: tftp [options] hostname localfile remotefile\n\nhostname   - The name of the remote host [:port]\nlocalfile  - The name of the local file to send or the name to use for\n\tthe received file\nremotefile - The name of the remote file to receive or the name for\n\tthe remote server to use to name the local file being sent.\n\noptions: (The default is to assume -r -b)\n\t-t timeout in seconds (default 60s)\n\t-s Send a local file\n\t-r Receive a remote file\n\t-a Use ASCII transfer mode\n\t-b Use binary transfer mode\n\t-v Verbose (trace packets)\n");
      System.exit(1);
    } 
    String hostname = args[argc];
    String localFilename = args[argc + 1];
    String remoteFilename = args[argc + 2];
    if (verbose) {
      tftp = new TFTPClient() {
          protected void trace(String direction, TFTPPacket packet) {
            System.out.println(String.valueOf(direction) + " " + packet);
          }
        };
    } else {
      tftp = new TFTPClient();
    } 
    tftp.setDefaultTimeout(timeout);
    boolean closed = false;
    if (receiveFile) {
      closed = receive(transferMode, hostname, localFilename, remoteFilename, tftp);
    } else {
      closed = send(transferMode, hostname, localFilename, remoteFilename, tftp);
    } 
    System.out.println("Recd: " + tftp.getTotalBytesReceived() + " Sent: " + tftp.getTotalBytesSent());
    if (!closed) {
      System.out.println("Failed");
      System.exit(1);
    } 
    System.out.println("OK");
  }
  
  private static boolean send(int transferMode, String hostname, String localFilename, String remoteFilename, TFTPClient tftp) {
    boolean closed;
    FileInputStream input = null;
    try {
      input = new FileInputStream(localFilename);
    } catch (IOException e) {
      tftp.close();
      throw new RuntimeException("Error: could not open local file for reading.", e);
    } 
    open(tftp);
    try {
      String[] parts = hostname.split(":");
      if (parts.length == 2) {
        tftp.sendFile(remoteFilename, transferMode, input, parts[0], Integer.parseInt(parts[1]));
      } else {
        tftp.sendFile(remoteFilename, transferMode, input, hostname);
      } 
    } catch (UnknownHostException e) {
      System.err.println("Error: could not resolve hostname.");
      System.err.println(e.getMessage());
      System.exit(1);
    } catch (IOException e) {
      System.err.println("Error: I/O exception occurred while sending file.");
      System.err.println(e.getMessage());
      System.exit(1);
    } finally {
      boolean bool = close(tftp, input);
    } 
    return closed;
  }
  
  private static boolean receive(int transferMode, String hostname, String localFilename, String remoteFilename, TFTPClient tftp) {
    boolean closed;
    FileOutputStream output = null;
    File file = new File(localFilename);
    if (file.exists()) {
      System.err.println("Error: " + localFilename + " already exists.");
      return false;
    } 
    try {
      output = new FileOutputStream(file);
    } catch (IOException e) {
      tftp.close();
      throw new RuntimeException("Error: could not open local file for writing.", e);
    } 
    open(tftp);
    try {
      String[] parts = hostname.split(":");
      if (parts.length == 2) {
        tftp.receiveFile(remoteFilename, transferMode, output, parts[0], Integer.parseInt(parts[1]));
      } else {
        tftp.receiveFile(remoteFilename, transferMode, output, hostname);
      } 
    } catch (UnknownHostException e) {
      System.err.println("Error: could not resolve hostname.");
      System.err.println(e.getMessage());
      System.exit(1);
    } catch (IOException e) {
      System.err.println(
          "Error: I/O exception occurred while receiving file.");
      System.err.println(e.getMessage());
      System.exit(1);
    } finally {
      boolean bool = close(tftp, output);
    } 
    return closed;
  }
  
  private static boolean close(TFTPClient tftp, Closeable output) {
    boolean closed;
    tftp.close();
    try {
      if (output != null)
        output.close(); 
      closed = true;
    } catch (IOException e) {
      closed = false;
      System.err.println("Error: error closing file.");
      System.err.println(e.getMessage());
    } 
    return closed;
  }
  
  private static void open(TFTPClient tftp) {
    try {
      tftp.open();
    } catch (SocketException e) {
      throw new RuntimeException("Error: could not open local UDP socket.", e);
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\examples\ftp\TFTPExample.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */