package ga.game.entity;

import ga.engine.scene.GameComponent;
import static ga.engine.scene.GameComponent.ATTRIBUTES_NONE;
import java.util.List;
import java.util.Map;

public class HealthComponent extends GameComponent {
    
    private double health;
    
    public HealthComponent(double health) {
        this.health = health;
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
        return ATTRIBUTES_NONE;
    }

    @Override
    public void setAttributes(Map<String, String> attributes) {
    }
    
    @Override
    public GameComponent instantiate() {
        return new HealthComponent(100);
    }
}