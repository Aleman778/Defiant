package ga.engine.scene;

import javafx.scene.layout.AnchorPane;

public class Camera extends GameComponent {
    
    public static Camera mainCamera = null;
    
    public float viewportX, viewportY;
    public float viewportW, viewportH;
    private final AnchorPane canvas;
    
    public Camera() {
        canvas = new AnchorPane();
        viewportX = 0.0f;
        viewportY = 0.0f;
        viewportW = 1.0f;
        viewportH = 1.0f;
    }
    
    public static void clearAll() {
        mainCamera.clear();
        mainCamera = null;
    }
    
    public AnchorPane getCanvas() {
        return canvas;
    }
    
    public void clear() {
        canvas.getChildren().clear();
    }
}
