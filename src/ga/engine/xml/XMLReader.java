package ga.engine.xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <b>XML Reader</b><br>
 * <i>added in version 1.0</i><br><br>
 * Fast and simple API for reading XML files, it is based on
 * SAX (<b>S</b>imple <b>A</b>PI for <b>X</b>ML)<br><br>
 * <b><u>Information about SAX:</u></b><br>
 * "SAX is an event-driven online algorithm for parsing XML documents"<br>
 * <i>- Wikipedia</i><br><br>
 * SAX only allows you to read XML documents, if you want to write a XML document
 * then use the <b>XML Writer</b> which is based on DOM (<b>D</b>ocument <b>O</b>bject <b>M</b>odel)<br><br>
 * <b><u>Implement the XML Reader:</u></b><br>
 * <ol>
 *      <li>Instantiate the XMLReader class</li>
 *      <li>Set up the parser (More information about parsing can be found below)</li>
 *      <li>Start parsing by calling XMLReader.parse(String xmlfile);</li>
 * </ol>
 * <b><u>Parsing a XML Document:</u></b><br>
 * Since this is an event-driven algorithm it will parse chronologically you
 * wont have all the data read when the parser reaches a new element.<br><br>
 * <i>You have four different events:</i><br>
 * <ul>
 *      <li><i>documentStart()</i> - This event is runned when the parsing has begun.</li>
 *      <li><i>documentEnd()</i> - This event is runned when the parsing has finished.</li>
 *      <li><i>nodeStart(String element, Attributes attri)</i> - This event is runned when an element is found this method will be called and gives you the name and attributes for that element.</li>
 *      <li><i>nodeEnd(String element, Attributes attri, String value)</i> - This event is runned when an element has ended and it will give you all the data about that element.</li>
 * </ul>
 */
public abstract class XMLReader implements ErrorHandler {

    private SAXParser parser;
    private DefaultHandler handler;
    private SAXParserFactory saxFactory;
    private List<Element> children;
    
    /**
     * <b>Constructor</b><br>
     * <i>added in version 1.0</i><br><br>
     * <b><u>Parsing a XML Document:</u></b><br>
     * Since this is an event-driven algorithm it will parse chronologically you
     * wont have all the data read when the parser reaches a new element.<br><br>
     * <i>You have four different events:</i><br>
     * <ul>
     *      <li><i>documentStart()</i> - This event is runned when the parsing has begun.</li>
     *      <li><i>documentEnd()</i> - This event is runned when the parsing has finished.</li>
     *      <li><i>nodeStart(String element, Attributes attri)</i> - This event is runned when an element is found this method will be called and gives you the name and attributes for that element.</li>
     *      <li><i>nodeEnd(String element, Attributes attri, String value)</i> - This event is runned when an element has ended and it will give you all the data about that element.</li>
     * </ul>
     */
    public XMLReader() {
        try {
            children = new ArrayList<>();
            saxFactory = SAXParserFactory.newInstance();
            saxFactory.setValidating(true);
            parser = saxFactory.newSAXParser();
            handler = new DefaultHandler() {
                
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    children.add(new Element(qName, attributes));
                    nodeStart(qName, attributes);
                }
                
                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    Element element = children.get(children.size() - 1);
                    nodeEnd(element.name, element.attri, element.value);
                    children.remove(children.size() - 1);
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    if (children.size() > 0) {
                        children.get(children.size() - 1).value += (new String(ch, start, length));
                    }
                }

                @Override
                public void startDocument() throws SAXException {
                    documentStart();
                }
                
                @Override
                public void endDocument() throws SAXException {
                    documentEnd();
                }

                @Override
                public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {

                }
            };
            
        } catch (ParserConfigurationException | SAXException | NullPointerException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * <b>Document Start</b><br>
     * <i>added in version 1.0</i><br><br>
     * This event is runned when the parsing has begun.
     */
    public abstract void documentStart();
    
    /**
     * <b>Document End</b><br>
     * <i>added in version 1.0</i><br><br>
     * This event is runned when the parsing has finished.
     */
    public abstract void documentEnd();
    
    /**
     * <b>Node Start</b><br>
     * <i>added in version 1.0</i><br><br>
     * This event is runned when an element is found this method will be called and gives you the name and attributes for that element.
     * @param element the name of the currently found element
     * @param attri the element attributes
     */
    public abstract void nodeStart(String element, Attributes attri);
    
    /**
     * <b>Node End</b><br>
     * <i>added in version 1.0</i><br><br>
     * AddYourDocumentationHere
     * @param element the name of the currently closed element
     * @param attri the element attributes
     * @param value the content inside the element
     */
    public abstract void nodeEnd(String element, Attributes attri, String value);
    
    /**
     * <b>Parse</b><br>
     * <i>added in version 1.0</i><br><br>
     * Starts the parsing process and when it is done the next line will be executed.
     * @param xmlfile The XML document to parse
     */
    public void parse(String xmlfile) {
        try {
            parser.parse(getClass().getResource("/" + xmlfile).openStream(), handler);
        } catch (NullPointerException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * <b>Warning</b><br>
     * <i>added in version 1.0</i><br><br>
     * Parsing warning will be logged via the <b>Console</b>.
     * @param exception
     * @throws org.xml.sax.SAXException
     */
    @Override
    public void warning(SAXParseException exception) throws SAXException {
        exception.printStackTrace();
    }
    
    /**
     * <b>Warning</b><br>
     * <i>added in version 1.0</i><br><br>
     * Parsing error will be logged via the <b>Console</b>.
     * @param exception
     * @throws org.xml.sax.SAXException
     */
    @Override
    public void error(SAXParseException exception) throws SAXException {
        exception.printStackTrace();
    }
    
    /**
     * <b>Fatal Error</b><br>
     * <i>added in version 1.0</i><br><br>
     * Parsing error will be reported to the <b>Console</b>.
     * @param exception
     * @throws org.xml.sax.SAXException
     */
    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        exception.printStackTrace();
    }
    
    class Element {
        
        public String name;
        public Attributes attri;
        public String value;
        
        public Element(String name, Attributes attri) {
            this.name = name;
            this.attri = attri;
            this.value = "";
        }
    }
}
