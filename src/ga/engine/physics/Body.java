package ga.engine.physics;

import ga.engine.scene.GameComponent;

public abstract class Body extends GameComponent {
    
    protected double mass = 1;
    protected double softness = 0.5;
    protected Vector2D velocity = new Vector2D();
    protected double friction = 0.25;

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
