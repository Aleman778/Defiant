package ga.engine.input;

import ga.engine.core.Application;
import ga.engine.physics.Vector2D;
import java.awt.AWTException;
import java.awt.Robot;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class Input {
    
    public static Set<MouseButton> mouseButtons;
    public static MouseButton mouseButtonDrag;
    public static double scrollPosition;
    public static Vector2D mousePosition;
    
    private static Set<KeyCode> keys;
    private static Set<KeyCode> keysPressed;
    private static Set<KeyCode> keysReleased;
    
    private final Scene scene;
    private static Robot robot;
    
    public Input(Scene scene) {
        this.scene = scene;
        scene.setOnKeyPressed((KeyEvent event) -> {
            pressedKey(event.getCode());
        });
        scene.setOnKeyReleased((KeyEvent event) -> {
            releasedKey(event.getCode());
        });
        scene.setOnMouseMoved((MouseEvent event) -> {
            mousePosition.x = event.getX();
            mousePosition.y = event.getY();
        });
        scene.setOnMouseDragged((MouseEvent event) -> {
            mousePosition.x = event.getX();
            mousePosition.y = event.getY();
            mouseButtonDrag = event.getButton();
        });
        scene.setOnMousePressed((MouseEvent event) -> {
            mouseButtons.add(event.getButton());
        });
        scene.setOnMouseReleased((MouseEvent event) -> {
            mouseButtons.remove(event.getButton());
        });
        scene.setOnScroll((ScrollEvent event) -> {
            scrollPosition = Math.signum(event.getDeltaY());
        });
        keys = new HashSet<>();
        keysPressed = new HashSet<>();
        keysReleased = new HashSet<>();
        mousePosition = new Vector2D();
        mouseButtons = new HashSet<>();
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(Input.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void pressedKey(KeyCode keyCode) {
        if (!keys.contains(keyCode))
            keysPressed.add(keyCode);
        keys.add(keyCode);
        
        if (getKeyPressed(KeyCode.F5)) {
            Application.setDevmode(!Application.isDevmodeEnabled());
        }
        if (getKeyPressed(KeyCode.F1)) {
            Application.restart();
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
    
    public static Set<KeyCode> getKeysPressed() {
        return keysPressed;
    }
    
    public static boolean getKey(KeyCode key) {
        return keys.contains(key);
    }
    
    public static boolean getMouseButton(MouseButton mb) {
        return mouseButtons.contains(mb);
    }
    
    public static Set<MouseButton> getMouseButtons() {
        return mouseButtons;
    }
    
    public static Vector2D getMousePosition() {
        return mousePosition;
    }
    
    public static void setMousePosition(Vector2D pos) {
        robot.mouseMove((int) pos.x, (int) pos.y);
    }

    public static double getScrollPosition() {
        return scrollPosition;
    }
}
