package ga.engine.scene;

import ga.engine.input.Input;
import ga.engine.physics.RigidBody;
import ga.engine.physics.Vector2D;
import ga.engine.rendering.ImageRenderer;
import ga.game.PlayerController;
import ga.game.entity.AI;
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
                .addComponent(new RigidBody(this, 0));
        root.addChild(object);
        GameObject object2 = new GameObject(32.0, 32.0, 0.0)
                .addComponent(new ImageRenderer("ga/game/grass_tile.png"))
                .addComponent(new RigidBody(this, 0));
        object.addChild(object2);
        GameObject object3 = new GameObject(32.0, 32.0, 0.0)
                .addComponent(new ImageRenderer("ga/game/grass_tile.png"))
                .addComponent(new RigidBody(this, 0));
        object2.addChild(object3);
        
        GameObject player = new GameObject(64, 0, 0)
                .addComponent(new ImageRenderer("ga/game/placeholder_player.png"))
                .addComponent(new PlayerController());
        RigidBody body = new RigidBody(this, 1.25);
        body.setID(2);
        body.setVelocity(new Vector2D(1, 0));
        body.setSoftness(0);
        player.addComponent(body);
        root.addChild(player);
        GameObject box2 = new GameObject(200, 200, 0)
                .addComponent(new ImageRenderer("ga/game/grass_tile.png"));
        box2.transform.scale(9, 0, 0);
        root.addChild(box2);
        RigidBody body2 = new RigidBody(this, 0);
        body2.setVelocity(new Vector2D(0, 0));
        box2.addComponent(body2);
                
        GameObject enemy = new GameObject(200, 100, 0)
                .addComponent(new ImageRenderer("ga/game/grass_tile.png"));
        RigidBody enemyBody = new RigidBody(this, 1);
        enemyBody.setID(3);
        enemy.addComponent(enemyBody);
        enemy.addComponent(new AI(this));
        root.addChild(enemy);
    }
    
    public javafx.scene.Scene get() {
        return scene;
    }
    
    public List<GameObject> getAllGameObjects() {
        return root.getGameObjects(new ArrayList<>());
    }
    
    public void update() {
        root.fixedUpdate();
        root.update();
        root.lateUpdate();
    }
    
    public void render() {
        group.getChildren().clear();
        root.render(group);
        
        //Clear Inputs
        input.clear();
    }
}
