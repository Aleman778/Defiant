package ga.engine.blueprint;

import ga.devkit.editor.EditorObject;
import ga.engine.scene.GameComponent;
import ga.engine.scene.GameObject;
import ga.engine.scene.Transform2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class Blueprint {
    
    private List<GameComponent> components;
    private Map<String, String> attributes;

    public Blueprint(List<GameComponent> components, Map<String, String> attributes) {
        this.components = components;
        this.attributes = attributes;
    }
    
    public List<String> getAllAttributes() {
        List<String> result = new ArrayList<>();
        for (GameComponent comp: components) {
            result.addAll(comp.getAttributes());
        }
        return null;
    }
    
    public String getAttribute(String attri) {
        return attributes.get(attri);
    }
    
    public final GameObject instantiate(Transform2D transform) {
        GameObject result = new GameObject(transform);
        for (GameComponent component: components) {
            GameComponent instantiated = component.instantiate();
            instantiated.setAttributes(attributes);
            result.addComponent(instantiated);
        }
        
        return result;
    }
    
    public final EditorObject editorInstantiate() {
        return null;
        
    }
}
