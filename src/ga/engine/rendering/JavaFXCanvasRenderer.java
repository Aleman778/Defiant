package ga.engine.rendering;

import ga.engine.physics.Vector2D;
import ga.engine.scene.GameComponent;
import ga.engine.scene.GameObject;
import ga.engine.scene.Transform2D;
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;

public class JavaFXCanvasRenderer {
    
    public static void renderAll(Canvas canvas, List<GameObject> objects) {
        objects.sort(new GameObject());
        for (GameObject object: objects) {
            render(canvas, object, object.getTransform());
        }
    }
    
    public static void render(Canvas canvas, GameObject object, Transform2D transform) {
        GraphicsContext g = canvas.getGraphicsContext2D();
        Vector2D position = transform.localPosition();
        double rotation = transform.localRotation();
        Vector2D scale = transform.scale;
        g.save();
        Affine affine = new Affine();
        affine.appendTranslation((int) position.x, (int) position.y);
        affine.appendRotation(rotation, transform.pivot.x, transform.pivot.y);
        affine.appendScale(scale.x, scale.y, transform.pivot.x, transform.pivot.y);
        g.setTransform(affine);
        for (GameComponent component : object.getAllComponents()) {
            component.render(g);
        }
        g.restore();
    }
}
