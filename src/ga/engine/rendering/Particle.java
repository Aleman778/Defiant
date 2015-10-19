package ga.engine.rendering;

import ga.engine.physics.Vector2D;
import javafx.scene.canvas.GraphicsContext;

public class Particle {
    private Vector2D position, velocity;
    private float size, life, lifeTime;

    public Particle(Vector2D position, Vector2D velocity, float size, float life) {
        this.position = position;
        this.velocity = velocity;
        this.size = (int) (0.75 * size * Math.random() + size / 2);
        this.lifeTime = life;
        this.life = life;
    }
    
    public boolean update() {
        position = position.add(velocity);
        life--;
        return life > 0;
    }
    
    public void render(GraphicsContext g) {
        g.setGlobalAlpha(life / lifeTime);
        g.fillOval(position.x, position.y, size * life / lifeTime, size * life / lifeTime);
    }
}
