package ga.engine.scene;

import ga.engine.rendering.ImageRenderer;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public final class GameScene {

    private final Scene scene;
    private final AnchorPane node;
    private final GameObject root;
    
    /**
     * Constructs a Scene from filepath scene
     * @param filepath path to xml scene
     */
    public GameScene(String filepath) {
        node = new AnchorPane();
        scene = new Scene(node);
        root = new GameObject(0, 0);
        
        //!!!!TEST SCENE DEBUG!!!! - REPLACE THIS WITH XML PARSER
        GameObject object = new GameObject(32, 32)
                .addComponent(new ImageRenderer("ga/game/grass_tile.png"));
        root.addChild(object);
        Camera camera = new Camera();
        GameObject cameraObject = new GameObject(0, 0)
                .addComponent(camera);
        root.addChild(cameraObject);
        addCamera(camera);   
    }
    
    public javafx.scene.Scene get() {
        return scene;
    }
    
    public List<GameObject> getAllGameObjects() {
        return root.getGameObjects(new ArrayList<GameObject>());
    }
    
    public void addCamera(Camera camera) {
        node.getChildren().add(camera.getCanvas());
        Camera.mainCamera = camera;
    }
    
    public void update() {
        root.update();
    }
    
    public void render() {
        if (Camera.mainCamera != null) {
            Camera.mainCamera.clear();
            root.render(Camera.mainCamera);
        }
    }
}
