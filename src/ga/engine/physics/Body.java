package ga.engine.physics;

import ga.engine.scene.GameComponent;
import java.util.ArrayList;

public abstract class Body extends GameComponent {
    
    protected double mass = 1;
    protected double softness = 0.2;
    protected Vector2D velocity = new Vector2D();
    protected double friction = 0.1;
    public double SPEED_LIMIT = 3;
    private boolean grounded = false;
    private ArrayList<Integer> collide = new ArrayList(), noCollide = new ArrayList();
    private int id = 1;

    @Override
    public void awoke() {
        super.awoke();
        collide.add(1);
        noCollide.add(0);
    }

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
    
    public void addCollide(int... id) {
        for (int i : id) {
            collide.add(i);
        }
    }
    
    public void setCollide(int... id) {
        collide.clear();
        collide.add(1);
        for (int i : id) {
            collide.add(i);
        }
    }

    public double getMass() {
        return mass;
    }
    
    public ArrayList<Integer> getCollide() {
        return collide;
    }
    
    public void addNoCollide(int... id) {
        noCollide.add(0);
        for (int i : id) {
            noCollide.add(i);
        }
    }
    
    public void setNoCollide(int... id) {
        noCollide.clear();
        for (int i : id) {
            noCollide.add(i);
        }
    }
    
    public ArrayList<Integer> getNoCollide() {
        return noCollide;
    }
    
    public void setID(int id) {
        this.id = id;
    }
    
    public int getID() {
        return id;
    }
    
    public abstract boolean physicsUpdate(Body otherBody);
}
