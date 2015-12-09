package ga.game;

import com.sun.javafx.geom.Rectangle;
import ga.engine.core.Application;
import ga.engine.physics.Body;
import ga.engine.physics.SimpleBody;
import ga.engine.physics.Vector2D;
import ga.engine.rendering.ImageRenderer;
import ga.engine.rendering.ParticleEmitter;
import ga.engine.scene.GameObject;
import ga.engine.xml.XMLReader;
import java.util.HashMap;
import java.util.Set;
import javafx.scene.canvas.GraphicsContext;
import org.xml.sax.Attributes;

public class Weapon {

    private ImageRenderer image;
    private int clipSize, maxAmmo;
    public long lastFire = 0, cooldown;
    private double spread, velocity = 20;
    public boolean single = true;
    private static HashMap<String, String> config = new HashMap<>();

    public Weapon(String imagePath, int clipSize, int maxAmmo, double spread, long cooldown, boolean burst) {
        image = new ImageRenderer(imagePath);
        this.clipSize = clipSize;
        this.maxAmmo = maxAmmo;
        this.spread = spread;
        this.cooldown = cooldown;
        this.single = !burst;
    }

    public double getCooldown() {
        return cooldown;
    }

    public GameObject fire(double direction) {
        direction = Math.toRadians(direction);
        GameObject o = new GameObject() {
            @Override
            public void fixedUpdate() {
                super.fixedUpdate();
                if (true) {
                    
                }
            }
        };
        ImageRenderer renderer = new ImageRenderer("textures/bullet_round.png");
        o.addComponent(renderer);
        SimpleBody body = new SimpleBody(new Rectangle(1, 1), 1, 1);
        body.gravity = new Vector2D(0, 0.01);
        body.setNoCollide(2);
        body.setVelocity(new Vector2D(velocity * Math.cos(direction), velocity * Math.sin(direction)));
        o.addComponent(body);
        ParticleEmitter e = new ParticleEmitter() {
            @Override
            public void physicsUpdate(Set<Body> retrievedBodies) {
                super.physicsUpdate(retrievedBodies);
                Body b = gameobject.getBody();
                if (b.velocity.x == 0 && b.velocity.y == 0
                        || gameobject.transform.position.x > Application.getWidth() * 1.2
                        || gameobject.transform.position.y > Application.getHeight() * 1.2
                        || gameobject.transform.position.x < -Application.getWidth() * 0.2
                        || gameobject.transform.position.y < -Application.getHeight() * 0.2) {
                    rate = 0;
                }
            }

            @Override
            public void update() {
                super.update();
                if (particles.isEmpty()) {
                    gameobject.destroy();
                }
            }
        };
        e.setConfig(ParticleEmitter.loadXMLConfig("particles/systems/WeaponTrail.psystem"));
        e.interpolate(true);
        e.physics(false);
        e.object.transform.position = new Vector2D(0, renderer.getImage().getHeight() / 2);
        o.addComponent(e);
        o.setAABB(0, 0, 1, 1);
        return o;
    }

    public void render(GraphicsContext g) {
        image.render(g);
    }
    
    public static Weapon loadXML(String filepath) {
        reader.parse(filepath);
        return new Weapon(config.get("sprite"), Integer.parseInt(config.get("clip")), Integer.parseInt(config.get("ammo")), Integer.parseInt(config.get("spread")), Long.parseLong(config.get("cooldown")), Boolean.valueOf((config.get("burst"))));
    }
    
    private static final XMLReader reader = new XMLReader() {

        @Override
        public void documentStart() {}

        @Override
        public void documentEnd() {}

        @Override
        public void nodeStart(String element, Attributes attri) {}

        @Override
        public void nodeEnd(String element, Attributes attri, String value) {
            if (!element.equals("Weapon")) {
                config.put(element, value);
            }
        }
    };
}
