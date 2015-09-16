package ga.game.entity;

import ga.engine.scene.GameComponent;

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
    }
    
    public boolean isAlive() {
        return health > 0;
    }
}
