package org.newdawn.slick.svg.inkscape;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.svg.Diagram;
import org.newdawn.slick.svg.Gradient;
import org.newdawn.slick.svg.Loader;
import org.newdawn.slick.svg.ParsingException;
import org.newdawn.slick.util.Log;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DefsProcessor implements ElementProcessor {
  public boolean handles(Element element) {
    if (element.getNodeName().equals("defs"))
      return true; 
    return false;
  }
  
  public void process(Loader loader, Element element, Diagram diagram, Transform transform) throws ParsingException {
    NodeList patterns = element.getElementsByTagName("pattern");
    for (int i = 0; i < patterns.getLength(); i++) {
      Element pattern = (Element)patterns.item(i);
      NodeList list = pattern.getElementsByTagName("image");
      if (list.getLength() == 0) {
        Log.warn("Pattern 1981 does not specify an image. Only image patterns are supported.");
      } else {
        Element image = (Element)list.item(0);
        String patternName = pattern.getAttribute("id");
        String ref = image.getAttributeNS("http://www.w3.org/1999/xlink", "href");
        diagram.addPatternDef(patternName, ref);
      } 
    } 
    NodeList linear = element.getElementsByTagName("linearGradient");
    ArrayList<Gradient> toResolve = new ArrayList();
    for (int j = 0; j < linear.getLength(); j++) {
      Element lin = (Element)linear.item(j);
      String name = lin.getAttribute("id");
      Gradient gradient = new Gradient(name, false);
      gradient.setTransform(Util.getTransform(lin, "gradientTransform"));
      if (stringLength(lin.getAttribute("x1")) > 0)
        gradient.setX1(Float.parseFloat(lin.getAttribute("x1"))); 
      if (stringLength(lin.getAttribute("x2")) > 0)
        gradient.setX2(Float.parseFloat(lin.getAttribute("x2"))); 
      if (stringLength(lin.getAttribute("y1")) > 0)
        gradient.setY1(Float.parseFloat(lin.getAttribute("y1"))); 
      if (stringLength(lin.getAttribute("y2")) > 0)
        gradient.setY2(Float.parseFloat(lin.getAttribute("y2"))); 
      String ref = lin.getAttributeNS("http://www.w3.org/1999/xlink", "href");
      if (stringLength(ref) > 0) {
        gradient.reference(ref.substring(1));
        toResolve.add(gradient);
      } else {
        NodeList steps = lin.getElementsByTagName("stop");
        for (int m = 0; m < steps.getLength(); m++) {
          Element s = (Element)steps.item(m);
          float offset = Float.parseFloat(s.getAttribute("offset"));
          String colInt = Util.extractStyle(s.getAttribute("style"), "stop-color");
          String opaInt = Util.extractStyle(s.getAttribute("style"), "stop-opacity");
          int col = Integer.parseInt(colInt.substring(1), 16);
          Color stopColor = new Color(col);
          stopColor.a = Float.parseFloat(opaInt);
          gradient.addStep(offset, stopColor);
        } 
        gradient.getImage();
      } 
      diagram.addGradient(name, gradient);
    } 
    NodeList radial = element.getElementsByTagName("radialGradient");
    int k;
    for (k = 0; k < radial.getLength(); k++) {
      Element rad = (Element)radial.item(k);
      String name = rad.getAttribute("id");
      Gradient gradient = new Gradient(name, true);
      gradient.setTransform(Util.getTransform(rad, "gradientTransform"));
      if (stringLength(rad.getAttribute("cx")) > 0)
        gradient.setX1(Float.parseFloat(rad.getAttribute("cx"))); 
      if (stringLength(rad.getAttribute("cy")) > 0)
        gradient.setY1(Float.parseFloat(rad.getAttribute("cy"))); 
      if (stringLength(rad.getAttribute("fx")) > 0)
        gradient.setX2(Float.parseFloat(rad.getAttribute("fx"))); 
      if (stringLength(rad.getAttribute("fy")) > 0)
        gradient.setY2(Float.parseFloat(rad.getAttribute("fy"))); 
      if (stringLength(rad.getAttribute("r")) > 0)
        gradient.setR(Float.parseFloat(rad.getAttribute("r"))); 
      String ref = rad.getAttributeNS("http://www.w3.org/1999/xlink", "href");
      if (stringLength(ref) > 0) {
        gradient.reference(ref.substring(1));
        toResolve.add(gradient);
      } else {
        NodeList steps = rad.getElementsByTagName("stop");
        for (int m = 0; m < steps.getLength(); m++) {
          Element s = (Element)steps.item(m);
          float offset = Float.parseFloat(s.getAttribute("offset"));
          String colInt = Util.extractStyle(s.getAttribute("style"), "stop-color");
          String opaInt = Util.extractStyle(s.getAttribute("style"), "stop-opacity");
          int col = Integer.parseInt(colInt.substring(1), 16);
          Color stopColor = new Color(col);
          stopColor.a = Float.parseFloat(opaInt);
          gradient.addStep(offset, stopColor);
        } 
        gradient.getImage();
      } 
      diagram.addGradient(name, gradient);
    } 
    for (k = 0; k < toResolve.size(); k++) {
      ((Gradient)toResolve.get(k)).resolve(diagram);
      ((Gradient)toResolve.get(k)).getImage();
    } 
  }
  
  private int stringLength(String value) {
    if (value == null)
      return 0; 
    return value.length();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\svg\inkscape\DefsProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */