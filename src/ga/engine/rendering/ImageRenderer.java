package ga.engine.rendering;

import ga.engine.resource.ResourceManager;
import ga.engine.scene.GameComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ImageRenderer extends GameComponent {
    
    private static final List<String> ATTRIBUTES = new ArrayList<>();
    
    static {
        ATTRIBUTES.add("Image Filepath");
    }
    
    protected Image image;
    
    public ImageRenderer(Image image) {
        this.image = image;
    }
    
    public ImageRenderer(String filepath) {
        this(ResourceManager.getImage(filepath));
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
        if (image != null) {
            g.drawImage(image, 0, 0);
        }
    }

    @Override
    public List<String> getAttributes() {
        return ATTRIBUTES;
    }

    @Override
    public void setAttributes(Map<String, String> attributes) {
        String filepath = attributes.get("Image Filepath");
        if (filepath != null) {
            image = ResourceManager.getImage(filepath);
        }
    }
    
    @Override
    public GameComponent instantiate() {
        return new ImageRenderer(ResourceManager.DEFAULT_IMAGE);
    }
}
