package ga.game;

import ga.engine.input.Input;
import ga.engine.physics.Body;
import ga.engine.physics.RigidBody;
import ga.engine.physics.Vector2D;
import ga.engine.rendering.ImageRenderer;
import ga.engine.scene.GameComponent;
import ga.engine.scene.GameObject;
import java.util.HashMap;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class PlayerController extends GameComponent {
    
    private final double SPEED = 0.2;
    private final double JUMP_HEIGHT = 6;
    private final float HEAD_ROTATION_LIMIT_UPPER = -20;
    private final float HEAD_ROTATION_LIMIT_LOWER = 45;
    private float armRotation, headRotation;
    private int selectedWeapon;
    private RigidBody body;
    private Vector2D armPivot;
    private GameObject head, arm;
    private ImageRenderer armRenderable;
    private HashMap<Integer, Image> weapons = new HashMap<Integer, Image>() {
        {
            put(0, new Image("textures/player/Arm_Med_vapen1.png"));
            put(1, new Image("textures/player/Arm_Med_vapen2.png"));
        }
    };
    
    @Override
    public void start() {
        gameobject.setAABB(0, 0, 32, 62);
        transform.pivot = new Vector2D(16, 62);
                
        body = (RigidBody) getComponent(RigidBody.class);
        head = new GameObject(7, 2);
        head.addComponent(new ImageRenderer("textures/player/Red_Player_Head.png"));
        head.getTransform().pivot = new Vector2D(9, 16);
        
        arm = new GameObject(12, 10);
        arm.addComponent(new ImageRenderer(weapons.get(0)));
        armPivot = new Vector2D(4, 15);
        arm.getTransform().pivot = armPivot;
        armRenderable = (ImageRenderer) arm.getComponent(ImageRenderer.class);
        
        gameobject.addChild(head);
        gameobject.addChild(arm);
        
    }

    @Override
    public void fixedUpdate() {
        //Walking
        Vector2D movement = new Vector2D();
        movement.x += (Input.getKey(KeyCode.A) == true) ? -1 : 0;
        movement.x += (Input.getKey(KeyCode.D) == true) ? 1 : 0;
        movement.x *= SPEED;
        
        //Jumping
        if (body.isGrounded()) {
            movement.y += (Input.getKey(KeyCode.SPACE) == true) ? -1 : 0;
            movement.y *= JUMP_HEIGHT;
        } 
        
        //Apply velocity
        if (movement.y != 0) {
            body.setVelocity(body.getVelocity().mul(new Vector2D(1, 0)));
        }
        if (Math.abs(body.getVelocity().add(movement).x) < body.SPEED_LIMIT) {
            body.setVelocity(body.getVelocity().add(movement));
        } else {
            body.getVelocity().x = Math.signum(body.getVelocity().x) * body.SPEED_LIMIT;
            body.getVelocity().y += movement.y;
        }
        
        //Looking around
        Vector2D diff = Input.getMousePosition().sub(arm.getTransform().localPosition().add(arm.getTransform().pivot));
        armRotation = (float) Math.toDegrees(Math.atan(diff.y / diff.x));
        headRotation = armRotation;
        if (diff.x < 0) {
            armRotation = armRotation - 360;
            headRotation = armRotation - 360;
            arm.transform.scale.x = -1;
            head.transform.scale.x = -1;
            transform.scale.x = -1;
            headRotation = Math.min(armRotation, -HEAD_ROTATION_LIMIT_UPPER - 360);
            headRotation = Math.max(headRotation, -HEAD_ROTATION_LIMIT_LOWER - 360);
        }
        else {
            arm.transform.rotation = 0;
            arm.transform.scale.x = 1;
            head.transform.scale.x = 1;
            transform.scale.x = 1;
            headRotation = Math.max(armRotation, HEAD_ROTATION_LIMIT_UPPER);
            headRotation = Math.min(headRotation, HEAD_ROTATION_LIMIT_LOWER);
        }
        arm.getTransform().rotation = armRotation;
        head.getTransform().rotation = headRotation;
        
        //Weapon Select
        if (Input.getScrollPosition() != 0) {
            selectedWeapon += Input.getScrollPosition();
            if (selectedWeapon < 0) {
                selectedWeapon = weapons.size() - 1;
            }
            if (selectedWeapon > weapons.size() - 1) {
                selectedWeapon = 0;
            }
            armRenderable.setImage(weapons.get(selectedWeapon));
            arm.transform.pivot = armPivot;
            Input.scrollPosition = 0;
        }
    }

    @Override
    public void onCollision(Body body, Vector2D normal, double penetration) {
        System.out.println("Enter");
    }
}
