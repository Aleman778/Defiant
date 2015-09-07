package ga.engine.physics;

import ga.engine.scene.GameComponent;
import ga.engine.scene.GameObject;
import ga.engine.scene.GameScene;

public class RigidBody extends Body {

    public Vector2D size, position;
    private final GameScene scene;
    private final GameObject object;

    public RigidBody(GameObject object, GameScene scene, Vector2D size, double mass) {
        this.scene = scene;
        this.object = object;
        this.size = size;
        this.mass = mass;
        position = new Vector2D(object.getTranslateX(), object.getTranslateY());
    }

    public void physicsUpdate() {
        position = new Vector2D(object.getTranslateX(), object.getTranslateY());
        if (mass != 0) {
            velocity = velocity.add(GameScene.gravity);
        }
        position = position.add(velocity);
        object.setTranslateX(position.dX);
        object.setTranslateY(position.dY);
        if (velocity.dX + velocity.dY != 0) {
            for (GameObject otherObject : scene.getAllGameObjects()) {
                for (GameComponent component : otherObject.getAllComponents()) {
                    if (component instanceof RigidBody) {
                        RigidBody body = (RigidBody) component;
                        Vector2D diff = body.position.sub(position);
                        double overlapX = (size.dX / 2) + body.size.dX / 2 - Math.abs(diff.dX);
                        if (overlapX > 0) {
                            double overlapY = (size.dY / 2) + body.size.dY / 2 - Math.abs(diff.dY);
                            if (overlapY > 0) {
                                Vector2D dir;
                                double dist;
                                if (overlapX < overlapY) {
                                    if (diff.dX < 0) {
                                        dir = new Vector2D(-1, 0);
                                    }
                                    else {
                                        dir = new Vector2D(0, 0);
                                    }
                                    dist = overlapX;
                                }
                                else {
                                    if (diff.dY < 0) {
                                        dir = new Vector2D(0, -1);
                                    }
                                    else {
                                        dir = new Vector2D(0, 1);
                                    }
                                    dist = overlapY;
                                }
                                Vector2D vel = body.velocity.sub(velocity);
                                double softness = Math.min(this.softness, body.softness);
                                double velNorm = vel.dot(dir);
                                double impulse = -(1 + softness) * velNorm;
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
                                Vector2D correction = dir.scale(percent * Math.max(dist - tolerance, 0.0) / (invMass + otherInvMass));
                                velocity = velocity.sub(correction.scale(invMass));
                                body.velocity = body.velocity.add(correction.scale(otherInvMass));
                                
                                Vector2D frictionVector = vel.sub(dir.scale(vel.dot(dir))).scale(friction);
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
