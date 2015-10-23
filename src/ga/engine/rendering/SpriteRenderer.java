package ga.engine.rendering;

import ga.engine.scene.GameComponent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class SpriteRenderer extends GameComponent {
    
    private Image sprite;
    private int offsetX, offsetY;
    private int width, height;
    
    public SpriteRenderer(Image sprite, int offsetX, int offsetY, int width, int height) {
        this.sprite = sprite;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
    } 

    public SpriteRenderer(String filepath, int offsetX, int offsetY, int width, int height) {
        this(new Image(ImageRenderer.class.getResource("/" + filepath).toExternalForm()), offsetX, offsetY, width, height);
    }
    
    public SpriteRenderer(Image sprite, int width, int height) {
        this(sprite, 0, 0, width, height);
    }
    
    public SpriteRenderer(String filepath, int width, int height) {
        this(new Image(ImageRenderer.class.getResource("/" + filepath).toExternalForm()), 0, 0, width, height);
    }

    @Override
    public void render(GraphicsContext g) {
        g.drawImage(sprite, offsetX, offsetY, width, height, 0, 0, width, height);
    }

    public Image getSprite() {
        return sprite;
    }

    public void setSprite(Image sprite) {
        this.sprite = sprite;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
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
}
