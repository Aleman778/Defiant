package ga.game.entity;

import ga.engine.physics.Body;
import ga.engine.physics.Vector2D;
import ga.engine.scene.GameComponent;
import static ga.engine.scene.GameComponent.ATTRIBUTES_NONE;
import ga.engine.scene.GameObject;
import ga.engine.scene.GameScene;
import ga.game.PlayerController;
import java.util.List;
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

    public AI() {
    }
    
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
        Body b = (Body) getComponent(Body.class);
        if (b != null) {
            b.addCollide(5);
        }
    }

    @Override
    public void fixedUpdate() {
        super.fixedUpdate();
        timeSinceLastJump++;
        Vector2D velocity = gameobject.getBody().getVelocity();
        Vector2D distToPlayer = player.getTransform().position.sub(gameobject.getTransform().position.add(new Vector2D(gameobject.getAABB().width / 2, 0)));
        if (Math.abs(distToPlayer.x) > FOLLOW_DISTANCE) {
            if (!canMoveInAir) {
                if (gameobject.getBody().isGrounded()) {
                    gameobject.getBody().setVelocity(velocity.add(distToPlayer.mul(new Vector2D(1, 0)).normalize().mul(SPEED)));
                }
            } else {
                if (!gameobject.getBody().isGrounded()) {
                    gameobject.getBody().setVelocity(velocity.add(distToPlayer.mul(new Vector2D(1, 0)).normalize().mul(AIR_SPEED)));
                } else {
                    gameobject.getBody().setVelocity(velocity.add(distToPlayer.mul(new Vector2D(1, 0)).normalize().mul(SPEED)));
                }
            }
        }
        if (distToPlayer.y < -64 && Math.abs(distToPlayer.x) < 96 && gameobject.getBody().isGrounded() && timeSinceLastJump > 65) {
            gameobject.getBody().setVelocity(velocity.add(new Vector2D(0, -JUMP_HEIGHT)));
            timeSinceLastJump = 0;
        }
        if (takingDamage > 0) {
            if (Math.abs(gameobject.getBody().getVelocity().x) > 0.5) {
                gameobject.getBody().setVelocity(velocity.add(new Vector2D(0, takingDamage * 0.25)));
            }
            takingDamage = 0;
        }
    }

    @Override
    public List<String> getAttributes() {
        return ATTRIBUTES_NONE;
    }

    @Override
    public void setAttributes(Map<String, String> attributes) {
    }
    
    @Override
    public GameComponent instantiate() {
        return new AI();
    }
}
