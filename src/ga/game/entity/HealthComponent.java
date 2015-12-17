package ga.game.entity;

import ga.engine.scene.GameComponent;
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
    public GameComponent instantiate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, Integer> getVars() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void xmlVar(String name, String value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}