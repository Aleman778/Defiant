package ga.engine.scene;

import ga.engine.physics.Body;
import ga.engine.physics.Vector2D;

public abstract class GameComponent {
    
    public Transform transform;
    public GameObject gameobject;
    
    public GameComponent() {
    }
    
    //EVENTS
    public void awoke() {}
    public void start() {}
    public void fixedUpdate() {}
    public void update() {}
    public void lateUpdate() {}
    public void onCollisionEnter() {}
    public void onCollision(Body body, Vector2D normal, double penetration) {}
    public void onCollisionExit() {}
}
