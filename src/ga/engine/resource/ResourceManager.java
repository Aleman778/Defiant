package ga.engine.resource;

import ga.engine.blueprint.Blueprint;
import ga.engine.blueprint.BlueprintParser;
import ga.engine.scene.GameComponent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.image.Image;

public final class ResourceManager {
    
    public static final Image DEFAULT_IMAGE = new Image("textures/nonrenderable.png");
    public static final Blueprint DEFAULT_BLUEPRINT = new Blueprint(new ArrayList<>(), new HashMap<>());
    
    private static final Map<String, Image> images = new HashMap<>();
    private static final Map<String, Blueprint> blueprints = new HashMap<>();
    
    public static Image getImage(String filepath) {
        if (images.containsKey(filepath)) {
            return images.get(filepath);
        }
        Image image;
        try {
            image = new Image(filepath);
        } catch (Exception e) {
            image = DEFAULT_IMAGE;
        }
        images.put(filepath, image);
        return image;
    }

    public static Blueprint getBlueprint(String filepath) {
        if (blueprints.containsKey(filepath)) {
            return blueprints.get(filepath);
        }
        Blueprint blueprint;
        try {
            blueprint = BlueprintParser.execute(filepath);
        } catch (Exception e) {
            blueprint = DEFAULT_BLUEPRINT;
        }
        blueprints.put(filepath, blueprint);
        return blueprint;
    }
    
    public static void unloadImage(String filepath) {
        if (!images.containsKey(filepath))
            return;
        
        images.remove(filepath);
    }
}
