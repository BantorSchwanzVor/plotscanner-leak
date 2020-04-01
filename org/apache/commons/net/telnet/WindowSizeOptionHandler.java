package org.apache.commons.net.telnet;

public class WindowSizeOptionHandler extends TelnetOptionHandler {
  private int m_nWidth = 80;
  
  private int m_nHeight = 24;
  
  protected static final int WINDOW_SIZE = 31;
  
  public WindowSizeOptionHandler(int nWidth, int nHeight, boolean initlocal, boolean initremote, boolean acceptlocal, boolean acceptremote) {
    super(31, initlocal, initremote, acceptlocal, acceptremote);
    this.m_nWidth = nWidth;
    this.m_nHeight = nHeight;
  }
  
  public WindowSizeOptionHandler(int nWidth, int nHeight) {
    super(31, false, false, false, false);
    this.m_nWidth = nWidth;
    this.m_nHeight = nHeight;
  }
  
  public int[] startSubnegotiationLocal() {
    int nCompoundWindowSize = this.m_nWidth * 65536 + this.m_nHeight;
    int nResponseSize = 5;
    if (this.m_nWidth % 256 == 255)
      nResponseSize++; 
    if (this.m_nWidth / 256 == 255)
      nResponseSize++; 
    if (this.m_nHeight % 256 == 255)
      nResponseSize++; 
    if (this.m_nHeight / 256 == 255)
      nResponseSize++; 
    int[] response = new int[nResponseSize];
    response[0] = 31;
    int nIndex = 1, nShift = 24;
    for (; nIndex < nResponseSize; 
      nIndex++, nShift -= 8) {
      int nTurnedOnBits = 255;
      nTurnedOnBits <<= nShift;
      response[nIndex] = (nCompoundWindowSize & nTurnedOnBits) >>> nShift;
      if (response[nIndex] == 255) {
        nIndex++;
        response[nIndex] = 255;
      } 
    } 
    return response;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\telnet\WindowSizeOptionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */