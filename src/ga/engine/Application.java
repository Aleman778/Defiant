/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ga.engine;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author alemen778
 */
public class Application extends javafx.application.Application {
    
    private static AnimationTimer gameloop;
    private static Stage window;
    private static Scene scene;
    
    @Override
    public void start(Stage primaryStage) {
        init(primaryStage);
        start();
    }
    
    public static void init(Stage window) {
        Application.window = window;
        Application.scene = null;
        gameloop = new AnimationTimer() {

            @Override
            public void handle(long now) {
                //INSERT GAME LOOP HERE! UPDATE / RENDER SCENE!
            }
        };
    }
    
    public static void start() {
        gameloop.start();
        window.setTitle("Ga Engine");
        window.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
