package ga.engine.physics;

import ga.engine.scene.GameScene;
import ga.engine.scene.GameComponent;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Body extends GameComponent {

    protected double mass = 1;
    protected double softness = 0.2;
    public Vector2D velocity = new Vector2D();
    protected double friction = 0;
    public double SPEED_LIMIT = 3;
    private boolean grounded = false;
    private ArrayList<Integer> collide = new ArrayList<>(), noCollide = new ArrayList<>();
    protected int id = 1;
    public Vector2D gravity;

    public Body(int id) {
        this.id = id;
    }

    public Body() {}

    @Override
    public void awoke() {
        collide.add(1);
        noCollide.add(0);
        this.gravity = GameScene.gravity;
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

    public double getFriction() {
        return friction;
    }

    public void setFriction(double friction) {
        this.friction = friction;
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

    public void removeCollide(int... id) {
        for (int i : id) {
            collide.remove((Object) i);
        }
    }

    public void setCollide(int... id) {
        collide.clear();
        collide.add(1);
        for (int i : id) {
            collide.add(i);
        }
    }
    
    public void setCollide(ArrayList<Integer> id) {
        collide = new ArrayList<>(id);
        collide.add(1);
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
    
    public void removeNoCollide(int... id) {
        for (int i : id) {
            noCollide.remove((Object) i);
        }
    }

    public void setNoCollide(int... id) {
        noCollide.clear();
        noCollide.add(0);
        for (int i : id) {
            noCollide.add(i);
        }
    }
    
    public void setNoCollide(ArrayList<Integer> id) {
        noCollide = new ArrayList<>(id);
        noCollide.add(0);
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

    public abstract HashMap<String, Object> physicsUpdate(Body otherBody);
}
