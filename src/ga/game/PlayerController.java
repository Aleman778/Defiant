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
        Vector2D movement = new Vector2D(Input.getAxis("horizontal") * SPEED, 0.0);
        if (isGrounded() && Input.getKeyPressed(KeyCode.SPACE)) {
            movement = movement.add(new Vector2D(0, -JUMP_HEIGHT));
        }
        
        body.setVelocity(body.getVelocity().add(movement));
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
