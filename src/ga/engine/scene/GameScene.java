package ga.engine.scene;

import com.sun.javafx.geom.Rectangle;
import ga.engine.animation.AnimationController;
import ga.engine.core.Application;
import ga.engine.input.Input;
import ga.engine.lighting.AmbientLight;
import ga.engine.lighting.LightMap;
import ga.engine.lighting.PointLight;
import ga.engine.physics.Body;
import ga.engine.physics.RigidBody;
import ga.engine.physics.Vector2D;
import ga.engine.rendering.ImageRenderer;
import ga.engine.rendering.JavaFXCanvasRenderer;
import ga.engine.rendering.SpriteRenderer;
import ga.game.PlayerController;
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
    private final LightMap lightmap;
    public static Vector2D gravity = new Vector2D(0, 0.2);
    
    private PointLight pointlight;
    
    /**
     * Constructs a Scene from filepath scene
     * @param filepath path to xml scene
     */
    public GameScene(String filepath) {
        group = new Group();
        scene = new Scene(group);
        root = new GameObject();
        input = new Input(scene);
        canvas = new Canvas(Application.getWidth(), Application.getHeight());
        group.getChildren().add(canvas);
        g = canvas.getGraphicsContext2D();
        
        lightmap = new LightMap();
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new Color(0.05, 0.05, 0.05, 1.0));
        group.getChildren().add(lightmap);
        
        Transform2D transform = new Transform2D(null);
        transform.scale = new Vector2D(300, 300);
        GameObject light = new GameObject(transform);
        pointlight = new PointLight();
        light.addComponent(pointlight);
        lightmap.addLight(pointlight);
        lightmap.setAmbientLight(ambient);
        
        //!!!!TEST SCENE DEBUG!!!! - REPLACE THIS WITH XML PARSER
        GameObject player = new GameObject(320, 0)
                .addComponent(new SpriteRenderer("textures/player/Player_Idle.png", 32, 64))
                .addComponent(new PlayerController())
                .addComponent(new AnimationController());
        RigidBody body = new RigidBody(1.25, 1);
        body.setID(2);
        body.setSoftness(0);
        player.addComponent(body);
        ((PlayerController)player.getComponent(PlayerController.class)).initParticles();
        root.addChild(player);
        player.transform.depth = -1;
        
        for (int i = 0; i < 320; i += 32) {
            GameObject box = new GameObject(320 + i, 320)
                    .addComponent(new ImageRenderer("textures/Jordlabb.png"));
            box.getTransform().pivot = new Vector2D(0, 0);
            box.setAABB(0, 0, 32, 32);
            RigidBody body2 = new RigidBody(0);
            box.addComponent(body2);
            root.addChild(box);
        }
//        
//        GameObject ant = new GameObject(700, 100)
//                .addComponent(new ImageRenderer("textures/myr.png"));
//        ant.addComponent(new RigidBody(1, 3));
//        ant.addComponent(new AI(this, 0.2, 4));
//        root.addChild(ant);
//        
//        GameObject spider = new GameObject(800, 100)
//                .addComponent(new ImageRenderer("textures/SpiderR.png"));
//        spider.addComponent(new RigidBody(1, 4));
//        spider.addComponent(new AI(this, 0.3, 5));
//        root.addChild(spider);
    }

    public javafx.scene.Scene get() {
        return scene;
    }

    public List<GameObject> getAllGameObjects() {
        return root.getGameObjects(new ArrayList<>());
    }

    public void setWidth(double width) {
        canvas.setWidth(width);
        lightmap.refresh();
    }

    public void setHeight(double height) {
        canvas.setHeight(height);
        lightmap.refresh();
    }

    public LightMap getLightmap() {
        return lightmap;
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
        
        //Render objects
        JavaFXCanvasRenderer.renderAll(canvas, getAllGameObjects());
        //renderAABB();
        
        //Render lightning
        lightmap.refresh();
        pointlight.refresh();
        
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
}
