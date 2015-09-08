package ga.engine.input;

import ga.engine.physics.Vector2D;
import ga.engine.scene.GameScene;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
        scene.get().setOnKeyPressed((KeyEvent event) -> {
            pressedKey(event.getCode());
        });
        scene.get().setOnKeyReleased((KeyEvent event) -> {
            releasedKey(event.getCode());
        });
        axis = new HashMap<>();
        keys = new HashSet<>();
        keysPressed = new HashSet<>();
        keysReleased = new HashSet<>();
        mousePosition = new Vector2D();
        mouseButton = MouseButton.NONE;
        axis.put("horizontal", new InputAxis(KeyCode.A, KeyCode.D));
        axis.put("vertical", new InputAxis(KeyCode.W, KeyCode.S));
    }
    
    public void update() {
        axis.forEach((String t, InputAxis u) -> {
            Iterator<KeyCode> it = keys.iterator();
            float val = 0.0f;
            while (it.hasNext()) {
                KeyCode key = it.next();
                if (key.equals(u.getKeyPos()))
                    val += lerp;
                if (key.equals(u.getKeyNeg()))
                    val -= lerp;
                Math.max(Math.min(lerp, val), -lerp);
            }
            
            if (val == 0.0f) {
                val -= (float) (Math.signum((int) (u.value * 10.0f)) * lerp) / 10.0f;
            }
            
            u.value += val;
            u.value = (float) ((int) (u.value * 10.0f)) / 10.0f;
            u.value = Math.max(Math.min(1.0f, u.value), -1.0f);
        });
    }
    
    private void pressedKey(KeyCode keyCode) {
        if (!keys.contains(keyCode))
            keysPressed.add(keyCode);
        keys.add(keyCode);
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
    
    public static float getAxis(String name) {
        InputAxis result = axis.get(name);
        if (result != null)
            return result.value;
        else
            return 0.0f;
    }
    
    public static float getAxisSignum(String name) {
        InputAxis result = axis.get(name);
        if (result != null)
            return Math.signum(result.value);
        else
            return 0.0f;
    }
    
    public static boolean getKeyPressed(KeyCode key) {
        return keysPressed.contains(key);
    }
    
    public static boolean getKeyReleased(KeyCode key) {
        return keysReleased.contains(key);
    }
    
    public static boolean getKey(KeyCode key) {
        return keysPressed.contains(key);
    }
    
    public static boolean getMouseButton(MouseButton mb) {
        return mouseButton.equals(mb);
    }
    
    public static MouseButton getMouseButton() {
        return mouseButton;
    }
}
