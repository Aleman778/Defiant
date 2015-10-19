package ga.engine.rendering;

import ga.engine.physics.Vector2D;
import ga.engine.scene.GameComponent;
import java.util.HashSet;
import java.util.Iterator;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ParticleEmitter extends GameComponent {

    protected Canvas canvas;
    protected GraphicsContext graphics;
    protected Color color;
    public final static int MODE_CONTINUOUS = 0, MODE_SINGLE = 1;
    protected int mode;
    protected float life, direction, spread, size;
    private HashSet<Particle> particles = new HashSet<>();

    public ParticleEmitter(float direction, float spread, float size, int mode, float life, Color color) {
        this.mode = mode;
        this.life = life;
        this.direction = direction;
        this.spread = spread;
        this.size = size;
        this.color = color;
        canvas = new Canvas(1920, 1080);
        graphics = canvas.getGraphicsContext2D();
    }

    @Override
    public void update() {
        super.update();
        graphics.setFill(color);
        graphics.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
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
    
    public void fire() {
        addParticles(10);
    }
    
    private void addParticles(int amount) {
        for (int i = 0; i < amount; i++) {
            float dir = (float) (direction + (spread * Math.random() - (spread / 2)));
            particles.add(new Particle(gameobject.getTransform().localPosition(), new Vector2D(Math.cos(Math.toRadians(dir)), Math.sin(Math.toRadians(dir))), size, (int) ((life - 100) * Math.random() + 100)));
        }
    }

    @Override
    public void render(GraphicsContext g) {
        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            Particle p = it.next();
            p.render(g);
        }
    }
}