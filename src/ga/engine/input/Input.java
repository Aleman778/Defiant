package ga.engine.input;

import ga.engine.core.Application;
import ga.engine.physics.Vector2D;
import java.util.HashSet;
import java.util.Set;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;

public class Input {
    
    public static MouseButton mouseButton;
    public static Vector2D mousePosition;
    public static double lerp = 0.2f;
    
    private static Set<KeyCode> keys;
    private static Set<KeyCode> keysPressed;
    private static Set<KeyCode> keysReleased;
    
    private final Scene scene;
    
    public Input(Scene scene) {
        this.scene = scene;
        scene.setOnKeyPressed((KeyEvent event) -> {
            pressedKey(event.getCode());
        });
        scene.setOnKeyReleased((KeyEvent event) -> {
            releasedKey(event.getCode());
        });
        keys = new HashSet<>();
        keysPressed = new HashSet<>();
        keysReleased = new HashSet<>();
        mousePosition = new Vector2D();
        mouseButton = MouseButton.NONE;
    }
    
    private void pressedKey(KeyCode keyCode) {
        if (!keys.contains(keyCode))
            keysPressed.add(keyCode);
        keys.add(keyCode);
        
        if (getKeyPressed(KeyCode.F5)) {
            Application.setDevmode(!Application.isDevmodeEnabled());
        }
    }
    
    private void releasedKey(KeyCode keyCode) {
        keys.remove(keyCode);
        keysReleased.add(keyCode);
    }
    
    public void clearAll() {
        keys.clear();
        keysPressed.clear();
        keysReleased.clear();
    }
    
    public void clear() {
        keysPressed.clear();
        keysReleased.clear();
    }
    
    public static boolean getKeyPressed(KeyCode key) {
        return keysPressed.contains(key);
    }
    
    public static boolean getKeyReleased(KeyCode key) {
        return keysReleased.contains(key);
    }
    
    public static boolean getKey(KeyCode key) {
        return keys.contains(key);
    }
    
    public static boolean getMouseButton(MouseButton mb) {
        return mouseButton.equals(mb);
    }
    
    public static MouseButton getMouseButton() {
        return mouseButton;
    }
}
