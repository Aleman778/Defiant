package ga.game.weapon;

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
    public boolean single = true, sights = true;
    private static HashMap<String, String> tempConfig = new HashMap<>();
    public ParticleEmitter spark, shell;
    public Image reloadImage = ResourceManager.getImage("textures/player/Player_reloading_color.png");
    public Image idleImage;
    public String type = "default";
    public HashMap<String, String> config = new HashMap<>();
    public Animation reloadAnimation = new Animation(9,0.26) {

        @Override
        public void animate(int frame) {
            image.setOffsetX(frame * 40);
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

    public Weapon(String imagePath, int clipSize, int maxAmmo, double spread, long cooldown, double recoil, double damage, long reload, boolean burst, boolean sights, String type) {
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
        this.sights = sights;
        this.type = type;
        shell = ParticleEmitter.loadXML("particles/systems/Shell.psystem");
        shell.setSprite(ResourceManager.getImage("textures/bullet.png"));
    }

    public Weapon(String imagePath, int clipSize, int maxAmmo, double spread, long cooldown, double recoil, double damage, long reload, boolean burst, boolean sights, String type, String flashPath) {
        this(imagePath, clipSize, maxAmmo, spread, cooldown, recoil, damage, reload, burst, sights, type);
        spark = ParticleEmitter.loadXML(flashPath);
    }

    public Weapon(HashMap<String, String> config) {
        this(config.get("sprite"),
                Integer.parseInt(config.get("clip")),
                Integer.parseInt(config.get("ammo")),
                Double.parseDouble(config.get("spread")),
                Long.parseLong(config.get("cooldown")),
                Double.valueOf(config.get("recoil")),
                Double.valueOf(config.get("damage")),
                Long.parseLong(config.get("reload")),
                Boolean.valueOf((config.get("burst"))),
                Boolean.valueOf((config.get("sights"))),
                config.get("type"));
        this.config = new HashMap<>(config);
    }

    public GameObject fire(double direction) {
        GameObject o = new GameObject();
        ImageRenderer renderer = new ImageRenderer("textures/bullet_round.png") {

            @Override
            public void fixedUpdate() {
                super.fixedUpdate();
                if (gameobject.getBody().velocity.x == 0 && gameobject.getBody().velocity.y == 0) {
                    image = ResourceManager.getImage("textures/bullet_impact.png");
                    }
                }

            @Override
            public void onCollision(Body body, Body otherBody, Vector2D normal, double penetration, int id) {
                HealthComponent otherHealth = (HealthComponent) otherBody.gameobject.getComponent(HealthComponent.class);
                if (otherHealth != null) {
                    otherHealth.damage(damage);
                }
            }
        };
        o.addComponent(renderer);
        SimpleBody body = new SimpleBody(new Rectangle(1, 1), 1, 1);
        body.gravity = new Vector2D(0, 0.01);
        body.setNoCollide(2);
        body.addCollide(3);
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
        tempConfig.clear();
        tempConfig.put("type", "default");
        reader.parse(filepath);
        return Weapon.create(tempConfig.get("type"), tempConfig);
    }

    public static Weapon create(String type, HashMap<String, String> config) {
        Weapon w;
        switch (type) {
            case "flamethrower":
                w = Flamethrower.instantiate(config);
                break;
            case "grenade_launcher":
                w = GrenadeLauncher.instantiate(config);
                break;
            case "nitrogenthrower":
                w = Nitrogenthrower.instantiate(config);
                break;
            default:
                w = Weapon.instantiate(config);
        }
        return w;
    }
    
    public static Weapon instantiate(HashMap<String, String> config) {
        return new Weapon(config);
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
            if (!element.equals("Weapon")) {
                tempConfig.put(element, value);
            }
        }
    };
}
