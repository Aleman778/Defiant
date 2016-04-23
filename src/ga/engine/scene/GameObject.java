package ga.engine.scene;

import com.sun.javafx.geom.Rectangle;
import ga.engine.core.Application;
import ga.engine.physics.Body;
import ga.engine.physics.Vector2D;
import ga.engine.rendering.ParticleEmitter;
import ga.game.PlayerController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;

public class GameObject implements Comparator<GameObject> {

    public Transform2D transform;
    public GameObject parent = null;
    private Rectangle AABB = new Rectangle();
    private final String tag;
    private final List<GameObject> children;
    private final List<GameComponent> components;
    private final List<GameObject> objectsToAdd;
    private final List<GameObject> objectsToRemove;
    private final List<GameComponent> componentsToAdd;
    private Body body = null;
    private boolean prevCollide = false;
    private boolean isBeingDestroyed = false;

    public GameObject() {
        this.tag = "untagged";
        this.transform = new Transform2D(this);
        this.children = new ArrayList<>();
        this.components = new ArrayList<>();
        this.objectsToAdd = new ArrayList<>();
        this.objectsToRemove = new ArrayList<>();
        this.componentsToAdd = new ArrayList<>();
    }
    
    public GameObject(String tag) {
        this.tag = tag;
        this.transform = new Transform2D(this);
        this.children = new ArrayList<>();
        this.components = new ArrayList<>();
        this.objectsToAdd = new ArrayList<>();
        this.objectsToRemove = new ArrayList<>();
        this.componentsToAdd = new ArrayList<>();
    }

    public GameObject(String tag, Transform2D transform) {
        this.tag = tag;
        this.transform = new Transform2D(this, transform);
        this.children = new ArrayList<>();
        this.components = new ArrayList<>();
        this.objectsToAdd = new ArrayList<>();
        this.objectsToRemove = new ArrayList<>();
        this.componentsToAdd = new ArrayList<>();
    }

    public GameObject(String tag, double x, double y) {
        this.tag = tag;
        this.transform = new Transform2D(this, x, y);
        this.children = new ArrayList<>();
        this.components = new ArrayList<>();
        this.objectsToAdd = new ArrayList<>();
        this.objectsToRemove = new ArrayList<>();
        this.componentsToAdd = new ArrayList<>();
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

    public void addAllChildren(Collection<GameObject> objects) {
        for (GameObject object: objects) {
            addChild(object);
        }
    }
    
    public GameObject removeChild(GameObject object) {
        children.remove(object);
        return object;
    }
    
    public void clearChildren() {
        children.clear();
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

        if (component instanceof Body) {
            body = (Body) component;
        }

        components.add(component);
        return this;
    }

    public void addComponents(Collection<GameComponent> components) {
        for (GameComponent component: components) {
            addComponent(component);
        }
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

    public boolean isBody() {
        return (body != null);
    }

    public Body getBody() {
        return body;
    }

    public void fixedUpdate() {
        for (GameObject o : objectsToAdd) {
            addChild(o);
        }
        objectsToAdd.clear();
        for (GameObject o : objectsToRemove) {
            children.remove(o);
        }
        for (GameComponent c : componentsToAdd) {
            addComponent(c);
        }
        componentsToAdd.clear();
        objectsToAdd.clear();
        objectsToRemove.clear();
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
            body.setVelocity(body.getVelocity().add(body.gravity));
        }
        transform.translate(body.getVelocity().x, body.getVelocity().y);
        Iterator<Body> it = retrievedBodies.iterator();
        while (it.hasNext()) {
            Body physicsBody = it.next();

            if (Math.signum(physicsBody.getVelocity().normalize().x) != 0 && Math.abs(physicsBody.getVelocity().x) > 0.5 && physicsBody.gameobject.getComponent(PlayerController.class) == null) {
                physicsBody.transform.scale.x = Math.signum(physicsBody.getVelocity().normalize().x);
            }

            HashMap<String, Object> collision = body.physicsUpdate(physicsBody);
            if (collision != null) {
            Vector2D normal = (Vector2D) collision.get("normal");
                if (normal.equals(new Vector2D(0, 1)) && ((int) collision.get("id")) == 1) {
                    colliding = true;
                }
            }
        }
        if (!colliding && !prevCollide) {
            body.setGrounded(false);
        }
        prevCollide = colliding;

        if (getComponent(ParticleEmitter.class) != null) {
            for (GameComponent emitter : getComponents(ParticleEmitter.class)) {
                ((ParticleEmitter) emitter).physicsUpdate(retrievedBodies);
            }

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

    public void render(GraphicsContext g) {
        Vector2D position = transform.localPosition();
        double rotation = transform.localRotation();
        Vector2D scale = transform.scale;
        g.save();
        Affine affine = new Affine();
        affine.appendTranslation((int) position.x, (int) position.y);
        affine.appendRotation(rotation, transform.pivot.x, transform.pivot.y);
        affine.appendScale(scale.x, scale.y, transform.pivot.x, transform.pivot.y);
        g.setTransform(affine);
        for (GameComponent component : components) {
            component.render(g);
        }
        g.restore();

        for (GameObject child : children) {
            child.render(g);
        }
    }

    public Transform2D getTransform() {
        return transform;
    }
    
    public String getTag() {
        return tag;
    }

    public void setAABB(int x, int y, int w, int h) {
        AABB = new Rectangle(x, y, w, h);
        transform.pivot = new Vector2D(w / 2, h / 2);
    }
    
    public void setAABB(Rectangle AABB) {
        this.AABB = AABB;
    }

    public Rectangle getAABB() {
        return AABB;
    }
    
    public Rectangle localAABB() {
        return new Rectangle((int) transform.position.x + AABB.x, (int) transform.position.y + AABB.y, AABB.width, AABB.height);
    }

    @Override
    public int compare(GameObject o1, GameObject o2) {
        return o2.transform.depth - o1.transform.depth;
    }
    
    public void queueObject(GameObject o) {
        objectsToAdd.add(o);
    }
    
    public void removeObject(GameObject o) {
        objectsToRemove.add(o);
    }
    
    public void destroy() {
        isBeingDestroyed = true;
        for (GameComponent component : components) {
            component.onDestroy();
        }
        parent.removeObject(this);
    }
    
    public boolean isBeingDestroyed() {
        return isBeingDestroyed;
    }
    
    public void queueComponent(GameComponent c) {
        componentsToAdd.add(c);
    }
    
    public static GameObject findObjectWithTag(String tag) {
        GameScene scene = Application.getScene();
        if (scene != null) {
            List<GameObject> objects = scene.getAllGameObjects();
            for (GameObject object: objects) {
                if (object.getTag().equals(tag)) {
                    return object;
                }
            }
        }
        return null;
    }
    
    public static List<GameObject> findObjectsWithTag(String tag) {
        GameScene scene = Application.getScene();
        List<GameObject> result = new ArrayList<>();
        if (scene != null) {
            List<GameObject> objects = scene.getAllGameObjects();
            for (GameObject object: objects) {
                if (object.getTag().equals(tag)) {
                    result.add(object);
                }
            }
        }
        return result;
    }
}
