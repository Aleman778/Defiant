package ga.game.entity;

import ga.engine.physics.Vector2D;
import ga.engine.scene.GameComponent;
import ga.engine.scene.GameObject;
import ga.engine.scene.GameScene;
import ga.game.PlayerController;

public class AI extends GameComponent {

    GameObject player;

    protected double SPEED = 0.1,
            JUMP_HEIGHT = 7,
            FOLLOW_DISTANCE = 0;
    private double timeSinceLastJump;
    private final boolean canMoveInAir = false;

    public AI(GameScene scene) {
        for (GameObject object : scene.getAllGameObjects()) {
            if (object.getComponent(PlayerController.class) != null) {
                player = object;
            }
        }
    }

    @Override
    public void fixedUpdate() {
        super.fixedUpdate();
        timeSinceLastJump++;
        Vector2D distToPlayer = player.getTransform().toVector2D().sub(gameobject.getTransform().toVector2D());
        if (Math.abs(distToPlayer.x) > FOLLOW_DISTANCE) {
            if (!canMoveInAir) {
                if (gameobject.getBody().isGrounded()) {
                    gameobject.getBody().setVelocity(gameobject.getBody().getVelocity().add(distToPlayer.mul(new Vector2D(1, 0)).normalize().mul(SPEED)));
                }
            }
            else {
                gameobject.getBody().setVelocity(gameobject.getBody().getVelocity().add(distToPlayer.mul(new Vector2D(1, 0)).normalize().mul(SPEED)));
            }
        }
        if (distToPlayer.y < -80 && Math.abs(distToPlayer.x) < 96 && gameobject.getBody().isGrounded() && timeSinceLastJump > 65) {
            gameobject.getBody().setVelocity(gameobject.getBody().getVelocity().add(new Vector2D(0, -JUMP_HEIGHT)));
            timeSinceLastJump = 0;
        }
    }

}
