package ga.engine.rendering;

import ga.engine.physics.Vector2D;
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
    
    public Vector2D getSize() {
        return new Vector2D(image.getWidth(), image.getHeight());
    }
    
    public void setImage(Image image) {
        this.image = image;
    }
    
    public void setImage(String filepath) {
        this.image = new Image(filepath);
    }

    @Override
    public void render(GraphicsContext g) {
        g.drawImage(image, 0, 0);
    }
}
