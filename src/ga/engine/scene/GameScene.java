package ga.engine.scene;

import com.sun.javafx.geom.Rectangle;
import ga.engine.core.Application;
import ga.engine.input.Input;
import ga.engine.physics.Body;
import ga.engine.physics.Vector2D;
import ga.engine.rendering.JavaFXCanvasRenderer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public final class GameScene {

    private final Scene scene;
    private final Group group;
    private final GameObject root;
    private final Input input;
    private final Canvas canvas;
    private final GraphicsContext g;
    private Color background = Color.web("#1e120c");
    public static Vector2D gravity = new Vector2D(0, 0.2);

    /**
     * Constructs a Scene from filepath scene.<br/>
     * <b>WARNING</b><br/>
     * Use SceneParser.execute(xmlfile) to load from XML
     * @param root the root object
     */
    public GameScene(GameObject root) {
        this.root = root;
        group = new Group();
        scene = new Scene(group);
        input = new Input(scene);
        canvas = new Canvas(Application.getWidth(), Application.getHeight());
        group.getChildren().add(canvas);
        g = canvas.getGraphicsContext2D();
    }

    public javafx.scene.Scene get() {
        return scene;
    }

    public List<GameObject> getAllGameObjects() {
        return root.getGameObjects(new ArrayList<>());
    }

    public void setWidth(double width) {
        canvas.setWidth(width);
    }

    public void setHeight(double height) {
        canvas.setHeight(height);
    }
    
    public void start() {
        for (GameObject object: getAllGameObjects()) {
            for (GameComponent component: object.getAllComponents()) {
                component.sceneStart();
            }
        }
    }
    
    public void end() {
        root.clearChildren();
        for (GameObject object: getAllGameObjects()) {
            for (GameComponent component: object.getAllComponents()) {
                component.sceneEnd();
            }
        }
    }

    public void update() {
        root.fixedUpdate();
        for (GameObject object : getAllGameObjects()) {
            if (!object.isBody()) {
                continue;
            }
            Set<Body> retrievedBodies = new HashSet<>();
            for (GameObject otherObjects : getAllGameObjects()) {
                if (object.equals(otherObjects) || !otherObjects.isBody()) {
                    continue;
                }
                retrievedBodies.add(otherObjects.getBody());
            }
            object.physicsUpdate(retrievedBodies);
        }
        root.update();
        root.lateUpdate();
    }

    public void render() {
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g.setFill(background);
        g.fillRect(-10, -10, canvas.getWidth() + 10, canvas.getHeight() + 10);
        
        //Render objects
        JavaFXCanvasRenderer.renderAll(canvas, getAllGameObjects());

        //Clear Inputs
        input.clear();
    }

    private void renderAABB() {
        g.setFill(new Color(1.0, 0.0, 0.0, 0.3));
        g.setStroke(new Color(0.0, 1.0, 0.0, 1.0));
        Iterator<GameObject> it = getAllGameObjects().iterator();
        while (it.hasNext()) {
            GameObject object = it.next();
            Rectangle aabb = object.getAABB();
            Vector2D position = object.getTransform().localPosition();
            g.fillRect(position.x + aabb.x, position.y + aabb.y, aabb.width, aabb.height);
        }
    }

    public GameObject getRoot() {
        return root;
    }
}
