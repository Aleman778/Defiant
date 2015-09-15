package ga.engine.rendering;

import ga.engine.physics.Vector2D;
import ga.engine.scene.GameComponent;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageRenderer extends GameComponent implements Renderable {
    
    private ImageView imageview;
    
    public ImageRenderer(Image image) {
        imageview = new ImageView(image);
    }
    
    public ImageRenderer(String filepath) {
        this(new Image(filepath));
    }
    
    public Vector2D getSize()
    {
        return new Vector2D(imageview.getImage().getWidth(), imageview.getImage().getHeight());
    }

    @Override
    public void render(Group group) {
        group.getChildren().add(imageview);
    }

    @Override
    public Node getNode() {
        return imageview;
    }
}
