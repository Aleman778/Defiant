package ga.engine.resource;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.image.Image;

public final class ResourceManager {
    
    private static Map<String, Image> images = new HashMap<>();
    
    public static Image get(String filepath) {
        if (images.containsKey(filepath)) {
            return images.get(filepath);
        }
        Image image = new Image(filepath);
        images.put(filepath, image);
        return image;
    }
    
    public static void remove(String filepath) {
        if (!images.containsKey(filepath))
            return;
        
        images.remove(filepath);
    }
}
