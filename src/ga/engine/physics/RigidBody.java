package ga.engine.physics;

import com.sun.javafx.geom.Rectangle;
import ga.engine.scene.GameComponent;
import ga.engine.scene.GameScene;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RigidBody extends Body {

    private static final List<String> ATTRIBUTES = new ArrayList<>();

    static {
        ATTRIBUTES.add("Mass");
        ATTRIBUTES.add("Body ID");
        ATTRIBUTES.add("Friction");
        ATTRIBUTES.add("Softness");
    }

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
    public HashMap<String, Object> physicsUpdate(Body otherBody) {
        if (gameobject == null) {
            return null;
        }
        if (velocity.x + velocity.y == 0) {
            return null;
        }
        if (otherBody.gameobject.parent == null) {
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

        Vector2D diff = otherBody.transform.position.add(new Vector2D(otherBounds.width / 2, otherBounds.height / 2)).sub(transform.position.add(new Vector2D(bounds.width / 2, bounds.height / 2)));
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
        //Collision Event
        onCollision(this, otherBody, normal, penetration, otherBody.getID());
        return new HashMap<String, Object>() {
            {
                put("body", otherBody);
                put("normal", normal);
                put("penetration", penetration);
                put("id", otherBody.getID());
            }
        };
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void onCollision(Body body, Body otherBody, Vector2D normal, double penetration, int id) {
        Vector2D vel = otherBody.velocity.sub(velocity);
        double bounce = Math.min(softness, otherBody.softness);
        double velNorm = vel.dot(normal);
        double impulse = -(1 + bounce) * velNorm;
        double invMass, otherInvMass;
        if (mass == 0) {
            invMass = 0;
        } else {
            invMass = 1 / mass;
        }
        if (otherBody.mass == 0) {
            otherInvMass = 0;
        } else {
            otherInvMass = 1 / otherBody.mass;
        }
        double percent = 0;
        if (!getNoCollide().contains(otherBody.getID()) && !otherBody.getNoCollide().contains(getID())) {
            if (getID() == otherBody.getID() || getCollide().contains(otherBody.getID()) || otherBody.getCollide().contains(id)) {
                impulse = impulse / (invMass + otherInvMass);
                if (bounce == 0) {
                    if (Math.abs(normal.x) == 1) {
                        velocity = velocity.mul(new Vector2D(0, 1));
                    }
                    if (Math.abs(normal.y) == 1) {
                        velocity = velocity.mul(new Vector2D(1, 0));
                    }
                    impulse = penetration / 2;
                }
                Vector2D impulseVector = normal.mul(impulse);
                double totalMass = mass + otherBody.mass;
                if (impulseVector.normalize().x == -1 || impulseVector.normalize().y == -1) {
                    velocity = velocity.add(impulseVector.mul(mass / totalMass));
                    otherBody.velocity = otherBody.velocity.sub(impulseVector.mul(otherBody.mass / totalMass));
                }
                velocity = velocity.sub(impulseVector.mul(mass / totalMass));
                otherBody.velocity = otherBody.velocity.add(impulseVector.mul(otherBody.mass / totalMass));

                if (bounce != 0 || (Math.abs(normal.x) == 1)) {
                    percent = 0.4;
                } else {
                    percent = 0.1;
                }

                final double tolerance = 0.01;
                Vector2D correction = normal.mul(percent * penetration - tolerance / (invMass + otherInvMass));
                velocity = velocity.sub(correction);
                otherBody.velocity = otherBody.velocity.add(correction.mul(otherInvMass));
                if (normal.equals(GameScene.gravity.normalize())) {
                    Vector2D frictionVector = vel.sub(normal.mul(vel.dot(normal))).mul(friction);
                    velocity = velocity.add(frictionVector);
                }
                if (normal.equals(new Vector2D(0, 1))) {
                    setGrounded(true);
                }
            }
        }
        if (-(1 + bounce) * velNorm > 0.5 || !singleEvent) {
            for (GameComponent comp : gameobject.getAllComponents()) {
                if (comp.getClass() != RigidBody.class) {
                    comp.onCollision(this, otherBody, normal, penetration, id);
                }
            }
        }
    }

    @Override
    public List<String> getAttributes() {
        return ATTRIBUTES;
    }

    @Override
    public void setAttributes(Map<String, String> attributes) {
        mass = Double.parseDouble(attributes.get("Mass"));
        id = Integer.parseInt(attributes.get("Body ID"));
        friction = Double.parseDouble(attributes.get("Friction"));
        softness = Double.parseDouble(attributes.get("Softness"));
    }

    @Override
    public GameComponent instantiate() {
        return new RigidBody(0);
    }
}
