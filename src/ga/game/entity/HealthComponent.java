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

    static {
        ATTRIBUTES.add("Health");
    }

    private double health, startHealth;

    public HealthComponent() {
        this.health = 1;
    }

    public void setHealth(double health) {
        this.health = health;
        startHealth = health;
    }

    public double getHealth() {
        return health;
    }

    public void heal(double amount) {
        amount = Math.min(amount, startHealth - amount);
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
            g.fillRect(-1, -1, transform.gameobject.getAABB().width + 2, 12);
            g.setFill(Color.RED);
            g.fillRect(0, 0, transform.gameobject.getAABB().width, 10);
            g.setFill(Color.GREEN);
            g.fillRect(0, 0, transform.gameobject.getAABB().width * (health / startHealth), 10);
        }
    }

    @Override
    public List<String> getAttributes() {
        return ATTRIBUTES;
    }

    @Override
    public void setAttributes(Map<String, String> attributes) {
        health = Double.parseDouble(attributes.get("Health"));
        startHealth = health;
    }

    @Override
    public GameComponent instantiate() {
        return new HealthComponent();
    }
}
