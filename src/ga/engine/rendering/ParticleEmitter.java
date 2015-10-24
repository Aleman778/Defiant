package ga.engine.rendering;

import ga.engine.physics.Vector2D;
import ga.engine.scene.GameComponent;
import ga.engine.scene.GameObject;
import java.util.HashSet;
import java.util.Iterator;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class ParticleEmitter extends GameComponent {

    private final HashSet<Particle> particles;
    public final static int MODE_CONTINUOUS = 0, MODE_SINGLE = 1;
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
        if (mode == MODE_CONTINUOUS) {
            addParticles(1);
        }
        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            Particle p = it.next();
            if (!p.update()) {
                it.remove();
            }
        }
    }
    
    public void fire(int amount) {
        addParticles(amount);
    }
    
    private void addParticles(int amount) {
        for (int i = 0; i < amount; i++) {
            float dir = (float) (direction + (spread * Math.random() - (spread / 2)));
            particles.add(new Particle(transform.position.add(object.transform.position), new Vector2D(Math.cos(Math.toRadians(dir)), Math.sin(Math.toRadians(dir))), size, (int) ((life - 100) * Math.random() + 100)));
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
