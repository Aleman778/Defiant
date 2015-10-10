package ga.engine.physics;

import com.sun.javafx.geom.Rectangle;
import ga.engine.scene.GameComponent;
import ga.engine.scene.GameScene;

public class RigidBody extends Body {

    public RigidBody(double mass) {
        this.mass = mass;
    }

    public RigidBody(double mass, int id) {
        this.mass = mass;
        this.id = id;
    }

    public RigidBody(double mass, int id, int sink) {
        super(id, sink);
        this.mass = mass;
        this.id = id;
    }

    @Override
    public Vector2D physicsUpdate(Body otherBody) {
        if (velocity.x + velocity.y == 0) {
            return null;
        }

        Rectangle bounds = AABB;
        Rectangle otherBounds = otherBody.AABB;
        Vector3D diff = (otherBody.transform.localPosition().add(new Vector3D(otherBounds.x, otherBounds.y, 0))).
                sub(transform.localPosition().add(new Vector3D(bounds.x, bounds.y, 0)));
        double overlapX = (bounds.width / 2) + (otherBounds.width / 2) - Math.abs(diff.x);
        if (overlapX > 0) {
            double overlapY = (bounds.height / 2) + (otherBounds.height / 2) - Math.abs(diff.y);
            if (overlapY > 0) {
                Vector2D normal;
                double penetration;
                if (overlapX < overlapY) {
                    if (diff.x < 0) {
                        normal = new Vector2D(-1, 0);
                    } else {
                        normal = new Vector2D(1, 0);
                    }
                    penetration = overlapX;
                } else {
                    if (diff.y < 0) {
                        normal = new Vector2D(0, -1);
                    } else {
                        normal = new Vector2D(0, 1);
                    }
                    penetration = overlapY;
                }
                //Collision Event
                onCollision(otherBody, normal, penetration);
                if (penetration > 0.65) {
                    for (GameComponent comp : gameobject.getAllComponents()) {
                        if (comp.getClass() != RigidBody.class) {
                            comp.onCollision(otherBody, normal, penetration);
                        }
                    }
                }
                return normal;
            }
        }
        return null;
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
        double impulse = -(1 + bounce) * velNorm;
        double invMass, otherInvMass;
        if (mass == 0) {
            invMass = 0;
        } else {
            invMass = 1 / mass;
        }
        if (body.mass == 0) {
            otherInvMass = 0;
        } else {
            otherInvMass = 1 / body.mass;
        }
        double percent = 0;
        if (!getNoCollide().contains(body.getID()) && !body.getNoCollide().contains(getID())) {
            if (getID() == body.getID() || getCollide().contains(body.getID())) {
                impulse = impulse / (invMass + otherInvMass);
                if (bounce == 0) {
                    if (Math.abs(normal.x) == 1) {
                        velocity = velocity.mul(new Vector2D(0, 1));
                    }
                    if (Math.abs(normal.y) == 1) {
                        velocity = velocity.mul(new Vector2D(1, 0));
                    }
                    impulse = penetration / 1;
                }
                Vector2D impulseVector = normal.mul(impulse);
                double totalMass = mass + body.mass;
                velocity = velocity.sub(impulseVector.mul(mass / totalMass));
                body.velocity = body.velocity.add(impulseVector.mul(body.mass / totalMass));

                if (bounce != 0) {
                    percent = 0.2;
                }

                final double tolerance = 0.01;
                Vector2D correction = normal.mul(percent * penetration - tolerance / (invMass + otherInvMass));
                velocity = velocity.sub(correction.mul(invMass));
                body.velocity = body.velocity.add(correction.mul(otherInvMass));
                if (normal.equals(GameScene.gravity.normalize())) {
                    Vector2D frictionVector = vel.sub(normal.mul(vel.dot(normal))).mul(Math.max(friction, body.friction));
                    velocity = velocity.add(frictionVector);
                }
                if (normal.equals(new Vector2D(0, 1))) {
                    setGrounded(true);
                }
            }
        }
    }
}
