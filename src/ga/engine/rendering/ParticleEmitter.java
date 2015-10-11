package ga.engine.rendering;

import com.sun.javafx.geom.Rectangle;
import ga.engine.physics.Vector2D;
import ga.engine.scene.GameComponent;
import java.util.HashSet;
import java.util.Iterator;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ParticleEmitter extends GameComponent implements Renderable {

    private final Canvas canvas;
    protected final GraphicsContext graphics;
    private Color color;
    public final static int MODE_CONTINUOUS = 0, MODE_SINGLE = 1;
    private int mode;
    private float life, direction, spread, size;
    private HashSet<Particle> particles = new HashSet<>();
    private Vector2D localPosition;

    public ParticleEmitter(Vector2D position, float direction, float spread, float size, int mode, float life, Color color) {
        this.mode = mode;
        this.life = life;
        this.direction = direction;
        this.spread = spread;
        this.size = size;
        this.localPosition = position;
        this.color = color;
        canvas = new Canvas(1920, 1080);
        graphics = canvas.getGraphicsContext2D();
        graphics.setFill(color);
    }

    @Override
    public void update() {
        super.update();
        graphics.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if (mode == MODE_CONTINUOUS) {
            addParticles(1);
        }
        for (Iterator<Particle> it = particles.iterator(); it.hasNext();) {
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
            particles.add(new Particle(this, gameobject.getTransform().localPosition().toVector2D().add(localPosition), new Vector2D(Math.cos(Math.toRadians(dir)), Math.sin(Math.toRadians(dir))), size, (int) ((life - 100) * Math.random() + 100)));
        }
    }

    @Override
    public void render(Group group) {
        group.getChildren().add(canvas);
    }

    @Override
    public Node getNode() {
        return canvas;
    }

    @Override
    public Rectangle computeAABB() {
        return new Rectangle();
    }
}
