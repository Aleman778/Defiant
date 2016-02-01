package ga.game.weapon;

import ga.engine.physics.Body;
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
            public void onCollision(Body body, Vector2D normal, double penetration, int id) {
                HealthComponent otherHealth = (HealthComponent) body.gameobject.getComponent(HealthComponent.class);
                if (otherHealth != null) {
                    otherHealth.damage(w.damage);
                }
            }
        };
        w.spark.setConfig(ParticleEmitter.loadXMLConfig("particles/systems/Fire4.psystem"));
        w.spark.physics(true);
        w.spark.physicsEvent = false;
        w.spark.id = 5;
        return w;
    }
}
