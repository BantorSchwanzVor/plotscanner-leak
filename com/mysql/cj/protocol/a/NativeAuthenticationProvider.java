package com.mysql.cj.protocol.a;

import com.mysql.cj.Constants;
import com.mysql.cj.Messages;
import com.mysql.cj.conf.PropertyDefinitions;
import com.mysql.cj.conf.PropertyKey;
import com.mysql.cj.conf.PropertySet;
import com.mysql.cj.conf.RuntimeProperty;
import com.mysql.cj.exceptions.ExceptionFactory;
import com.mysql.cj.exceptions.ExceptionInterceptor;
import com.mysql.cj.exceptions.UnableToConnectException;
import com.mysql.cj.exceptions.WrongArgumentException;
import com.mysql.cj.protocol.AuthenticationPlugin;
import com.mysql.cj.protocol.AuthenticationProvider;
import com.mysql.cj.protocol.Protocol;
import com.mysql.cj.protocol.ServerSession;
import com.mysql.cj.protocol.a.authentication.CachingSha2PasswordPlugin;
import com.mysql.cj.protocol.a.authentication.MysqlClearPasswordPlugin;
import com.mysql.cj.protocol.a.authentication.MysqlNativePasswordPlugin;
import com.mysql.cj.protocol.a.authentication.MysqlOldPasswordPlugin;
import com.mysql.cj.protocol.a.authentication.Sha256PasswordPlugin;
import com.mysql.cj.protocol.a.result.OkPacket;
import com.mysql.cj.util.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NativeAuthenticationProvider implements AuthenticationProvider<NativePacketPayload> {
  protected static final int AUTH_411_OVERHEAD = 33;
  
  private static final String NONE = "none";
  
  protected String seed;
  
  private boolean useConnectWithDb;
  
  private ExceptionInterceptor exceptionInterceptor;
  
  private PropertySet propertySet;
  
  private Protocol<NativePacketPayload> protocol;
  
  public void init(Protocol<NativePacketPayload> prot, PropertySet propSet, ExceptionInterceptor excInterceptor) {
    this.protocol = prot;
    this.propertySet = propSet;
    this.exceptionInterceptor = excInterceptor;
  }
  
  public void connect(ServerSession sessState, String user, String password, String database) {
    long clientParam = sessState.getClientParam();
    NativeCapabilities capabilities = (NativeCapabilities)sessState.getCapabilities();
    NativePacketPayload buf = capabilities.getInitialHandshakePacket();
    this.seed = capabilities.getSeed();
    sessState.setServerDefaultCollationIndex(capabilities.getServerDefaultCollationIndex());
    sessState.setStatusFlags(capabilities.getStatusFlags());
    int capabilityFlags = capabilities.getCapabilityFlags();
    if ((capabilityFlags & 0x8000) != 0) {
      String seedPart2;
      StringBuilder newSeed;
      clientParam |= 0x8000L;
      int authPluginDataLength = capabilities.getAuthPluginDataLength();
      if (authPluginDataLength > 0) {
        seedPart2 = buf.readString(NativeConstants.StringLengthDataType.STRING_FIXED, "ASCII", authPluginDataLength - 8);
        newSeed = new StringBuilder(authPluginDataLength);
      } else {
        seedPart2 = buf.readString(NativeConstants.StringSelfDataType.STRING_TERM, "ASCII");
        newSeed = new StringBuilder(20);
      } 
      newSeed.append(this.seed);
      newSeed.append(seedPart2);
      this.seed = newSeed.toString();
    } else {
      throw (UnableToConnectException)ExceptionFactory.createException(UnableToConnectException.class, "CLIENT_SECURE_CONNECTION is required", getExceptionInterceptor());
    } 
    if ((capabilityFlags & 0x20) != 0 && ((Boolean)this.propertySet.getBooleanProperty(PropertyKey.useCompression).getValue()).booleanValue())
      clientParam |= 0x20L; 
    this
      .useConnectWithDb = (database != null && database.length() > 0 && !((Boolean)this.propertySet.getBooleanProperty(PropertyKey.createDatabaseIfNotExist).getValue()).booleanValue());
    if (this.useConnectWithDb)
      clientParam |= 0x8L; 
    RuntimeProperty<Boolean> useInformationSchema = this.propertySet.getProperty(PropertyKey.useInformationSchema);
    if (this.protocol.versionMeetsMinimum(8, 0, 3) && !((Boolean)useInformationSchema.getValue()).booleanValue() && !useInformationSchema.isExplicitlySet())
      useInformationSchema.setValue(Boolean.valueOf(true)); 
    PropertyDefinitions.SslMode sslMode = (PropertyDefinitions.SslMode)this.propertySet.getEnumProperty(PropertyKey.sslMode).getValue();
    if ((capabilityFlags & 0x800) == 0 && sslMode != PropertyDefinitions.SslMode.DISABLED && sslMode != PropertyDefinitions.SslMode.PREFERRED)
      throw (UnableToConnectException)ExceptionFactory.createException(UnableToConnectException.class, Messages.getString("MysqlIO.15"), getExceptionInterceptor()); 
    if ((capabilityFlags & 0x4) != 0) {
      clientParam |= 0x4L;
      sessState.setHasLongColumnInfo(true);
    } 
    if (!((Boolean)this.propertySet.getBooleanProperty(PropertyKey.useAffectedRows).getValue()).booleanValue())
      clientParam |= 0x2L; 
    if (((Boolean)this.propertySet.getBooleanProperty(PropertyKey.allowLoadLocalInfile).getValue()).booleanValue())
      clientParam |= 0x80L; 
    if (((Boolean)this.propertySet.getBooleanProperty(PropertyKey.interactiveClient).getValue()).booleanValue())
      clientParam |= 0x400L; 
    if ((capabilityFlags & 0x800000) != 0);
    if ((capabilityFlags & 0x1000000) != 0)
      clientParam |= 0x1000000L; 
    if ((capabilityFlags & 0x80000) != 0) {
      sessState.setClientParam(clientParam);
      proceedHandshakeWithPluggableAuthentication(sessState, user, password, database, buf);
    } else {
      throw (UnableToConnectException)ExceptionFactory.createException(UnableToConnectException.class, "CLIENT_PLUGIN_AUTH is required", getExceptionInterceptor());
    } 
  }
  
  private Map<String, AuthenticationPlugin<NativePacketPayload>> authenticationPlugins = null;
  
  private List<String> disabledAuthenticationPlugins = null;
  
  private String clientDefaultAuthenticationPlugin = null;
  
  private String clientDefaultAuthenticationPluginName = null;
  
  private String serverDefaultAuthenticationPluginName = null;
  
  private void loadAuthenticationPlugins() {
    this.clientDefaultAuthenticationPlugin = (String)this.propertySet.getStringProperty(PropertyKey.defaultAuthenticationPlugin).getValue();
    if (this.clientDefaultAuthenticationPlugin == null || "".equals(this.clientDefaultAuthenticationPlugin.trim()))
      throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, 
          Messages.getString("AuthenticationProvider.BadDefaultAuthenticationPlugin", new Object[] { this.clientDefaultAuthenticationPlugin }), getExceptionInterceptor()); 
    String disabledPlugins = (String)this.propertySet.getStringProperty(PropertyKey.disabledAuthenticationPlugins).getValue();
    if (disabledPlugins != null && !"".equals(disabledPlugins)) {
      this.disabledAuthenticationPlugins = new ArrayList<>();
      List<String> pluginsToDisable = StringUtils.split(disabledPlugins, ",", true);
      Iterator<String> iter = pluginsToDisable.iterator();
      while (iter.hasNext())
        this.disabledAuthenticationPlugins.add(iter.next()); 
    } 
    this.authenticationPlugins = new HashMap<>();
    boolean defaultIsFound = false;
    List<AuthenticationPlugin<NativePacketPayload>> pluginsToInit = new LinkedList<>();
    pluginsToInit.add(new MysqlNativePasswordPlugin());
    pluginsToInit.add(new MysqlClearPasswordPlugin());
    pluginsToInit.add(new Sha256PasswordPlugin());
    pluginsToInit.add(new CachingSha2PasswordPlugin());
    pluginsToInit.add(new MysqlOldPasswordPlugin());
    String authenticationPluginClasses = (String)this.propertySet.getStringProperty(PropertyKey.authenticationPlugins).getValue();
    if (authenticationPluginClasses != null && !"".equals(authenticationPluginClasses)) {
      List<String> pluginsToCreate = StringUtils.split(authenticationPluginClasses, ",", true);
      String className = null;
      try {
        for (int i = 0, s = pluginsToCreate.size(); i < s; i++) {
          className = pluginsToCreate.get(i);
          pluginsToInit.add((AuthenticationPlugin<NativePacketPayload>)Class.forName(className).newInstance());
        } 
      } catch (Throwable t) {
        throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, 
            Messages.getString("AuthenticationProvider.BadAuthenticationPlugin", new Object[] { className }), t, this.exceptionInterceptor);
      } 
    } 
    for (AuthenticationPlugin<NativePacketPayload> plugin : pluginsToInit) {
      plugin.init(this.protocol);
      if (addAuthenticationPlugin(plugin))
        defaultIsFound = true; 
    } 
    if (!defaultIsFound)
      throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, 
          Messages.getString("AuthenticationProvider.DefaultAuthenticationPluginIsNotListed", new Object[] { this.clientDefaultAuthenticationPlugin }), getExceptionInterceptor()); 
  }
  
  private boolean addAuthenticationPlugin(AuthenticationPlugin<NativePacketPayload> plugin) {
    boolean isDefault = false;
    String pluginClassName = plugin.getClass().getName();
    String pluginProtocolName = plugin.getProtocolPluginName();
    boolean disabledByClassName = (this.disabledAuthenticationPlugins != null && this.disabledAuthenticationPlugins.contains(pluginClassName));
    boolean disabledByMechanism = (this.disabledAuthenticationPlugins != null && this.disabledAuthenticationPlugins.contains(pluginProtocolName));
    if (disabledByClassName || disabledByMechanism) {
      if (this.clientDefaultAuthenticationPlugin.equals(pluginClassName))
        throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, 
            Messages.getString("AuthenticationProvider.BadDisabledAuthenticationPlugin", new Object[] { disabledByClassName ? pluginClassName : pluginProtocolName }), getExceptionInterceptor()); 
    } else {
      this.authenticationPlugins.put(pluginProtocolName, plugin);
      if (this.clientDefaultAuthenticationPlugin.equals(pluginClassName)) {
        this.clientDefaultAuthenticationPluginName = pluginProtocolName;
        isDefault = true;
      } 
    } 
    return isDefault;
  }
  
  private AuthenticationPlugin<NativePacketPayload> getAuthenticationPlugin(String pluginName) {
    AuthenticationPlugin<NativePacketPayload> plugin = this.authenticationPlugins.get(pluginName);
    if (plugin != null && !plugin.isReusable())
      try {
        plugin = (AuthenticationPlugin<NativePacketPayload>)plugin.getClass().newInstance();
        plugin.init(this.protocol);
      } catch (Throwable t) {
        throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, 
            Messages.getString("AuthenticationProvider.BadAuthenticationPlugin", new Object[] { plugin.getClass().getName() }), t, 
            getExceptionInterceptor());
      }  
    return plugin;
  }
  
  private void checkConfidentiality(AuthenticationPlugin<?> plugin) {
    if (plugin.requiresConfidentiality() && !this.protocol.getSocketConnection().isSSLEstablished())
      throw ExceptionFactory.createException(
          Messages.getString("AuthenticationProvider.AuthenticationPluginRequiresSSL", new Object[] { plugin.getProtocolPluginName() }), getExceptionInterceptor()); 
  }
  
  private void proceedHandshakeWithPluggableAuthentication(ServerSession sessState, String user, String password, String database, NativePacketPayload challenge) {
    if (this.authenticationPlugins == null)
      loadAuthenticationPlugins(); 
    boolean skipPassword = false;
    int passwordLength = 16;
    int userLength = (user != null) ? user.length() : 0;
    int databaseLength = (database != null) ? database.length() : 0;
    int packLength = (userLength + passwordLength + databaseLength) * 3 + 7 + 33;
    long clientParam = sessState.getClientParam();
    int serverCapabilities = sessState.getCapabilities().getCapabilityFlags();
    AuthenticationPlugin<NativePacketPayload> plugin = null;
    NativePacketPayload fromServer = null;
    ArrayList<NativePacketPayload> toServer = new ArrayList<>();
    boolean done = false;
    NativePacketPayload last_sent = null;
    boolean old_raw_challenge = false;
    int counter = 100;
    while (0 < counter--) {
      if (!done) {
        if (challenge != null) {
          if (challenge.isOKPacket())
            throw ExceptionFactory.createException(
                Messages.getString("AuthenticationProvider.UnexpectedAuthenticationApproval", new Object[] { plugin.getProtocolPluginName() }), getExceptionInterceptor()); 
          clientParam |= 0xEA201L;
          if (((Boolean)this.propertySet.getBooleanProperty(PropertyKey.allowMultiQueries).getValue()).booleanValue())
            clientParam |= 0x10000L; 
          if ((serverCapabilities & 0x400000) != 0 && 
            !((Boolean)this.propertySet.getBooleanProperty(PropertyKey.disconnectOnExpiredPasswords).getValue()).booleanValue())
            clientParam |= 0x400000L; 
          if ((serverCapabilities & 0x100000) != 0 && 
            !"none".equals(this.propertySet.getStringProperty(PropertyKey.connectionAttributes).getValue()))
            clientParam |= 0x100000L; 
          if ((serverCapabilities & 0x200000) != 0)
            clientParam |= 0x200000L; 
          sessState.setClientParam(clientParam);
          if ((serverCapabilities & 0x800) != 0 && this.propertySet
            .getEnumProperty(PropertyKey.sslMode).getValue() != PropertyDefinitions.SslMode.DISABLED)
            negotiateSSLConnection(packLength); 
          String pluginName = null;
          if ((serverCapabilities & 0x80000) != 0)
            if (!this.protocol.versionMeetsMinimum(5, 5, 10) || (this.protocol
              .versionMeetsMinimum(5, 6, 0) && !this.protocol.versionMeetsMinimum(5, 6, 2))) {
              pluginName = challenge.readString(NativeConstants.StringLengthDataType.STRING_FIXED, "ASCII", ((NativeCapabilities)sessState
                  .getCapabilities()).getAuthPluginDataLength());
            } else {
              pluginName = challenge.readString(NativeConstants.StringSelfDataType.STRING_TERM, "ASCII");
            }  
          plugin = getAuthenticationPlugin(pluginName);
          if (plugin == null) {
            plugin = getAuthenticationPlugin(this.clientDefaultAuthenticationPluginName);
          } else if (pluginName.equals(Sha256PasswordPlugin.PLUGIN_NAME) && !this.protocol.getSocketConnection().isSSLEstablished() && this.propertySet
            .getStringProperty(PropertyKey.serverRSAPublicKeyFile).getValue() == null && 
            !((Boolean)this.propertySet.getBooleanProperty(PropertyKey.allowPublicKeyRetrieval).getValue()).booleanValue()) {
            plugin = getAuthenticationPlugin(this.clientDefaultAuthenticationPluginName);
            skipPassword = !this.clientDefaultAuthenticationPluginName.equals(pluginName);
          } 
          this.serverDefaultAuthenticationPluginName = plugin.getProtocolPluginName();
          checkConfidentiality(plugin);
          fromServer = new NativePacketPayload(StringUtils.getBytes(this.seed));
        } else {
          plugin = getAuthenticationPlugin((this.serverDefaultAuthenticationPluginName == null) ? this.clientDefaultAuthenticationPluginName : this.serverDefaultAuthenticationPluginName);
          checkConfidentiality(plugin);
          fromServer = new NativePacketPayload(StringUtils.getBytes(this.seed));
        } 
      } else {
        challenge = (NativePacketPayload)this.protocol.checkErrorMessage();
        old_raw_challenge = false;
        if (plugin == null)
          plugin = getAuthenticationPlugin((this.serverDefaultAuthenticationPluginName == null) ? this.clientDefaultAuthenticationPluginName : this.serverDefaultAuthenticationPluginName); 
        if (challenge.isOKPacket()) {
          OkPacket ok = OkPacket.parse(challenge, null);
          sessState.setStatusFlags(ok.getStatusFlags(), true);
          plugin.destroy();
          break;
        } 
        if (challenge.isAuthMethodSwitchRequestPacket()) {
          skipPassword = false;
          String pluginName = challenge.readString(NativeConstants.StringSelfDataType.STRING_TERM, "ASCII");
          if (!plugin.getProtocolPluginName().equals(pluginName)) {
            plugin.destroy();
            plugin = getAuthenticationPlugin(pluginName);
            if (plugin == null)
              throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, 
                  Messages.getString("AuthenticationProvider.BadAuthenticationPlugin", new Object[] { pluginName }), getExceptionInterceptor()); 
          } else {
            plugin.reset();
          } 
          checkConfidentiality(plugin);
          fromServer = new NativePacketPayload(StringUtils.getBytes(challenge.readString(NativeConstants.StringSelfDataType.STRING_TERM, "ASCII")));
        } else {
          if (!this.protocol.versionMeetsMinimum(5, 5, 16)) {
            old_raw_challenge = true;
            challenge.setPosition(challenge.getPosition() - 1);
          } 
          fromServer = new NativePacketPayload(challenge.readBytes(NativeConstants.StringSelfDataType.STRING_EOF));
        } 
      } 
      plugin.setAuthenticationParameters(user, skipPassword ? null : password);
      done = plugin.nextAuthenticationStep(fromServer, toServer);
      if (toServer.size() > 0) {
        if (challenge == null) {
          String str = getEncodingForHandshake();
          last_sent = new NativePacketPayload(packLength + 1);
          last_sent.writeInteger(NativeConstants.IntegerDataType.INT1, 17L);
          last_sent.writeBytes(NativeConstants.StringSelfDataType.STRING_TERM, StringUtils.getBytes(user, str));
          if (((NativePacketPayload)toServer.get(0)).getPayloadLength() < 256) {
            last_sent.writeInteger(NativeConstants.IntegerDataType.INT1, ((NativePacketPayload)toServer.get(0)).getPayloadLength());
            last_sent.writeBytes(NativeConstants.StringSelfDataType.STRING_EOF, ((NativePacketPayload)toServer.get(0)).getByteBuffer(), 0, ((NativePacketPayload)toServer.get(0)).getPayloadLength());
          } else {
            last_sent.writeInteger(NativeConstants.IntegerDataType.INT1, 0L);
          } 
          if (this.useConnectWithDb) {
            last_sent.writeBytes(NativeConstants.StringSelfDataType.STRING_TERM, StringUtils.getBytes(database, str));
          } else {
            last_sent.writeInteger(NativeConstants.IntegerDataType.INT1, 0L);
          } 
          last_sent.writeInteger(NativeConstants.IntegerDataType.INT1, 
              AuthenticationProvider.getCharsetForHandshake(str, sessState.getCapabilities().getServerVersion()));
          last_sent.writeInteger(NativeConstants.IntegerDataType.INT1, 0L);
          if ((serverCapabilities & 0x80000) != 0)
            last_sent.writeBytes(NativeConstants.StringSelfDataType.STRING_TERM, StringUtils.getBytes(plugin.getProtocolPluginName(), str)); 
          if ((clientParam & 0x100000L) != 0L)
            appendConnectionAttributes(last_sent, (String)this.propertySet.getStringProperty(PropertyKey.connectionAttributes).getValue(), str); 
          this.protocol.send(last_sent, last_sent.getPosition());
          continue;
        } 
        if (challenge.isAuthMethodSwitchRequestPacket()) {
          this.protocol.send(toServer.get(0), ((NativePacketPayload)toServer.get(0)).getPayloadLength());
          continue;
        } 
        if (challenge.isAuthMoreData() || old_raw_challenge) {
          for (NativePacketPayload buffer : toServer)
            this.protocol.send(buffer, buffer.getPayloadLength()); 
          continue;
        } 
        String enc = getEncodingForHandshake();
        last_sent = new NativePacketPayload(packLength);
        last_sent.writeInteger(NativeConstants.IntegerDataType.INT4, clientParam);
        last_sent.writeInteger(NativeConstants.IntegerDataType.INT4, 16777215L);
        last_sent.writeInteger(NativeConstants.IntegerDataType.INT1, 
            AuthenticationProvider.getCharsetForHandshake(enc, sessState.getCapabilities().getServerVersion()));
        last_sent.writeBytes(NativeConstants.StringLengthDataType.STRING_FIXED, new byte[23]);
        last_sent.writeBytes(NativeConstants.StringSelfDataType.STRING_TERM, StringUtils.getBytes(user, enc));
        if ((serverCapabilities & 0x200000) != 0) {
          last_sent.writeBytes(NativeConstants.StringSelfDataType.STRING_LENENC, ((NativePacketPayload)toServer.get(0)).readBytes(NativeConstants.StringSelfDataType.STRING_EOF));
        } else {
          last_sent.writeInteger(NativeConstants.IntegerDataType.INT1, ((NativePacketPayload)toServer.get(0)).getPayloadLength());
          last_sent.writeBytes(NativeConstants.StringSelfDataType.STRING_EOF, ((NativePacketPayload)toServer.get(0)).getByteBuffer());
        } 
        if (this.useConnectWithDb)
          last_sent.writeBytes(NativeConstants.StringSelfDataType.STRING_TERM, StringUtils.getBytes(database, enc)); 
        if ((serverCapabilities & 0x80000) != 0)
          last_sent.writeBytes(NativeConstants.StringSelfDataType.STRING_TERM, StringUtils.getBytes(plugin.getProtocolPluginName(), enc)); 
        if ((clientParam & 0x100000L) != 0L)
          appendConnectionAttributes(last_sent, (String)this.propertySet.getStringProperty(PropertyKey.connectionAttributes).getValue(), enc); 
        this.protocol.send(last_sent, last_sent.getPosition());
      } 
    } 
    if (counter == 0)
      throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, 
          Messages.getString("CommunicationsException.TooManyAuthenticationPluginNegotiations"), getExceptionInterceptor()); 
    this.protocol.afterHandshake();
    if (!this.useConnectWithDb)
      this.protocol.changeDatabase(database); 
  }
  
  private Map<String, String> getConnectionAttributesMap(String attStr) {
    Map<String, String> attMap = new HashMap<>();
    if (attStr != null) {
      String[] pairs = attStr.split(",");
      for (String pair : pairs) {
        int keyEnd = pair.indexOf(":");
        if (keyEnd > 0 && keyEnd + 1 < pair.length())
          attMap.put(pair.substring(0, keyEnd), pair.substring(keyEnd + 1)); 
      } 
    } 
    attMap.put("_client_name", "MySQL Connector/J");
    attMap.put("_client_version", "8.0.19");
    attMap.put("_runtime_vendor", Constants.JVM_VENDOR);
    attMap.put("_runtime_version", Constants.JVM_VERSION);
    attMap.put("_client_license", "GPL");
    return attMap;
  }
  
  private void appendConnectionAttributes(NativePacketPayload buf, String attributes, String enc) {
    NativePacketPayload lb = new NativePacketPayload(100);
    Map<String, String> attMap = getConnectionAttributesMap(attributes);
    for (String key : attMap.keySet()) {
      lb.writeBytes(NativeConstants.StringSelfDataType.STRING_LENENC, StringUtils.getBytes(key, enc));
      lb.writeBytes(NativeConstants.StringSelfDataType.STRING_LENENC, StringUtils.getBytes(attMap.get(key), enc));
    } 
    buf.writeInteger(NativeConstants.IntegerDataType.INT_LENENC, lb.getPosition());
    buf.writeBytes(NativeConstants.StringLengthDataType.STRING_FIXED, lb.getByteBuffer(), 0, lb.getPosition());
  }
  
  public String getEncodingForHandshake() {
    String enc = (String)this.propertySet.getStringProperty(PropertyKey.characterEncoding).getValue();
    if (enc == null)
      enc = "UTF-8"; 
    return enc;
  }
  
  public ExceptionInterceptor getExceptionInterceptor() {
    return this.exceptionInterceptor;
  }
  
  private void negotiateSSLConnection(int packLength) {
    this.protocol.negotiateSSLConnection(packLength);
  }
  
  public void changeUser(ServerSession serverSession, String userName, String password, String database) {
    proceedHandshakeWithPluggableAuthentication(serverSession, userName, password, database, null);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\a\NativeAuthenticationProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */