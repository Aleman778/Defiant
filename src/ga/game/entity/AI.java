package ga.game.entity;

import ga.engine.physics.Body;
import ga.engine.physics.Vector2D;
import ga.engine.scene.GameComponent;
import ga.engine.scene.GameObject;
import ga.engine.scene.GameScene;
import ga.game.PlayerController;
import java.util.Map;

public class AI extends GameComponent {

    GameObject player;

    protected double SPEED = 0.1,
            JUMP_HEIGHT = 7,
            FOLLOW_DISTANCE = 5;
    private double timeSinceLastJump;
    private final boolean canMoveInAir = true;
    private final double AIR_SPEED = 0.05;
    public double takingDamage = 0;
    private Body body;

    public AI(GameScene scene) {
        for (GameObject object : scene.getAllGameObjects()) {
            if (object.getComponent(PlayerController.class) != null) {
                player = object;
            }
        }
    }

    public AI(GameScene scene, double speed, double jump) {
        this(scene);
        SPEED = speed;
        JUMP_HEIGHT = jump;
    }

    @Override
    public void awoke() {
        body = (Body) getComponent(Body.class);
        if (body != null) {
            body.addCollide(5);
        }
    }

    @Override
    public void fixedUpdate() {
        super.fixedUpdate();
        timeSinceLastJump++;
        Vector2D velocity = new Vector2D();
        Vector2D distToPlayer = player.getTransform().position.sub(gameobject.getTransform().position.add(new Vector2D(gameobject.getAABB().width / 2, 0)));
        if (Math.abs(distToPlayer.x) > FOLLOW_DISTANCE) {
            body.setFriction(0);
            if (!canMoveInAir) {
                if (body.isGrounded()) {
                    velocity = velocity.add(distToPlayer.mul(new Vector2D(1, 0)).normalize().mul(SPEED));
                }
            } else {
                if (!body.isGrounded()) {
                    velocity = velocity.add(distToPlayer.mul(new Vector2D(1, 0)).normalize().mul(AIR_SPEED));
                } else {
                    velocity = velocity.add(distToPlayer.mul(new Vector2D(1, 0)).normalize().mul(SPEED));
                }
            }
            body.setVelocity(body.getVelocity().add(velocity));
        } else {
            body.setFriction(0.2);
        }
        if (Math.abs(body.getVelocity().x) > body.SPEED_LIMIT) {
            body.setVelocity(new Vector2D(body.SPEED_LIMIT * Math.signum(body.getVelocity().x), body.getVelocity().y));
        }
        if (distToPlayer.y < -64 && Math.abs(distToPlayer.x) < 96 && body.isGrounded() && timeSinceLastJump > 65) {
            body.setVelocity(body.getVelocity().add(new Vector2D(0, -JUMP_HEIGHT)));
            timeSinceLastJump = 0;
        }
        if (takingDamage > 0) {
            if (Math.abs(body.getVelocity().x) > 0.5) {
                body.setVelocity(body.getVelocity().add(new Vector2D(0, takingDamage * 0.25)));
            }
            takingDamage = 0;
        }
    }

    @Override
    public GameComponent instantiate() {
        return null;
    }

    @Override
    public Map<String, Integer> getVars() {
        return null;
    }

    @Override
    public void xmlVar(String name, String value) {
    }
}
