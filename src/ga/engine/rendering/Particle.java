package ga.engine.rendering;

import ga.engine.physics.Vector2D;

public class Particle {
    private Vector2D position, velocity;
    private float size, life;
    private ParticleEmitter emitter;

    public Particle(ParticleEmitter emitter, Vector2D position, Vector2D velocity, float size, float life) {
        this.position = position;
        this.velocity = velocity;
        this.size = size;
        this.life = life;
        this.emitter = emitter;
    }
    
    public boolean update() {
        position = position.add(velocity);
        emitter.graphics.setGlobalAlpha(life / 100);
        emitter.graphics.fillOval(position.x, position.y, size, size);
        life--;
        return life > 0;
    }
}
