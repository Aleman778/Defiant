package ga.game;

import com.sun.javafx.geom.Rectangle;
import ga.engine.animation.Animation;
import ga.engine.core.Application;
import ga.engine.physics.Body;
import ga.engine.physics.SimpleBody;
import ga.engine.physics.Vector2D;
import ga.engine.rendering.ImageRenderer;
import ga.engine.rendering.ParticleEmitter;
import ga.engine.rendering.SpriteRenderer;
import ga.engine.resource.ResourceManager;
import ga.engine.scene.GameObject;
import ga.engine.xml.XMLReader;
import ga.game.entity.HealthComponent;
import java.util.HashMap;
import java.util.Set;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.xml.sax.Attributes;

public class Weapon {

    public SpriteRenderer image;
    public int clipSize, maxAmmo, ammo, spareAmmo;
    public long lastFire = 0, cooldown, reload, lastReload = 0;
    public double spread, velocity = 20, recoil = 0, damage;
    public boolean single = true;
    private static HashMap<String, String> config = new HashMap<>();
    public ParticleEmitter spark;
    public Image reloadImage = ResourceManager.get("<RELOAD>");
    public Image idleImage;
    public Animation reloadAnimation = new Animation(1) {
        
        @Override
        public void animate(int frame) {
            image.setOffsetX(frame * 32);
            image.setOffsetY(0);
            image.setSprite(reloadImage);
            image.setWidth((int) reloadImage.getWidth() / getFrames());
            image.setHeight((int) reloadImage.getHeight());
        }
    },
    idleAnimation = new Animation(1) {
        
        @Override
        public void animate(int frame) {
            image.setOffsetX(0);
            image.setOffsetY(0);
            image.setSprite(idleImage);
            image.setWidth((int) idleImage.getWidth() / getFrames());
            image.setHeight((int) idleImage.getHeight());
        }
    };

    public Weapon(String imagePath, int clipSize, int maxAmmo, double spread, long cooldown, double recoil, double damage, long reload, boolean burst) {
        image = new SpriteRenderer(imagePath);
        spark = ParticleEmitter.loadXML("particles/systems/Spark.psystem");
        idleImage = image.getSprite();
        this.clipSize = clipSize;
        this.maxAmmo = maxAmmo;
        this.ammo = clipSize;
        this.spareAmmo = maxAmmo;
        this.spread = spread;
        this.cooldown = cooldown;
        this.recoil = recoil;
        this.damage = damage;
        this.reload = reload;
        this.single = !burst;
    }

    public Weapon(String imagePath, int clipSize, int maxAmmo, double spread, long cooldown, double recoil, double damage, long reload, boolean burst, String flashPath) {
        this(imagePath, clipSize, maxAmmo, spread, cooldown, recoil, damage, reload, burst);
        spark = ParticleEmitter.loadXML(flashPath);
    }

    public GameObject fire(double direction) {
        GameObject o = new GameObject();
        ImageRenderer renderer = new ImageRenderer("textures/bullet_round.png") {

            @Override
            public void fixedUpdate() {
                super.fixedUpdate();
                if (gameobject.getBody().velocity.x == 0 && gameobject.getBody().velocity.y == 0) {
                    image = ResourceManager.get("<HIT_MARKER>");
                }
            }

            @Override
            public void onCollision(Body body, Vector2D normal, double penetration) {
                HealthComponent otherHealth = (HealthComponent) body.gameobject.getComponent(HealthComponent.class);
                if (otherHealth != null) {
                    otherHealth.damage(damage);
                }
            }
        };
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

    public Image getImage() {
        return image.getSprite();
    }

    public static Weapon loadXML(String filepath) {
        config.clear();
        reader.parse(filepath);
        if (config.get("type") != null) {
            if (config.get("type").equals("flamethrower")) {
                Weapon w = new Weapon(config.get("sprite"),
                        Integer.parseInt(config.get("clip")),
                        Integer.parseInt(config.get("ammo")),
                        Double.parseDouble(config.get("spread")),
                        Long.parseLong(config.get("cooldown")),
                        Double.valueOf(config.get("recoil")),
                        Double.valueOf(config.get("damage")),
                        Long.parseLong(config.get("reload")),
                        Boolean.valueOf((config.get("burst")))) {

                            @Override
                            public GameObject fire(double direction) {
                                return null;
                            }
                        };
                w.spark = new ParticleEmitter() {

                    @Override
                    public void onCollision(Body body, Vector2D normal, double penetration) {
                        HealthComponent otherHealth = (HealthComponent) body.gameobject.getComponent(HealthComponent.class);
                        if (otherHealth != null && Math.random() < 0.1) {
                            otherHealth.damage(w.damage);
                        }
                    }
                };
                w.spark.setConfig(ParticleEmitter.loadXMLConfig("particles/systems/Fire.psystem"));
                w.spark.physics(true);
                w.spark.physicsEvent = true;
                w.spark.id = 5;
                return w;
            }
        }
        return new Weapon(config.get("sprite"),
                Integer.parseInt(config.get("clip")),
                Integer.parseInt(config.get("ammo")),
                Double.parseDouble(config.get("spread")),
                Long.parseLong(config.get("cooldown")),
                Double.valueOf(config.get("recoil")),
                Double.valueOf(config.get("damage")),
                Long.parseLong(config.get("reload")),
                Boolean.valueOf((config.get("burst"))));
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
