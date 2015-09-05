package ga.engine.core;

import ga.engine.scene.GameScene;
import javafx.animation.AnimationTimer;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {
    
    private static AnimationTimer gameloop;
    private static Stage window;
    private static GameScene scene;
    
    @Override
    public void start(Stage primaryStage) {
        init(primaryStage);
        start();
    }
    
    public static void init(Stage window) {
        Application.window = window;
        Application.scene = new GameScene("WRITE XML PATH LATER");
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
}
