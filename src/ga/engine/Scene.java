package ga.engine;

import javafx.scene.layout.AnchorPane;

public class Scene {
    
    private javafx.scene.Scene scene;
    private AnchorPane node;
    
    /**
     * Constructs a Scene from filepath scene
     * @param filepath path to xml scene
     */
    public Scene(String filepath) {
        node = new AnchorPane();
        scene = new javafx.scene.Scene(node);
    }
    
    public javafx.scene.Scene get() {
        return scene;
    }
    
    public void update() {
        
    }
    
    public void render() {
        
    }
}
