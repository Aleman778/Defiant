package ga.game.entity;

import ga.engine.scene.GameComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class HealthComponent extends GameComponent {

    private static final List<String> ATTRIBUTES = new ArrayList<>();
    private static final double HP_SCALE = 1, HP_HEIGHT = 7;

    static {
        ATTRIBUTES.add("Health");
    }

    private double health, maxHealth;

    public HealthComponent() {
        this.health = 1;
    }

    public void setHealth(int health) {
        this.health = health;
        maxHealth = health;
    }

    public double getHealth() {
        return health;
    }
    
    public double getMaxHealth() {
        return maxHealth;
    }

    public void heal(double amount) {
        amount = Math.min(amount, maxHealth - amount);
        this.health += amount;
    }

    public void damage(double amount) {
        amount = Math.min(amount, health);
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
    public void render(GraphicsContext g) {
        if (!gameobject.getTag().equals("player")) {
            Affine a = g.getTransform();
            Affine b = new Affine(1, a.getMxy(), transform.position.x, a.getMyx(), a.getMyy(), transform.position.y - 15);
            g.setTransform(b);
            int x = (int) (transform.gameobject.getAABB().width / 2 - (HP_SCALE * maxHealth) / 2);
            g.fillRect(x - 1, -1, HP_SCALE * maxHealth + 2, HP_HEIGHT + 2);
            g.setFill(Color.RED);
            g.fillRect(x, 0, HP_SCALE * maxHealth, HP_HEIGHT);
            g.setFill(Color.GREEN);
            g.fillRect(x, 0, HP_SCALE * maxHealth * (health / maxHealth), HP_HEIGHT);
        }
    }

    @Override
    public List<String> getAttributes() {
        return ATTRIBUTES;
    }

    @Override
    public void setAttributes(Map<String, String> attributes) {
        health = Integer.parseInt(attributes.get("Health"));
        maxHealth = health;
    }

    @Override
    public GameComponent instantiate() {
        return new HealthComponent();
    }
}