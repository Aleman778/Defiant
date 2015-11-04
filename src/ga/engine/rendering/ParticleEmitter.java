package ga.engine.rendering;

import com.sun.javafx.geom.Rectangle;
import ga.engine.physics.Body;
import ga.engine.physics.Vector2D;
import ga.engine.scene.GameComponent;
import ga.engine.scene.GameObject;
import ga.engine.scene.GameScene;
import ga.engine.xml.XMLReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import org.xml.sax.Attributes;

public class ParticleEmitter extends GameComponent {

    private final HashSet<Particle> particles;
    public static ParticleConfiguration tempConfig = new ParticleConfiguration();
    protected Color color;
    protected String mode;
    protected float life, direction, spread, size;
    public GameObject object;
    protected Image sprite;
    protected double gravityScale = 0.6;
    protected Rectangle shape = new Rectangle(0, 0, 1, 1);
    protected ParticleConfiguration config = new ParticleConfiguration();

    public ParticleEmitter(float direction, float spread, float size, String mode, float life, Color color) {
        config.setValue("mode", String.valueOf(mode));
        config.setValue("life", String.valueOf(life));
        config.setValue("direction", String.valueOf(direction));
        config.setValue("spread", String.valueOf(spread));
        config.setValue("size", String.valueOf(size));
        config.setValue("color", String.format("#%X", color.hashCode()));
        setConfig(config);
        this.particles = new HashSet<>();
        object = new GameObject();
    }

    @Override
    public void update() {
        super.update();
        switch (mode) {
            case "MODE_CONTINUOUS":
                addParticles(1);
                break;
            case "MODE_CONTINUOUS_MIRRORED":
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
                p.body.setVelocity(p.body.getVelocity().add(GameScene.gravity.mul(gravityScale)));
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
        if (mode.equals("MODE_SINGLE_MIRRORED")) {
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
            Vector2D pos = new Vector2D((Math.random() * shape.width), (Math.random() * shape.height));
            Particle p = new Particle(transform.position.add(object.transform.position).add(new Vector2D(shape.x, shape.y))
                    .add(pos),
                    new Vector2D(Math.cos(Math.toRadians(dir)), Math.sin(Math.toRadians(dir))),
                    size, (int) ((life - 100) * Math.random() + 100), color.deriveColor(1, 1, 1 + (-0.1 + Math.random() * 0.2), 1));
            if (sprite != null) {
                p.sprite = sprite;
            }
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

    public static Image cropImage(Image image, int x, int y, int width, int height) {
        return new WritableImage(image.getPixelReader(), x, y, width, height);
    }

    public void setSprite(Image sprite) {
        this.sprite = sprite;
    }

    public void setShape(Rectangle shape) {
        this.shape = shape;
    }

    public ParticleConfiguration getConfig() {
        return config;
    }

    public void setConfig(ParticleConfiguration config) {
        this.config = config;
        direction = Float.parseFloat(config.getValue("direction"));
        life = Float.parseFloat(config.getValue("life"));
        mode = config.getValue("mode");
        spread = Float.parseFloat(config.getValue("spread"));
        size = Float.parseFloat(config.getValue("size"));
        color = Color.web(config.getValue("color"));
    }

    public static ParticleEmitter loadXML(String filepath) {
        reader.parse(filepath);
        ParticleEmitter e = new ParticleEmitter(0, 0, 0, "", 0, Color.BLUE);
        e.setConfig(tempConfig);
        return e;
    }

    private static final XMLReader reader = new XMLReader() {

        @Override
        public void documentStart() {
        }

        @Override
        public void documentEnd() {
        }

        @Override
        public void nodeStart(String element, Attributes attri) {
        }

        @Override
        public void nodeEnd(String element, Attributes attri, String value) {
            if (!element.equals("particleSystem")) {
                tempConfig.setValue(element, value);
            }
        }
    };
}
