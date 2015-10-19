package ga.engine.rendering;

import ga.engine.scene.GameComponent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ImageRenderer extends GameComponent {
    
    protected Image image;
    
    public ImageRenderer(Image image) {
        this.image = image;
    }
    
    public ImageRenderer(String filepath) {
        this(new Image(ImageRenderer.class.getResource("/" + filepath).toExternalForm()));
    }

    public void setImage(Image image) {
        this.image = image;
    }
    
    public void setImage(String path) {
        this.image = new Image(path);
    }

    @Override
    public void render(GraphicsContext g) {
        g.drawImage(image, 0, 0);
    }
}
