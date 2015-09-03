package ga.engine.scene;

import java.util.ArrayList;
import java.util.List;

public class GameObject {

    private final List<GameObject> children;
    private final List<GameComponent> components;
    
    public GameObject() {
        children = new ArrayList<>();
        components = new ArrayList<>();
    }
    
    public GameObject addChild(GameObject object) {
        children.add(object);
        return object;
    }
    
    public GameObject addComponent(GameComponent component) {
        components.add(component);
        return this;
    }
    
    public void update() {
        for (GameComponent component: components) {
            component.update();
        }
    }
    
    public void render() {
        for (GameComponent component: components) {
            component.render();
        }
    }
}
