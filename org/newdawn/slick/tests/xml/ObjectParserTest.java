package org.newdawn.slick.tests.xml;

import org.newdawn.slick.util.xml.ObjectTreeParser;
import org.newdawn.slick.util.xml.SlickXMLException;

public class ObjectParserTest {
  public static void main(String[] argv) throws SlickXMLException {
    ObjectTreeParser parser = new ObjectTreeParser("org.newdawn.slick.tests.xml");
    parser.addElementMapping("Bag", ItemContainer.class);
    GameData parsedData = (GameData)parser.parse("testdata/objxmltest.xml");
    parsedData.dump("");
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\tests\xml\ObjectParserTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */