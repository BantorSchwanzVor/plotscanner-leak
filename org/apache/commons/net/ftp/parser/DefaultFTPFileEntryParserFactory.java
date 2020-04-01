package org.apache.commons.net.ftp.parser;

import java.util.Locale;
import java.util.regex.Pattern;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFileEntryParser;

public class DefaultFTPFileEntryParserFactory implements FTPFileEntryParserFactory {
  private static final String JAVA_IDENTIFIER = "\\p{javaJavaIdentifierStart}(\\p{javaJavaIdentifierPart})*";
  
  private static final String JAVA_QUALIFIED_NAME = "(\\p{javaJavaIdentifierStart}(\\p{javaJavaIdentifierPart})*\\.)+\\p{javaJavaIdentifierStart}(\\p{javaJavaIdentifierPart})*";
  
  private static final Pattern JAVA_QUALIFIED_NAME_PATTERN = Pattern.compile("(\\p{javaJavaIdentifierStart}(\\p{javaJavaIdentifierPart})*\\.)+\\p{javaJavaIdentifierStart}(\\p{javaJavaIdentifierPart})*");
  
  public FTPFileEntryParser createFileEntryParser(String key) {
    if (key == null)
      throw new ParserInitializationException("Parser key cannot be null"); 
    return createFileEntryParser(key, null);
  }
  
  private FTPFileEntryParser createFileEntryParser(String key, FTPClientConfig config) {
    UnixFTPEntryParser unixFTPEntryParser;
    FTPFileEntryParser parser = null;
    if (JAVA_QUALIFIED_NAME_PATTERN.matcher(key).matches())
      try {
        Class<?> parserClass = Class.forName(key);
        try {
          parser = (FTPFileEntryParser)parserClass.newInstance();
        } catch (ClassCastException e) {
          throw new ParserInitializationException(String.valueOf(parserClass.getName()) + 
              " does not implement the interface " + 
              "org.apache.commons.net.ftp.FTPFileEntryParser.", e);
        } catch (Exception e) {
          throw new ParserInitializationException("Error initializing parser", e);
        } catch (ExceptionInInitializerError e) {
          throw new ParserInitializationException("Error initializing parser", e);
        } 
      } catch (ClassNotFoundException classNotFoundException) {} 
    if (parser == null) {
      String ukey = key.toUpperCase(Locale.ENGLISH);
      if (ukey.indexOf("UNIX_LTRIM") >= 0) {
        unixFTPEntryParser = new UnixFTPEntryParser(config, true);
      } else if (ukey.indexOf("UNIX") >= 0) {
        unixFTPEntryParser = new UnixFTPEntryParser(config, false);
      } else if (ukey.indexOf("VMS") >= 0) {
        VMSVersioningFTPEntryParser vMSVersioningFTPEntryParser = new VMSVersioningFTPEntryParser(config);
      } else if (ukey.indexOf("WINDOWS") >= 0) {
        parser = createNTFTPEntryParser(config);
      } else if (ukey.indexOf("OS/2") >= 0) {
        OS2FTPEntryParser oS2FTPEntryParser = new OS2FTPEntryParser(config);
      } else if (ukey.indexOf("OS/400") >= 0 || 
        ukey.indexOf("AS/400") >= 0) {
        parser = createOS400FTPEntryParser(config);
      } else if (ukey.indexOf("MVS") >= 0) {
        MVSFTPEntryParser mVSFTPEntryParser = new MVSFTPEntryParser();
      } else if (ukey.indexOf("NETWARE") >= 0) {
        NetwareFTPEntryParser netwareFTPEntryParser = new NetwareFTPEntryParser(config);
      } else if (ukey.indexOf("MACOS PETER") >= 0) {
        MacOsPeterFTPEntryParser macOsPeterFTPEntryParser = new MacOsPeterFTPEntryParser(config);
      } else if (ukey.indexOf("TYPE: L8") >= 0) {
        unixFTPEntryParser = new UnixFTPEntryParser(config);
      } else {
        throw new ParserInitializationException("Unknown parser type: " + key);
      } 
    } 
    if (unixFTPEntryParser instanceof org.apache.commons.net.ftp.Configurable)
      unixFTPEntryParser.configure(config); 
    return (FTPFileEntryParser)unixFTPEntryParser;
  }
  
  public FTPFileEntryParser createFileEntryParser(FTPClientConfig config) throws ParserInitializationException {
    String key = config.getServerSystemKey();
    return createFileEntryParser(key, config);
  }
  
  public FTPFileEntryParser createUnixFTPEntryParser() {
    return (FTPFileEntryParser)new UnixFTPEntryParser();
  }
  
  public FTPFileEntryParser createVMSVersioningFTPEntryParser() {
    return (FTPFileEntryParser)new VMSVersioningFTPEntryParser();
  }
  
  public FTPFileEntryParser createNetwareFTPEntryParser() {
    return (FTPFileEntryParser)new NetwareFTPEntryParser();
  }
  
  public FTPFileEntryParser createNTFTPEntryParser() {
    return createNTFTPEntryParser(null);
  }
  
  private FTPFileEntryParser createNTFTPEntryParser(FTPClientConfig config) {
    if (config != null && "WINDOWS".equals(
        config.getServerSystemKey()))
      return (FTPFileEntryParser)new NTFTPEntryParser(config); 
    FTPClientConfig config2 = (config != null) ? new FTPClientConfig(config) : null;
    return (FTPFileEntryParser)new CompositeFileEntryParser(
        new FTPFileEntryParser[] { (FTPFileEntryParser)new NTFTPEntryParser(config), 
          (FTPFileEntryParser)new UnixFTPEntryParser(config2, (
            config2 != null && "UNIX_LTRIM".equals(config2.getServerSystemKey()))) });
  }
  
  public FTPFileEntryParser createOS2FTPEntryParser() {
    return (FTPFileEntryParser)new OS2FTPEntryParser();
  }
  
  public FTPFileEntryParser createOS400FTPEntryParser() {
    return createOS400FTPEntryParser(null);
  }
  
  private FTPFileEntryParser createOS400FTPEntryParser(FTPClientConfig config) {
    if (config != null && 
      "OS/400".equals(config.getServerSystemKey()))
      return (FTPFileEntryParser)new OS400FTPEntryParser(config); 
    FTPClientConfig config2 = (config != null) ? new FTPClientConfig(config) : null;
    return (FTPFileEntryParser)new CompositeFileEntryParser(
        new FTPFileEntryParser[] { (FTPFileEntryParser)new OS400FTPEntryParser(config), 
          (FTPFileEntryParser)new UnixFTPEntryParser(config2, (
            config2 != null && "UNIX_LTRIM".equals(config2.getServerSystemKey()))) });
  }
  
  public FTPFileEntryParser createMVSEntryParser() {
    return (FTPFileEntryParser)new MVSFTPEntryParser();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\ftp\parser\DefaultFTPFileEntryParserFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */