package org.apache.commons.net.examples;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.CodeSource;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class Main {
  public static void main(String[] args) throws Throwable {
    Properties fp = new Properties();
    InputStream ras = Main.class.getResourceAsStream("examples.properties");
    if (ras != null) {
      fp.load(ras);
    } else {
      System.err.println("[Cannot find examples.properties file, so aliases cannot be used]");
    } 
    if (args.length == 0) {
      if ((Thread.currentThread().getStackTrace()).length > 2) {
        System.out.println("Usage: mvn -q exec:java  -Dexec.arguments=<alias or exampleClass>,<exampleClass parameters> (comma-separated, no spaces)");
        System.out.println("Or   : mvn -q exec:java  -Dexec.args=\"<alias or exampleClass> <exampleClass parameters>\" (space separated)");
      } else if (fromJar()) {
        System.out.println(
            "Usage: java -jar commons-net-examples-m.n.jar <alias or exampleClass> <exampleClass parameters>");
      } else {
        System.out.println(
            "Usage: java -cp target/classes examples/Main <alias or exampleClass> <exampleClass parameters>");
      } 
      List<String> l = (List)Collections.list(fp.propertyNames());
      if (l.isEmpty())
        return; 
      Collections.sort(l);
      System.out.println("\nAliases and their classes:");
      for (String s : l) {
        System.out.printf("%-25s %s%n", new Object[] { s, fp.getProperty(s) });
      } 
      return;
    } 
    String shortName = args[0];
    String fullName = fp.getProperty(shortName);
    if (fullName == null)
      fullName = shortName; 
    fullName = fullName.replace('/', '.');
    try {
      Class<?> clazz = Class.forName(fullName);
      Method m = clazz.getDeclaredMethod("main", new Class[] { args.getClass() });
      String[] args2 = new String[args.length - 1];
      System.arraycopy(args, 1, args2, 0, args2.length);
      try {
        m.invoke(null, new Object[] { args2 });
      } catch (InvocationTargetException ite) {
        Throwable cause = ite.getCause();
        if (cause != null)
          throw cause; 
        throw ite;
      } 
    } catch (ClassNotFoundException e) {
      System.out.println(e);
    } 
  }
  
  private static boolean fromJar() {
    CodeSource codeSource = Main.class.getProtectionDomain().getCodeSource();
    if (codeSource != null)
      return codeSource.getLocation().getFile().endsWith(".jar"); 
    return false;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\examples\Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */