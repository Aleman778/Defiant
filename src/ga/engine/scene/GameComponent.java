package ga.engine.scene;

public abstract class GameComponent {
    
    public float x, y;
    
    public GameComponent(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    //EVENTS
    public void awoke() {}
    public void start() {}
    public void fixedUpdate() {}
    public void update() {}
    public void lateUpdate() {}
}
