package ga.game.entity;

import ga.engine.physics.Vector2D;
import ga.engine.scene.GameComponent;
import ga.engine.scene.GameObject;
import ga.engine.scene.GameScene;
import ga.game.PlayerController;

public class AI extends GameComponent {
    
    GameObject player;
    
    private final double SPEED = 0.1;
    private final double JUMP_HEIGHT = 6;
    
    public AI(GameScene scene)
    {
        for (GameObject object : scene.getAllGameObjects()) {
            if (object.getComponent(PlayerController.class) != null) {
                player = object;
            }
        }
    }

    @Override
    public void fixedUpdate() {
        super.fixedUpdate();
        Vector2D distToPlayer = player.getTransform().toVector2D().sub(gameobject.getTransform().toVector2D());
        gameobject.getBody().setVelocity(gameobject.getBody().getVelocity().add(distToPlayer.mul(new Vector2D(1, 0)).normalize().mul(SPEED)));
        if (distToPlayer.y < -80 && gameobject.getBody().isGrounded()) {
            gameobject.getBody().setVelocity(gameobject.getBody().getVelocity().add(new Vector2D(0, -JUMP_HEIGHT)));
        }
    }
    
}
