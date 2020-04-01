package org.newdawn.slick.svg.inkscape;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.svg.Diagram;
import org.newdawn.slick.svg.Figure;
import org.newdawn.slick.svg.Loader;
import org.newdawn.slick.svg.NonGeometricData;
import org.newdawn.slick.svg.ParsingException;
import org.w3c.dom.Element;

public class RectProcessor implements ElementProcessor {
  public void process(Loader loader, Element element, Diagram diagram, Transform t) throws ParsingException {
    Transform transform = Util.getTransform(element);
    transform = new Transform(t, transform);
    float width = Float.parseFloat(element.getAttribute("width"));
    float height = Float.parseFloat(element.getAttribute("height"));
    float x = Float.parseFloat(element.getAttribute("x"));
    float y = Float.parseFloat(element.getAttribute("y"));
    Rectangle rect = new Rectangle(x, y, width + 1.0F, height + 1.0F);
    Shape shape = rect.transform(transform);
    NonGeometricData data = Util.getNonGeometricData(element);
    data.addAttribute("width", width);
    data.addAttribute("height", height);
    data.addAttribute("x", x);
    data.addAttribute("y", y);
    diagram.addFigure(new Figure(3, shape, data, transform));
  }
  
  public boolean handles(Element element) {
    if (element.getNodeName().equals("rect"))
      return true; 
    return false;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\svg\inkscape\RectProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */