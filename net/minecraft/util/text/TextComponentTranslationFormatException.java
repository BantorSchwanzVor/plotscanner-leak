package net.minecraft.util.text;

public class TextComponentTranslationFormatException extends IllegalArgumentException {
  public TextComponentTranslationFormatException(TextComponentTranslation component, String message) {
    super(String.format("Error parsing: %s: %s", new Object[] { component, message }));
  }
  
  public TextComponentTranslationFormatException(TextComponentTranslation component, int index) {
    super(String.format("Invalid index %d requested for %s", new Object[] { Integer.valueOf(index), component }));
  }
  
  public TextComponentTranslationFormatException(TextComponentTranslation component, Throwable cause) {
    super(String.format("Error while parsing: %s", new Object[] { component }), cause);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\text\TextComponentTranslationFormatException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */