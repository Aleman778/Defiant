package ga.engine.input;

import ga.engine.physics.Vector2D;
import ga.engine.scene.GameScene;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class Input {
    
    public static MouseButton mouseButton;
    public static Vector2D mousePosition;
    public static float lerp = 0.2f;
    
    private static Set<KeyCode> keys;
    private static Set<KeyCode> keysPressed;
    private static Set<KeyCode> keysReleased;
    private static Map<String, InputAxis> axis;
    
    private final GameScene scene;
    
    public Input(GameScene scene) {
        this.scene = scene;
        keysPressed = new HashSet<>();
        keysReleased = new HashSet<>();
        mousePosition = new Vector2D();
        mouseButton = MouseButton.NONE;
    }
}
