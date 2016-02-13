package ga.engine.blueprint;

import ga.devkit.editor.EditorObject;
import ga.engine.physics.Vector2D;
import ga.engine.scene.GameComponent;
import ga.engine.scene.GameObject;
import ga.engine.scene.Transform2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class Blueprint {
    
    private final Vector2D pivot;
    private final List<GameComponent> components;
    private final Map<String, String> attributes;

    public Blueprint(Vector2D pivot, List<GameComponent> components, Map<String, String> attributes) {
        this.pivot = pivot;
        this.components = components;
        this.attributes = attributes;
    }
    
    public List<String> getAllAttributes() {
        List<String> result = new ArrayList<>();
        for (GameComponent comp: components) {
            result.addAll(comp.getAttributes());
        }
        return result;
    }
    
    public String getAttribute(String attri) {
        return attributes.get(attri);
    }
    
    public final GameObject instantiate(String tag, Transform2D transform) {
        transform.pivot = pivot;
        GameObject result = new GameObject(tag, transform);
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
