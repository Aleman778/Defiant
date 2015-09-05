package ga.engine.scene;

import ga.engine.physics.RigidBody;
import ga.engine.physics.Vector2D;
import ga.engine.rendering.ImageRenderer;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.Scene;

public final class GameScene {

    private final Scene scene;
    private final Group group;
    private final GameObject root;
    public static Vector2D gravity = new Vector2D(0, 0.2);
    
    /**
     * Constructs a Scene from filepath scene
     * @param filepath path to xml scene
     */
    public GameScene(String filepath) {
        group = new Group();
        scene = new Scene(group);
        root = new GameObject(new Transform(null));
        
        //!!!!TEST SCENE DEBUG!!!! - REPLACE THIS WITH XML PARSER
        GameObject object = new GameObject(new Transform(null, 32.0, 32.0, 0.0))
                .addComponent(new ImageRenderer("ga/game/grass_tile.png"));
        object.transform.rotate(45, 45, 45);
        root.addChild(object);
        GameObject box1 = new GameObject(200, 100)
                .addComponent(new ImageRenderer("ga/game/grass_tile.png"));
        RigidBody body = new RigidBody(box1, this, new Vector2D(32, 32), 1.25);
        body.setVelocity(new Vector2D(0, 1));
        box1.addComponent(body);
        root.addChild(box1);
        GameObject box2 = new GameObject(200, 200)
                .addComponent(new ImageRenderer("ga/game/grass_tile.png"));
        RigidBody body2 = new RigidBody(box2, this, new Vector2D(32, 32), 0);
        body2.setVelocity(new Vector2D(0, 0));
        body2.setSoftness(0.12);
        box1.addComponent(body2);
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
