package ga.engine.rendering;

import ga.engine.resource.ResourceManager;
import ga.engine.scene.GameComponent;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ImageRenderer extends GameComponent {
    
    private static final HashMap<String, Integer> variables = new HashMap<>();
    
    static {
        variables.put("image", GameComponent.TYPE_STRING);
    }
    
    protected Image image;
    
    public ImageRenderer(Image image) {
        this.image = image;
    }
    
    public ImageRenderer(String filepath) {
        this(ResourceManager.get(filepath));
    }

    public void setImage(Image image) {
        this.image = image;
    }
    
    public void setImage(String path) {
        this.image = new Image(path);
    }
    
    public Image getImage() {
        return image;
    }

    @Override
    public void render(GraphicsContext g) {
        g.drawImage(image, 0, 0);
    }

    @Override
    public GameComponent instantiate() {
        return new ImageRenderer(image);
    }

    @Override
    public Map<String, Integer> getVars() {
        return variables;
    }
    
    @Override
    public void xmlVar(String name, String value) {
        switch (name) {
            case "image":
                image = ResourceManager.get(value);
                break;
        }
    }
}
