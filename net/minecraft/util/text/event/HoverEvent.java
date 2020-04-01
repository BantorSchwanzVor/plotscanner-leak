package net.minecraft.util.text.event;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.util.text.ITextComponent;

public class HoverEvent {
  private final Action action;
  
  private final ITextComponent value;
  
  public HoverEvent(Action actionIn, ITextComponent valueIn) {
    this.action = actionIn;
    this.value = valueIn;
  }
  
  public Action getAction() {
    return this.action;
  }
  
  public ITextComponent getValue() {
    return this.value;
  }
  
  public boolean equals(Object p_equals_1_) {
    if (this == p_equals_1_)
      return true; 
    if (p_equals_1_ != null && getClass() == p_equals_1_.getClass()) {
      HoverEvent hoverevent = (HoverEvent)p_equals_1_;
      if (this.action != hoverevent.action)
        return false; 
      if (this.value != null) {
        if (!this.value.equals(hoverevent.value))
          return false; 
      } else if (hoverevent.value != null) {
        return false;
      } 
      return true;
    } 
    return false;
  }
  
  public String toString() {
    return "HoverEvent{action=" + this.action + ", value='" + this.value + '\'' + '}';
  }
  
  public int hashCode() {
    int i = this.action.hashCode();
    i = 31 * i + ((this.value != null) ? this.value.hashCode() : 0);
    return i;
  }
  
  public enum Action {
    SHOW_TEXT("show_text", true),
    SHOW_ITEM("show_item", true),
    SHOW_ENTITY("show_entity", true);
    
    private static final Map<String, Action> NAME_MAPPING = Maps.newHashMap();
    
    private final boolean allowedInChat;
    
    private final String canonicalName;
    
    static {
      byte b;
      int i;
      Action[] arrayOfAction;
      for (i = (arrayOfAction = values()).length, b = 0; b < i; ) {
        Action hoverevent$action = arrayOfAction[b];
        NAME_MAPPING.put(hoverevent$action.getCanonicalName(), hoverevent$action);
        b++;
      } 
    }
    
    Action(String canonicalNameIn, boolean allowedInChatIn) {
      this.canonicalName = canonicalNameIn;
      this.allowedInChat = allowedInChatIn;
    }
    
    public boolean shouldAllowInChat() {
      return this.allowedInChat;
    }
    
    public String getCanonicalName() {
      return this.canonicalName;
    }
    
    public static Action getValueByCanonicalName(String canonicalNameIn) {
      return NAME_MAPPING.get(canonicalNameIn);
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\text\event\HoverEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */