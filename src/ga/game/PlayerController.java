package ga.game;

import ga.engine.input.Input;
import ga.engine.physics.Body;
import ga.engine.physics.RigidBody;
import ga.engine.physics.Vector2D;
import ga.engine.physics.Vector3D;
import ga.engine.rendering.ImageRenderer;
import ga.engine.scene.GameComponent;
import ga.engine.scene.GameObject;
import javafx.scene.input.KeyCode;

public class PlayerController extends GameComponent {
    
    private RigidBody body;
    private final double SPEED = 0.2;
    private final double JUMP_HEIGHT = 6;
    private Vector2D movement;
    private GameObject arm;
    private Vector3D armRotation;
    
    @Override
    public void start() {
        body = (RigidBody) getComponent(RigidBody.class);
        arm = new GameObject()
                .addComponent(new ImageRenderer("ga/game/RED_Player_Arm.png"));
        gameobject.addChild(arm);
        ((ImageRenderer)arm.getRenderable()).setPivot(new Vector2D(8, 3));
    }

    @Override
    public void fixedUpdate() {
        movement = new Vector2D((Input.getKey(KeyCode.A) == true ? -1 : 0 + (Input.getKey(KeyCode.D) == true ? 1 : 0)) * SPEED,
        ((Input.getKeyPressed(KeyCode.SPACE) == true && body.isGrounded()) ? -1 : 0) * JUMP_HEIGHT);
        body.setVelocity(body.getVelocity().add(movement));
        if (Math.abs(body.getVelocity().x) > body.SPEED_LIMIT) {
            body.getVelocity().x = Math.signum(body.getVelocity().x) * body.SPEED_LIMIT;
        }
        armRotation = new Vector3D(0, 0, (Input.getKey(KeyCode.W) == true ? -5 : 0) + (Input.getKey(KeyCode.S) == true ? 5 : 0));
        arm.getTransform().rotate(armRotation);
    }
    
    public Vector2D getMovement() {
        return movement;
    }

    @Override
    public void onCollision(Body body, Vector2D normal, double penetration) {
        System.out.println("Enter");
    }
}
