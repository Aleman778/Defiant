package ga.engine.scene;

import ga.engine.rendering.ImageRenderer;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.Scene;

public final class GameScene {

    private final Scene scene;
    private final Group group;
    private final GameObject root;
    
    /**
     * Constructs a Scene from filepath scene
     * @param filepath path to xml scene
     */
    public GameScene(String filepath) {
        group = new Group();
        scene = new Scene(group);
        root = new GameObject(0, 0);
        
        //!!!!TEST SCENE DEBUG!!!! - REPLACE THIS WITH XML PARSER
        GameObject object = new GameObject(32, 32)
                .addComponent(new ImageRenderer("ga/game/grass_tile.png"));
        root.addChild(object);
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
