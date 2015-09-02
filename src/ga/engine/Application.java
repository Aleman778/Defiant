package ga.engine;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {
    
    private static AnimationTimer gameloop;
    private static Stage window;
    private static GameScene scene;
    private static final Group root = new Group();
    private static final KeyboardHandler keyboard = new KeyboardHandler();
    
    @Override
    public void start(Stage primaryStage) {
        init(primaryStage);
        start();
    }
    
    public static void init(Stage window) {
        Application.window = window;
        Application.scene = new GameScene(root);
        window.setScene(scene);
        final ImageView background = new ImageView(new Image(Application.class.getResourceAsStream("Placeholder Grass.png")));
        root.getChildren().add(background);
        scene.setOnKeyPressed(keyboard);
        scene.setOnKeyReleased(keyboard);
        gameloop = new AnimationTimer() {

            @Override
            public void handle(long now) {
                //INSERT GAME LOOP HERE! UPDATE / RENDER SCENE!
                background.setVisible(!keyboard.getPressed(KeyCode.SPACE));
            }
        };
    }
    
    public static void start() {
        gameloop.start();
        window.setTitle("Ga Engine");
        window.setHeight(250);
        window.setWidth(250);
        window.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
