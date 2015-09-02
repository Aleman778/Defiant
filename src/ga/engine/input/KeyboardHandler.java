package ga.engine.input;

import java.util.ArrayList;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyboardHandler implements javafx.event.EventHandler<KeyEvent> {

    ArrayList<KeyCode> keys = new ArrayList<>();

    @Override
    public void handle(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_PRESSED && !keys.contains(event.getCode())) {
            keys.add(event.getCode());
        }
        if (event.getEventType() == KeyEvent.KEY_RELEASED) {
            keys.remove(event.getCode());
        }
    }
    /**
     * Gets all keys pressed.
     * @return an ArrayList containing all currently held down keys
     */

    ArrayList<KeyCode> getKeys() {
        return keys;
    }
    /**
     * Checks if a key is pressed or not.
     * @param key the KeyCode to check for
     * @return true if the key is currently being held down
     */

    boolean getPressed(KeyCode key) {
        return keys.contains(key);
    }
}
