package ga.engine.rendering;

import ga.engine.physics.Body;
import ga.engine.physics.ParticleBody;
import ga.engine.physics.Vector2D;
import ga.engine.scene.GameObject;
import ga.engine.scene.Transform2D;
import javafx.scene.canvas.GraphicsContext;

public class Particle {
    public float size, life, lifeTime;
    public ParticleBody body;

    public Particle(Vector2D position, Vector2D velocity, float size, float life) {
        body = new ParticleBody(this, 1, 5);
        body.gameobject = new GameObject();
        body.transform = new Transform2D(body.gameobject, position.x, position.y);
        body.velocity = velocity;
        this.size = (int) (0.75 * size * Math.random() + size / 2);
        this.lifeTime = life;
        this.life = life;
    }
    
    public boolean update() {
        life--;
        return life > 0;
    }
    
    public void render(GraphicsContext g) {
        g.setGlobalAlpha(life / lifeTime);
        g.fillOval(body.transform.position.x, body.transform.position.y, size * life / lifeTime, size * life / lifeTime);
    }
    
    public void physicsUpdate(Body body) {
        this.body.physicsUpdate(body);
    }
}
