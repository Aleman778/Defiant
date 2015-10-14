package ga.engine.rendering;

import ga.engine.physics.Vector2D;

public class Particle {
    private Vector2D position, velocity;
    private float size, life, lifeTime;
    private ParticleEmitter emitter;

    public Particle(ParticleEmitter emitter, Vector2D position, Vector2D velocity, float size, float life) {
        this.position = position;
        this.velocity = velocity;
        this.size = (int) (0.75 * size * Math.random() + size / 2);
        this.lifeTime = life;
        this.life = life;
        this.emitter = emitter;
    }
    
    public boolean update() {
        position = position.add(velocity);
        emitter.graphics.setGlobalAlpha(life / lifeTime);
        emitter.graphics.fillOval(position.x, position.y, size * life / lifeTime, size * life / lifeTime);
        life--;
        return life > 0;
    }
}
