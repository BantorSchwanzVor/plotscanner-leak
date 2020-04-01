package org.seltak.anubis.utils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;

public class ChatHelper {
  public static String getLastContaining(String contains) {
    try {
      List<Object> chatLines = getChat();
      for (Object line : chatLines) {
        String sLine = (String)line;
        if (sLine.contains(contains))
          return sLine; 
      } 
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return "";
  }
  
  public static String getLastStarting(String startsWith) {
    try {
      List<Object> chatLines = getChat();
      for (Object line : chatLines) {
        String sLine = (String)line;
        if (sLine.startsWith(startsWith))
          return sLine; 
      } 
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return "";
  }
  
  public static String getLastEnding(String endsWith) {
    try {
      List<Object> chatLines = getChat();
      for (Object line : chatLines) {
        String sLine = (String)line;
        if (sLine.endsWith(endsWith))
          return sLine; 
      } 
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return "";
  }
  
  public static List<Object> getChat() {
    try {
      return (List<Object>)((Minecraft.getMinecraft()).ingameGUI.getChatGUI()).drawnChatLines.stream().map(new Function<ChatLine, String>() {
            public String apply(ChatLine e) {
              return e.getChatComponent().getUnformattedText();
            }
          }).collect(Collectors.toList());
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubi\\utils\ChatHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */