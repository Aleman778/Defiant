package ga.engine.scene;

import ga.engine.animation.AnimationController;
import ga.engine.physics.Body;
import ga.engine.physics.RigidBody;
import ga.engine.physics.SimpleBody;
import ga.engine.physics.Vector2D;
import ga.engine.rendering.ImageRenderer;
import ga.engine.rendering.ParticleEmitter;
import ga.engine.rendering.SpriteRenderer;
import ga.engine.resource.ResourceManager;
import ga.game.HUD;
import ga.game.PlayerController;
import ga.game.weapon.WeaponController;
import ga.game.entity.AI;
import ga.game.entity.FriendlyAI;
import ga.game.entity.HealthComponent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.scene.canvas.GraphicsContext;

public abstract class GameComponent {
    
    private static final HashMap<String, GameComponent> components = new HashMap<>();
    
    public static final List<String> ATTRIBUTES_NONE = new ArrayList<>();
    
    static {
        //Init all GameComponents
        components.put("ImageRenderer", new ImageRenderer(ResourceManager.DEFAULT_IMAGE));
        components.put("SpriteRenderer", new SpriteRenderer(ResourceManager.DEFAULT_IMAGE, 32, 32));
        components.put("AnimationController", new AnimationController());
        components.put("HealthComponent", new HealthComponent());
        components.put("PlayerController", new PlayerController());
        components.put("WeaponController", new WeaponController());
        components.put("SimpleBody", new SimpleBody(null, 0, 0));
        components.put("ParticleEmitter", new ParticleEmitter());
        components.put("Rigidbody", new RigidBody(0));
        components.put("HUD", new HUD());
        components.put("AI", new AI());
    }
    
    public Transform2D transform;
    public GameObject gameobject;
    
    public GameComponent() {
        
    }
    
    public final <T extends GameComponent> GameComponent getComponent(Class<T> component) {
        return gameobject.getComponent(component);
    }
    
    public final <T extends GameComponent> Set<GameComponent> getComponents(Class<T> component) {
        return gameobject.getComponents(component);
    }
    
    public final List<GameComponent> getAllComponents() {
        return gameobject.getAllComponents();
    }
    
    public static GameComponent get(String component) {
        return components.get(component);
    }
    
    public abstract List<String> getAttributes();
    public abstract void setAttributes(Map<String, String> attributes);
    public abstract GameComponent instantiate();
    
    //EVENTS
    public void awoke() {}
    public void start() {}
    public void sceneStart() {}
    public void sceneEnd() {}
    public void fixedUpdate() {}
    public void update() {}
    public void lateUpdate() {}
    public void render(GraphicsContext g) {}
    public void onCollisionEnter(Body body, Body otherBody, Vector2D normal, double penetration, int id) {}
    public void onCollision(Body body, Body otherBody, Vector2D normal, double penetration, int id) {}
    public void onCollisionExit() {}
}
