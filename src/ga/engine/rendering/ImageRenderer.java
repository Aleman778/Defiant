package ga.engine.rendering;

import com.sun.javafx.geom.Rectangle;
import ga.engine.physics.Vector2D;
import ga.engine.scene.GameComponent;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageRenderer extends GameComponent implements Renderable {
    
    private ImageView imageview;
    protected Image image;
    protected Vector2D pivot;
    
    public ImageRenderer(Image image) {
        this.image = image;
        this.imageview = new ImageView(this.image);
        imageview.setLayoutY(-(image.getHeight() - 32) / 2);
        imageview.setLayoutX(-(image.getWidth() - 32) / 2);
        pivot = new Vector2D(image.getWidth() / 2, image.getHeight() / 2);
    }
    
    public ImageRenderer(String filepath) {
        this(new Image(ImageRenderer.class.getResource("/" + filepath).toExternalForm()));
    }
    
    public Vector2D getSize() {
        return new Vector2D(imageview.getImage().getWidth(), imageview.getImage().getHeight());
    }
    
    public Vector2D getPivot() {
        return pivot;
    }
    
    public void setPivot(Vector2D pivot) {
        this.pivot = pivot;
    }

    @Override
    public void render(Group group) {
        group.getChildren().add(imageview);
    }

    @Override
    public Node getNode() {
        return imageview;
    }

    @Override
    public Rectangle computeAABB() {
        return new Rectangle(0, 0, (int) (image.getWidth() * Math.abs(transform.scale.x)), (int) (image.getHeight() * Math.abs(transform.scale.y)));
    }
}
