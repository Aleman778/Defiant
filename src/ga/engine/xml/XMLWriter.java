package ga.engine.xml;

import java.io.FileWriter;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLWriter {
    
    public Document document = null;
    
    public XMLWriter() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            document = docBuilder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    public Element createElement(String element, String... attributes) {
        Element result = document.createElement(element);
        for (int i = 1; i < attributes.length; i += 2) {
            result.setAttribute(attributes[i - 1], attributes[i]);
        }
        document.appendChild(result);
        return result;
    }
    
    public Element createElementValue(String element, String value, String... attributes) {
        Element result = document.createElement(element);
        result.appendChild(document.createTextNode(value));
        for (int i = 1; i < attributes.length; i += 2) {
            result.setAttribute(attributes[i - 1], attributes[i]);
        }
        document.getDocumentElement().appendChild(result);
        return result;
    }
    
    public Element createElement(Element parent, String element, String... attributes) {
        Element result = document.createElement(element);
        for (int i = 1; i < attributes.length; i += 2) {
            result.setAttribute(attributes[i - 1], attributes[i]);
        }
        parent.appendChild(result);
        return result;
    }
    
    public Element createElementValue(Element parent, String element, String value, String... attributes) {
        Element result = document.createElement(element);
        result.appendChild(document.createTextNode(value));
        for (int i = 1; i < attributes.length; i += 2) {
            result.setAttribute(attributes[i - 1], attributes[i]);
        }
        parent.appendChild(result);
        return result;
    }
    
    public void save(String filepath) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(new DOMSource(document), new StreamResult(new FileWriter("res/" + filepath)));
        } catch (TransformerException | IOException e) {
            e.printStackTrace();
        }
    }
}
