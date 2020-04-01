import java.util.Arrays;
import net.minecraft.client.main.Main;

public class Start {
  public static void main(String[] args) {
    Main.main(concat(new String[] { "--version", "mcp", "--accessToken", "0", "--assetsDir", "assets", "--assetIndex", "1.12", "--userProperties", "{}" }, args));
  }
  
  public static <T> T[] concat(Object[] first, Object[] second) {
    Object[] result = Arrays.copyOf(first, first.length + second.length);
    System.arraycopy(second, 0, result, first.length, second.length);
    return (T[])result;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\Start.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */