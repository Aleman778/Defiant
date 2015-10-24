package ga.engine.physics;

import com.sun.javafx.geom.Rectangle;
import ga.engine.rendering.Particle;
import ga.engine.scene.Transform2D;

public class ParticleBody extends RigidBody {

    Particle particle;

    public ParticleBody(Particle particle, double mass, int id) {
        super(mass, id);
        this.particle = particle;
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
        onCollision(otherBody, new Vector2D(), 0);
        return null;
    }

    @Override
    public void onCollision(Body body, Vector2D normal, double penetration) {
        if (body.getID() == 1) {
            velocity = new Vector2D();
            mass = 0;
        }
    }

    @Override
    public void awoke() {
        gameobject.setAABB((int) particle.size / 2, (int) particle.size / 2, 1, 1);
        transform = new Transform2D(gameobject);
    }
}
