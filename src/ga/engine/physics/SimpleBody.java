package ga.engine.physics;

import com.sun.javafx.geom.Rectangle;
import ga.engine.scene.GameComponent;
import ga.engine.scene.Transform2D;

public class SimpleBody extends RigidBody {

    Rectangle AABB;
    public boolean eventOnly = false;

    public SimpleBody(Rectangle AABB, double mass, int id) {
        super(mass, id);
        this.AABB = AABB;
    }

    @Override
    public Vector2D physicsUpdate(Body otherBody) {
        if (velocity.x + velocity.y == 0) {
            return null;
        }
        if (otherBody.gameobject.parent == null) {
            return null;
        }
        if (this == otherBody) {
            return null;
        }
        if (otherBody instanceof SimpleBody) {
            return null;
        }

        Transform2D t;
        Rectangle bounds;
        if (gameobject.getBody() instanceof SimpleBody) {
            t = gameobject.transform;
            bounds = gameobject.getAABB();
        } else {
            t = transform;
            bounds = new Rectangle(1, 1);
        }

        Rectangle otherBounds = otherBody.gameobject.getAABB();
        double x = t.position.x,
                y = t.position.y,
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
        onCollision(otherBody, new Vector2D(), 0);
        return null;
    }

    @Override
    public void onCollision(Body body, Vector2D normal, double penetration) {
        if ((!getNoCollide().contains(body.getID()) && !body.getNoCollide().contains(getID()))) {
            if (getID() == body.getID() || getCollide().contains(body.getID()) || body.getCollide().contains(id)) {
                if (!eventOnly) {
                    velocity = new Vector2D();
                    mass = 0;
                }
                for (GameComponent comp : gameobject.getAllComponents()) {
                    if (comp.getClass() != SimpleBody.class) {
                        comp.onCollision(body, normal, penetration);
                    }
                }
            }
        }
    }

    @Override
    public void awoke() {
        gameobject.setAABB(AABB);
        transform = new Transform2D(gameobject);
    }
}
