package ga.engine.physics;

import ga.engine.scene.GameComponent;

public abstract class Body extends GameComponent {
    
    private double mass = 1;
    private double softness = 0.5;
    private Vector2D velocity = new Vector2D();
    private double friction = 0.25;

    @Override
    public void update() {
        super.update();
    }
    
    public void setVelocity(Vector2D vel) {
        velocity = vel;
    }
    
    public void setSoftness(double softness) {
        this.softness = softness;
    }
    
}
