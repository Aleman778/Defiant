package ga.game.entity;

import ga.engine.animation.Animation;
import ga.engine.animation.AnimationController;
import ga.engine.physics.Body;
import ga.engine.physics.Vector2D;
import ga.engine.rendering.SpriteRenderer;
import ga.engine.resource.ResourceManager;
import ga.engine.scene.GameComponent;
import ga.engine.scene.GameObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AI extends GameComponent {
    
    private static final List<String> ATTRIBUTES = new ArrayList<>();
    
    static {
        ATTRIBUTES.add("AI Speed");
        ATTRIBUTES.add("AI Jump Height");
        ATTRIBUTES.add("AI Follow Distance");
    }
    private GameObject player;

    private final double AIR_SPEED = 0.05;
    protected double speed = 0.1;
    protected double jumpHeight = 7;
    protected double followDistance = 5;
    private double timeSinceLastJump;
    private final boolean canMoveInAir = true;
    public double takingDamage = 0;
    private Body body;
    private SpriteRenderer renderable;
    private AnimationController AC;

    @Override
    public void start() {
        body = (Body) getComponent(Body.class);
        if (body != null) {
            body.addCollide(5);
            body.SPEED = speed;
            body.INIT_SPEED = speed;
        }
        
        renderable = (SpriteRenderer) getComponent(SpriteRenderer.class);
        AC = (AnimationController) getComponent(AnimationController.class);
        Animation idleAnim = new Animation(0) {

            @Override
            public void animate(int frame) {
                renderable.setOffsetX(0);
                renderable.setOffsetY(0);
                renderable.setSprite(ResourceManager.getImage("textures/Ant.png"));
            }
        };
        Animation walkAnim = new Animation(12, 0.1) {

            @Override
            public void animate(int frame) {
                setSpeed(Math.abs(body.getVelocity().x) / 3);
                setSpeed(getSpeed() * body.velocity.normalize().x * transform.scale.x);
                renderable.setOffsetX(100 * frame);
                renderable.setOffsetY(0);
                renderable.setSprite(ResourceManager.getImage("textures/AntWalkAnimation.png"));
            }
        };
        AC.addAnimation("idle", idleAnim);
        AC.addAnimation("walking", walkAnim);
    }

    @Override
    public void sceneStart() {
        player = GameObject.findObjectWithTag("player");
    }

    @Override
    public void fixedUpdate() {
        if (player == null) {
            player = GameObject.findObjectWithTag("player");
        }
        
        speed = body.SPEED;
        if (body.SPEED < body.INIT_SPEED) {
            body.SPEED += 0.0005;
        }
        if (body.SPEED_LIMIT < body.SPEED_LIMIT_INIT) {
            body.SPEED_LIMIT += 0.005;
        }
        timeSinceLastJump++;
        Vector2D velocity = new Vector2D();
        Vector2D distToPlayer = player.getTransform().position.sub(gameobject.getTransform().position.add(new Vector2D(gameobject.getAABB().width / 2, 0)));
        if (Math.abs(distToPlayer.x) > followDistance) {
            body.setFriction(0);
            if (!canMoveInAir) {
                if (body.isGrounded()) {
                    velocity = velocity.add(distToPlayer.mul(new Vector2D(1, 0)).normalize().mul(speed));
                }
            } else {
                if (!body.isGrounded()) {
                    velocity = velocity.add(distToPlayer.mul(new Vector2D(1, 0)).normalize().mul(AIR_SPEED));
                } else {
                    velocity = velocity.add(distToPlayer.mul(new Vector2D(1, 0)).normalize().mul(speed));
                }
            }
            body.setVelocity(body.getVelocity().add(velocity));
        } else {
            body.setFriction(0.2);
        }
        if (Math.abs(body.getVelocity().x) > body.SPEED_LIMIT) {
            body.setVelocity(new Vector2D(body.SPEED_LIMIT * Math.signum(body.getVelocity().x), body.getVelocity().y));
        }
        if (distToPlayer.y < -64 && Math.abs(distToPlayer.x) < 96 && body.isGrounded() && timeSinceLastJump > 65) {
            body.setVelocity(body.getVelocity().add(new Vector2D(0, -jumpHeight)));
            timeSinceLastJump = 0;
        }
        if (takingDamage > 0) {
            if (Math.abs(body.getVelocity().x) > 0.5) {
                body.setVelocity(body.getVelocity().add(new Vector2D(0, takingDamage * 0.25)));
            }
            takingDamage = 0;
        }
        if (Math.abs(body.getVelocity().x) < 0.001) {
         //   AC.setState("idle");
        } else {
            AC.setState("walking");
        }
    }

    @Override
    public List<String> getAttributes() {
        return ATTRIBUTES;
    }

    @Override
    public void setAttributes(Map<String, String> attributes) {
        speed = Double.parseDouble(attributes.get("AI Speed"));
        jumpHeight = Double.parseDouble(attributes.get("AI Jump Height"));
        followDistance = Double.parseDouble(attributes.get("AI Follow Distance"));
    }
    
    @Override
    public GameComponent instantiate() {
        return new AI();
    }
}
