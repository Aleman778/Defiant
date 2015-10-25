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
        super(id);
        this.mass = mass;
    }

    @Override
    public Vector2D physicsUpdate(Body otherBody) {
        if (velocity.x + velocity.y == 0) {
            return null;
        }

        Rectangle bounds = gameobject.getAABB();
        Rectangle otherBounds = otherBody.gameobject.getAABB();
        double x = transform.position.x,
                y = transform.position.y,
                xMax = x + bounds.width,
                yMax = y + bounds.height,
                otherX = otherBody.transform.position.x,
                otherY = otherBody.transform.position.y,
                otherXMax = otherX + otherBounds.width,
                otherYMax = otherY + otherBounds.height;

        if (xMax < otherX || x > otherXMax) {
            return null;
        }
        if (yMax < otherY || y > otherYMax) {
            return null;
        }

        Vector2D diff = (otherBody.transform.localPosition().add(new Vector2D(bounds.width / 2, bounds.y / 2))).sub(transform.localPosition().add(new Vector2D(otherBounds.width / 2, otherBounds.y / 2)));
        double overlapX = 0, overlapX2 = 0, overlapY = 0, overlapY2 = 0;

        overlapX = xMax - otherX;
        overlapX2 = otherXMax - x;

        overlapY = yMax - otherY;
        overlapY2 = otherYMax - y;

        if (overlapX < 0) {
            overlapX = 10000;
        }
        if (overlapX2 < 0) {
            overlapX2 = 10000;
        }
        if (overlapY < 0) {
            overlapY = 10000;
        }
        if (overlapY2 < 0) {
            overlapY2 = 10000;
        }
        if (overlapX2 < overlapX) {
            overlapX = overlapX2;
        }
        if (overlapY2 < overlapY) {
            overlapY = overlapY2;
        }

        Vector2D normal;
        double penetration;
        if (overlapX < overlapY) {
            if (overlapY < 1) {
                return null;
            }
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
        if (normal.x != -1 && otherBody.velocity.sub(velocity).dot(normal) > 0) {
            return null;
        }
        //Collision Event
        onCollision(otherBody, normal, penetration);
        return normal;
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
                if (impulseVector.normalize().x == -1 || impulseVector.normalize().y == -1) {
                    velocity = velocity.add(impulseVector.mul(mass / totalMass));
                    body.velocity = body.velocity.sub(impulseVector.mul(body.mass / totalMass));
                }
                velocity = velocity.sub(impulseVector.mul(mass / totalMass));
                body.velocity = body.velocity.add(impulseVector.mul(body.mass / totalMass));

                if (bounce != 0) {
                    percent = 0.4;
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
        if (-(1 + bounce) * velNorm > 0.5) {
            for (GameComponent comp : gameobject.getAllComponents()) {
                if (comp.getClass() != RigidBody.class) {
                    comp.onCollision(body, normal, penetration);
                }
            }
        }
    }
}
