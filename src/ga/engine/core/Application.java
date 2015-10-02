package ga.engine.core;

import ga.devkit.core.Devkit;
import ga.engine.scene.GameScene;
import javafx.animation.AnimationTimer;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {
    
    private static boolean devmode = false;
    private static AnimationTimer gameloop;
    private static Stage window;
    private static GameScene scene;
    private static Devkit devkit;
    
    @Override
    public void start(Stage primaryStage) {
        init(primaryStage);
        start();
    }
    
    public static void init(Stage window) {
        Application.window = window;
        Application.scene = new GameScene("WRITE XML PATH LATER");
        Application.devkit = new Devkit();
        window.setScene(scene.get());
        gameloop = new AnimationTimer() {

            @Override
            public void handle(long now) {
                if (scene != null) {
                    scene.update();
                    scene.render();
                }
            }
        };
    }
    
    public static void start() {
        gameloop.start();
        window.setTitle("Ga Engine");
        window.setWidth(640);
        window.setHeight(480);
        window.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public static GameScene getScene() {
        return scene;
    }
    
    public static void setDevmode(boolean enable) {
        devmode = enable;
        if (devmode)
            Application.window.setScene(devkit.get());
        else
            Application.window.setScene(scene.get());
    }
    
    public static boolean isDevmodeEnabled() {
        return devmode;
    }
}
