package ga.engine.scene;

public abstract class GameComponent {
    
    public GameComponent() {
    }
    
    //EVENTS
    public void awoke() {}
    public void start() {}
    public void fixedUpdate() {}
    public void update() {}
    public void lateUpdate() {}
}
