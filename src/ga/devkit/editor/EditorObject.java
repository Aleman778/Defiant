package ga.devkit.editor;

import com.sun.javafx.geom.Rectangle;
import ga.engine.physics.Vector2D;
import ga.engine.scene.GameObject;
import ga.engine.scene.Transform2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class EditorObject extends GameObject {
    
    private static final Image NONE_RENDERABLE_IMAGE = new Image("ga/devkit/editor/nonrenderable.png");
    private String name;
    
    public EditorObject(String name, Transform2D transform) {
        super(transform);
        this.name = name;
    }

    public EditorObject(String name, double x, double y, double z) {
        super(x, y);
        this.name = name;
    }

    public EditorObject(String name) {
        super();
        this.name = name;
    }
    
    @Override
    public void render(GraphicsContext g) {
        super.render(g);
    }
    
    public void renderAABB(GraphicsContext g) {
        Vector2D position = transform.localPosition();
        g.save();
        Affine affine = new Affine();
        affine.appendTranslation((int) position.x, (int) position.y);
        
        g.setTransform(affine);
        Rectangle aabb = getAABB();
        g.setFill(new Color(1.0, 0.0, 0.0, 0.2));
        g.fillRect(aabb.x, aabb.y, aabb.width, aabb.height);
        g.restore();
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
