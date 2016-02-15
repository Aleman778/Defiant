package ga.game;

import ga.game.weapon.WeaponController;
import ga.engine.animation.Animation;
import ga.engine.animation.AnimationController;
import ga.engine.input.Input;
import ga.engine.physics.Body;
import ga.engine.physics.RigidBody;
import ga.engine.physics.Vector2D;
import ga.engine.rendering.ImageRenderer;
import ga.engine.rendering.SpriteRenderer;
import ga.engine.rendering.ParticleEmitter;
import ga.engine.resource.ResourceManager;
import ga.engine.scene.GameComponent;
import ga.engine.scene.GameObject;
import java.util.List;
import java.util.Map;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class PlayerController extends GameComponent {
    
    public double SPEED = 0.1;
    private double JUMP_HEIGHT = 6;
    private final float HEAD_ROTATION_LIMIT_UPPER = -20;
    private final float HEAD_ROTATION_LIMIT_LOWER = 45;
    public float armRotation, headRotation;
    private RigidBody body;
    private Vector2D armPivot;
    public GameObject head, arm;
    private SpriteRenderer renderable;
    private AnimationController AC;
    private ParticleEmitter landEmitter;

    @Override
    public void start() {
        gameobject.setAABB(0, 0, 32, 62);
        transform.pivot = new Vector2D(16, 62);

        body = (RigidBody) getComponent(RigidBody.class);
        body.addNoCollide(3);
        head = new GameObject("player_head", 7, 2);
        head.addComponent(new ImageRenderer("textures/player/Red_Player_Head.png"));
        head.getTransform().pivot = new Vector2D(9, 16);
        head.getTransform().depth = -2;

        arm = new GameObject("player_arm", 12, 10);
        WeaponController weaponController = new WeaponController();
        weaponController.player = this;
        arm.addComponent(weaponController);
        armPivot = new Vector2D(4, 15);
        arm.getTransform().pivot = armPivot;
        arm.getTransform().depth = -3;
        arm.addComponent(new AnimationController());

        gameobject.addChild(head);
        gameobject.addChild(arm);

        renderable = (SpriteRenderer) getComponent(SpriteRenderer.class);
        AC = (AnimationController) getComponent(AnimationController.class);
        Animation idleAnim = new Animation(0) {

            @Override
            public void animate(int frame) {
                renderable.setOffsetX(0);
                renderable.setOffsetY(0);
                renderable.setSprite(ResourceManager.getImage("textures/player/Player_Idle.png"));
                head.getTransform().position.y = 2;
            }
        };
        Animation walkAnim = new Animation(20, 0.1) {

            @Override
            public void animate(int frame) {
                setSpeed(Math.abs(body.getVelocity().x) / 3);
                setSpeed(getSpeed() * body.velocity.normalize().x * transform.scale.x);
                renderable.setOffsetX(32 * frame);
                renderable.setOffsetY(0);
                renderable.setSprite(ResourceManager.getImage("textures/player/Player_Walking.png"));

                if (frame == 1) {
                    head.getTransform().position.y = 3;
                } else {
                    head.getTransform().position.y = 2;
                }
            }
        };
        Animation jumpAnim = new Animation(1) {
            @Override
            public void animate(int frame) {
                renderable.setOffsetX(0);
                renderable.setOffsetY(0);
                renderable.setSprite(ResourceManager.getImage("textures/player/Player_Jumping.png"));
            }
        };
        AC.addAnimation("idle", idleAnim);
        AC.addAnimation("walking", walkAnim);
        AC.addAnimation("jumping", jumpAnim);
    }

    @Override
    public void fixedUpdate() {
        //Walking
        Vector2D movement = new Vector2D();
        movement.x += (Input.getKey(KeyCode.A) == true) ? -1 : 0;
        movement.x += (Input.getKey(KeyCode.D) == true) ? 1 : 0;
        movement.x *= SPEED;
        if (movement.x != 0) {
            body.setFriction(0);
        } else {
            body.setFriction(0.2);
        }

        //Set animation state
        if (movement.x == 0) {
            AC.setState("idle");
        } else {
            AC.setState("walking");
        }

        //Jumping
        if (body.isGrounded()) {
            movement.y += (Input.getKey(KeyCode.SPACE) == true) ? -1 : 0;
            movement.y *= JUMP_HEIGHT;
        } else {
            AC.setState("jumping");
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
        } else {
            arm.transform.rotation = 0;
            arm.transform.scale.x = 1;
            head.transform.scale.x = 1;
            transform.scale.x = 1;
            headRotation = Math.max(armRotation, HEAD_ROTATION_LIMIT_UPPER);
            headRotation = Math.min(headRotation, HEAD_ROTATION_LIMIT_LOWER);
        }
        arm.getTransform().rotation = armRotation;
        head.getTransform().rotation = headRotation;
    }

    @Override
    public void onCollision(Body body, Body otherBody, Vector2D normal, double penetration, int id) {
        if (otherBody.getMass() == 0 && otherBody.getVelocity().sub(gameobject.getBody().getVelocity()).y > 2 && normal.equals(new Vector2D(0, 1))) {
            if (otherBody.gameobject.getComponent(ImageRenderer.class) != null) {
                Image i = ((ImageRenderer) otherBody.gameobject.getComponent(ImageRenderer.class)).getImage();
                int size = 5;
                landEmitter.setSprite(ParticleEmitter.cropImage(i, Math.max((int) (Math.random() * i.getWidth() - size), 0), 0, size, size));
            }
            landEmitter.fire(10);
        }
    }

    public void initParticles() {
        landEmitter = ParticleEmitter.loadXML("particles/systems/LandEmitter.psystem");
        gameobject.addComponent(landEmitter);
        landEmitter.object.transform.position = new Vector2D(16, 50);
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
        return new PlayerController();
    }
}
