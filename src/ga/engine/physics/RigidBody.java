package ga.engine.physics;

import ga.engine.scene.GameComponent;
import ga.engine.scene.GameObject;
import ga.engine.scene.GameScene;

public class RigidBody extends Body {

    public Vector2D size;
    private boolean colliding = false;
    private final GameScene scene;

    public RigidBody(GameScene scene, Vector2D size, double mass) {
        this.scene = scene;
        this.size = size;
        this.mass = mass;
    }

    @Override
    public void physicsUpdate() {
        if (mass != 0) {
            velocity = velocity.add(GameScene.gravity);
        }
        
        transform.translate(velocity.x, velocity.y, 0);
        if (velocity.x + velocity.y != 0) {
            for (GameObject otherObject : scene.getAllGameObjects()) {
                if (otherObject.equals(gameobject))
                    continue;
                
                for (GameComponent component : otherObject.getAllComponents()) {
                    if (component instanceof RigidBody) {
                        RigidBody body = (RigidBody) component;
                        Vector3D diff = body.transform.localPosition().sub(transform.localPosition());
                        double overlapX = (size.x / 2) + body.size.x / 2 - Math.abs(diff.x);
                        if (overlapX > 0) {
                            double overlapY = (size.y / 2) + body.size.y / 2 - Math.abs(diff.y);
                            if (overlapY > 0) {
                                Vector2D normal;
                                double penetration;
                                if (overlapX < overlapY) {
                                    if (diff.x < 0) {
                                        normal = new Vector2D(-1, 0);
                                    }
                                    else {
                                        normal = new Vector2D(0, 0);
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
                                
                                if (!colliding) {
                                    colliding = true;
                                    for (GameComponent comp: gameobject.getAllComponents()) {
                                        comp.onCollisionEnter(body, normal, penetration);
                                    }
                                }
                                
                                //Collision Event
                                for (GameComponent comp: gameobject.getAllComponents())
                                    comp.onCollision(body, normal, penetration);
                                
                                continue;
                            }
                        }
                        
                        if (colliding) {
                            colliding = false;
                            for (GameComponent comp: gameobject.getAllComponents()) {
                                onCollisionExit();
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
    }

    @Override
    public void onCollisionExit() {
        colliding = false;
    }

    @Override
    public void onCollisionEnter(Body body, Vector2D normal, double penetration) {
        colliding = true;
    }

    @Override
    public void onCollision(Body body, Vector2D normal, double penetration) {
        Vector2D vel = body.velocity.sub(velocity);
        double e = Math.min(this.softness, body.softness);
        double velNorm = vel.dot(normal);
        double impulse = -(1 + e) * velNorm;
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
        Vector2D correction = normal.mul(percent * Math.max(penetration - tolerance, 0.0) / (invMass + otherInvMass));
        velocity = velocity.sub(correction.mul(invMass));
        body.velocity = body.velocity.add(correction.mul(otherInvMass));

        Vector2D frictionVector = vel.sub(normal.mul(vel.dot(normal))).mul(Math.max(friction, body.friction));
        velocity = velocity.add(frictionVector);
    }  
}
