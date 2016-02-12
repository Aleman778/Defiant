package ga.game.weapon;

import com.sun.javafx.geom.Rectangle;
import ga.engine.core.Application;
import ga.engine.physics.Body;
import ga.engine.physics.SimpleBody;
import ga.engine.physics.Vector2D;
import ga.engine.rendering.ImageRenderer;
import ga.engine.rendering.ParticleEmitter;
import ga.engine.resource.ResourceManager;
import ga.engine.scene.GameComponent;
import ga.engine.scene.GameObject;
import ga.game.entity.HealthComponent;
import java.util.HashMap;
import java.util.Set;

public class GrenadeLauncher extends Weapon {

    public GrenadeLauncher(HashMap<String, String> config) {
        super(config);
    }

    public static Weapon instantiate(HashMap<String, String> config) {
        Weapon w = new Flamethrower(config) {
            @Override
            public GameObject fire(double direction) {
                GameObject o = new GameObject() {
                    boolean isMoving = true;

                    @Override
                    public void fixedUpdate() {
                        super.fixedUpdate();
                        if (getBody().velocity.x == 0 && getBody().velocity.y == 0 && isMoving) {
                            isMoving = false;
                            image.setSprite(ResourceManager.getImage("<EXPLOSION>"));
                            ParticleEmitter impact = new ParticleEmitter();
                            for (GameComponent e : getComponents(ParticleEmitter.class)) {
                                if (((ParticleEmitter) e).getConfig().getValue("mode").equals("MODE_SINGLE")) {
                                    impact = (ParticleEmitter) e;
                                }
                            }
                            if (impact != null) {
                                impact.fire();
                                impact.gravityScale = 0;
                            }
                        }
                    }
                };
                ImageRenderer renderer = new ImageRenderer("textures/grenade.png") {
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
                body.gravity = new Vector2D(0, 0.25);
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
                };
                e.setConfig(ParticleEmitter.loadXMLConfig("particles/systems/WeaponTrail.psystem"));
                e.interpolate(true);
                e.physics(false);
                e.object.transform.position = new Vector2D(0, renderer.getImage().getHeight() / 2);
                o.addComponent(e);
                ParticleEmitter impact = new ParticleEmitter() {
                    @Override
                    public void fixedUpdate() {
                        super.fixedUpdate();
                        if (particles.isEmpty() && gameobject.getBody().getVelocity().length() == 0 && gravityScale == 0) {
                            gameobject.destroy();
                        }
                    }
                };
                impact.setConfig(ParticleEmitter.loadXMLConfig("particles/systems/Explosion.psystem"));
                impact.physics = false;
                o.addComponent(impact);
                o.setAABB(0, 0, 1, 1);
                return o;
            }
        };
        return w;
    }
}
