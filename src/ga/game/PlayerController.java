package ga.game;

import ga.engine.input.Input;
import ga.engine.physics.Body;
import ga.engine.physics.RigidBody;
import ga.engine.physics.Vector2D;
import ga.engine.physics.Vector3D;
import ga.engine.rendering.ImageRenderer;
import ga.engine.rendering.ParticleEmitter;
import ga.engine.scene.GameComponent;
import ga.engine.scene.GameObject;
import java.util.HashMap;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class PlayerController extends GameComponent {
    
    private RigidBody body;
    private final double SPEED = 0.2;
    private final double JUMP_HEIGHT = 6;
    private final float HEAD_ROTATION_LIMIT_UPPER = -20;
    private final float HEAD_ROTATION_LIMIT_LOWER = 45;
    private Vector2D movement, armPivot = new Vector2D(4, 15);
    private GameObject head, arm;
    private float armRotation, headRotation;
    private int selectedWeapon;
    private HashMap<Integer, Image> weapons = new HashMap<Integer, Image>() {
        {
            put(0, new Image("ga/game/Arm_Med_vapen1.png"));
            put(1, new Image("ga/game/Arm_Med_vapen2.png"));
        }
    };
    
    @Override
    public void start() {
        body = (RigidBody) getComponent(RigidBody.class);
        head = new GameObject(0, -21, 0)
                .addComponent(new ImageRenderer("ga/game/Red_Player_Head.png"));
        gameobject.addChild(head);
        ((ImageRenderer)head.getRenderable()).setPivot(new Vector2D(9, 16));
        arm = new GameObject(24, -10, 0)
                .addComponent(new ImageRenderer(weapons.get(0)));
        gameobject.addChild(arm);
        ((ImageRenderer)arm.getRenderable()).setPivot(armPivot);
    }

    @Override
    public void fixedUpdate() {arm.getTransform().scale = new Vector3D(1, 1, 1);
        movement = new Vector2D((Input.getKey(KeyCode.A) == true ? -1 : 0 + (Input.getKey(KeyCode.D) == true ? 1 : 0)) * SPEED,
        ((Input.getKeyPressed(KeyCode.SPACE) == true && body.isGrounded()) ? -1 : 0) * JUMP_HEIGHT);
        if (movement.y != 0) {
            if (getComponent(ParticleEmitter.class) != null) {
                ((ParticleEmitter)getComponent(ParticleEmitter.class)).fire();
            }
        }
        body.setVelocity(body.getVelocity().add(movement));
        if (Math.abs(body.getVelocity().x) > body.SPEED_LIMIT) {
            body.getVelocity().x = Math.signum(body.getVelocity().x) * body.SPEED_LIMIT;
        }
        
        Vector2D diff = Input.getMousePosition().sub(gameobject.transform.localPosition().toVector2D());
        armRotation = (float) Math.toDegrees(Math.atan(diff.y / diff.x));
        headRotation = armRotation;
        if (diff.x < 0) {
            armRotation = 360 - armRotation;
            transform.rotation.y = 180;
            headRotation = Math.max(armRotation, 360 + HEAD_ROTATION_LIMIT_UPPER);
            headRotation = Math.min(headRotation, 360 + HEAD_ROTATION_LIMIT_LOWER);
        }
        else {
            arm.getTransform().rotation.x = 0;
            transform.rotation.y = 0;
            headRotation = Math.max(armRotation, HEAD_ROTATION_LIMIT_UPPER);
            headRotation = Math.min(headRotation, HEAD_ROTATION_LIMIT_LOWER);
        }
        arm.getTransform().rotation.z = armRotation;
        head.getTransform().rotation.z = headRotation;
        
        if (Input.getScrollPosition() != 0) {
            selectedWeapon += Input.getScrollPosition();
            if (selectedWeapon < 0) {
                selectedWeapon = weapons.size() - 1;
            }
            if (selectedWeapon > weapons.size() - 1) {
                selectedWeapon = 0;
            }
            ((ImageRenderer) arm.getRenderable()).setImage(weapons.get(selectedWeapon));
            ((ImageRenderer) arm.getRenderable()).setPivot(armPivot);
            Input.scrollPosition = 0;
        }
    }
    
    public Vector2D getMovement() {
        return movement;
    }

    @Override
    public void onCollision(Body body, Vector2D normal, double penetration) {
        System.out.println("Enter");
    }
}
