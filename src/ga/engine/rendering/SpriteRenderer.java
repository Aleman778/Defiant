package ga.engine.rendering;

import ga.engine.resource.ResourceManager;
import ga.engine.scene.GameComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class SpriteRenderer extends GameComponent {
    
    private static final List<String> ATTRIBUTES = new ArrayList<>();
    
    static {
        ATTRIBUTES.add("Sprite Filepath");
        ATTRIBUTES.add("Sprite OffsetX");
        ATTRIBUTES.add("Sprite OffsetY");
        ATTRIBUTES.add("Sprite Width");
        ATTRIBUTES.add("Sprite Height");
    }
    
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
        this(ResourceManager.getImage(filepath), offsetX, offsetY, width, height);
    }
    
    public SpriteRenderer(Image sprite, int width, int height) {
        this(sprite, 0, 0, width, height);
    }
    
    public SpriteRenderer(String filepath, int width, int height) {
        this(ResourceManager.getImage(filepath), 0, 0, width, height);
    }
    
    public SpriteRenderer(String filepath) {
        this(ResourceManager.getImage(filepath), 0, 0, (int) ResourceManager.getImage(filepath).getWidth(), (int) ResourceManager.getImage(filepath).getHeight());
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

    @Override
    public List<String> getAttributes() {
        return ATTRIBUTES;
    }

    @Override
    public void setAttributes(Map<String, String> attributes) {
        sprite = ResourceManager.getImage(attributes.get("Sprite Filepath"));
        offsetX = (int) Float.parseFloat(attributes.get("Sprite OffsetX"));
        offsetY = (int) Float.parseFloat(attributes.get("Sprite OffsetY"));
        width = (int) Float.parseFloat(attributes.get("Sprite Width"));
        height = (int) Float.parseFloat(attributes.get("Sprite Height"));
    }
    
    @Override
    public GameComponent instantiate() {
        return new SpriteRenderer(ResourceManager.DEFAULT_IMAGE, 32, 32);
    }
}
