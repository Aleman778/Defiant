package ga.engine.physics;

import com.sun.javafx.geom.Rectangle;
import ga.engine.scene.GameComponent;
import ga.engine.scene.Transform2D;
import java.util.HashMap;

public class SimpleBody extends RigidBody {

    Rectangle AABB;
    public boolean eventOnly = false;

    public SimpleBody(Rectangle AABB, double mass, int id) {
        super(mass, id);
        this.AABB = AABB;
    }

    @Override
    public HashMap<String, Object> physicsUpdate(Body otherBody) {
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
        onCollision(this, otherBody, new Vector2D(), 0, otherBody.getID());
        return null;
    }

    @Override
    public void onCollision(Body body, Body otherBody, Vector2D normal, double penetration, int id) {
        if ((!getNoCollide().contains(otherBody.getID()) && !otherBody.getNoCollide().contains(getID()))) {
            if (getID() == otherBody.getID() || getCollide().contains(otherBody.getID()) || otherBody.getCollide().contains(id)) {
                if (!eventOnly) {
                    velocity = new Vector2D();
                    mass = 0;
                }
                for (GameComponent comp : gameobject.getAllComponents()) {
                    if (comp.getClass() != SimpleBody.class) {
                        comp.onCollision(this, otherBody, normal, penetration, id);
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
