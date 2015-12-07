package ga.game;

import com.sun.javafx.geom.Rectangle;
import ga.engine.core.Application;
import ga.engine.physics.Body;
import ga.engine.physics.SimpleBody;
import ga.engine.physics.Vector2D;
import ga.engine.rendering.ImageRenderer;
import ga.engine.rendering.ParticleEmitter;
import ga.engine.scene.GameObject;
import java.util.Set;
import javafx.scene.canvas.GraphicsContext;

public class Weapon {

    private ImageRenderer image;
    private int clipSize, maxAmmo;
    public long lastFire = 0, cooldown;
    private double spread;

    public Weapon(String imagePath, int clipSize, int maxAmmo, double spread, long cooldown) {
        image = new ImageRenderer(imagePath);
        this.clipSize = clipSize;
        this.maxAmmo = maxAmmo;
        this.spread = spread;
        this.cooldown = cooldown;
    }

    public double getCooldown() {
        return cooldown;
    }

    public GameObject fire() {
        GameObject o = new GameObject() {
            @Override
            public void fixedUpdate() {
                super.fixedUpdate();
                if (true) {
                    
                }
            }
        };
        SimpleBody body = new SimpleBody(new Rectangle(1, 1), 1, 1);
        body.gravity = new Vector2D(0, 0.1);
        body.setNoCollide(2);
        body.setVelocity(new Vector2D(20, 0));
        o.addComponent(body);
        ParticleEmitter e = new ParticleEmitter() {
            @Override
            public void physicsUpdate(Set<Body> retrievedBodies) {
                super.physicsUpdate(retrievedBodies);
                Body b = gameobject.getBody();
                if (b.velocity.x == 0 && b.velocity.y == 0 || gameobject.transform.position.x > Application.getWidth() * 1.2 || gameobject.transform.position.y > Application.getHeight() * 1.2) {
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
        o.addComponent(e);
        o.setAABB(0, 0, 1, 1);
        return o;
    }

    public void render(GraphicsContext g) {
        image.render(g);
    }
}
