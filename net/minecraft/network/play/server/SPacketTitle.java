package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.Locale;
import javax.annotation.Nullable;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ITextComponent;

public class SPacketTitle implements Packet<INetHandlerPlayClient> {
  private Type type;
  
  private ITextComponent message;
  
  private int fadeInTime;
  
  private int displayTime;
  
  private int fadeOutTime;
  
  public SPacketTitle() {}
  
  public SPacketTitle(Type typeIn, ITextComponent messageIn) {
    this(typeIn, messageIn, -1, -1, -1);
  }
  
  public SPacketTitle(int fadeInTimeIn, int displayTimeIn, int fadeOutTimeIn) {
    this(Type.TIMES, null, fadeInTimeIn, displayTimeIn, fadeOutTimeIn);
  }
  
  public SPacketTitle(Type typeIn, @Nullable ITextComponent messageIn, int fadeInTimeIn, int displayTimeIn, int fadeOutTimeIn) {
    this.type = typeIn;
    this.message = messageIn;
    this.fadeInTime = fadeInTimeIn;
    this.displayTime = displayTimeIn;
    this.fadeOutTime = fadeOutTimeIn;
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.type = (Type)buf.readEnumValue(Type.class);
    if (this.type == Type.TITLE || this.type == Type.SUBTITLE || this.type == Type.ACTIONBAR)
      this.message = buf.readTextComponent(); 
    if (this.type == Type.TIMES) {
      this.fadeInTime = buf.readInt();
      this.displayTime = buf.readInt();
      this.fadeOutTime = buf.readInt();
    } 
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeEnumValue(this.type);
    if (this.type == Type.TITLE || this.type == Type.SUBTITLE || this.type == Type.ACTIONBAR)
      buf.writeTextComponent(this.message); 
    if (this.type == Type.TIMES) {
      buf.writeInt(this.fadeInTime);
      buf.writeInt(this.displayTime);
      buf.writeInt(this.fadeOutTime);
    } 
  }
  
  public void processPacket(INetHandlerPlayClient handler) {
    handler.handleTitle(this);
  }
  
  public Type getType() {
    return this.type;
  }
  
  public ITextComponent getMessage() {
    return this.message;
  }
  
  public int getFadeInTime() {
    return this.fadeInTime;
  }
  
  public int getDisplayTime() {
    return this.displayTime;
  }
  
  public int getFadeOutTime() {
    return this.fadeOutTime;
  }
  
  public enum Type {
    TITLE, SUBTITLE, ACTIONBAR, TIMES, CLEAR, RESET;
    
    public static Type byName(String name) {
      byte b;
      int i;
      Type[] arrayOfType;
      for (i = (arrayOfType = values()).length, b = 0; b < i; ) {
        Type spackettitle$type = arrayOfType[b];
        if (spackettitle$type.name().equalsIgnoreCase(name))
          return spackettitle$type; 
        b++;
      } 
      return TITLE;
    }
    
    public static String[] getNames() {
      String[] astring = new String[(values()).length];
      int i = 0;
      byte b;
      int j;
      Type[] arrayOfType;
      for (j = (arrayOfType = values()).length, b = 0; b < j; ) {
        Type spackettitle$type = arrayOfType[b];
        astring[i++] = spackettitle$type.name().toLowerCase(Locale.ROOT);
        b++;
      } 
      return astring;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\server\SPacketTitle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */