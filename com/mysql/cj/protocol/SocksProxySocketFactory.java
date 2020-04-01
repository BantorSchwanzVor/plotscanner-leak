package com.mysql.cj.protocol;

import com.mysql.cj.conf.PropertyKey;
import com.mysql.cj.conf.PropertySet;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

public class SocksProxySocketFactory extends StandardSocketFactory {
  protected Socket createSocket(PropertySet props) {
    String socksProxyHost = (String)props.getStringProperty(PropertyKey.socksProxyHost).getValue();
    int socksProxyPort = ((Integer)props.getIntegerProperty(PropertyKey.socksProxyPort).getValue()).intValue();
    return new Socket(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(socksProxyHost, socksProxyPort)));
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\SocksProxySocketFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */