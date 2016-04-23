package ga.devkit.editor;

import com.sun.javafx.geom.Rectangle;
import ga.engine.physics.Vector2D;
import ga.engine.scene.GameComponent;
import ga.engine.scene.GameObject;
import ga.engine.scene.Transform2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class EditorObject extends GameObject {
    
    private static final Image NONE_RENDERABLE_IMAGE = new Image("ga/devkit/editor/nonrenderable.png");
    
    private final Map<String, String> attributes;
    private String blueprint;
    private String name;
    
    public EditorObject(String name, Transform2D transform) {
        super("untagged", transform);
        this.attributes = new HashMap<>();
        this.name = name;
    }

    public EditorObject(String name, double x, double y) {
        super("untagged", x, y);
        this.attributes = new HashMap<>();
        this.name = name;
    } 

    public EditorObject(String name) {
        super();
        this.attributes = new HashMap<>();
        this.name = name;
    }

    public GameObject addComponent(EditorComponent component) {
        addComponent(component.get());
        return this;
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
    
    public void setAttribute(String attri, String value) {
        if (attributes.containsKey(attri)) {
            attributes.replace(attri, value);
        } else {
            attributes.put(attri, value);
        }
    }
    
    public String getAttribute(String attri) {
        return attributes.get(attri);
    }
    
    public List<String> getAllAttributes() {
        List<String> result = new ArrayList<>();
        for (GameComponent comp: getAllComponents()) {
            result.addAll(comp.getAttributes());
        }
        return result;
    }
    
    public EditorObject instantiate() {
        EditorObject result = new EditorObject(name);
        Rectangle aabb = getAABB();
        result.setAABB(aabb.x, aabb.y, aabb.width, aabb.height);
        result.getTransform().depth = transform.depth;
        result.getTransform().position = new Vector2D(transform.position.x, transform.position.y);
        result.getTransform().rotation = transform.rotation;
        result.getTransform().scale = new Vector2D(transform.scale.x, transform.scale.y);
        result.getTransform().pivot = new Vector2D(transform.pivot.x, transform.pivot.y);
        for (GameComponent component: getAllComponents()) {
//            GameComponent comp = component.instantiate();
//            if (comp != null) {
//                result.addComponent(comp);
//            }
        }
        return result;
    }
}
