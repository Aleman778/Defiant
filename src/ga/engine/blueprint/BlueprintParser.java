package ga.engine.blueprint;

import ga.engine.scene.GameComponent;
import ga.engine.xml.XMLReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xml.sax.Attributes;

public class BlueprintParser extends XMLReader {

    private List<GameComponent> components;
    private Map<String, String> attributes;
    private Blueprint blueprint;
    
    private BlueprintParser(String filepath) {
        components = new ArrayList<>();
        attributes = new HashMap<>();
        parse(filepath);
        blueprint = new Blueprint(components, attributes);
    }
    
    public static Blueprint execute(String filepath) {
        BlueprintParser parser = new BlueprintParser(filepath);
        parser.parse(filepath);
        return parser.blueprint;
    }
    
    @Override
    public void documentStart() {
    }

    @Override
    public void documentEnd() {
    }

    @Override
    public void nodeStart(String element, Attributes attri) {
        switch (element) {
            case "component":
                GameComponent component = GameComponent.get(attri.getValue("type"));
                if (component != null) {
                    components.add(component);
                }
                break;
            case "attribute":
                String attribute = attri.getValue("type");
                String value = attri.getValue("value");
                if (attribute != null) {
                    attributes.put(attribute, value);
                }
                break;
        }
    }

    @Override
    public void nodeEnd(String element, Attributes attri, String value) {
    }
    
}
