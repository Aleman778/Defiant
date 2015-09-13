package ga.engine.physics;

import ga.engine.scene.GameComponent;

public abstract class Body extends GameComponent {
    
    double mass = 1;
    double softness = 0.2;
    Vector2D velocity = new Vector2D();
    double friction = 0.25;

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
