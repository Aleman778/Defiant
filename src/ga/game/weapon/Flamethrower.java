package ga.game.weapon;

import ga.engine.physics.Body;
import ga.engine.physics.SimpleBody;
import ga.engine.physics.Vector2D;
import ga.engine.rendering.ParticleEmitter;
import ga.engine.scene.GameObject;
import ga.game.entity.HealthComponent;
import java.util.HashMap;

public class Flamethrower extends Weapon {

    public Flamethrower(HashMap<String, String> config) {
        super(config);
    }

    public static Weapon instantiate(HashMap<String, String> config) {
        Weapon w = new Flamethrower(config) {
            @Override
            public GameObject fire(double direction) {
                return null;
            }
        };
        w.spark = new ParticleEmitter() {

            @Override
            public void onCollision(Body body, Body otherBody, Vector2D normal, double penetration, int id) {
                if (body instanceof SimpleBody) {
                    if (id == 1) {
                        body.setVelocity(new Vector2D());
                    } else if (body.getVelocity().length() > 3) {
                        body.setVelocity(body.getVelocity().mul(0.5));
                    }
                    HealthComponent otherHealth = (HealthComponent) otherBody.gameobject.getComponent(HealthComponent.class);
                    if (otherHealth != null) {
                        otherHealth.damage(w.damage);
                    }
                }
            }
        };
        w.spark.setConfig(ParticleEmitter.loadXMLConfig("particles/systems/Fire4.psystem"));
        w.spark.noCollide.remove((Object) 3);
        w.spark.collide.add(3);
        w.spark.physics(true);
        w.spark.physicsEvent = true;
        w.spark.id = 5;
        return w;
    }
}
