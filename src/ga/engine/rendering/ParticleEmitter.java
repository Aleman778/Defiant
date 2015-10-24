package ga.engine.rendering;

import ga.engine.physics.Body;
import ga.engine.physics.Vector2D;
import ga.engine.scene.GameComponent;
import ga.engine.scene.GameObject;
import ga.engine.scene.GameScene;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class ParticleEmitter extends GameComponent {

    private final HashSet<Particle> particles;
    public final static int MODE_CONTINUOUS = 0, MODE_SINGLE = 1, MODE_CONTINUOUS_MIRRORED = 2, MODE_SINGLE_MIRRORED = 3;
    protected Color color;
    protected int mode;
    protected float life, direction, spread, size;
    public Vector2D offset = new Vector2D();
    public GameObject object;

    public ParticleEmitter(float direction, float spread, float size, int mode, float life, Color color) {
        this.mode = mode;
        this.life = life;
        this.direction = direction;
        this.spread = spread;
        this.size = size;
        this.color = color;
        this.particles = new HashSet<>();
        object = new GameObject();
    }

    @Override
    public void update() {
        super.update();
        switch (mode) {
            case MODE_CONTINUOUS:
                addParticles(1);
                break;
            case MODE_CONTINUOUS_MIRRORED:
                addParticles(1);
                direction = 180 - direction;
                addParticles(1);
                direction = 0 + direction;
                break;
        }
        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            Particle p = it.next();
            if (!p.update()) {
                it.remove();
            }
        }
    }
    
    public void physicsUpdate(Set<Body> retrievedBodies) {
        for (Iterator<Particle> it = particles.iterator(); it.hasNext();) {
            Particle p = it.next();
            if (p.body.getMass() != 0) {
                p.body.setVelocity(p.body.getVelocity().add(GameScene.gravity.mul(0.6)));
                p.body.transform.position = p.body.transform.position.add(p.body.velocity);
            }
            Iterator<Body> bodyIt = retrievedBodies.iterator();
            while (bodyIt.hasNext()) {
                Body physicsBody = bodyIt.next();
                p.physicsUpdate(physicsBody);
            }
        }
    }
    
    public void fire(int amount) {
        if (mode == MODE_SINGLE_MIRRORED) {
            addParticles(amount / 2);
            direction = 180 - direction;
            addParticles(amount / 2);
            direction = 0 + direction;
        } else {
            addParticles(amount);
        }
    }
    
    private void addParticles(int amount) {
        for (int i = 0; i < amount; i++) {
            float dir = (float) (direction + (spread * Math.random() - (spread / 2)));
            Particle p = new Particle(transform.position.add(object.transform.position), new Vector2D(Math.cos(Math.toRadians(dir)), Math.sin(Math.toRadians(dir))), size, (int) ((life - 100) * Math.random() + 100));
            particles.add(p);
        }
    }

    @Override
    public void render(GraphicsContext g) {
        g.translate(-transform.position.x, -transform.position.y);
        g.setTransform(new Affine());
        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            Particle p = it.next();
            p.render(g);
        }
    }
}
