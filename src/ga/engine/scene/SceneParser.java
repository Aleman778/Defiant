package ga.engine.scene;

import ga.engine.xml.XMLReader;
import java.io.File;
import org.xml.sax.Attributes;

public class SceneParser extends XMLReader {

    private int width, height;
    private GameObject root;
    
    @Override
    public void documentStart() {
    }

    @Override
    public void documentEnd() {
    }

    @Override
    public void nodeStart(String element, Attributes attri) {
    }

    @Override
    public void nodeEnd(String element, Attributes attri, String value) {
    }

    @Override
    public void parse(File xmlfile) {
        super.parse(xmlfile);
    }
    
    public GameObject getRoot() {
        return root;
    }
    
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
