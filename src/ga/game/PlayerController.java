package ga.game;

import ga.engine.input.Input;
import ga.engine.physics.Body;
import ga.engine.physics.RigidBody;
import ga.engine.physics.Vector2D;
import ga.engine.scene.GameComponent;
import javafx.scene.input.KeyCode;

public class PlayerController extends GameComponent {
    
    private RigidBody body;
    private boolean grounded;
    private final double SPEED = 0.2;
    private final double JUMP_HEIGHT = 4; 
    
    @Override
    public void start() {
        body = (RigidBody) getComponent(RigidBody.class);
        grounded = false;
    }

    @Override
    public void fixedUpdate() {
        int horizontal = (Input.getKey(KeyCode.A) == true ? -1 : 0) +
                (Input.getKey(KeyCode.D) == true ? 1 : 0);
        int jump = Input.getKeyPressed(KeyCode.SPACE) == true ? 1 : 0;
        Vector2D movement = new Vector2D(horizontal * SPEED, -jump * JUMP_HEIGHT);
        body.setVelocity(body.getVelocity().add(movement));
        if (Math.abs(body.getVelocity().x) > body.SPEED_LIMIT) {
            body.getVelocity().x = Math.signum(body.getVelocity().x) * body.SPEED_LIMIT;
        }
    }
    
    private boolean isGrounded() {
        return grounded;
    }

    @Override
    public void onCollisionEnter(Body body, Vector2D normal, double penetration) {
        System.out.println("Enter");
        if (normal.equals(new Vector2D(0.0, 1.0))) {
            grounded = true;
        }
    }

    @Override
    public void onCollisionExit() {
        System.out.println("Exit!");
        grounded = false;
    }
}
