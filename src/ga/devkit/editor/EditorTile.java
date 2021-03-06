package ga.devkit.editor;

import com.sun.javafx.geom.Rectangle;
import ga.engine.physics.Vector2D;
import ga.engine.resource.ResourceManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class EditorTile {
    
    private Image image;
    private int depth;
    private int tileX, tileY;
    private int width, height;
    private Vector2D position;

    public EditorTile(Image image, int depth, int tileX, int tileY, int width, int height, Vector2D position) {
        this.image = image;
        this.depth = depth;
        this.tileX = tileX;
        this.tileY = tileY;
        this.width = width;
        this.height = height;
        this.position = position;
    }
    
    public EditorTile(String image, int depth, int tileX, int tileY, int width, int height, Vector2D position) {
        this(ResourceManager.getImage(image), depth, tileX, tileY, width, height, position);
    }

    public void render(GraphicsContext g) {
        g.drawImage(image, tileX, tileY, width, height, position.x, position.y, width, height);
    }
    
    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getTileX() {
        return tileX;
    }

    public void setTileX(int tilex) {
        this.tileX = tilex;
    }

    public int getTileY() {
        return tileY;
    }

    public void setTileY(int tiley) {
        this.tileY = tiley;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }
    
    public Rectangle localAABB() {
        return new Rectangle((int) position.x, (int) position.y, width, height);
    }
    
    public EditorTile instantiate() {
        Vector2D pos = new Vector2D();
        pos.x = position.x;
        pos.y = position.y;
        return new EditorTile(image, depth, tileX, tileY, width, height, pos);
    }
}
