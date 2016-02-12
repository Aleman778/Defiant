package ga.engine.rendering;

import com.sun.javafx.geom.Rectangle;
import ga.engine.physics.Body;
import ga.engine.physics.Vector2D;
import ga.engine.scene.GameComponent;
import static ga.engine.scene.GameComponent.ATTRIBUTES_NONE;
import ga.engine.scene.GameObject;
import ga.engine.scene.GameScene;
import ga.engine.xml.XMLReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import org.xml.sax.Attributes;

public class ParticleEmitter extends GameComponent {

    protected final HashSet<Particle> particles;
    public static ParticleConfiguration tempConfig = new ParticleConfiguration();
    protected Color color, colorMid, colorEnd;
    protected String mode, particleShape;
    public float life, direction, spread, size, sizeEnd, sizeStep, velocity, velocityMin, velocityMax, velocityStep, rate, colorPoint, random;
    public GameObject object;
    protected Image sprite;
    public double gravityScale = 0.6;
    protected Rectangle shape = new Rectangle(0, 0, 1, 1);
    protected ParticleConfiguration config = new ParticleConfiguration();
    public boolean interpolate = false, physics = true, physicsEvent = false;
    public int interpolationScale = 8, id = 5;
    protected Vector2D lastPosition;
    public ArrayList<Integer> collide = new ArrayList<>(), noCollide = new ArrayList<Integer>() {
        {
            add(2);
            add(3);
        }
    };

    public ParticleEmitter(float direction, float spread, float size, String mode, float life, Color color) {
        config.setValue("mode", String.valueOf(mode));
        config.setValue("life", String.valueOf(life));
        config.setValue("direction", String.valueOf(direction));
        config.setValue("spread", String.valueOf(spread));
        config.setValue("size", String.valueOf(size));
        config.setValue("sizeEnd", String.valueOf(size));
        config.setValue("color", String.format("#%X", color.hashCode()));
        setConfig(config);
        this.particles = new HashSet<>();
        object = new GameObject();
    }

    public ParticleEmitter() {
        this.particles = new HashSet<>();
        object = new GameObject();
    }

    @Override
    public void update() {
        super.update();
        switch (mode) {
            case "MODE_CONTINUOUS":
                addParticles((int) rate);
                break;
            case "MODE_CONTINUOUS_MIRRORED":
                addParticles((int) rate);
                direction = 180 - direction;
                addParticles((int) rate);
                direction = 0 + direction;
                break;
        }
        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            Particle p = it.next();
            p.size += sizeStep;
            p.velocity += (velocityStep - 1);
            if (velocityMax != 0) {
                p.velocity = Math.min(p.velocity, velocityMax);
            }
            if (velocityMin != 0) {
                p.velocity = Math.max(p.velocity, velocityMin);
            }
            if (!p.update()) {
                it.remove();
            }
        }
    }

    public void physicsUpdate(Set<Body> retrievedBodies) {
        for (Iterator<Particle> it = particles.iterator(); it.hasNext();) {
            Particle p = it.next();
            if (p.body.getMass() != 0) {
                p.body.setVelocity(p.body.getVelocity().add(GameScene.gravity.mul(gravityScale)).mul(p.velocity));
                p.body.transform.position = p.body.transform.position.add(p.body.velocity);
            }
            if (physics) {
                Iterator<Body> bodyIt = retrievedBodies.iterator();
                while (bodyIt.hasNext()) {
                    Body physicsBody = bodyIt.next();
                    p.physicsUpdate(physicsBody);
                }
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

    public void fire() {
        if (mode.equals("MODE_SINGLE_MIRRORED")) {
            addParticles((int) ((rate * 10) / 2));
            direction = 180 - direction;
            addParticles((int) ((rate * 10) / 2));
            direction = 0 + direction;
        } else {
            addParticles((int) rate * 10);
        }
    }

    private void addParticles(int amount) {
        if (interpolate) {
            if (lastPosition != null) {
                Vector2D pos = object.transform.position;
                interpolate = false;
                int x = (int) ((transform.position.x - lastPosition.x) / 2), y = (int) ((transform.position.y - lastPosition.y) / 2);
                while (x > 1 || y > 1) {
                    object.transform.position.x += x;
                    object.transform.position.y += y;
                    addParticles(amount);
                    object.transform.position.x -= 2 * x;
                    object.transform.position.y -= 2 * y;
                    addParticles(amount);
                    object.transform.position.x += x;
                    object.transform.position.y += y;
                    x /= interpolationScale;
                    y /= interpolationScale;
                }
                interpolate = true;
                object.transform.position = pos;
            }
            lastPosition = transform.position;
        }
        for (int i = 0; i < amount; i++) {
            float dir = (float) (direction + (spread * Math.random() - (spread / 2)));
            Vector2D pos = new Vector2D((Math.random() * shape.width), (Math.random() * shape.height));
            Particle p = new Particle(gameobject, transform.position.add(object.transform.position).add(new Vector2D(shape.x, shape.y))
                    .add(pos),
                    new Vector2D(Math.cos(Math.toRadians(dir)), Math.sin(Math.toRadians(dir))).mul(velocity),
                    size,
                    (int) ((life - 100) * Math.random() + 100),
                    color.deriveColor(1, 1, 1 + (-0.1 + Math.random() * 0.2), 1),
                    physicsEvent,
                    id);
            p.body.setCollide(collide);
            p.body.setNoCollide(noCollide);
            if (random > 0) {
                p.velocity = (float) (velocity * (0.95 + Math.random() * random));
            } else {
                p.velocity = velocity;
            }
            p.shape = particleShape;
            if (sprite != null) {
                p.sprite = sprite;
            }
            p.interpolate = interpolate;
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

            if (p.life / p.lifeTime < colorPoint) {
                p.color = color.interpolate(colorMid, (p.life / p.lifeTime) / colorPoint);
            } else {
                p.color = colorMid.interpolate(colorEnd, ((p.life / p.lifeTime) - colorPoint));
            }

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
        try {
            direction = Float.parseFloat(config.getValue("direction"));
            life = Float.parseFloat(config.getValue("life"));
            mode = config.getValue("mode");
            spread = Float.parseFloat(config.getValue("spread"));
            size = Float.parseFloat(config.getValue("size"));
            sizeEnd = Float.parseFloat(config.getValue("sizeEnd"));
            sizeStep = Float.parseFloat(config.getValue("sizeStep"));
            color = Color.web(config.getValue("color"));
            gravityScale = Float.parseFloat(config.getValue("gravity"));
            velocity = Float.parseFloat(config.getValue("velocity"));
            velocityMin = Float.parseFloat(config.getValue("velocityMin"));
            velocityMax = Float.parseFloat(config.getValue("velocityMax"));
            velocityStep = Float.parseFloat(config.getValue("velocityStep"));
            rate = Float.parseFloat(config.getValue("rate"));
            particleShape = config.getValue("particleShape");
            shape = new Rectangle(Integer.parseInt((config.getValue("shape").split(",")[0].trim())), Integer.parseInt(config.getValue("shape").split(",")[1].trim()));
            if (config.getValue("colorEnd").equals("")) {
                colorMid = color;
                colorEnd = color;
            } else {
                colorMid = Color.web(config.getValue("colorMid"));
                colorEnd = Color.web(config.getValue("colorEnd"));
            }
            colorPoint = Float.parseFloat(config.getValue("colorPoint"));

        } catch (NullPointerException ex) {

        }
        if (sizeStep == 0 && size != sizeEnd) {
            sizeStep = (sizeEnd - size) / life;
        }
        random = Float.parseFloat(config.getValue("random"));
    }

    public void interpolate(boolean interpolate) {
        this.interpolate = interpolate;
    }

    public void physics(boolean physics) {
        this.physics = physics;
    }

    public static ParticleEmitter loadXML(String filepath) {
        tempConfig = new ParticleConfiguration();
        reader.parse(filepath);
        ParticleEmitter e = new ParticleEmitter(0, 0, 0, "", 0, Color.BLUE);
        e.setConfig(tempConfig);
        return e;
    }

    public static ParticleConfiguration loadXMLConfig(String filepath) {
        tempConfig = new ParticleConfiguration();
        reader.parse(filepath);
        return tempConfig;
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

    @Override
    public List<String> getAttributes() {
        return ATTRIBUTES_NONE;
    }

    @Override
    public void setAttributes(Map<String, String> attributes) {
    }
    
    @Override
    public GameComponent instantiate() {
        return new ParticleEmitter();
    }
}
