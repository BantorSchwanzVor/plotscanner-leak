package org.newdawn.slick.svg;

import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;

public class ParsingException extends SlickException {
  public ParsingException(String nodeID, String message, Throwable cause) {
    super("(" + nodeID + ") " + message, cause);
  }
  
  public ParsingException(Element element, String message, Throwable cause) {
    super("(" + element.getAttribute("id") + ") " + message, cause);
  }
  
  public ParsingException(String nodeID, String message) {
    super("(" + nodeID + ") " + message);
  }
  
  public ParsingException(Element element, String message) {
    super("(" + element.getAttribute("id") + ") " + message);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\svg\ParsingException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */