package ga.engine.scene;

import ga.engine.input.Input;
import ga.engine.physics.RigidBody;
import ga.engine.physics.Vector2D;
import ga.engine.rendering.ImageRenderer;
import ga.game.PlayerController;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.Scene;

public final class GameScene {

    private final Scene scene;
    private final Group group;
    private final GameObject root;
    private final Input input;
    public static Vector2D gravity = new Vector2D(0, 0.2);
    
    /**
     * Constructs a Scene from filepath scene
     * @param filepath path to xml scene
     */
    public GameScene(String filepath) {
        group = new Group();
        scene = new Scene(group);
        root = new GameObject();
        input = new Input(this);
        
        //!!!!TEST SCENE DEBUG!!!! - REPLACE THIS WITH XML PARSER
        GameObject object = new GameObject(32.0, 32.0, 0.0)
                .addComponent(new ImageRenderer("ga/game/grass_tile.png"))
                .addComponent(new RigidBody(this, new Vector2D(32, 32), 0));
        root.addChild(object);
        GameObject object2 = new GameObject(32.0, 32.0, 0.0)
                .addComponent(new ImageRenderer("ga/game/grass_tile.png"))
                .addComponent(new RigidBody(this, new Vector2D(32, 32), 0));
        object.addChild(object2);
        GameObject object3 = new GameObject(32.0, 32.0, 0.0)
                .addComponent(new ImageRenderer("ga/game/grass_tile.png"))
                .addComponent(new RigidBody(this, new Vector2D(32, 32), 0));
        object2.addChild(object3);
        
        GameObject box1 = new GameObject(64, 0, 0)
                .addComponent(new ImageRenderer("ga/game/grass_tile.png"))
                .addComponent(new PlayerController());
        RigidBody body = new RigidBody(this, new Vector2D(32, 32), 1.25);
        body.setVelocity(new Vector2D(1, 0));
        box1.addComponent(body);
        root.addChild(box1);
        GameObject box2 = new GameObject(200, 200, 0)
                .addComponent(new ImageRenderer("ga/game/grass_tile.png"));
        box2.getTransform().scale(10, 0, 0);
        RigidBody body2 = new RigidBody(this, new Vector2D(320, 32), 0);
        body2.setVelocity(new Vector2D(0, 0));
        body2.setSoftness(0.12);
        box2.addComponent(body2);
        root.addChild(box2);
    }
    
    public javafx.scene.Scene get() {
        return scene;
    }
    
    public List<GameObject> getAllGameObjects() {
        return root.getGameObjects(new ArrayList<GameObject>());
    }
    
    public void update() {
        root.update();
    }
    
    public void render() {
        group.getChildren().clear();
        root.render(group);
    }
}
