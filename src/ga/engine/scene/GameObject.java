package ga.engine.scene;

import ga.engine.rendering.Renderable;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;

public class GameObject {

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
    
    public GameObject addComponent(GameComponent component) {
        components.add(component);
        return this;
    }
    
    public void update() {
        for (GameComponent component: components) {
            component.update();
        }
        
        for (GameObject child: children) {
            child.update();
        }
    }
    
    public void render(Camera camera) {
        if (renderable != null) {
            renderable.render(camera);
            Node node = renderable.getNode();
            node.setTranslateX(x);
            node.setTranslateY(y);
        }
            
        for (GameObject child: children) {
            child.render(camera);
        }
    }
}
