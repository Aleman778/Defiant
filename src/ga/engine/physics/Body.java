package ga.engine.physics;

import ga.engine.scene.GameComponent;

public abstract class Body extends GameComponent {
    
    double mass = 1;
    double softness = 0.5;
    Vector2D velocity = new Vector2D();

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
