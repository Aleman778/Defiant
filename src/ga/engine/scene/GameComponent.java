package ga.engine.scene;

import ga.engine.physics.Body;
import ga.engine.physics.Vector2D;
import java.util.List;
import java.util.Set;
import javafx.scene.canvas.GraphicsContext;

public abstract class GameComponent {
    
    public Transform transform;
    public GameObject gameobject;
    
    public GameComponent() {
    }
    
    public <T extends GameComponent> GameComponent getComponent(Class<T> component) {
        return gameobject.getComponent(component);
    }
    public <T extends GameComponent> Set<GameComponent> getComponents(Class<T> component) {
        return gameobject.getComponents(component);
    }
    
    public List<GameComponent> getAllComponents() {
        return gameobject.getAllComponents();
    }
    
    
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
