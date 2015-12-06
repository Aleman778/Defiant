package ga.devkit.editor;

import ga.engine.scene.GameComponent;
import java.util.Map;

public class EditorComponent {

    private Map<String, String> variables;
    private final GameComponent component;
    
    public EditorComponent(String component) {
        this.component = GameComponent.get(component).instantiate();
    }

    public EditorComponent(GameComponent component) {
        this.component = component;
    }
    
    public GameComponent get() {
        return component;
    }
    
    public Map<String, String> getVars() {
        return variables;
    }
}
