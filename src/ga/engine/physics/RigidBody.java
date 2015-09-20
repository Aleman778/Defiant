package ga.engine.physics;

import ga.engine.scene.GameComponent;
import ga.engine.scene.GameObject;
import ga.engine.scene.GameScene;
import java.awt.Rectangle;

public class RigidBody extends Body {

    private boolean colliding = false;
    private final GameScene scene;

    public RigidBody(GameScene scene, double mass) {
        this.scene = scene;
        this.mass = mass;
    }

    @Override
    public void physicsUpdate() {
        if (mass != 0) {
            velocity = velocity.add(scene.gravity);
        }
        
        colliding = false;

        transform.translate(velocity.x, velocity.y, 0);
        if (velocity.x + velocity.y != 0) {
            for (GameObject otherObject : scene.getAllGameObjects()) {
                if (otherObject.equals(gameobject)) {
                    continue;
                }
                
                for (GameComponent component : otherObject.getAllComponents()) {
                    if (component instanceof RigidBody) {
                        RigidBody body = (RigidBody) component;
                        Rectangle bounds = gameobject.computeAABB();
                        Rectangle otherBounds = otherObject.computeAABB();
                        Vector3D diff = (body.transform.localPosition().add(new Vector3D(otherBounds.x, otherBounds.y, 0))).
                                sub(transform.localPosition().add(new Vector3D(bounds.x, bounds.y, 0)));
                        double overlapX = (bounds.width / 2) + (otherBounds.width / 2) - Math.abs(diff.x);
                        if (overlapX > 0) {
                            double overlapY = (bounds.height / 2) + (otherBounds.height / 2) - Math.abs(diff.y);
                            if (overlapY > 0) {
                                Vector2D normal;
                                double penetration;
                                colliding = true;
                                if (overlapX < overlapY) {
                                    if (diff.x < 0) {
                                        normal = new Vector2D(-1, 0);
                                    }
                                    else {
                                        normal = new Vector2D(1, 0);
                                    }
                                    penetration = overlapX;
                                }
                                else {
                                    if (diff.y < 0) {
                                        normal = new Vector2D(0, -1);
                                    }
                                    else {
                                        normal = new Vector2D(0, 1);
                                    }
                                    penetration = overlapY;
                                }
                                //Collision Event
                                onCollision(body, normal, penetration);
                                if (penetration > 0.65) {
                                    for (GameComponent comp : gameobject.getAllComponents()) {
                                        if (comp.getClass() != RigidBody.class) {
                                            comp.onCollision(body, normal, penetration);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (!colliding) {
                    setGrounded(false);
                }
            }
        }
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void onCollision(Body body, Vector2D normal, double penetration) {
        Vector2D vel = body.velocity.sub(velocity);
        double bounce = Math.min(softness, body.softness);
        double velNorm = vel.dot(normal);
        double impulse = -(0.8 + bounce) * velNorm;
        double invMass, otherInvMass;
        if (mass == 0) {
            invMass = 0;
        }
        else {
            invMass = 1 / mass;
        }
        if (body.mass == 0) {
            otherInvMass = 0;
        }
        else {
            otherInvMass = 1 / body.mass;
        }
        impulse = impulse / invMass + otherInvMass;
        Vector2D impulseVector = normal.mul(impulse);
        double totalMass = mass + body.mass;
        velocity = velocity.sub(impulseVector.mul(mass / totalMass));
        body.velocity = body.velocity.add(impulseVector.mul(body.mass / totalMass));

        final double percent = 0.8;
        final double tolerance = 0.01;
        Vector2D correction = normal.mul(percent * penetration - tolerance / (invMass + otherInvMass));
        velocity = velocity.sub(correction.mul(invMass));
        body.velocity = body.velocity.add(correction.mul(otherInvMass));
        if (normal.equals(scene.gravity.normalize())) {
            Vector2D frictionVector = vel.sub(normal.mul(vel.dot(normal))).mul(Math.max(friction, body.friction));
            velocity = velocity.add(frictionVector);
        }
        if (impulse < 7 && normal.equals(new Vector2D(0, 1))) {
            setGrounded(true);
        }
    }
}
