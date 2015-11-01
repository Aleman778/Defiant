package ga.engine.scene;

import com.sun.javafx.geom.Rectangle;
import ga.engine.physics.Body;
import ga.engine.physics.Vector2D;
import ga.engine.rendering.ParticleEmitter;
import ga.game.PlayerController;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;

public class GameObject {

    public Transform2D transform;
    public GameObject parent = null;
    private Rectangle AABB = new Rectangle();
    private final List<GameObject> children;
    private final List<GameComponent> components;
    private Body body = null;

    public GameObject() {
        this.transform = new Transform2D(this);
        this.children = new ArrayList<>();
        this.components = new ArrayList<>();
    }

    public GameObject(Transform2D transform) {
        this.transform = new Transform2D(this, transform);
        this.children = new ArrayList<>();
        this.components = new ArrayList<>();
    }

    public GameObject(double x, double y) {
        this.transform = new Transform2D(this, x, y);
        this.children = new ArrayList<>();
        this.components = new ArrayList<>();
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
        transform.translate(body.getVelocity().x, body.getVelocity().y);
        Iterator<Body> it = retrievedBodies.iterator();
        while (it.hasNext()) {
            Body physicsBody = it.next();

            if (Math.signum(physicsBody.getVelocity().normalize().x) != 0 && Math.abs(physicsBody.getVelocity().x) > 0.5 && physicsBody.gameobject.getComponent(PlayerController.class) == null) {
                physicsBody.transform.rotation = Math.min(Math.signum(physicsBody.getVelocity().normalize().x) * 180, 0);
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

    public void setAABB(int x, int y, int w, int h) {
        AABB = new Rectangle(x, y, w, h);
    }

    public Rectangle getAABB() {
        return AABB;
    }
    
    public Rectangle localAABB() {
        return new Rectangle((int) transform.position.x + AABB.x, (int) transform.position.y + AABB.y, AABB.width, AABB.height);
    }
}
