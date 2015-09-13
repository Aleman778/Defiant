package ga.engine.physics;

import ga.engine.scene.GameComponent;
import ga.engine.scene.GameObject;
import ga.engine.scene.GameScene;

public class RigidBody extends Body {

    public Vector2D size;
    private final GameScene scene;

    public RigidBody(GameScene scene, Vector2D size, double mass) {
        this.scene = scene;
        this.size = size;
        this.mass = mass;
    }

    public void physicsUpdate() {
        if (mass != 0) {
            velocity = velocity.add(scene.gravity);
        }
        
        transform.translate(velocity.x, velocity.y, 0);
        if (velocity.x + velocity.y != 0) {
            for (GameObject otherObject : scene.getAllGameObjects()) {
                for (GameComponent component : otherObject.getAllComponents()) {
                    if (component instanceof RigidBody) {
                        RigidBody body = (RigidBody) component;
                        Vector3D diff = body.transform.localPosition().sub(transform.localPosition());
                        double overlapX = (size.x / 2) + body.size.x / 2 - Math.abs(diff.x);
                        if (overlapX > 0) {
                            double overlapY = (size.y / 2) + body.size.y / 2 - Math.abs(diff.y);
                            if (overlapY > 0) {
                                Vector2D dir;
                                double dist;
                                if (overlapX < overlapY) {
                                    if (diff.x < 0) {
                                        dir = new Vector2D(-1, 0);
                                    }
                                    else {
                                        dir = new Vector2D(1, 0);
                                    }
                                    dist = overlapX;
                                }
                                else {
                                    if (diff.y < 0) {
                                        dir = new Vector2D(0, -1);
                                    }
                                    else {
                                        dir = new Vector2D(0, 1);
                                    }
                                    dist = overlapY;
                                }
                                Vector2D vel = body.velocity.sub(velocity);
                                double bounce = Math.min(softness, body.softness);
                                double velNorm = vel.dot(dir);
                                double impulse = -(1 + bounce) * velNorm;
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
                                Vector2D impulseVector = dir.scale(impulse);
                                double totalMass = mass + body.mass;
                                velocity = velocity.sub(impulseVector.scale(mass / totalMass));
                                body.velocity = body.velocity.add(impulseVector.scale(body.mass / totalMass));
                                
                                final double percent = 0.8;
                                final double tolerance = 0.01;
                                Vector2D correction = dir.scale(percent * dist - tolerance / (invMass + otherInvMass));
                                velocity = velocity.sub(correction.scale(invMass));
                                body.velocity = body.velocity.add(correction.scale(otherInvMass));
                                
                                Vector2D frictionVector = vel.sub(dir.scale(vel.dot(dir))).scale(Math.max(friction, body.friction));
                                velocity = velocity.add(frictionVector);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void update() {
        super.update();
        physicsUpdate();
    }

}
