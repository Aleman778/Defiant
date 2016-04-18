package ga.engine.resource;

import ga.engine.blueprint.Blueprint;
import ga.engine.blueprint.BlueprintParser;
import ga.engine.physics.Vector2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;

public final class ResourceManager {
    
    public static final Blueprint DEFAULT_BLUEPRINT = new Blueprint(new Vector2D(0, 0), new ArrayList<>(), new HashMap<>());
    public static final AudioClip DEFAULT_AUDIO = new AudioClip(ResourceManager.class.getResource("/audio/music/FL3-140.mp3").toString());
    public static final Image DEFAULT_IMAGE = new Image("textures/nonrenderable.png");
    public static final Font DEFAULT_FONT = Font.loadFont("res/fonts/GeosansLight.tff", 16);
    
    private static final Map<String, Font> fonts = new HashMap<>();
    private static final Map<String, Image> images = new HashMap<>();
    private static final Map<String, Blueprint> blueprints = new HashMap<>();
    private static final Map<String, AudioClip> audioclips = new HashMap<>();
    
    public static Image getImage(String filepath) {
        if (images.containsKey(filepath)) {
            return images.get(filepath);
        }
        Image image;
        try {
            image = new Image(filepath);
        } catch (Exception e) {
            return DEFAULT_IMAGE;
        }
        images.put(filepath, image);
        return image;
    }

    public static void unloadImage(String filepath) {
        if (!images.containsKey(filepath))
            return;
        
        images.remove(filepath);
    }
    
    public static Blueprint getBlueprint(String filepath) {
        if (blueprints.containsKey(filepath)) {
            return blueprints.get(filepath);
        }
        Blueprint blueprint;
        try {
            blueprint = BlueprintParser.execute(filepath);
        } catch (Exception e) {
            return DEFAULT_BLUEPRINT;
        }
        blueprints.put(filepath, blueprint);
        return blueprint;
    }
    
    public static void unloadBlueprint(String filepath) {
        if (!blueprints.containsKey(filepath))
            return;
        
        blueprints.remove(filepath);
    }
    
    
    public static AudioClip getAudio(String filepath) {
        if (audioclips.containsKey(filepath)) {
            return audioclips.get(filepath);
        }
        AudioClip audio;
        try {
            audio = new AudioClip(ResourceManager.class.getResource("/" + filepath).toString());
        } catch (Exception e) {
            return DEFAULT_AUDIO;
        }
        audioclips.put(filepath, audio);
        return audio;
    }
    
    public static Font getFont(String filepath, int size) {
        String relpath = filepath + ":" + size;
        
        if (fonts.containsKey(relpath)) {
            return fonts.get(relpath);
        }
        Font font;
        try {
            font = Font.loadFont(ResourceManager.class.getResource("/" + filepath).toExternalForm(), size);
        } catch (Exception e) {
            return DEFAULT_FONT;
        }
        fonts.put(relpath, font);
        return font;
    }
    
    public static void unloadFont(String filepath, int size) {
        String relpath = filepath + ":" + size;
        if (!fonts.containsKey(relpath))
            return;
        
        fonts.remove(relpath);
    }
}
