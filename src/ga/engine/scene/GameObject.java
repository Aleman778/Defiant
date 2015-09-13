package ga.engine.scene;

import ga.engine.physics.Body;
import ga.engine.physics.Vector3D;
import ga.engine.rendering.Renderable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;

public final class GameObject {

    public Transform transform;
    public GameObject parent = null;
    private final List<GameObject> children;
    private final List<GameComponent> components;
    
    private Renderable renderable = null;
    private Body body = null;
    
    public GameObject(Vector3D position, Vector3D rotation, Vector3D scale) {
        this.transform = new Transform(this, position, rotation, scale);
        this.children = new ArrayList<>();
        this.components = new ArrayList<>();
    }
    
    public GameObject(double x, double y, double z) {
        this(new Vector3D(x, y, z), new Vector3D(), new Vector3D(1, 1, 1));
    }
    
    public GameObject() {
        this(new Vector3D(), new Vector3D(), new Vector3D(1, 1, 1));
    }
    
    public GameObject addChild(GameObject object) {
        if (object.parent != null)
            return null;
        
        object.parent = this;
        for (GameComponent component: object.getAllComponents()) {
            component.start();
        }
        
        children.add(object);
        return object;
    }
    
    public List<GameObject> getChildren() {
        return children;
    }
    
    public GameObject getParent() {
        return parent;
    }
    
    public List<GameObject> getGameObjects(List<GameObject> result) {
        result.addAll(children);
        for (GameObject child: children) {
            child.getGameObjects(result);
        }
        return result;
    }
    
    public GameObject addComponent(GameComponent component) {
        if (component.gameobject != null)
            return null;
        
        component.awoke();
        if (parent != null)
            component.start();
        
        if (component instanceof Renderable) {
            renderable = (Renderable) component;
        }
        
        if (component instanceof Body) {
            body = (Body) component;
        }
        
        component.gameobject = this;
        component.transform = transform;
        components.add(component);
        return this;
    }
    
    public <T extends GameComponent> GameComponent getComponent(Class<T> component) {
        for (GameComponent comp: components) {
            if (comp.getClass().equals(component) || component.isInstance(comp))
                return comp;
        }
        return null;
    }
    
    public <T extends GameComponent> Set<GameComponent> getComponents(Class<T> component) {
        Set<GameComponent> result = new HashSet<>();
        for (GameComponent comp: components) {
            if (comp.getClass().equals(component) || component.isInstance(comp))
                result.add(comp);
        }
        return result;
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
            Vector3D rotation = transform.localRotation();
            Vector3D position = transform.localPosition();
            Rotate rx = new Rotate(rotation.x, 0, 0, 0, Rotate.X_AXIS);
            Rotate ry = new Rotate(rotation.y, 0, 0, 0, Rotate.Y_AXIS);
            Rotate rz = new Rotate(rotation.z, 0, 0, 0, Rotate.Z_AXIS);
            node.getTransforms().addAll(rx, ry, rz);
            node.setTranslateX((int) position.x);
            node.setTranslateY((int) position.y);
            node.setTranslateZ((int) position.z);
            node.setScaleX(transform.scale.x);
            node.setScaleY(transform.scale.y);
            node.setScaleZ(transform.scale.z);
        }
            
        for (GameObject child: children) {
            child.render(group);
        }
    }
    
    public void setTranslateX(double x) {
        transform.position.x = x;
    }
    
    public double getTranslateX() {
        return transform.position.x;
    }
    
    public void setTranslateY(double y) {
        transform.position.y = y;
    }
    
    public double getTranslateY() {
        return transform.position.y;
    }
    
    public void setTranslateZ(double z) {
        transform.position.z = z;
    }
    
    public double getTranslateZ() {
        return transform.position.z;
    }
}
