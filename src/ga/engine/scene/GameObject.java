package ga.engine.scene;

import ga.engine.rendering.Renderable;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.Node;

public final class GameObject {

    public float x, y;
    
    private final List<GameObject> children;
    private final List<GameComponent> components;
    
    private Renderable renderable;
    
    public GameObject(float x, float y) {
        this.x = x;
        this.y = y;
        children = new ArrayList<>();
        components = new ArrayList<>();
    }
    
    public GameObject addChild(GameObject object) {
        children.add(object);
        return object;
    }
    
    public List<GameObject> getChildren() {
        return children;
    }
    
    public List<GameObject> getGameObjects(List<GameObject> result) {
        result.addAll(children);
        for (GameObject child: children) {
            child.getGameObjects(result);
        }
        return result;
    }
    
    public GameObject addComponent(GameComponent component) {
        if (component instanceof Renderable) {
            renderable = (Renderable) component;
        }
        
        components.add(component);
        return this;
    }
    
    public List<GameComponent> getAllComponents() {
        return components;
    }
    
    public void update() {
        for (GameComponent component: components) {
            component.update();
        }
        
        for (GameObject child: children) {
            child.update();
        }
    }
    
    public void render(Group group) {
        if (renderable != null) {
            renderable.render(group);
            Node node = renderable.getNode();
            node.setTranslateX(x);
            node.setTranslateY(y);
        }
            
        for (GameObject child: children) {
            child.render(group);
        }
    }
}