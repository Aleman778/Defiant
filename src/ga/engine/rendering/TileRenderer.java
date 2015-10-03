package ga.engine.rendering;

import com.sun.javafx.geom.Rectangle;
import ga.engine.physics.Vector2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TileRenderer extends ImageRenderer {

    private Vector2D size;

    public TileRenderer(Image image, int width, int height) {
        super(image);
        size = new Vector2D(width, height);
    }

    public TileRenderer(String filepath, int width, int height) {
        super(filepath);
        size = new Vector2D(width, height);
    }

    @Override
    public void render(Group group) {
        // AABB is apparantly symmetrical around position, using x - width / 2 for image rendering
        for (int x = (int) (transform.position.x - (image.getWidth() * size.x) / 2 + image.getWidth() / 2); x < transform.position.x + (image.getWidth() * size.x) / 2; x += image.getWidth()) {
            for (int y = (int) (transform.position.y - (image.getHeight() * size.y) / 2 + image.getHeight() / 2); y < transform.position.y + (image.getHeight() * size.y) / 2; y += image.getHeight()) {
                ImageView view = new ImageView(image);
                view.setX(x);
                view.setY(y);
                group.getChildren().add(view);
            }
        }
    }

    @Override
    public Rectangle computeAABB() {
        return new Rectangle(0, 0, (int) (size.x * image.getWidth() * Math.abs(transform.scale.x)), (int) (size.y * image.getHeight() * Math.abs(transform.scale.y)));
    }
}
