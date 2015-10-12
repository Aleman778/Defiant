package ga.devkit.ui;

import ga.engine.physics.Vector2D;
import ga.engine.rendering.ParticleEmitter;
import ga.engine.scene.GameObject;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TitledPane;
import javafx.scene.paint.Color;

public class ParticleEditor extends Interface implements Initializable {
    
    @FXML
    private Canvas preview;
    @FXML
    private TitledPane previewContainer;
    private ParticleEmitter_Editor emitter;
    @FXML
    private Slider direction;
    @FXML
    private Slider spread;
    @FXML
    private Slider size;
    @FXML
    private ColorPicker color;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        color.setValue(Color.BLACK);
        emitter = new ParticleEmitter_Editor(preview, new Vector2D(), (float) direction.getValue(), (float) spread.getValue(), (float) size.getValue(), ParticleEmitter.MODE_CONTINUOUS, 100, color.getValue());
        new GameObject(preview.getWidth() / 2, preview.getHeight() / 2, 0).addComponent(emitter);
        new AnimationTimer() {

            @Override
            public void handle(long now) {
                emitter.update();
            }
        }.start();
        direction.valueProperty().addListener((observable, newValue, oldValue) -> {
            emitter.setDirection(newValue.floatValue());
        });
        spread.valueProperty().addListener((observable, newValue, oldValue) -> {
            emitter.setSpread(newValue.floatValue());
        });
        size.valueProperty().addListener((observable, newValue, oldValue) -> {
            emitter.setSize(newValue.floatValue());
        });
        color.setOnAction((ActionEvent event) -> {
            emitter.setColor(color.getValue());
        });
    }
}
