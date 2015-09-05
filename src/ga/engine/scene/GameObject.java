package ga.engine.scene;

import ga.engine.rendering.Renderable;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;

public final class GameObject {

    public Transform transform;
    private final List<GameObject> children;
    private final List<GameComponent> components;
    
    private Renderable renderable;
    
    public GameObject(Transform transform) {
        this.transform = new Transform(this, transform);
        this.children = new ArrayList<>();
        this.components = new ArrayList<>();
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
    
    public Transform getTransform() {
        return transform;
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
            node.getTransforms().clear();
            Rotate rx = new Rotate(transform.rotation.dX, 0, 0, 0, Rotate.X_AXIS);
            Rotate ry = new Rotate(transform.rotation.dY, 0, 0, 0, Rotate.Y_AXIS);
            Rotate rz = new Rotate(transform.rotation.dZ, 0, 0, 0, Rotate.Z_AXIS);
            node.getTransforms().addAll(rx, ry, rz);
            node.setTranslateX(transform.position.dX);
            node.setTranslateY(transform.position.dY);
            node.setTranslateZ(transform.position.dZ);
            node.setScaleX(transform.scale.dX);
            node.setScaleY(transform.scale.dY);
            node.setScaleZ(transform.scale.dZ);
        }
            
        for (GameObject child: children) {
            child.render(group);
        }
    }
}
