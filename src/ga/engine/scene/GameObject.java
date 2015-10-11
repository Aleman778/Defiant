package ga.engine.scene;

import com.sun.javafx.geom.Rectangle;
import ga.engine.physics.Body;
import ga.engine.physics.Vector2D;
import ga.engine.physics.Vector3D;
import ga.engine.rendering.ImageRenderer;
import ga.engine.rendering.Renderable;
import ga.game.PlayerController;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;

public class GameObject {

    public Transform transform;
    public GameObject parent = null;
    private final List<GameObject> children;
    private final List<GameComponent> components;

    private Renderable renderable = null;
    private Body body = null;

    private HashSet<Renderable> subRenderers = new HashSet<>();

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
        if (object.parent != null) {
            return null;
        }

        object.parent = this;
        for (GameComponent component : object.getAllComponents()) {
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
        for (GameObject child : children) {
            child.getGameObjects(result);
        }
        return result;
    }

    public GameObject addComponent(GameComponent component) {
        if (component.gameobject != null) {
            return null;
        }

        component.gameobject = this;
        component.transform = transform;

        component.awoke();
        if (parent != null) {
            component.start();
        }

        if (component instanceof Renderable) {
            if (renderable == null) {
                renderable = (Renderable) component;
            }
            else {
                subRenderers.add((Renderable) component);
            }
        }

        if (component instanceof Body) {
            body = (Body) component;
        }

        components.add(component);
        return this;
    }

    public <T extends GameComponent> GameComponent getComponent(Class<T> component) {
        for (GameComponent comp : components) {
            if (comp.getClass().equals(component) || component.isInstance(comp)) {
                return comp;
            }
        }
        return null;
    }

    public <T extends GameComponent> Set<GameComponent> getComponents(Class<T> component) {
        Set<GameComponent> result = new HashSet<>();
        for (GameComponent comp : components) {
            if (comp.getClass().equals(component) || component.isInstance(comp)) {
                result.add(comp);
            }
        }
        return result;
    }

    public List<GameComponent> getAllComponents() {
        return components;
    }

    public boolean isRenderable() {
        return (renderable != null);
    }

    public Renderable getRenderable() {
        return renderable;
    }

    public boolean isBody() {
        return (body != null);
    }

    public Body getBody() {
        return body;
    }

    public void fixedUpdate() {
        for (GameComponent component : components) {
            component.fixedUpdate();
        }

        for (GameObject child : children) {
            child.fixedUpdate();
        }
    }

    public void physicsUpdate(Set<Body> retrievedBodies) {
        boolean colliding = false;
        if (body.getMass() != 0) {
            body.setVelocity(body.getVelocity().add(GameScene.gravity));
        }
        transform.translate(body.getVelocity().x, body.getVelocity().y, 0);
        Iterator<Body> it = retrievedBodies.iterator();
        while (it.hasNext()) {
            Body physicsBody = it.next();

            if (Math.signum(physicsBody.getVelocity().normalize().x) != 0 && Math.abs(physicsBody.getVelocity().x) > 0.5 && physicsBody.gameobject.getComponent(PlayerController.class) == null) {
                physicsBody.transform.rotation.y = Math.min(Math.signum(physicsBody.getVelocity().normalize().x) * 180, 0);
            }

            Vector2D normal = body.physicsUpdate(physicsBody);
            if (normal != null) {
                if (normal.equals(new Vector2D(0, 1))) {
                    colliding = true;
                }
            }

        }
        if (!colliding) {
            body.setGrounded(false);
        }
    }

    public void update() {
        for (GameComponent component : components) {
            component.update();
        }

        for (GameObject child : children) {
            child.update();
        }
    }

    public void lateUpdate() {
        for (GameComponent component : components) {
            component.lateUpdate();
        }

        for (GameObject child : children) {
            child.lateUpdate();
        }
    }

    public void render(Group group) {
        if (renderable != null) {
            renderable.render(group);
            Node node = renderable.getNode();
            node.getTransforms().clear();
            Vector3D rotation = transform.localRotation();
            Vector3D position = transform.localPosition();
            Rotate rx = new Rotate(rotation.x, ((ImageRenderer) renderable).getPivot().x, ((ImageRenderer) renderable).getPivot().y, 0, Rotate.X_AXIS);
            Rotate ry = new Rotate(rotation.y, ((ImageRenderer) renderable).getPivot().x, ((ImageRenderer) renderable).getPivot().y, 0, Rotate.Y_AXIS);
            Rotate rz = new Rotate(rotation.z, ((ImageRenderer) renderable).getPivot().x, ((ImageRenderer) renderable).getPivot().y, 0, Rotate.Z_AXIS);
            node.getTransforms().addAll(rx, ry, rz);
            node.setTranslateX((int) position.x);
            node.setTranslateY((int) position.y);
            node.setTranslateZ((int) position.z);
            node.setScaleX(transform.scale.x);
            node.setScaleY(transform.scale.y);
            node.setScaleZ(transform.scale.z);
            for (Renderable subRenderer : subRenderers) {
                subRenderer.render(group);
            }
        }

        for (GameObject child : children) {
            child.render(group);
        }
    }

    public Transform getTransform() {
        return transform;
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

    public Rectangle computeAABB() {
        if (isRenderable()) {
            return renderable.computeAABB();
        }
        return new Rectangle();
    }
}
