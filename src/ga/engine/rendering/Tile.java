package ga.engine.rendering;

import ga.engine.resource.ResourceManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Tile {
    
    public int x, y;
    public int width, height;
    public int offsetX, offsetY;
    public Image image;

    public Tile() {
        this(null, 0, 0, 0, 0, 0, 0);
    }
    
    public Tile(int x, int y, int width, int height, int offsetX, int offsetY) {
        this(null, x, y, width, height, offsetX, offsetY);
    }
    
    public Tile(Image image, int x, int y, int width, int height, int offsetX, int offsetY) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }
    
    public void setImage(String filepath) {
        image = ResourceManager.getImage(filepath);
    }
    
    public void render(GraphicsContext g) {
        g.drawImage(image, offsetX, offsetY, width, height, x, y, width, height);
    }
}
