package ga.engine.scene;

import ga.engine.physics.Body;
import ga.engine.physics.Vector2D;
import ga.engine.rendering.ImageRenderer;
import ga.engine.rendering.SpriteRenderer;
import ga.engine.resource.ResourceManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.scene.canvas.GraphicsContext;

public abstract class GameComponent {
    
    private static final HashMap<String, GameComponent> components = new HashMap<>();
    public static final int TYPE_STRING = 0;
    public static final int TYPE_FILE = 1;
    public static final int TYPE_FLOAT = 2;
    public static final int TYPE_INTEGER = 3;
    public static final int TYPE_BOOLEAN = 4;
    
    static {
        components.put("ImageRenderer", new ImageRenderer(ResourceManager.DEFAULT_IMAGE));
        components.put("SpriteRenderer", new SpriteRenderer(ResourceManager.DEFAULT_IMAGE, 32, 32));
    }
    
    public Transform2D transform;
    public GameObject gameobject;
    
    public GameComponent() {
    }
    
    public final <T extends GameComponent> GameComponent getComponent(Class<T> component) {
        return gameobject.getComponent(component);
    }
    public final <T extends GameComponent> Set<GameComponent> getComponents(Class<T> component) {
        return gameobject.getComponents(component);
    }
    
    public final List<GameComponent> getAllComponents() {
        return gameobject.getAllComponents();
    }
    
    public abstract GameComponent instantiate();
    public abstract Map<String, Integer> getVars();
    public abstract void xmlVar(String name, String value);
    
    public static GameComponent get(String component) {
        return components.get(component);
    }
    
    //EVENTS
    public void awoke() {}
    public void start() {}
    public void fixedUpdate() {}
    public void update() {}
    public void lateUpdate() {}
    public void render(GraphicsContext g) {}
    public void onCollisionEnter(Body body, Body otherBody, Vector2D normal, double penetration, int id) {}
    public void onCollision(Body body, Body otherBody, Vector2D normal, double penetration, int id) {}
    public void onCollisionExit() {}
}
