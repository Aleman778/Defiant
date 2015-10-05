package ga.engine.scene;

import ga.engine.input.Input;
import ga.engine.physics.Body;
import ga.engine.physics.RigidBody;
import ga.engine.physics.Vector2D;
import ga.engine.rendering.ImageRenderer;
import ga.game.PlayerController;
import ga.game.Wall;
import ga.game.entity.AI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        input = new Input(scene);
        
        //!!!!TEST SCENE DEBUG!!!! - REPLACE THIS WITH XML PARSER
       
        new Wall(root, 0, 15 * 32, 32, 1);
        new Wall(root, 0, 0, 1, 16);
        new Wall(root, 0, 0, 32, 1);
        new Wall(root, 31 * 32, 0, 1, 16);
        
        new Wall(root, 0, 14 * 32, 3, 1);
        new Wall(root, 200, 12 * 32, 3, 1);
        new Wall(root, 300, 10 * 32, 3, 1);
        new Wall(root, 500, 8 * 32, 3, 1);
        new Wall(root, 600, 6 * 32, 3, 1);
        
        GameObject player = new GameObject(32, 15 * 32 - 64, 0)
                .addComponent(new ImageRenderer("ga/game/RED_Player.png"))
                .addComponent(new PlayerController());
        RigidBody body = new RigidBody(1.25, 1, 2);
        body.setID(2);
        body.setVelocity(new Vector2D(1, 0));
        body.setSoftness(0);
        player.addComponent(body);
        root.addChild(player);
                
        GameObject ant = new GameObject(700, 100, 0)
                .addComponent(new ImageRenderer("ga/game/myr.png"));
        ant.addComponent(new RigidBody(1, 3, 2));
        ant.addComponent(new AI(this, 0.2, 4));
        root.addChild(ant);
        
        GameObject spider = new GameObject(800, 100, 0)
                .addComponent(new ImageRenderer("ga/game/SpiderR.png"));
        spider.addComponent(new RigidBody(1, 4, 2));
        spider.addComponent(new AI(this, 0.3, 5));
        root.addChild(spider);
    }
    
    public javafx.scene.Scene get() {
        return scene;
    }
    
    public List<GameObject> getAllGameObjects() {
        return root.getGameObjects(new ArrayList<>());
    }
    
    public void update() {
        root.fixedUpdate();
        for (GameObject object : getAllGameObjects()) {
            if (!object.isBody())
                continue;
            Set<Body> retrievedBodies = new HashSet<>();
            for (GameObject otherObjects: getAllGameObjects()) {
                if (object.equals(otherObjects) || !otherObjects.isBody())
                    continue;
                retrievedBodies.add(otherObjects.getBody());
            }
            object.physicsUpdate(retrievedBodies);
        }
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
