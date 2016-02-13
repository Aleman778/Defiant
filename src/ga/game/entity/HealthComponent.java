package ga.game.entity;

import ga.engine.scene.GameComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HealthComponent extends GameComponent {
    
    private static final List<String> ATTRIBUTES = new ArrayList<>();
    
    static {
        ATTRIBUTES.add("Health");
    }
    
    private double health;
    
    public HealthComponent() {
        this.health = 1;
    }
    
    public void setHealth(double health) {
        this.health = health;
    }
    
    public double getHealth() {
        return health;
    }
    
    public void heal(double amount) {
        this.health += amount;
    }
    
    public void damage(double amount) {
        this.health -= amount;
        AI ai = (AI) getComponent(AI.class);
        if (ai != null) {
            ai.takingDamage = amount;
        }
    }
    
    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public void fixedUpdate() {
        if (!isAlive()) {
            gameobject.destroy();
        }
    }

    @Override
    public List<String> getAttributes() {
        return ATTRIBUTES;
    }

    @Override
    public void setAttributes(Map<String, String> attributes) {
        health = Double.parseDouble(attributes.get("Health"));
    }
    
    @Override
    public GameComponent instantiate() {
        return new HealthComponent();
    }
}