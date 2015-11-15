package ga.engine.scene;

import ga.engine.physics.Body;
import ga.engine.physics.Vector2D;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javafx.scene.canvas.GraphicsContext;

public abstract class GameComponent {
    
    public static final HashMap<String, GameComponent> components = new HashMap<>();
    
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
    public abstract void xmlvar(String name, String value);
    
    //EVENTS
    public void awoke() {}
    public void start() {}
    public void fixedUpdate() {}
    public void update() {}
    public void lateUpdate() {}
    public void render(GraphicsContext g) {}
    public void onCollisionEnter(Body body, Vector2D normal, double penetration) {}
    public void onCollision(Body body, Vector2D normal, double penetration) {}
    public void onCollisionExit() {}
}
