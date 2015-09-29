package ga.engine.scene;

import ga.engine.input.Input;
import ga.engine.physics.Body;
import ga.engine.physics.RigidBody;
import ga.engine.physics.Vector2D;
import ga.engine.rendering.ImageRenderer;
import ga.game.PlayerController;
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
        input = new Input(this);
        
        //!!!!TEST SCENE DEBUG!!!! - REPLACE THIS WITH XML PARSER
        GameObject object = new GameObject(32.0, 32.0, 0.0)
                .addComponent(new ImageRenderer("ga/game/Jordlabb.png"))
                .addComponent(new RigidBody(0));
        root.addChild(object);
        GameObject object2 = new GameObject(32.0, 32.0, 0.0)
                .addComponent(new ImageRenderer("ga/game/Jordlabb.png"))
                .addComponent(new RigidBody(0));
        object.addChild(object2);
        GameObject object3 = new GameObject(32.0, 32.0, 0.0)
                .addComponent(new ImageRenderer("ga/game/Jordlabb.png"))
                .addComponent(new RigidBody(0));
        object2.addChild(object3);
        
        GameObject box2 = new GameObject(200, 200, 0)
                .addComponent(new ImageRenderer("ga/game/Jordlabb.png"));
        box2.transform.scale(9, 0, 0);
        root.addChild(box2);
        RigidBody body2 = new RigidBody(0);
        body2.setVelocity(new Vector2D(0, 0));
        box2.addComponent(body2);
        
        GameObject player = new GameObject(64, 0, 0)
                .addComponent(new ImageRenderer("ga/game/Aplayertest2.png"))
                .addComponent(new PlayerController());
        RigidBody body = new RigidBody(1.25, 1, 2);
        body.setID(2);
        body.setVelocity(new Vector2D(1, 0));
        body.setSoftness(0);
        player.addComponent(body);
        root.addChild(player);
                
        GameObject ant = new GameObject(200, 100, 0)
                .addComponent(new ImageRenderer("ga/game/myr.png"));
        ant.addComponent(new RigidBody(1, 3, 2));
        ant.addComponent(new AI(this, 0.1, 4));
        root.addChild(ant);
        
        GameObject spider = new GameObject(250, 100, 0)
                .addComponent(new ImageRenderer("ga/game/SpiderR.png"));
        spider.addComponent(new RigidBody(1, 4, 2));
        spider.addComponent(new AI(this, 0.15, 5));
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
