package ga.engine.physics;

import ga.engine.scene.GameComponent;

public abstract class Body extends GameComponent {
    
    protected double mass = 1;
    protected double softness = 0.2;
    protected Vector2D velocity = new Vector2D();
    protected double friction = 0.1;
    public double SPEED_LIMIT = 3;
    private boolean grounded = false;

    @Override
    public void update() {
        super.update();
    }
    
    public void setVelocity(Vector2D vel) {
        velocity = vel;
    }
    
    public Vector2D getVelocity() {
        return velocity;
    }
    
    public void setSoftness(double softness) {
        this.softness = softness;
    }
        
    public void setGrounded(boolean grounded) {
        this.grounded = grounded;
    }
    
    public boolean isGrounded() {
        return grounded;
    }
    
    public abstract void physicsUpdate();
}
