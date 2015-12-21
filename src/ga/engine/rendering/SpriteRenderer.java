package ga.engine.rendering;

import ga.engine.resource.ResourceManager;
import ga.engine.scene.GameComponent;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class SpriteRenderer extends GameComponent {
    
    private static final HashMap<String, Integer> variables = new HashMap<>();
    
    static {
        variables.put("image", GameComponent.TYPE_STRING);
        variables.put("offsetX", GameComponent.TYPE_INTEGER);
        variables.put("offsetY", GameComponent.TYPE_INTEGER);
        variables.put("width", GameComponent.TYPE_INTEGER);
        variables.put("height", GameComponent.TYPE_INTEGER);
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
        this(ResourceManager.get(filepath), offsetX, offsetY, width, height);
    }
    
    public SpriteRenderer(Image sprite, int width, int height) {
        this(sprite, 0, 0, width, height);
    }
    
    public SpriteRenderer(String filepath, int width, int height) {
        this(ResourceManager.get(filepath), 0, 0, width, height);
    }
    
    public SpriteRenderer(String filepath) {
        this(ResourceManager.get(filepath), 0, 0, (int) ResourceManager.get(filepath).getWidth(), (int) ResourceManager.get(filepath).getHeight());
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
    public GameComponent instantiate() {
        return null;
    }

    @Override
    public Map<String, Integer> getVars() {
        return variables;
    }
    
    @Override
    public void xmlVar(String name, String value) {
        switch (name) {
            case "image":
                sprite = ResourceManager.get(value);
                break;
            case "offsetX":
                offsetX = (int) Float.parseFloat(value);
                break;
            case "offsetY":
                offsetY = (int) Float.parseFloat(value);
                break;
            case "width":
                width = (int) Float.parseFloat(value);
                break;
            case "height":
                height = (int) Float.parseFloat(value);
                break;
        }
    }
}
