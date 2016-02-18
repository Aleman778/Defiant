package ga.game.weapon;

import ga.engine.physics.Body;
import ga.engine.physics.Vector2D;
import ga.engine.rendering.ParticleEmitter;
import ga.engine.scene.GameObject;
import ga.game.entity.HealthComponent;
import java.util.HashMap;

public class Nitrogenthrower extends Weapon {

    public Nitrogenthrower(HashMap<String, String> config) {
        super(config);
    }

    public static Weapon instantiate(HashMap<String, String> config) {
        Weapon w = new Nitrogenthrower(config) {
            @Override
            public GameObject fire(double direction) {
                return null;
            }
        };
        w.spark = new ParticleEmitter() {

            @Override
            public void onCollision(Body body, Body otherBody, Vector2D normal, double penetration, int id) {
                if (id == 1) {
                    body.setVelocity(new Vector2D());
                } else if (body.getVelocity().length() > 3) {
                    body.setVelocity(body.getVelocity().mul(0.5));
                }
                if (id != 1 && otherBody.SPEED > 0.02) {
                    otherBody.SPEED -= 0.001;
                }
                if (otherBody.SPEED_LIMIT > 1) {
                    otherBody.SPEED_LIMIT -= 0.0001;
                }
                HealthComponent otherHealth = (HealthComponent) otherBody.gameobject.getComponent(HealthComponent.class);
                if (otherHealth != null) {
                    otherHealth.damage(w.damage);
                }
            }
        };
        w.spark.setConfig(ParticleEmitter.loadXMLConfig("particles/systems/Ice.psystem"));
        w.spark.noCollide.remove((Object) 3);
        w.spark.collide.add(3);
        w.spark.physics(true);
        w.spark.physicsEvent = true;
        w.spark.id = 5;
        return w;
    }
}
