package ga.engine.scene;

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
    public void onCollision() {}
    public void onCollisionExit() {}
}
