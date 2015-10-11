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
    private float armRotation;
    
    @Override
    public void start() {
        body = (RigidBody) getComponent(RigidBody.class);
        arm = new GameObject(12, -8, 0)
                .addComponent(new ImageRenderer("textures/Red_arm2ss.png"));
        gameobject.addChild(arm);
        ((ImageRenderer)arm.getRenderable()).setPivot(new Vector2D(5, 4));
    }

    @Override
    public void fixedUpdate() {arm.getTransform().scale = new Vector3D(1, 1, 1);
        movement = new Vector2D((Input.getKey(KeyCode.A) == true ? -1 : 0 + (Input.getKey(KeyCode.D) == true ? 1 : 0)) * SPEED,
        ((Input.getKeyPressed(KeyCode.SPACE) == true && body.isGrounded()) ? -1 : 0) * JUMP_HEIGHT);
        body.setVelocity(body.getVelocity().add(movement));
        if (Math.abs(body.getVelocity().x) > body.SPEED_LIMIT) {
            body.getVelocity().x = Math.signum(body.getVelocity().x) * body.SPEED_LIMIT;
        }
        
        Vector2D diff = Input.getMousePosition().sub(gameobject.transform.localPosition().toVector2D());
        armRotation = (float) Math.toDegrees(Math.atan(diff.y / diff.x));
        if (diff.x < 0) {
            armRotation = 360 - armRotation;
            transform.rotation.y = 180;
        }
        else {
            arm.getTransform().rotation.x = 0;
            transform.rotation.y = 0;
        }
        arm.getTransform().rotation.z = armRotation;
    }
    
    public Vector2D getMovement() {
        return movement;
    }

    @Override
    public void onCollision(Body body, Vector2D normal, double penetration) {
        System.out.println("Enter");
    }
}
