package ga.engine;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class GameScene {

    private Scene scene;
    private AnchorPane node;
    
    /**
     * Constructs a Scene from filepath scene
     * @param filepath path to xml scene
     */
    public GameScene(String filepath) {
        node = new AnchorPane();
        scene = new Scene(node);
    }
    
    public javafx.scene.Scene get() {
        return scene;
    }
    
    public void update() {
        
    }
    
    public void render() {
        
    }
}
