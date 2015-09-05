package ga.engine.rendering;

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

    @Override
    public void render(Group group) {
        group.getChildren().add(imageview);
    }

    @Override
    public Node getNode() {
        return imageview;
    }
}
