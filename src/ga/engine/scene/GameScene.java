package ga.engine.scene;

import ga.engine.input.KeyboardHandler;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class GameScene {

    private Scene scene;
    private AnchorPane node;
    private KeyboardHandler keyboard;
    
    /**
     * Constructs a Scene from filepath scene
     * @param filepath path to xml scene
     */
    public GameScene(String filepath) {
        node = new AnchorPane();
        scene = new Scene(node);
        keyboard = new KeyboardHandler();
        scene.setOnKeyPressed(keyboard);
        scene.setOnKeyReleased(keyboard);
    }
    
    public javafx.scene.Scene get() {
        return scene;
    }
    
    public void update() {
        
    }
    
    public void render() {
        
    }
}
