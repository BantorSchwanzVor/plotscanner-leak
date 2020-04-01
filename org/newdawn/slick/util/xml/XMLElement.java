package org.newdawn.slick.util.xml;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

public class XMLElement {
  private Element dom;
  
  private XMLElementList children;
  
  private String name;
  
  XMLElement(Element xmlElement) {
    this.dom = xmlElement;
    this.name = this.dom.getTagName();
  }
  
  public String[] getAttributeNames() {
    NamedNodeMap map = this.dom.getAttributes();
    String[] names = new String[map.getLength()];
    for (int i = 0; i < names.length; i++)
      names[i] = map.item(i).getNodeName(); 
    return names;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getAttribute(String name) {
    return this.dom.getAttribute(name);
  }
  
  public String getAttribute(String name, String def) {
    String value = this.dom.getAttribute(name);
    if (value == null || value.length() == 0)
      return def; 
    return value;
  }
  
  public int getIntAttribute(String name) throws SlickXMLException {
    try {
      return Integer.parseInt(getAttribute(name));
    } catch (NumberFormatException e) {
      throw new SlickXMLException("Value read: '" + getAttribute(name) + "' is not an integer", e);
    } 
  }
  
  public int getIntAttribute(String name, int def) throws SlickXMLException {
    try {
      return Integer.parseInt(getAttribute(name, def));
    } catch (NumberFormatException e) {
      throw new SlickXMLException("Value read: '" + getAttribute(name, def) + "' is not an integer", e);
    } 
  }
  
  public double getDoubleAttribute(String name) throws SlickXMLException {
    try {
      return Double.parseDouble(getAttribute(name));
    } catch (NumberFormatException e) {
      throw new SlickXMLException("Value read: '" + getAttribute(name) + "' is not a double", e);
    } 
  }
  
  public double getDoubleAttribute(String name, double def) throws SlickXMLException {
    try {
      return Double.parseDouble(getAttribute(name, def));
    } catch (NumberFormatException e) {
      throw new SlickXMLException("Value read: '" + getAttribute(name, def) + "' is not a double", e);
    } 
  }
  
  public boolean getBooleanAttribute(String name) throws SlickXMLException {
    String value = getAttribute(name);
    if (value.equalsIgnoreCase("true"))
      return true; 
    if (value.equalsIgnoreCase("false"))
      return false; 
    throw new SlickXMLException("Value read: '" + getAttribute(name) + "' is not a boolean");
  }
  
  public boolean getBooleanAttribute(String name, boolean def) throws SlickXMLException {
    String value = getAttribute(name, def);
    if (value.equalsIgnoreCase("true"))
      return true; 
    if (value.equalsIgnoreCase("false"))
      return false; 
    throw new SlickXMLException("Value read: '" + getAttribute(name, def) + "' is not a boolean");
  }
  
  public String getContent() {
    String content = "";
    NodeList list = this.dom.getChildNodes();
    for (int i = 0; i < list.getLength(); i++) {
      if (list.item(i) instanceof org.w3c.dom.Text)
        content = String.valueOf(content) + list.item(i).getNodeValue(); 
    } 
    return content;
  }
  
  public XMLElementList getChildren() {
    if (this.children != null)
      return this.children; 
    NodeList list = this.dom.getChildNodes();
    this.children = new XMLElementList();
    for (int i = 0; i < list.getLength(); i++) {
      if (list.item(i) instanceof Element)
        this.children.add(new XMLElement((Element)list.item(i))); 
    } 
    return this.children;
  }
  
  public XMLElementList getChildrenByName(String name) {
    XMLElementList selected = new XMLElementList();
    XMLElementList children = getChildren();
    for (int i = 0; i < children.size(); i++) {
      if (children.get(i).getName().equals(name))
        selected.add(children.get(i)); 
    } 
    return selected;
  }
  
  public String toString() {
    String value = "[XML " + getName();
    String[] attrs = getAttributeNames();
    for (int i = 0; i < attrs.length; i++)
      value = String.valueOf(value) + " " + attrs[i] + "=" + getAttribute(attrs[i]); 
    value = String.valueOf(value) + "]";
    return value;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slic\\util\xml\XMLElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */