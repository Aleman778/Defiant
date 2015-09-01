/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ga.engine;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author alemen778
 */
public class Application extends javafx.application.Application {
    
    private Stage window;
    private Scene scene;
    
    @Override
    public void start(Stage primaryStage) {
        this.window = primaryStage;
        window.setTitle("GA Engine");
        window.show();
    }
    
    public static void main(String[] args) {
        System.out.println("Programmet körs!");
        launch(args);
    }
}
