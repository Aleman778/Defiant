package ga.engine.physics;

import com.sun.javafx.geom.Rectangle;
import ga.engine.scene.Transform2D;

public class SimpleBody extends RigidBody {

    Rectangle AABB;

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

        Rectangle bounds = gameobject.getAABB();
        Rectangle otherBounds = otherBody.gameobject.getAABB();
        double x = gameobject.transform.position.x,
                y = gameobject.transform.position.y,
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
        if (!getNoCollide().contains(body.getID()) && !body.getNoCollide().contains(getID())) {
            if (getID() == body.getID() || getCollide().contains(body.getID()) || body.getCollide().contains(id)) {
                velocity = new Vector2D();
                mass = 0;
            }
        }
    }

    @Override
    public void awoke() {
        gameobject.setAABB(AABB);
        transform = new Transform2D(gameobject);
    }
}
