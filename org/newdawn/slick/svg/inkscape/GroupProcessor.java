package org.newdawn.slick.svg.inkscape;

import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.svg.Diagram;
import org.newdawn.slick.svg.Loader;
import org.newdawn.slick.svg.ParsingException;
import org.w3c.dom.Element;

public class GroupProcessor implements ElementProcessor {
  public boolean handles(Element element) {
    if (element.getNodeName().equals("g"))
      return true; 
    return false;
  }
  
  public void process(Loader loader, Element element, Diagram diagram, Transform t) throws ParsingException {
    Transform transform = Util.getTransform(element);
    transform = new Transform(t, transform);
    loader.loadChildren(element, transform);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\svg\inkscape\GroupProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */