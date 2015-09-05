package ga.engine.rendering;

import ga.engine.scene.Camera;
import ga.engine.scene.GameComponent;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageRenderer extends GameComponent implements Renderable {
    
    private ImageView imageview;
    
    public ImageRenderer(String filepath) {
        imageview = new ImageView(filepath);
    }
    
    public ImageRenderer(Image image) {
        imageview = new ImageView(image);
    }

    @Override
    public void render(Camera camera) {
        camera.getCanvas().getChildren().add(imageview);
    }

    @Override
    public Node getNode() {
        return imageview;
    }
}
